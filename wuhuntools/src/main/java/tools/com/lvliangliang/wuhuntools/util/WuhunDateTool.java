package tools.com.lvliangliang.wuhuntools.util;

import java.text.SimpleDateFormat;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/22 0022
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunDateTool {

    private static final String yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    private static final String MMdd_HHmm = "mm/dd HH:mm";

    /**
     * 获取时间类型为：yyyy-MM-dd HH:mm:ss
     * @param time  long类型的当前时间
     * @return      yyyy-MM-dd HH:mm:ss
     */
    public static String getyyyyMMddHHmmss(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(yyyyMMdd_HHmmss);
        return sdf.format(time);
    }

    /**
     *
     * @param time
     * @return
     */
    public static String getMMddHHmm(long time){
        SimpleDateFormat sdf = new SimpleDateFormat(MMdd_HHmm);
        return sdf.format(time);
    }
}
