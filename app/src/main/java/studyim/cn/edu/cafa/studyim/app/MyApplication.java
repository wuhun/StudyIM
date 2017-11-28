package studyim.cn.edu.cafa.studyim.app;

import android.app.Application;
import android.content.Context;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import tools.com.lvliangliang.wuhuntools.WuhunTools;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;

/**
 * 作者：悟魂 ————2017/11/7 0007.
 * 版本：1.0
 * 说明：全局application
 */

public class MyApplication extends Application{

    public static Context mContext;
    public static boolean isDebug;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        this.mContext = getApplicationContext();
        WuhunTools.init(this);
        WuhunDebug.getInstence(true);
        initOKGo();
        initData();
    }

    private void initData() {
        isDebug = false;
    }


    private void initOKGo() {
        // okgo日志
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("okgo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);//日志级别
        loggingInterceptor.setColorLevel(Level.INFO);
        // okhttp3.8.1初始化
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .cookieJar(new CookieJarImpl(new DBCookieStore(WuhunTools.getContext())));
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build());

    }

    /** 获取context */
    public static Context getContext(){
        if(mContext!=null) return mContext;
        throw new NullPointerException("Please executer MyApplication.init() 初始化失败");
    }

}
