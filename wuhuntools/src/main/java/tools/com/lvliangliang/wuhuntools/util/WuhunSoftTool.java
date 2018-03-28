package tools.com.lvliangliang.wuhuntools.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/3/28 0028
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunSoftTool {

    public static InputMethodManager getInputManager(Activity activity){
        return (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    /** 强制隐藏软键盘 */
    public static void hideSoft(Activity activity){
        InputMethodManager imm = getInputManager(activity);
        if (imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void showSoft(Activity activity, View view){
        InputMethodManager imm = getInputManager(activity);
        if(imm != null) {
            imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        }
    }


    public static void toggleSoft(Activity activity){
        InputMethodManager imm = getInputManager(activity);
        if(imm != null) {
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
