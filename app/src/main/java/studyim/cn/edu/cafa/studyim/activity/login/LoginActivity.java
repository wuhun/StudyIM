package studyim.cn.edu.cafa.studyim.activity.login;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.MainActivity;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.Friend;
import studyim.cn.edu.cafa.studyim.model.FriendListModel;
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.model.LoginUserModel;
import studyim.cn.edu.cafa.studyim.model.RolesModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.permission.PermissionListener;
import tools.com.lvliangliang.wuhuntools.permission.PermissionsUtil;
import tools.com.lvliangliang.wuhuntools.ui.ClearWriteEditText;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunDeviceTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.loadDialog.LoadDialog;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;
import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;
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

    private String username;
    private String password;

    private Context mContext;
    //    private Gson gson;
    private LoadDialog ld;

    private static int LOGIN_SUCCESS = 0x01;
    private static int LOGIN_FAIL = 0x02;
    private static int LOGIN_ERROR = 0x03;
    private static int BROWSE = 0x04;//预览
    //    private static int FIND_GROUP_MEMEBERLIST = 0x05;
    private static int GO_TO_MAIN = 0x06;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();

        PermissionsUtil.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) { }

            @Override
            public void permissionDenied(@NonNull String[] permission) { }
        }, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
    }

    private void initView() {
//        gson = new Gson();
        mContext = this;
//        WuhunDrawTools.DrawTextViewUnderline(tvLoginRegister);
//        WuhunDrawTools.DrawTextViewUnderline(tvLoginQueryPwd);
        etLoginNum.setText(getSPUtil().getUsername());
    }

    @OnClick({R.id.btn_login_sign, R.id.tv_login_query_pwd, R.id.tv_login_register, R.id.tv_browse})
    public void onClick(View view) {
        if (!WuhunNetTools.isAvailable(getContext())) {
            WuhunToast.error("网络无法访问，请检查网络连接").show();
            return;
        }
        switch (view.getId()) {
            case R.id.btn_login_sign:
                login();
                break;
            case R.id.tv_login_query_pwd:
                // TODO: 2017/11/8 忘记密码
                jumpToActivity(FotgotPwdActivity.class);
                break;
            case R.id.tv_login_register:
                // TODO: 2017/11/8 注册
                jumpToActivity(RegistActivity.class);
                break;
            case R.id.tv_browse:
//                WuhunToast.info("游客方式浏览").show();
                getSPUtil().setUSERID("");
                getSPUtil().setTokens("");
                getSPUtil().setRctoken("");
                getSPUtil().setUsername("");

                showLoadDialog();
                handler.sendEmptyMessageDelayed(BROWSE, 500);
                break;
            default:
                WuhunToast.info("没有找到内容");
                break;
        }
    }

    private String imei;

    private void login() {
//        准备参数
        username = etLoginNum.getText().toString().trim();
        password = etLoginPwd.getText().toString().trim();

        if (PermissionsUtil.hasPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            imei = WuhunDeviceTool.getIMEI(LoginActivity.this);
        } else {
            PermissionsUtil.requestPermission(this, new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permissions) {
                    imei = WuhunDeviceTool.getIMEI(LoginActivity.this);
                }

                @Override
                public void permissionDenied(@NonNull String[] permissions) {
                }
            }, new String[]{Manifest.permission.READ_PHONE_STATE});
        }

//      非空校验
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
//        加载数据
        LoadDialog.show(LoginActivity.this);
        // TODO: 2017/11/9 网络请求
        if (MyApplication.isDebug) {
            handler.sendEmptyMessageDelayed(LOGIN_SUCCESS, 1000);
        } else {
            HttpUtil.login(username, password, imei, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    handler.sendEmptyMessage(LOGIN_ERROR);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        String result = response.body().string();
                        LoginUserModel loginModel = getGson().fromJson(result, LoginUserModel.class);
                        if (null == loginModel) {
                            handler.sendEmptyMessage(LOGIN_ERROR);
                            return;
                        }

                        if (loginModel.getCode() == 1) {//成功
                            WuhunDebug.debug("登录成功=>" + result);
                            getSPUtil().setUSERID(loginModel.getResult().getUserId());
                            getSPUtil().setTokens(loginModel.getResult().getTokens());
                            getSPUtil().setRctoken(loginModel.getResult().getToken());
                            getSPUtil().setUsername(etLoginNum.getText().toString());

                            List<RolesModel> roles = loginModel.getResult().getRoles();
                            getSPUtil().setRoletype(roles.get(roles.size() - 1).getRoleType());
                            TestLog.i("权限类型：" + roles.get(roles.size() - 1).getRoleType() + " == " + roles.get(roles.size() - 1).getRoleName());
//                            WuhunDebug.debug("==>" + getSPUtil().getUsername());
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
                    } else {
                        handler.sendEmptyMessage(LOGIN_ERROR);
                    }
                }
            });
        }
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOGIN_SUCCESS) {
                initConstacts();
            } else if (msg.what == LOGIN_FAIL) {
                LoginUserModel model = ((LoginUserModel) msg.obj);
                if (null != model.getMsg()) {
                    WuhunToast.normal(model.getMsg()).show();
                } else {
                    WuhunToast.normal("用户名或密码错误").show();
                }
            } else if (msg.what == LOGIN_ERROR) {
                WuhunToast.normal(getResources().getString(R.string.request_error)).show();
            } else if (msg.what == BROWSE) {
                jumpToActivity(MainActivity.class);
//            }else if(msg.what == FIND_GROUP_MEMEBERLIST) {
//                List<GroupModel> groups= (List<GroupModel>) msg.obj;
//                initGroupMemeberList(groups);
            } else if (msg.what == GO_TO_MAIN) {
                LoadDialog.dismiss(mContext);
                jumpToActivity(MainActivity.class);
                LoginActivity.this.finish();
            }
            LoadDialog.dismiss(LoginActivity.this);
            super.handleMessage(msg);
        }
    };

    private void initConstacts() {
        initFriendList();
        initGroupList();
    }

    /**
     * 同步好友列表
     */
    private void initFriendList() {
        /** 获取好友列表 */
        HttpUtil.getFriendList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    FriendListModel friendList = getGson().fromJson(result, FriendListModel.class);
                    if (friendList != null && friendList.getCode() == 1 && friendList.getResult() != null) {
                        String HOME_URL = friendList.getBefore();
                        List<Friend> friends = friendList.getResult();
                        DBManager.getmInstance().setAllUserInfo(friends);
                    }
                }
            }
        });
    }

    /**
     * 同步群组信息
     */
    private void initGroupList() {
        HttpUtil.getGroupList(null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                BaseModel<GroupModel> model = MyApplication.getGson().fromJson(result, new TypeToken<BaseModel<GroupModel>>() {
                }.getType());
                if (model != null && model.getCode() == 1 && response.isSuccessful()) {
                    List<GroupModel> groups = model.getResult();
                    String before = WuhunDataTool.isNullString(model.getBefore()) ? Constant.HOME_URL : model.getBefore();
                    DBManager.getmInstance().saveGroups(groups, before);

                    handler.sendEmptyMessageDelayed(GO_TO_MAIN, 1000);
                }
            }
        });
    }

    private void showLoadDialog() {
        ld = new LoadDialog(mContext, false, "正在加载中……");
        ld.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (ld != null && ld.isShowing()) {
            ld.dismiss();
            ld = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
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
