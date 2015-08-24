package android.com.simplehttpdatasync.datasync.net;

/**
 * @ClassName: RequestListener
 * @Description: 发起访问接口的请求时所需的回调接口
 * @date 2015-8-12 下午11:12:51
 * 
 */
public interface RequestListener {
	/**
	 * @Title: onComplete
	 * @Description: 用于获取服务器返回的响应内容
	 * @param @param response
	 * @return void
	 * @throws
	 */
	public void onComplete(String response);

	/**
	 * @Title: onError
	 * @Description: 用于获取错误信息
	 * @param @param e
	 * @return void
	 * @throws
	 */
	public void onError(String error);

}
