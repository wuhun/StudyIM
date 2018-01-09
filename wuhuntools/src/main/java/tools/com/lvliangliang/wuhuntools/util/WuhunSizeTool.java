package tools.com.lvliangliang.wuhuntools.util;

import tools.com.lvliangliang.wuhuntools.WuhunTools;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/5 0005
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunSizeTool {

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue) {
        final float scale = getScale();
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(float pxValue) {
        final float scale = getScale();
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px( float spValue) {
        final float fontScale = getScale();
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp( float pxValue) {
        final float fontScale = getScale();
        return (int) (pxValue / fontScale + 0.5f);
    }


    private static float getScale() {
        return WuhunTools.getContext().getResources().getDisplayMetrics().density;
    }
}
