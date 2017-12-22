package tools.com.lvliangliang.wuhuntools.app;

import android.app.ActivityManager;
import android.content.Context;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/6 0006
 * 版    本：1.0
 * 描    述：工具类
 * 修订历史：
 * ================================================
 */
public class WuhunApplication {

    /**
     *  获取当前进程
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context){
        int pid = android.os.Process.myPid();//当前进程
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if(appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

}
