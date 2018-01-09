package tools.com.lvliangliang.wuhuntools.util;

import android.view.Window;
import android.view.WindowManager;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/8 0008
 * 版    本：1.0
 * 描    述：屏幕相关
 * 修订历史：
 * ================================================
 */
public class WuhunScreenTool {

    /**
     *
     * @param window  window对象
     * @param shadowValue   背景值：
     */
    public static void setDialogBackShadow(Window window, float shadowValue){
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
//        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setAttributes(layoutParams);
//        window.setGravity(Gravity.BOTTOM); //底部
//        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//宽高
    }

}
