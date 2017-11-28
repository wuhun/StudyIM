package tools.com.lvliangliang.wuhuntools.widget;

import android.graphics.Paint;
import android.widget.TextView;

/**
 * 作者：悟魂 ————2017/11/8 0008.
 * 版本：1.0
 * 说明：绘制图形相关
 */

public class WuhunDrawTools {

    public static void DrawTextViewUnderline(TextView tv){
        tv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv.getPaint().setAntiAlias(true);//这里要加抗锯齿
    }
}
