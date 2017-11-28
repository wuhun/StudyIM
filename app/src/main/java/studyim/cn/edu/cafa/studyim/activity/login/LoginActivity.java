package studyim.cn.edu.cafa.studyim.activity.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.MainActivity;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.model.LoginUserModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.ui.ClearWriteEditText;
import tools.com.lvliangliang.wuhuntools.util.WuhunDeviceTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunDrawTools;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.loadDialog.LoadDialog;

import static tools.com.lvliangliang.wuhuntools.WuhunTools.getContext;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.body_img_backgroud)
    ImageView bodyImgBackgroud;
    @BindView(R.id.body_frm_backgroud)
    FrameLayout bodyFrmBackgroud;
    @BindView(R.id.login_logo)
    ImageView loginLogo;
    @BindView(R.id.et_login_num)
    ClearWriteEditText etLoginNum;
    @BindView(R.id.et_login_pwd)
    ClearWriteEditText etLoginPwd;
    @BindView(R.id.btn_login_sign)
    Button btnLoginSign;
    @BindView(R.id.tv_login_register)
    TextView tvLoginRegister;
    @BindView(R.id.tv_login_query_pwd)
    TextView tvLoginQueryPwd;
    @BindView(R.id.tv_browse)
    TextView tvBrowse;

    private Gson gson;

    private String username;
    private String password;

    private static int LOGIN_SUCCESS = 0x01;
    private static int LOGIN_FAIL = 0x02;
    private static int LOGIN_ERROR = 0x03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        gson = new Gson();
        WuhunDrawTools.DrawTextViewUnderline(tvLoginRegister);
        WuhunDrawTools.DrawTextViewUnderline(tvLoginQueryPwd);
    }

    @OnClick({R.id.btn_login_sign, R.id.tv_login_query_pwd, R.id.tv_login_register, R.id.tv_browse})
    public void onClick(View view) {
        if (!WuhunNetTools.isAvailable(getContext())) {
            WuhunToast.error("网络无法访问，请检查网络连接").show();
            return;
        }
        switch (view.getId()) {
            case R.id.btn_login_sign:
                goToMain();
                break;
            case R.id.tv_login_query_pwd:
                // TODO: 2017/11/8 忘记密码
                WuhunToast.info("忘记密码").show();
                break;
            case R.id.tv_login_register:
                // TODO: 2017/11/8 注册
                WuhunToast.info("注册").show();
                break;
            case R.id.tv_browse:
//                WuhunToast.info("游客方式浏览").show();
                jumpToActivity(MainActivity.class);
                break;
            default:
                WuhunToast.info("没有找到内容");
                break;
        }
    }

    private void goToMain() {
        username = etLoginNum.getText().toString().trim();
        password = etLoginPwd.getText().toString().trim();
        String imei = WuhunDeviceTool.getIMEI(LoginActivity.this);

        if (TextUtils.isEmpty(username)) {
            WuhunToast.normal("请输入学号或手机号").show();
            etLoginNum.setShakeAnimation();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            WuhunToast.normal("请输入密码").show();
            etLoginPwd.setShakeAnimation();
            return;
        }
        LoadDialog.show(LoginActivity.this);
        // TODO: 2017/11/9 网络请求
        if (MyApplication.isDebug) {
            handler.sendEmptyMessageDelayed(LOGIN_SUCCESS, 1000);
        } else {
            HttpUtil.login(username, password, imei, new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    String result = response.body().toString();
                    LoginUserModel loginModel = gson.fromJson(result, LoginUserModel.class);
                    if (null == loginModel)
                        handler.sendEmptyMessage(LOGIN_ERROR);
                    if (loginModel.getCode() == 1) {//成功
                        WuhunDebug.debug("=>"+ result);
                        Message msg = handler.obtainMessage();
                        msg.obj = loginModel;
                        msg.what = LOGIN_SUCCESS;
                        handler.sendMessage(msg);
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.obj = loginModel;
                        msg.what = LOGIN_FAIL;
                        handler.sendMessage(msg);
                    }
                }

                @Override
                public void onError(Response<String> response) {
                    super.onError(response);
                    WuhunToast.normal("error").show();
                }
            });
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_SUCCESS) {
                // TODO: 2017/11/20 缓存token
                LoadDialog.dismiss(LoginActivity.this);
                jumpToActivity(MainActivity.class);
                LoginActivity.this.finish();
            } else if (msg.what == LOGIN_FAIL) {
                LoginUserModel model = ((LoginUserModel) msg.obj);
                if (null != model.getMsg())
                    WuhunToast.normal(model.getMsg()).show();
            } else if (msg.what == LOGIN_ERROR) {
                WuhunToast.normal("服务器内部错误，请联系管理员").show();
            }
            LoadDialog.dismiss(LoginActivity.this);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (WuhunToast.doubleClickExit()) {
                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
