package android.com.simplehttpdatasync.datasync;


import android.com.simplehttpdatasync.datasync.base.DataSyncBase;
import android.com.simplehttpdatasync.datasync.net.RequestListener;
import android.com.simplehttpdatasync.datasync.util.Parameters;

/**
 * @author quitokong
 * @ClassName: ExampleUserDataSync
 * @Description: 用户登陆信息同步类
 * @date 2015年8月12日 上午1:46:07
 */
public class ExampleUserDataSync extends DataSyncBase {

    // 网络请求动作标签
    protected static final String ACTION = "action";
    protected static final String MODULE = "module";
    // 模块
    protected static final String MODULE_USER_CENTER = "user_center";
    // 动作
    protected static final String ACTION_REGISTER = "register";
    protected static final String ACTION_GET_VERIFYCODE = "get_verifyode";
    // 标签
    protected static final String TELEPHONE = "telephone";
    protected static final String PASSWORD = "password";
    protected static final String CODE = "code";

    /**
     * 获取验证码
     */
    public void getVerifyCode(String telephone, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_GET_VERIFYCODE);
        parameters.add(TELEPHONE, telephone);
        post(SERVER_IP, "postJsonString", parameters, pListener);
    }

    /**
     * 注册
     */
    public void register(String telephone, String pwd, String code, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_REGISTER);
        parameters.add(TELEPHONE, telephone);
        parameters.add(PASSWORD, pwd);
        parameters.add(CODE, code);
        get(SERVER_IP, parameters, pListener);
    }


}
