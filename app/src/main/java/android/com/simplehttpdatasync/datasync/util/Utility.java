package android.com.simplehttpdatasync.datasync.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class Utility {

	public static Bundle parseUrl(String url) {
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				params.putString(URLDecoder.decode(v[0]),
						URLDecoder.decode(v[1]));
			}
		}
		return params;
	}

	public static String encodeUrl(Parameters parameters) {
		if (parameters == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		boolean first = true;
		int size = parameters.size();
		for (int loc = 0; loc < size; loc++) {
			if (first) {
				first = false;
			} else {
				sb.append("&");
			}
			String _key = parameters.getKey(loc);
			String _value = parameters.getValue(_key);
			if (_value == null) {
				// Log.i("encodeUrl", "key:" + _key + " 's value is null");
			} else {
				// Log.i("encodeUrl", URLEncoder.encode(parameters.getKey(loc))
				// + "=" + URLEncoder.encode(parameters.getValue(loc)));
				sb.append(URLEncoder.encode(parameters.getKey(loc))
						+ "="
						+ URLEncoder.encode(encodeBase64(parameters.getValue(
						loc).getBytes())));
			}

		}
		return sb.toString();
	}

	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	/**
	 * @Title: encode
	 * @Description: 加密
	 * @param @param data
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String encodeBase64(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 0x0ff) << 8)
					| (((int) data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;

			if (n++ >= 14) {
				n = 0;
				buf.append(" ");
			}
		}

		if (i == start + len - 2) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = (((int) data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	/**
	 * @Title: decode
	 * @Description: 解密
	 * @param @param s
	 * @param @return
	 * @return String
	 * @throws
	 */
	public static String decodeBase64(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return new String(decodedBytes);
	}

	private static void decode(String s, OutputStream os) throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;

			if (i == len)
				break;

			int tri = (decode(s.charAt(i)) << 18)
					+ (decode(s.charAt(i + 1)) << 12)
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);

			i += 4;
		}
	}

	private static int decode(char c) {
		if (c >= 'A' && c <= 'Z')
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z')
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return ((int) c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	private static boolean isBundleEmpty(Parameters bundle) {
		if (bundle == null || bundle.size() == 0) {
			return true;
		}
		return false;
	}

	private static boolean deleteDependon(File file, int maxRetryCount) {
		int retryCount = 1;
		maxRetryCount = (maxRetryCount < 1) ? 5 : maxRetryCount;
		boolean isDeleted = false;

		if (file != null) {
			while ((!(isDeleted)) && (retryCount <= maxRetryCount)
					&& (file.isFile()) && (file.exists())) {
				if (!((isDeleted = file.delete()))) {
					// LogUtils.i(file.getAbsolutePath() + "删除失败，失败次数为:" +
					// retryCount);
					++retryCount;
				}
			}

		}

		return isDeleted;
	}

	private static void mkdirs(File dir_) {
		if (dir_ == null) {
			return;
		}
		if ((!(dir_.exists())) && (!(dir_.mkdirs()))) {
			throw new RuntimeException("fail to make " + dir_.getAbsolutePath());
		}
	}

	private static void createNewFile(File file_) {
		if (file_ == null) {
			return;
		}
		if (!(__createNewFile(file_))) {
			throw new RuntimeException(file_.getAbsolutePath()
					+ " doesn't be created!");
		}
	}

	private static void delete(File f) {
		if ((f != null) && (f.exists()) && (!(f.delete()))) {
			throw new RuntimeException(f.getAbsolutePath()
					+ " doesn't be deleted!");
		}

	}

	private static boolean __createNewFile(File file_) {
		if (file_ == null) {
			return false;
		}
		makesureParentExist(file_);
		if (file_.exists()) {
			delete(file_);
		}
		try {
			return file_.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	private static boolean deleteDependon(String filepath, int maxRetryCount) {
		if (TextUtils.isEmpty(filepath)) {
			return false;
		}
		return deleteDependon(new File(filepath), maxRetryCount);
	}

	private static boolean deleteDependon(String filepath) {
		return deleteDependon(filepath, 0);
	}

	private static boolean doesExisted(File file) {
		return ((file != null) && (file.exists()));
	}

	private static boolean doesExisted(String filepath) {
		if (TextUtils.isEmpty(filepath)) {
			return false;
		}
		return doesExisted(new File(filepath));
	}

	private static void makesureParentExist(File file_) {
		if (file_ == null) {
			return;
		}
		File parent = file_.getParentFile();
		if ((parent != null) && (!(parent.exists()))) {
			mkdirs(parent);
		}
	}

	private static void makesureFileExist(File file) {
		if (file == null) {
			return;
		}
		if (!(file.exists())) {
			makesureParentExist(file);
			createNewFile(file);
		}
	}

	private static void makesureFileExist(String filePath_) {
		if (filePath_ == null) {
			return;
		}
		makesureFileExist(new File(filePath_));
	}

	/**
	 * @Title: isNet
	 * @Description: 检查当前网络是否可用
	 * @param @param pContext
	 * @param @return
	 * @return boolean true 可用，false 不可用
	 * @throws
	 */
	public static boolean isNet(Context pContext) {
		ConnectivityManager _Manager = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo[] = _Manager.getAllNetworkInfo();
		// 注意，这个判断一定要的哦，要不然会出错
		if (networkInfo != null) {
			for (int i = 0; i < networkInfo.length; i++) {
				if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	// 判断当前网络是否为wifi
	public static boolean isWifi(Context pContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) pContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 上传图片的策略
	 * 
	 * @author SinaDev
	 * 
	 */
	public static final class UploadImageUtils {
		private static void revitionImageSizeHD(String picfile, int size,
				int quality) throws IOException {
			if (size <= 0) {
				throw new IllegalArgumentException(
						"size must be greater than 0!");
			}
			if (!doesExisted(picfile)) {
				throw new FileNotFoundException(picfile == null ? "null"
						: picfile);
			}

			if (!BitmapHelper.verifyBitmap(picfile)) {
				throw new IOException("");
			}

			int photoSizesOrg = 2 * size;
			FileInputStream input = new FileInputStream(picfile);
			final BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, opts);
			try {
				input.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			int rate = 0;
			for (int i = 0;; i++) {
				if ((opts.outWidth >> i <= photoSizesOrg && (opts.outHeight >> i <= photoSizesOrg))) {
					rate = i;
					break;
				}
			}

			opts.inSampleSize = (int) Math.pow(2, rate);
			opts.inJustDecodeBounds = false;

			Bitmap temp = safeDecodeBimtapFile(picfile, opts);

			if (temp == null) {
				throw new IOException("Bitmap decode error!");
			}

			deleteDependon(picfile);
			makesureFileExist(picfile);

			int org = temp.getWidth() > temp.getHeight() ? temp.getWidth()
					: temp.getHeight();
			float rateOutPut = size / (float) org;

			if (rateOutPut < 1) {
				Bitmap outputBitmap;
				while (true) {
					try {
						outputBitmap = Bitmap.createBitmap(
								((int) (temp.getWidth() * rateOutPut)),
								((int) (temp.getHeight() * rateOutPut)),
								Bitmap.Config.ARGB_8888);
						break;
					} catch (OutOfMemoryError e) {
						System.gc();
						rateOutPut = (float) (rateOutPut * 0.8);
					}
				}
				if (outputBitmap == null) {
					temp.recycle();
				}
				Canvas canvas = new Canvas(outputBitmap);
				Matrix matrix = new Matrix();
				matrix.setScale(rateOutPut, rateOutPut);
				canvas.drawBitmap(temp, matrix, new Paint());
				temp.recycle();
				temp = outputBitmap;
			}
			final FileOutputStream output = new FileOutputStream(picfile);
			if (opts != null && opts.outMimeType != null
					&& opts.outMimeType.contains("png")) {
				temp.compress(Bitmap.CompressFormat.PNG, quality, output);
			} else {
				temp.compress(Bitmap.CompressFormat.JPEG, quality, output);
			}
			try {
				output.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			temp.recycle();
		}

		private static void revitionImageSize(String picfile, int size,
				int quality) throws IOException {
			if (size <= 0) {
				throw new IllegalArgumentException(
						"size must be greater than 0!");
			}

			if (!doesExisted(picfile)) {
				throw new FileNotFoundException(picfile == null ? "null"
						: picfile);
			}

			if (!BitmapHelper.verifyBitmap(picfile)) {
				throw new IOException("");
			}

			FileInputStream input = new FileInputStream(picfile);
			final BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(input, null, opts);
			try {
				input.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int rate = 0;
			for (int i = 0;; i++) {
				if ((opts.outWidth >> i <= size)
						&& (opts.outHeight >> i <= size)) {
					rate = i;
					break;
				}
			}

			opts.inSampleSize = (int) Math.pow(2, rate);
			opts.inJustDecodeBounds = false;

			Bitmap temp = safeDecodeBimtapFile(picfile, opts);

			if (temp == null) {
				throw new IOException("Bitmap decode error!");
			}

			deleteDependon(picfile);
			makesureFileExist(picfile);
			final FileOutputStream output = new FileOutputStream(picfile);
			if (opts != null && opts.outMimeType != null
					&& opts.outMimeType.contains("png")) {
				temp.compress(Bitmap.CompressFormat.PNG, quality, output);
			} else {
				temp.compress(Bitmap.CompressFormat.JPEG, quality, output);
			}
			try {
				output.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			temp.recycle();
		}

		public static boolean revitionPostImageSize(String picfile) {
			try {
				// if (Weibo.isWifi) {
				// revitionImageSizeHD(picfile, 1600, 75);
				// } else {
				revitionImageSize(picfile, 1024, 75);
				// }
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		/**
		 * 如果加载时遇到OutOfMemoryError,则将图片加载尺寸缩小一半并重新加载
		 * 
		 * @param bmpFile
		 * @param opts
		 *            注意：opts.inSampleSize 可能会被改变
		 * @return
		 */
		private static Bitmap safeDecodeBimtapFile(String bmpFile,
				BitmapFactory.Options opts) {
			BitmapFactory.Options optsTmp = opts;
			if (optsTmp == null) {
				optsTmp = new BitmapFactory.Options();
				optsTmp.inSampleSize = 1;
			}

			Bitmap bmp = null;
			FileInputStream input = null;

			final int MAX_TRIAL = 5;
			for (int i = 0; i < MAX_TRIAL; ++i) {
				try {
					input = new FileInputStream(bmpFile);
					bmp = BitmapFactory.decodeStream(input, null, opts);
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
					optsTmp.inSampleSize *= 2;
					try {
						input.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				} catch (FileNotFoundException e) {
					break;
				}
			}

			return bmp;
		}
	}

}
