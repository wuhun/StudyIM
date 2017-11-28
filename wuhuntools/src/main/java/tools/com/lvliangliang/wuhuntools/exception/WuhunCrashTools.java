package tools.com.lvliangliang.wuhuntools.exception;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

/**
 * 作者：悟魂 ————2017/11/2 0002.
 * 版本：1.0
 * 说明：java系统提供的 UncaughtExceptionHandle
 *      用于 检测某个由于未捕获的异常 而终结的情况
 * 参考：http://blog.csdn.net/cym_lmy/article/details/24704089
 */

public class WuhunCrashTools implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler mHandler;
    private boolean mInitialized;//是否初始化成功
    private String mCrashDirPath;//错误日志写入目录
    private String mVersionName;
    private int mVersionCode;

    private Application mApplication;

    /////////////////////////////////////////////////////////////////////////////////
    //// 单例模式~
    private volatile static WuhunCrashTools mInstance;

    private WuhunCrashTools(Application application) {
        this.mApplication = application;
    }
    /**
     * 获取单例
     * <p>在Application中初始化{@code RxCrashTool.getInstance().init(this);}</p>
     *
     * @return 单例
     */
    public static WuhunCrashTools getInstance(Application application) {
        if (mInstance == null) {
            synchronized (WuhunCrashTools.class) {
                if (mInstance == null) {
                    mInstance = new WuhunCrashTools(application);
                }
            }
        }
        return mInstance;
    }
    /////////////////////////////////////////////////////////////////////////////////

    /**
     * 初始化
     *
     * @return {@code true}: 成功<br>{@code false}: 失败
     */
    public boolean init() {
        // 保证只初始化一次
        if (mInitialized) return true;
//        try {
//            PackageManager packageManager = mContext.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
//            int labelRes = packageInfo.applicationInfo.labelRes;
//            String name = mContext.getResources().getString(labelRes);
//            mCrashDirPath = RxFileTool.getRootPath() + File.separator + name + File.separator + "crash" + File.separator;
//        } catch (Exception e) {
//            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                mCrashDirPath = mContext.getExternalCacheDir().getPath() + File.separator + "crash" + File.separator;
//            } else {
//                mCrashDirPath = mContext.getCacheDir().getPath() + File.separator + "crash" + File.separator;
//            }
//        }

        //初始化应用信息
        try {
            PackageInfo pi = mApplication.getPackageManager().getPackageInfo(mApplication.getPackageName(), 0);
            mVersionName = pi.versionName;
            mVersionCode = pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        // 添加自定义捕获异常
        mHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(WuhunCrashTools.this);
        return mInitialized = true;//初始化成功
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
//        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
//        final String fullPath = mCrashDirPath + now + ".txt";
//        if (!RxFileTool.createOrExistsFile(fullPath)) return;
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PrintWriter pw = null;
//                try {
//                    pw = new PrintWriter(new FileWriter(fullPath, false));
//                    pw.write(getCrashHead());
//                    throwable.printStackTrace(pw);
//                    Throwable cause = throwable.getCause();
//                    while (cause != null) {
//                        cause.printStackTrace(pw);
//                        cause = cause.getCause();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } finally {
//                    RxFileTool.closeIO(pw);
//                }
//            }
//        }).start();

        //打印错误日志
        new Thread(new Runnable() {
            @Override
            public void run() {
                String crashHead = getCrashHead();
                TestLog.i(crashHead);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    throwable.printStackTrace(new PrintStream(baos));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    WuhunFileTool.closeIO(baos);
                }
                TestLog.i("==byteArrayOutputStream==>" + baos.toString());

                TestLog.i("==Cause==>" + throwable.getCause());
                TestLog.i("==message==>" + throwable.getMessage());
            }
        }).start();

        //是否自定义处理异常
        if (!handleException(throwable) && mHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mHandler.uncaughtException(thread, throwable);
        } else {
            try {
//                RxToast.error(mContext, "很抱歉,程序异常,即将退出应用.").show();
//                RxToast.error(mContext, "很抱歉,程序异常,即将退出应用.").show();
                TestLog.e("很抱歉,程序异常,即将退出应用1.");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                TestLog.i("error : " + e.toString());
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);//非正常退出，就是说无论程序正在执行与否，都退出，
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                WuhunToast.error("很抱歉,程序出现异常,即将退出.", true).show();
                Looper.loop();
            }
        }.start();
        return true;
    }

    /**
     * 获取崩溃头
     *
     * @return 崩溃头
     */
    private String getCrashHead() {
        return "\n************* Crash Log Head ****************" +
                "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                "\nDevice Model       : " + Build.MODEL +// 设备型号
                "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                "\nApp VersionName    : " + mVersionName +
                "\nApp VersionCode    : " + mVersionCode +
                "\n************* Crash Log Head ****************\n\n";
    }

//    private void showDialog(Throwable throwable) {
//        Looper.prepare();
//        new AlertDialog.Builder(mApplication)
//                .setTitle("提示")
//                .setCancelable(false)
//                .setMessage("程序崩溃了..." + throwable.getMessage())
//                .setNeutralButton("我知道了", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        System.exit(0);//:正常退出，程序正常执行结束退出
//                    }
//                })
//                .create().show();
//        Looper.loop();
//    }
}
