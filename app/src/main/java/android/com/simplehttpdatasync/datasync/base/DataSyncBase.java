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
    public static final String SERVER_IP = "192.168.1.1/webapi/?";
    // post请求方式
    protected static final String HTTPMETHOD_POST = "POST";
    // get请求方式
    protected static final String HTTPMETHOD_GET = "GET";

    // 网络请求动作标签
    protected static final String ACTION = "action";
    protected static final String MODULE = "module";

    /**
     * @param @param storeid 商店的ID
     * @param @param url 连接的服务器的地址，必须传递过来
     * @param @param params 传递给服务器的参数，必须传递过来
     * @param @param httpMethod 模式：get/post
     * @param @param listener
     * @return void
     * @throws
     * @Title: request
     * @Description:
     */
    protected void request( final String url,
                           final Parameters parameters, final String httpMethod,
                           RequestListener listener) {
        AsyncRunner.request(url, parameters, httpMethod, listener);
    }

}
