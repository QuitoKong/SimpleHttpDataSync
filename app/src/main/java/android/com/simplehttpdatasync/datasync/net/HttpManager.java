package android.com.simplehttpdatasync.datasync.net;

import android.com.simplehttpdatasync.datasync.util.OkHttpUtil;
import android.com.simplehttpdatasync.datasync.util.Parameters;
import android.com.simplehttpdatasync.datasync.util.Utility;


import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class HttpManager {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * @param url    服务器地址
     * @param method "GET"or “POST”
     * @param params 存放参数的容器
     * @return 响应结果
     * @throws Exception
     */
    public static String openUrl(String url, String postJson, String method, Parameters params)
            throws IOException {
        Request request = null;
        try {
            url = url + Utility.encodeUrl(params);
            if (method.equals("POST")) {
                RequestBody body = RequestBody.create(JSON, postJson);
                request = new Request.Builder().url(url)
                        .post(body)
                        .build();
            } else if (method.equals("GET")) {
                request = new Request.Builder().url(url).build();
            }
            Response response = OkHttpUtil.getInstance().newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
