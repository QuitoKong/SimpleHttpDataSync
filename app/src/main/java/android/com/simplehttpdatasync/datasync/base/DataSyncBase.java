package android.com.simplehttpdatasync.datasync.base;

import android.com.simplehttpdatasync.datasync.net.AsyncRunner;
import android.com.simplehttpdatasync.datasync.net.RequestListener;
import android.com.simplehttpdatasync.datasync.util.Parameters;

/**
 * @author @quitokong
 * @ClassName: DataSynsBase
 * @Description: 数据同步的基类，每个接口类都继承了此抽象类
 * @date 2015-8-12 下午11:15:41
 */
public abstract class DataSyncBase {

    // IP : 访问的服务器地址
    public static final String SERVER_IP = "192.168.1.1/api/?";
    // post请求方式
    private static final String HTTPMETHOD_POST = "POST";
    // get请求方式
    private static final String HTTPMETHOD_GET = "GET";

    /**
     * @param url        连接的服务器的地址，必须传递过来
     * @param json       post的内容
     * @param parameters 传递给服务器的参数，必须传递过来
     * @param httpMethod 模式：get/post
     * @param listener
     * @return void
     * @throws
     * @Title: request
     * @Description:
     */
    private void request(final String url, final String json,
                         final Parameters parameters, final String httpMethod,
                         RequestListener listener) {
        AsyncRunner.request(url, json, parameters, httpMethod, listener);
    }

    protected void post(final String url, final String json,
                        final Parameters parameters, RequestListener listener) {
        request(url, json, parameters, HTTPMETHOD_GET, listener);
    }

    protected void get(final String url,
                       final Parameters parameters, RequestListener listener) {
        request(url, null, parameters, HTTPMETHOD_POST, listener);
    }

}
