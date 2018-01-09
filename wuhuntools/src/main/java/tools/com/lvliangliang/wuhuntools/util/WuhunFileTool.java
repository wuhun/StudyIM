package tools.com.lvliangliang.wuhuntools.util;

import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/28 0028
 * 版    本：1.0
 * 描    述：
 *      sdCardIsAvailable           sd卡是否可用
 * 修订历史：
 *
 * 备份点：
 *      File.separator          代表"\"，用于拼接目录
 * ================================================
 */
public class WuhunFileTool {

    /** 文件名称 */
    public static String getFileName(String FileName){
        String name = null;
        if (FileName.indexOf("/") != -1) {
            name = FileName.substring(FileName.lastIndexOf("/")+1);
        } else {
            name = FileName;
        }
        return name;
    }

    public static boolean deleteFileForUri(Uri FileUri){
        if (FileUri == null) return false;

        File file = new File(FileUri.getPath());
        if (file.exists())
            return file.delete();
        else
            return false;
    }

    /**
     * 得到SD卡根目录.
     */
    public static File getRootPath() {
        File path = null;
        if (sdCardIsAvailable()) {
            path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
        } else {
            path = Environment.getDataDirectory();  // "/data"
        }
        return path;
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param filePath 文件路径
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(String filePath) {
        return createOrExistsFile(getFileByPath(filePath));
    }

    /**
     * 根据文件路径获取文件
     *
     * @param filePath 文件路径
     * @return 文件
     */
    public static File getFileByPath(String filePath) {
        return WuhunDataTool.isNullString(filePath) ? null : new File(filePath);
    }

    /**
     * 判断文件是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsFile(File file) {
        if (file == null) return false;
        // 如果存在，是文件则返回true，是目录则返回false
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断目录是否存在，不存在则判断是否创建成功
     *
     * @param file 文件
     * @return {@code true}: 存在或创建成功<br>{@code false}: 不存在或创建失败
     */
    public static boolean createOrExistsDir(File file) {
        // 如果存在，是目录则返回true，是文件则返回false，不存在则返回是否创建成功
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 获取sd卡根目录
     * @return
     */
    public static File getSDPath(){
        if (sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return null;
        }
    }

    /**
     * SD卡是否可用.
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sd = new File(Environment.getExternalStorageDirectory().getPath());
            return sd.canWrite();
        } else
            return false;
    }
}
