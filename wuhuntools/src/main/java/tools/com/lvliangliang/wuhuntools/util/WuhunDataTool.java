package tools.com.lvliangliang.wuhuntools.util;

import android.support.annotation.Nullable;

import java.util.regex.Pattern;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/9 0009
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 * 各种字符的unicode编码的范围：
 *      汉字：[0x4e00,0x9fa5]（或十进制[19968,40869]）
 *      数字：[0x30,0x39]（或十进制[48, 57]）
 *      小写字母：[0x61,0x7a]（或十进制[97, 122]）
 *      大写字母：[0x41,0x5a]（或十进制[65, 90]）
 * ================================================
 */
public class WuhunDataTool {

    /**
     * 判断字符串是否为空 为空即true
     *
     * @param str 字符串
     * @return
     */
    public static boolean isNullString(@Nullable String str) {
        return str == null || str.length() == 0 || "".equals(str) || "null".equals(str);
    }

    /**
     * 判断字符串首字符是否是字母
     * @param str
     * @return boolean
     */
    public static boolean isFirstLetter(String str) {
        char c = str.charAt(0);
        if (((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串首字符是否是字母
     * @param c
     * @return boolean
     */
    public static boolean isFirstLetter(char c) {
        if (((c >= 65 && c <= 90) || (c >= 97 && c <= 122))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为汉字
     * @param str
     * @return
     */
    public boolean strIsGB2312(String str){
        char[] chars=str.toCharArray();
        boolean isGB2312=false;
        for(int i=0;i<chars.length;i++){
            byte[] bytes=(""+chars[i]).getBytes();
            if(bytes.length==2){
                int[] ints=new int[2];
                ints[0]=bytes[0]& 0xff;
                ints[1]=bytes[1]& 0xff;

                if(ints[0]>=0x81 && ints[0]<=0xFE
                        && ints[1]>=0x40 && ints[1]<=0xFE){
                    isGB2312=true;
                    break;
                }
            }
        }
        return isGB2312;
    }

    /**
     * 判断字符串是否仅为数字:
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 首字母大写
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(String s) {
        if (isNullString(s) || !Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }
}
