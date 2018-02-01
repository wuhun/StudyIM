package studyim.cn.edu.cafa.studyim.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.model.PhoneCodeModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.downtime.DownTimer;
import studyim.cn.edu.cafa.studyim.util.downtime.DownTimerListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.ui.ClearWriteEditText;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

public class fotgotPwdActivity extends BaseActivity {

    @BindView(R.id.body_img_backgroud)
    ImageView bodyImgBackgroud;
    @BindView(R.id.body_frm_backgroud)
    FrameLayout bodyFrmBackgroud;
    @BindView(R.id.login_logo)
    ImageView loginLogo;
    @BindView(R.id.student_num)
    ClearWriteEditText studentNum;
    @BindView(R.id.phone_num)
    ClearWriteEditText phoneNum;
    @BindView(R.id.reg_code)
    ClearWriteEditText regCode;
    @BindView(R.id.reg_getcode)
    Button regGetcode;
    @BindView(R.id.new_pwd)
    ClearWriteEditText newPwd;
    @BindView(R.id.again_new_pwd)
    ClearWriteEditText againNewPwd;
    @BindView(R.id.btn_forgot)
    Button btnForgot;
    @BindView(R.id.ll_more)
    LinearLayout llMore;

    String mStudentNum , mPhoneNum, mPassword;
    private Context mContext;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fotgot_pwd);
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
                            // TODO: 2018/1/31 修改密码
                            updatePwd();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            // 这里是验证成功的回调，可以处理验证成功后您自己的逻辑，需要注意的是这里不是主线程
                            Log.i("wuhun", "验证码已经发送");
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.info("验证码已经发送").show();
                                }
                            });
                        } else if(event == SMSSDK.RESULT_ERROR) {
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.error("验证码错误").show();
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

    /** 忘记密码 */
    private void updatePwd() {
        mStudentNum = studentNum.getText().toString().trim();
        mPhoneNum = phoneNum.getText().toString().trim();
        mPassword = newPwd.getText().toString().trim();
        String mNewPwd = againNewPwd.getText().toString().trim();

        if(TextUtils.isEmpty(mStudentNum)) {
            WuhunToast.error("请输入学号").show();
            return;
        }
        if(TextUtils.isEmpty(mPhoneNum)) {
            WuhunToast.error("请输入电话号码").show();
            return;
        }
        if(TextUtils.isEmpty(mPassword)) {
            WuhunToast.error("请输入新密码").show();
            return;
        }
        if(!mPassword.equals(mNewPwd)) {
            WuhunToast.error("两次密码不一致").show();
            return;
        }

        HttpUtil.changePassword(mStudentNum, mPhoneNum, mPassword, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                fotgotPwdActivity.this.finish();
            }
        });
    }


    private void initListener() {
        regGetcode.setOnClickListener(mClick);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.reg_getcode:
                    if (TextUtils.isEmpty(phoneNum.getText().toString().trim())) {
                        WuhunToast.info("请输入电话号码").show();
                    } else {
//                        isRequestCode = true;
                        DownTimer downTimer = new DownTimer();
                        downTimer.setListener(new DownTimerListener() {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                TestLog.i("ismain ==> " + WuhunThread.isMainThread());
                                regGetcode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
                                regGetcode.setClickable(false);
                                regGetcode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
//                                isBright = false;
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
            }
        }
    };
}
