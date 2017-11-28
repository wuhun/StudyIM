package tools.com.lvliangliang.wuhuntools.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

/**
 * 作者：悟魂 ————2017/11/2 0002.
 * 版本：1.0
 * 说明：
 */

public class WuhunFileTool {

    /**
     * 写入文件
     * @param filepath 文件路径 带文件名和后缀
     * @param logStr   输出内容
     */
    public static void mFileWrite(String filepath, String logStr) {
        FileWriter fw = null;
        try {
            File file = new File(filepath);
            if (file.length() + logStr.length() >= 1024 * 1024) {
                //重写写入
                fw = new FileWriter(filepath, false);
            } else {
                //追加内容
                fw = new FileWriter(filepath, true);
            }
            fw.write(logStr + "\r\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (fw != null) {
                    fw.close();
                    fw = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 以UTF-8的形式写入文件
     * @param filePath
     * @param logstr
     */
    public static void writeChineseFile(String filePath, String logstr) {
        FileOutputStream fos = null;
        Writer out = null;
        try {
            fos = new FileOutputStream(filePath);
            out = new OutputStreamWriter(fos, "UTF-8");
            out.write(logstr);
            out.flush();
        } catch (IOException e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        } finally {
            closeIO(out,fos);
        }
    }

    /**
     * 读取文件内容：
     * @param file  文件，包含文件名即后缀
     * @return
     */
    public static String bufferInputRead(File file){
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        StringBuffer sb = new StringBuffer();
        String line = "";
        try {
            read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            bufferedReader = new BufferedReader(read);
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            closeIO(bufferedReader,read);
        }
        return null;
    }


    /**
     * 关闭IO
     *
     * @param closeables closeable
     */
    public static void closeIO(Closeable... closeables) {
        if (closeables == null) return;
        try {
            for (Closeable closeable : closeables) {
                if (closeable != null) {
                    closeable.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
