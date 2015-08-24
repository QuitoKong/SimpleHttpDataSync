package android.com.simplehttpdatasync.datasync.net;

import android.com.simplehttpdatasync.datasync.util.Parameters;
import android.com.simplehttpdatasync.datasync.util.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

public class HttpManager {
	private static final int CONNECTION_TIMEOUT = 5 * 1000;
	private static final int SOCKET_TIMEOUT = 20 * 1000;
	private static final String HTTPMETHOD_POST = "POST";
	private static final String HTTPMETHOD_GET = "GET";

	/**
	 * 
	 * @param url
	 *            服务器地址
	 * @param method
	 *            "GET"or “POST”
	 * @param params
	 *            存放参数的容器
	 * @return 响应结果
	 * @throws Exception
	 */
	public static String openUrl(String url, String method, Parameters params)
			throws Exception {
		HttpUriRequest request = null;
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
		// 构造HttpClient的实例
		HttpClient httpClient = new DefaultHttpClient(httpParameters);
		try {
			url = url + Utility.encodeUrl(params);
			if (method.equals(HTTPMETHOD_GET)) {
				HttpGet get = new HttpGet(url);
				request = get;
			} else if (method.equals(HTTPMETHOD_POST)) {
				HttpPost post = new HttpPost(url);
				// Post运作传送变数必须用NameValuePair[]阵列储存
				post.setEntity(new UrlEncodedFormEntity(
						getNameValuePair(params), HTTP.UTF_8));
				request = post;
			}
			// 使用execute方法发送HTTP请求，并返回HttpResponse对象
			HttpResponse response = httpClient.execute(request);
			return getResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static List<NameValuePair> getNameValuePair(Parameters params) {
		int size = params.size();
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		for (int loc = 0; loc < size; loc++) {
			values.add(new BasicNameValuePair(params.getKey(loc), params
					.getValue(loc)));
		}
		return values;
	}

	/**
	 * @Title: openUrl
	 * @Description: 向服务器发出请求
	 * @param @param httpResponse
	 * @param @return
	 * @param @throws Exception 设定文件
	 * @return String 返回类型
	 * @throws
	 */
	public static String getResponse(HttpResponse httpResponse)
			throws Exception {
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {// SC_OK = 200，获得返回结果
			return EntityUtils.toString(httpResponse.getEntity());
		} else {
			return "返回码：" + statusCode;
		}
	}

}
