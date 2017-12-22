package studyim.cn.edu.cafa.studyim.util;


import android.os.Handler;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/7 0007
 * 版    本：1.0
 * 描    述：线程管理工具
 * 修订历史：
 * ================================================
 */
public class ThreadUtils {

    public static void runInSubThread(Runnable run){
        new Thread().start();
    }

    public static void runInUiThread(Runnable run){
        handler.post(run);
    }

    private static Handler handler = new Handler();
}
