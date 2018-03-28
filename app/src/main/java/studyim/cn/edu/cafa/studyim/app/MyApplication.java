package studyim.cn.edu.cafa.studyim.app;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mob.MobSDK;
import com.tencent.smtt.sdk.QbSdk;

import java.io.File;

import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.util.SPUtil;
import tools.com.lvliangliang.wuhuntools.WuhunTools;
import tools.com.lvliangliang.wuhuntools.app.WuhunAppTool;
import tools.com.lvliangliang.wuhuntools.exception.WuhunDebug;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;

/**
 * 作者：悟魂 ————2017/11/7 0007.
 * 版本：1.0
 * 说明：全局application
 */

public class MyApplication extends RongApplication {

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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void init() {
        this.mContext = getApplicationContext();
        WuhunTools.init(this);
        WuhunDebug.getInstence(false);
        initX5webview();
        initData();
    }

    private void initX5webview() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            @Override
            public void onViewInitFinished(boolean arg0) {}

            @Override
            public void onCoreInitFinished() {}
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }

    private void initData() {
        isDebug = false;
        gson = new GsonBuilder().setLenient().create();
        MobSDK.init(this, "2409fdff977a5", "78d9694ad5afe70d33e3d8fea2cd63f8"); //mob初始化
    }

    public static Gson getGson() {
        if(null == gson) {
            gson = new GsonBuilder().setLenient().create();
        }
        return gson;
    }

    /** 获取更新数据 */
    public static String updateSettingFile(){
        String downPath = WuhunFileTool.getRootPath() + File.separator + WuhunAppTool.getAppName(mContext) + File.separator + "Setting" + File.separator;
        String setting_path = downPath + WuhunFileTool.getFileName(Constant.ME_FRAGMENT_UPDATE_SETTINGS);
        if (WuhunFileTool.createOrExistsFile(setting_path)) {
            return setting_path;
        } else {
            return null;
        }
    }

    /** 获取拍照相机目录 */
    public static File getCameraPifFile(String fileName){
        String pic = WuhunFileTool.getRootPath() + File.separator + WuhunAppTool.getAppName(mContext) + File.separator + "pictrue" + File.separator;
        File file = new File(pic, fileName);
        return file;
    }

    /** 获取下载文件目录 */
    public static File getDownloadFile(String fileName){
        String pic = WuhunFileTool.getRootPath() + File.separator + WuhunAppTool.getAppName(mContext) + File.separator + "download" + File.separator;
        File file = new File(pic, fileName);
        return file;
    }

    /** 获取下载文件目录 */
    public static File getUploadFile(String backSpace, String fileName){
        String pic = WuhunFileTool.getRootPath() + File.separator + WuhunAppTool.getAppName(mContext) + File.separator + backSpace + File.separator;
        File file = new File(pic, fileName);
        return file;
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
