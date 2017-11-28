package studyim.cn.edu.cafa.studyim.activity.login;

import android.os.Bundle;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        goToLogin();
    }

    private void goToLogin(){
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        jumpToActivity(LoginActivity.class);
        WelcomeActivity.this.finish();
    }
}
