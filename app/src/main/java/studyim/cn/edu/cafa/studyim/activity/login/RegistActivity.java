package studyim.cn.edu.cafa.studyim.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.model.PhoneCodeModel;
import studyim.cn.edu.cafa.studyim.util.downtime.DownTimer;
import studyim.cn.edu.cafa.studyim.util.downtime.DownTimerListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.ui.ClearWriteEditText;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.loadDialog.LoadDialog;

public class RegistActivity extends BaseActivity {

    @BindView(R.id.body_img_backgroud)
    ImageView bodyImgBackgroud;
    @BindView(R.id.body_frm_backgroud)
    FrameLayout bodyFrmBackgroud;
    @BindView(R.id.login_logo)
    ImageView loginLogo;
    @BindView(R.id.nickname)
    ClearWriteEditText nickname;
    @BindView(R.id.phone_num)
    ClearWriteEditText phoneNum;
    @BindView(R.id.reg_code)
    ClearWriteEditText regCode;
    @BindView(R.id.reg_getcode)
    Button regGetcode;
    @BindView(R.id.login_pwd)
    ClearWriteEditText loginPwd;
    @BindView(R.id.new_login_pwd)
    ClearWriteEditText newLoginPwd;
    @BindView(R.id.btn_regist)
    Button btnRegist;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.et_country)
    EditText etCountry;

    private Context mContext;
    private boolean isRequestCode = false; //是否请求验证码
    boolean isBright = true; //
    private String mPhone, mCode, mNickName, mPassword, mNewPassword, country;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);

        initView();
        initListener();
    }

    private void initView() {
        mContext = this;
        // 创建EventHandler对象
        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    final String msg = throwable.getMessage();
                    final PhoneCodeModel model = MyApplication.getGson().fromJson(msg, PhoneCodeModel.class);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TestLog.i("mob返回结果：" + msg);
                            if(model != null && !WuhunDataTool.isNullString(model.getDetail())) {
                                Toast.makeText(mContext, model.getDetail(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //回调完成
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //验证码验证成功
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            Log.i("wuhun", "验证码验证成功");
                            regist();
                        }
                        //已发送验证码
                        else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 这里是验证成功的回调，可以处理验证成功后您自己的逻辑，需要注意的是这里不是主线程
                            Log.i("wuhun", "验证码已经发送");
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.info("验证码已经发送").show();
                                }
                            });
                        }
                    } else {
                        Log.i("wuhun", "获取验证码失败" + result);
                    }
                }
            }
        };
        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initListener() {
        regGetcode.setOnClickListener(mClick);
        btnRegist.setOnClickListener(mClick);
        addEditTextListener();
    }

    private void addEditTextListener() {
//        phoneNum.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 11 && isBright) {
//                    if (WuhunELUtil.isMobile(s.toString().trim())) {
//                        mPhone = s.toString().trim();
//                        // TODO: 2018/1/29 验证手机是否注册
////                        if (cprres.isResult()) {
////                            mGetCode.setClickable(true);
////                            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
////                        } else {
////                            mGetCode.setClickable(false);
////                            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
////                            Toast.makeText(mContext, "手机号已被注册", Toast.LENGTH_SHORT).show();
////                        }
//                        WuhunELUtil.onHideSoftInput(mContext, phoneNum);
//                    } else {
//                        Toast.makeText(mContext, "非法手机号", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    regGetcode.setClickable(false);
//                    regGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//        regCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() == 6) {
//                    WuhunELUtil.onHideSoftInput(mContext, regCode);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.reg_getcode:
                    country = etCountry.getText().toString().trim();
                    if (TextUtils.isEmpty(phoneNum.getText().toString().trim())) {
                        WuhunToast.info("请输入电话号码").show();
                    } else {
                        isRequestCode = true;
                        DownTimer downTimer = new DownTimer();
                        downTimer.setListener(new DownTimerListener() {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TestLog.i("ismain ==> " + WuhunThread.isMainThread());
                                regGetcode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
                                regGetcode.setClickable(false);
                                regGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
//                                isBright = false;
                                sendCode(country, phoneNum.getText().toString().trim());
                            }

                            @Override
                            public void onFinish() {
                                regGetcode.setText("获取验证码");
                                regGetcode.setClickable(true);
                                regGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_red_normal));
//                                isBright = true;
                            }
                        });
                        downTimer.startDown(60 * 1000);
                        // TODO: 2018/1/29 发送验证请求
                    }
                    break;
                case R.id.btn_regist:
                    mPhone = phoneNum.getText().toString().trim();
                    mCode = regCode.getText().toString().trim();
                    mNickName = nickname.getText().toString().trim();
                    mPassword = loginPwd.getText().toString().trim();
                    mNewPassword = newLoginPwd.getText().toString().trim();
                    country = etCountry.getText().toString().trim();

                    if (TextUtils.isEmpty(mNickName)) {
                        WuhunToast.info("昵称不能为空").show();
                        nickname.setShakeAnimation();
                        return;
                    }
                    if (mNickName.contains(" ")) {
                        WuhunToast.info("昵称不能包含空格").show();
                        nickname.setShakeAnimation();
                        return;
                    }

                    if (TextUtils.isEmpty(mPhone)) {
                        WuhunToast.info("手机号不能为空").show();
                        phoneNum.setShakeAnimation();
                        return;
                    }
                    if (TextUtils.isEmpty(mCode)) {
                        WuhunToast.info("请输入验证码").show();
                        regCode.setShakeAnimation();
                        return;
                    }
                    if (TextUtils.isEmpty(mPassword)) {
                        WuhunToast.info("密码不能为空").show();
                        loginPwd.setShakeAnimation();
                        return;
                    }
                    if (mPassword.contains(" ")) {
                        WuhunToast.info("密码不能包含空格").show();
                        loginPwd.setShakeAnimation();
                        return;
                    }
                    if (!mPassword.equals(mNewPassword)) {
                        WuhunToast.info("两次输入密码不一致").show();
                        loginPwd.setShakeAnimation();
                        newLoginPwd.setShakeAnimation();
                        return;
                    }
                    if (!isRequestCode) {
                        WuhunToast.info("未向服务器发送验证码").show();
                        return;
                    }
                    submitCode(country, mPhone, mCode);
                    LoadDialog.show(mContext);
                    // TODO: 2018/1/29 注册请求
                    break;
            }
        }
    };

    /** 注册成功 */
    private void regist() {}
}
