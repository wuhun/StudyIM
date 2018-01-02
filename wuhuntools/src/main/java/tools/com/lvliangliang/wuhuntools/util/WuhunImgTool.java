package tools.com.lvliangliang.wuhuntools.util;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2018/1/2 0002
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunImgTool {

    public static boolean isImage(String imgPath){
        if(WuhunDataTool.isNullString(imgPath))
            return false;
        String path = imgPath.toLowerCase();
        return path.endsWith(".png") || path.endsWith(".jpg")
                || path.endsWith(".jpeg") || path.endsWith(".bmp") || path.endsWith(".gif");
    }

}
