package studyim.cn.edu.cafa.studyim.base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

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


    @Override
    protected void onDestroy() {
        this.removeActivity(this);
        super.onDestroy();
    }
}
