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

    public static final String DATE1 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE2 = "yyyyMMdd_HHmmss";
    public static final String DATE3 = "mm/dd HH:mm";
    public static final String DATE4 = "yyyy-MM-dd";

    /**
     * 获取时间类型结果
     * @param time              当前时间 - long类型
     * @param dateFormat        转换的格式类型
     * @return
     */
    public static String getDateFormat(long time, String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(time);
    }

}
