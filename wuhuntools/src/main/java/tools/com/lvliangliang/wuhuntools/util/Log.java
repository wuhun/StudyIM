package tools.com.lvliangliang.wuhuntools.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 日志记录
 */
public class Log {
    private static Log mLogFile;

    private static final String EMPTY = "";
    private static String filePath = "/sdcard"; //在MyApplication中设置
    private static final String LOG_FILE_NAME = "sipapplog.txt";

    public static Log getLogInstance() {
        if (mLogFile == null) {
            mLogFile = new Log();
        }
        return mLogFile;
    }

    /**
     * 设置日志路径
     */
    public static void setFilePath(String path) {
        filePath = path;
    }

    /**
     * 日志
     */
    public static void d(String logTag, String logStr) {
        writeLogToFile(logTag, logStr);
        android.util.Log.d(logTag, logStr);
    }

    public static void d(String logTag, String logStr, Throwable e) {
        String exceptionStr = logStr + '\n' + android.util.Log.getStackTraceString(e);
        writeLogToFile(logTag, exceptionStr);
        android.util.Log.d(logTag, logStr, e);
    }

    public static void i(String logTag, String logStr) {
        writeLogToFile(logTag, logStr);
        android.util.Log.i(logTag, logStr);
    }

    public static void i(String logTag, String logStr, Throwable e) {
        String exceptionStr = logStr + '\n' + android.util.Log.getStackTraceString(e);
        writeLogToFile(logTag, exceptionStr);
        android.util.Log.i(logTag, logStr, e);
    }

    public static void w(String logTag, String logStr) {
        writeLogToFile(logTag, logStr);
        android.util.Log.w(logTag, logStr);
    }

    public static void w(String logTag, String logStr, Throwable e) {
        String exceptionStr = logStr + '\n' + android.util.Log.getStackTraceString(e);
        writeLogToFile(logTag, exceptionStr);
        android.util.Log.w(logTag, logStr, e);
    }

    public static void e(String logTag, String logStr) {
        writeLogToFile(logTag, logStr);
        android.util.Log.e(logTag, logStr);
    }

    public static void e(String logTag, String logStr, Throwable e) {
        String exceptionStr = logStr + '\n' + android.util.Log.getStackTraceString(e);
        writeLogToFile(logTag, exceptionStr);
        android.util.Log.e(logTag, logStr, e);
    }

    public static void e(String logTag, Throwable e) {
        e(logTag, "", e);
    }

    /**
     * 记录日志
     *
     * @param logTag
     * @param logStr
     */
    private static void writeLogToFile(String logTag, String logStr) {
        SimpleDateFormat formater = new SimpleDateFormat("'<'yyyy-MM-dd HH:mm:ss.SSS'>'", Locale.getDefault());
        String date = formater.format(new Date());

        String filepath = createLogFile();
        File file = new File(filepath);

        StringBuffer sb = new StringBuffer();
        sb.append(date + " " + logTag + " " + logStr);
//        IOUtil.mFileWrite(filepath, logStr);
    }

    /**
     * 创建log路径
     *
     * @return 返回创建的路径
     */
    private static String createLogFile() {
        //File.separator考虑到跨平台。相当于"/"
        File file = new File(filePath + File.separator + LOG_FILE_NAME);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file.getPath();
    }
}
