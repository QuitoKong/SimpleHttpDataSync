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

    // 模块
    protected static final String MODULE_USER_CENTER = "user_center";
    // 动作
    protected static final String ACTION_LOGIN = "login";
    protected static final String ACTION_REGISTER = "register";
    protected static final String ACTION_GET_VERIFYCODE = "get_verifyode";
    protected static final String ACTION_EDIT_PASSWORD = "edit_password";
    protected static final String ACTION_RESET_PASSWORD = "reset_password";
    protected static final String ACTION_REBIND_PHONE = "rebind_phone";
    // 标签
    protected static final String TELEPHONE = "telephone";
    protected static final String OLDTELEPHONE = "oldtelephone";
    protected static final String NEWTELEPHONE = "newtelephone";
    protected static final String PASSWORD = "password";
    protected static final String OLDPASSWORD = "oldPassword";
    protected static final String NEWPASSWORD = "newPassword";
    protected static final String CODE = "code";

    /**
     *  获取验证码
     */
    public void getVerifyCode(String telephone, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_GET_VERIFYCODE);
        parameters.add(TELEPHONE, telephone);
        request(SERVER_IP + "?", parameters, HTTPMETHOD_POST, pListener);
    }

    /**
     *  注册
     */
    public void register(String telephone, String pwd, String code, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_REGISTER);
        parameters.add(TELEPHONE, telephone);
        parameters.add(PASSWORD, pwd);
        parameters.add(CODE, code);
        request(SERVER_IP, parameters, HTTPMETHOD_POST, pListener);
    }

    /**
     *  登陆
     */
    public void login(String telephone, String pwd,
                      RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_LOGIN);
        parameters.add(TELEPHONE, telephone);
        parameters.add(PASSWORD, pwd);
        request(SERVER_IP, parameters, HTTPMETHOD_POST, pListener);
    }

    /**
     * 修改密码
     */
    public void updatePwd(String telephone, String oldPwd,
                          String newPwd, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_EDIT_PASSWORD);
        parameters.add(TELEPHONE, telephone);
        parameters.add(OLDPASSWORD, oldPwd);
        parameters.add(NEWPASSWORD, newPwd);
        request(SERVER_IP, parameters, HTTPMETHOD_POST, pListener);
    }

    /*
     * 重置密码
     */
    public void resetPwd(String telephone, String newPwd, String code, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_RESET_PASSWORD);
        parameters.add(TELEPHONE, telephone);
        parameters.add(NEWPASSWORD, newPwd);
        parameters.add(CODE, code);
        request(SERVER_IP, parameters, HTTPMETHOD_POST, pListener);
    }

    /*
   改绑手机
    */
    public void rebindPhone(String telephone, String newTelephone, String password, String code, RequestListener pListener) {
        Parameters parameters = new Parameters();
        parameters.add(MODULE, MODULE_USER_CENTER);
        parameters.add(ACTION, ACTION_REBIND_PHONE);
        parameters.add(OLDTELEPHONE, telephone);
        parameters.add(NEWTELEPHONE, newTelephone);
        parameters.add(PASSWORD, password);
        parameters.add(CODE, code);
        request(SERVER_IP, parameters, HTTPMETHOD_POST, pListener);
    }

}
