package studyim.cn.edu.cafa.studyim.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import cn.smssdk.SMSSDK;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

/**
 * 作者：悟魂 ————2017/11/8 0008.
 * 版本：1.0
 * 说明：
 */

public class BaseActivity extends AppCompatActivity {

    private List<Activity> activityList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //避免视频播放出现闪烁
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //硬件加速
        getWindow().setFlags(
                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        //防止键盘遮挡
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音

        this.addActivity(this);
    }

    public void addActivity(Activity activity) {
        activityList.add(this);
    }

    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    public void closeActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    public void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    public void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

//    public void jumpToWebViewActivity(String url) {
//        Intent intent = new Intent(this, WebViewActivity.class);
//        intent.putExtra("url", url);
//        jumpToActivity(intent);
//    }

//    public void jumpToActivityAndClearTask(Class activity) {
//        Intent intent = new Intent(this, activity);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//        finish();
//    }
//
//    public void jumpToActivityAndClearTop(Class activity) {
//        Intent intent = new Intent(this, activity);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
    public void resultFailToast(String msg) {
        if (WuhunDataTool.isNullString(msg)) {
            WuhunToast.normal(msg).show();
        } else {
            WuhunToast.normal("获取失败").show();
        }
    }


    public void showNoNetDialog(Context mContext){
        new AlertDialog.Builder(mContext)
                .setTitle("请检查网络")
                .setMessage("当前无网络连接，请检查网络状态")
                .setPositiveButton("确定", null).show();
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, String phone, String code) {
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.removeActivity(this);
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }
}
