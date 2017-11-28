package tools.com.lvliangliang.wuhuntools;

import android.app.Application;
import android.content.Context;

import tools.com.lvliangliang.wuhuntools.exception.WuhunCrashTools;

/**
 * 作者：悟魂 ————2017/11/2 0002.
 * 版本：1.0
 * 说明：
 */

public class WuhunTools {

    private static Context mContext;

    /**
     * 初始化方法
     *
     * @param application
     */
    public static void init(Application application) {
        WuhunTools.mContext = application.getApplicationContext();
        WuhunCrashTools.getInstance(application).init();
    }

    /**
     * 获取ApplicationContext
     *
     * @return ApplicationContext
     */
    public static Context getContext() {
        if (mContext != null) return mContext;
        throw new NullPointerException("请先调用init()方法");
    }

    /////////////////////////////////////////////////////////////////////////////////
    //// 我是华丽丽的分割线~
    /////////////////////////////////////////////////////////////////////////////////


}
