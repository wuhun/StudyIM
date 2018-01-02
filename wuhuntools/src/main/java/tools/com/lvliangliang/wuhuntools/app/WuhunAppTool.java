package tools.com.lvliangliang.wuhuntools.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/29 0029
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunAppTool {

    /**
     * 获取App名称
     *
     * @param context     上下文
     * @return App名称
     */
    public static String getAppName(Context context){
        String appName = null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            int labelRes = pi.applicationInfo.labelRes;
            appName = context.getResources().getString(labelRes);//应用程序名称
            if(appName == null)
                return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();//应用程序名称
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appName;
    }
}
