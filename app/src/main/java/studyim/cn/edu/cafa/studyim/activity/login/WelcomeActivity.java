package studyim.cn.edu.cafa.studyim.activity.login;

import android.os.Bundle;
import android.text.TextUtils;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.MainActivity;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        init();
    }

    private void init() {
        if (!TextUtils.isEmpty(getSPUtil().getTokens())) {
            goToMain();
        } else {
            goToLogin();
        }
    }

    private void goToMain() {
        jumpToActivity(MainActivity.class);
        WelcomeActivity.this.finish();
    }

    private void goToLogin(){
        jumpToActivity(LoginActivity.class);
        WelcomeActivity.this.finish();
    }
}
