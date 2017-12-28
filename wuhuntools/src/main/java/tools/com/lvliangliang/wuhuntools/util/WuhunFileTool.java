package tools.com.lvliangliang.wuhuntools.util;

import android.os.Environment;

import java.io.File;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/28 0028
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class WuhunFileTool {

    /**
     * 获取sd卡根目录
     * @return
     */
    public static File getSDPath(){
        if (sdCardExist()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    /**
     * 判断sd卡是否存在
     * @return
     */
    public static boolean sdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
}
