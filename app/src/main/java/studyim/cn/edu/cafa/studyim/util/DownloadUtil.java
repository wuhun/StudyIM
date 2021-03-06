package studyim.cn.edu.cafa.studyim.util;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;

/**
 * 作者：悟魂 ————2017/11/29.
 * 版本：1.0
 * 说明：
 */

public class DownloadUtil {

    private static DownloadUtil instence;
    private final OkHttpClient okHttpClient;

    public static DownloadUtil getInstence() {
        if (instence == null) {
            instence = new DownloadUtil();
        }
        return instence;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    public void download(final String url) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                TestLog.i("==>onfailure");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                TestLog.i("==>response \n" + response.body().string());
            }
        });
    }


    /**
     * @param url      下载连接
     * @param saveDir  储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
//                TestLog.i("==>onfailure");
                e.printStackTrace();
                listener.onDownloadFailed();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                TestLog.i("==>response");
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
//                String savePath = isExistDir(saveDir);
                File file = new File(saveDir);
//                TestLog.i("=下载存储目录=>" + file.getAbsolutePath());
                try {
                    if (WuhunFileTool.createOrExistsFile(file)) {
                        is = response.body().byteStream();
                        long total = response.body().contentLength();
//                    File file = new File(savePath, getNameFromUrl(url));
                        fos = new FileOutputStream(file);
                        long sum = 0;
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            sum += len;
                            int progress = (int) (sum * 1.0f / total * 100);
                            // 下载中
                            listener.onDownloading(progress);
                        }
                        fos.flush();
                        // 下载完成
                        listener.onDownloadSuccess();
                    } else {
                        listener.onDownloadFailed();
                    }
                } catch (Exception e) {
//                    TestLog.i("==>" + e.getMessage());
                    e.printStackTrace();
                    listener.onDownloadFailed();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置 Environment.getExternalStorageDirectory()
        File downloadFile = new File(WuhunFileTool.getRootPath(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param url
     * @return 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess();

        /**
         * @param progress 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed();
    }
}

