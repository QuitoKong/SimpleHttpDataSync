package android.com.simplehttpdatasync.datasync.net;


import android.com.simplehttpdatasync.datasync.util.Parameters;
import android.com.simplehttpdatasync.datasync.util.Utility;

public class AsyncRunner {
	/**
	 * 请求接口数据，并在获取到数据后通过RequestListener将responsetext回传给调用者
	 * 
	 * @param url
	 *            服务器地址
	 * @param params
	 *            存放参数的容器
	 * @param httpMethod
	 *            "GET"or “POST”
	 * @param listener
	 *            回调对象
	 */
	public static void request(final String url, final Parameters params,
			final String httpMethod, final RequestListener listener) {
		new Thread() {
			@Override
			public void run() {
				try {
					String resp = HttpManager.openUrl(url, httpMethod, params);
					if (resp == null) {
						listener.onError(Utility.decodeBase64(resp));
					} else {
						listener.onComplete(Utility.decodeBase64(resp));
					}
				} catch (Exception e) {
					e.printStackTrace();
					listener.onError(e.getMessage());
				}
			}
		}.start();

	}

}
