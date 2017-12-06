package studyim.cn.edu.cafa.studyim.util;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 作者：悟魂 ————2017/9/22 0022.
 * 版本：1.0
 * 说明：
 */

public class OkHttpUtil {

    private static final OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(60000, TimeUnit.MILLISECONDS).build();

    /**
     * 该不会开启异步线程。
     * @param request
     * @return
     * @throws IOException
     */
    public static Response execute(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    /**
     * 开启异步线程访问网络
     * @param request
     * @param responseCallback
     */
    public static void enqueue(Request request, Callback responseCallback) {
        mOkHttpClient.newCall(request).enqueue(responseCallback);
    }

    /** get请求 String-json 数据 */
    public static String get(String url){
        try {
            Request request = new Request.Builder().url(url).build();
            Response response  = execute(request);
            if(response.isSuccessful()){
                return response.body().string();
            }else{
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** get请求 String-json 数据 */
    public static void get(String url, Callback callback){
            Request request = new Request.Builder().url(url).build();
            enqueue(request, callback);
    }

    public static void post(String url) {
        try {
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), "1");
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ajax_post(String url, Callback callback) {
        //创建 MediaType 设置上传文件类型
        MediaType MEDIATYPE = MediaType.parse("text/plain; charset=utf-8");
        //获取请求体
        RequestBody requestBody = RequestBody.create(MEDIATYPE, "");
        //创建请求
        Request request = new Request.Builder().url(url)
                .post(requestBody)
                .build();
        enqueue(request, callback);
    }

    public static void post(String url, String json, Callback callback) {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /** 异步get请求 */
    private void ajax_get(String url, Callback callback) {
        Request request = new Request.Builder().url(url).build();
        enqueue(request, callback);
    }
    /////////////////////////////////////////////////////////////////////////////////
    //// 我是华丽丽的分割线~
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 文件上传表单
     * @param url
     * @param map      提交字段
     * @param filemap       提交文件位置
     * @param callback
     */
    public static void post(String url, Map<String, String> map, Map<String, File> filemap, Callback callback) {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
                //TestLog.i("sfz 图片上传参数"+entry.getKey() + " - " + entry.getValue());
            }
        }
        if (filemap != null) {
            for (Map.Entry<String, File> entry : filemap.entrySet()) {
                builder.addFormDataPart(
                        entry.getKey(),
                        (entry.getValue()).getName(),
                        RequestBody.create(MediaType.parse("image/png"), entry.getValue()));
                //TestLog.i("sfz 图片上传参数"+entry.getKey() + " - " + (entry.getValue()).getName() + " - " + (entry.getValue()).getAbsolutePath());
            }
        }
        // 构建请求体
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        mOkHttpClient.newCall(request).enqueue(callback);
    }

    /**
     * 普通字段提交
     */
    public static void post(String url, Map<String, String> map, Callback callback) {
//        TestLog.i("请求地址：" + url);
//        StringBuffer sb = new StringBuffer();
//        sb.append("参数：");

        FormBody.Builder builder = new FormBody.Builder();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                builder.add(entry.getKey(), entry.getValue().toString());
//                sb.append(entry.getKey() + " - " + entry.getValue().toString() + "\n");
            }
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url)
                .header("content-type", "text/html;charset:utf-8")
                .post(requestBody)
                .build();

//        TestLog.i("==> " + sb.toString());
        mOkHttpClient.newCall(request).enqueue(callback);
    }

}
