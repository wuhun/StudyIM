package tools.com.lvliangliang.wuhuntools.util;

import android.os.Handler;
import android.os.Looper;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/28 0028
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunThread {

    public static final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * 运行到主线程 - 用于回调中控制ui视图
     * 常用方法：
     *      1、handler发送数据
     *      2、View.post(runnable)
     *      3、Activity.runOnUiThread(runnable)  //===> 对View.post封装
     * @param run
     */
    public static void runOnUiThread(Runnable run){
        if(!isMainThread()){
            mHandler.post(run);
        } else {
            run.run();
        }
    }
    /**
     * 运行在子线程
     * @param run
     */
    public static void runThread(Runnable run){
        new Thread(run).start();
    }

    /**
     * 判断当前线程是否为主线程
     * @return
     */
    public static boolean isMainThread(){
        return Looper.myLooper() == Looper.getMainLooper();
    }
}
