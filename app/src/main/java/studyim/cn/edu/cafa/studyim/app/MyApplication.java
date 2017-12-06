package studyim.cn.edu.cafa.studyim.app;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import studyim.cn.edu.cafa.studyim.util.SPUtil;
import tools.com.lvliangliang.wuhuntools.WuhunTools;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;

/**
 * 作者：悟魂 ————2017/11/7 0007.
 * 版本：1.0
 * 说明：全局application
 */

public class MyApplication extends Application {

    public static Context mContext;
    public static Gson gson;
    private static SPUtil spUtil;
    public static boolean isDebug;
    public static final long DEFAULT_MILLISECONDS = 20000;//20秒

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        this.mContext = getApplicationContext();
        WuhunTools.init(this);
        WuhunDebug.getInstence(true);
        initData();
    }

    private void initData() {
        isDebug = false;
        gson = new GsonBuilder().setLenient().create();
    }

    public static Gson getGson() {
        if(null == gson) {
            gson = new GsonBuilder().setLenient().create();
        }
        return gson;
    }

    //    private void initOKGo() {
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
//                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

//        HttpHeaders headers = new HttpHeaders();
////        headers.put("tokens", );    //header不支持中文，不允许有特殊字符
//        // okgo日志
//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("okgo");
//        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);//日志级别
//        loggingInterceptor.setColorLevel(Level.INFO);
//        // okhttp3.8.1初始化
//        OkHttpClient.Builder builder = new OkHttpClient.Builder()
//                .addInterceptor(loggingInterceptor)
//                .readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
//                .writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
//                .connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
//                .cookieJar(new CookieJarImpl(new DBCookieStore(WuhunTools.getContext())));
//        OkGo.getInstance().init(this)
//                .setOkHttpClient(builder.build())
//                .addCommonHeaders(headers);
//    }

    /**
     * 获取context
     */
    public static Context getContext() {
        if (mContext != null) return mContext;
        throw new NullPointerException("Please executer MyApplication.init() 初始化失败");
    }

    public static SPUtil getSPUtil(){
        if(spUtil == null) {
            spUtil = SPUtil.getInstance(mContext);
        }
        return spUtil;
    }
}
