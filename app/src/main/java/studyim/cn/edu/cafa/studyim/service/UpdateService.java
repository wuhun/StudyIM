package studyim.cn.edu.cafa.studyim.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.File;

import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.model.CheckVersionModel;
import studyim.cn.edu.cafa.studyim.util.DownloadUtil;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

public class UpdateService extends Service {

    public static final String SERVICE_VERSION_INFO = "service_version_info";

    private CheckVersionModel.ResultBean serviceModel;
    private String donwloadFile;
    private int notifyId;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        notifyId = startId;
        buildNotification();

        serviceModel = (CheckVersionModel.ResultBean) intent.getSerializableExtra(SERVICE_VERSION_INFO);

        if (serviceModel != null) {
            //TestLog.i("UpdateService: " + serviceModel);
            String downloadUrl = serviceModel.getDownloadUrl();
            String fileName = WuhunFileTool.getFileName(downloadUrl);
            File file = MyApplication.getUploadFile("version", fileName);
            donwloadFile = file.getAbsolutePath();
            downloadApk(serviceModel.getDownloadUrl());
        } else {
            WuhunToast.info("下载地址错误！");
        }

        return super.onStartCommand(intent, flags, startId);
    }


    NotificationManager manager;
    NotificationCompat.Builder builder;
    /** 更新提示 */
    private void buildNotification() {
        boolean isLollipop = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        int smallIcon = getResources().getIdentifier(
                "ic_launcher.png", "mipmap", getPackageName());

        if (smallIcon <= 0 || !isLollipop) {
            smallIcon = getApplicationInfo().icon;
        }

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("准备下载")
                .setWhen(System.currentTimeMillis())
                .setProgress(100, 1, false)
                .setSmallIcon(smallIcon);
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), ""))

        manager.notify(notifyId, builder.build());

//        PendingIntent contentIntent = PendingIntent.getActivity(
//                this, 0, new Intent(this, MainActivity.class), 0);
//.setContentIntent(contentIntent);
    }

    /** 下载apk
     * @param downloadUrl*/
    private void downloadApk(final String downloadUrl) {
        DownloadUtil.getInstence().download(downloadUrl, donwloadFile, new DownloadUtil.OnDownloadListener() {
            @Override
            public void onDownloadSuccess() {
                WuhunThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = installIntent(donwloadFile);
                        PendingIntent intent = PendingIntent.getActivity(UpdateService.this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(intent);
                        builder.setContentText("下载完成");
                        builder.setDefaults(Notification.DEFAULT_ALL);
                        manager.notify(notifyId, builder.build());

                        startActivity(i);
                        stopSelf();
                    }
                });
            }

            @Override
            public void onDownloading(final int progress) {
                WuhunThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.setProgress(100, progress, false);
                        builder.setContentText(String.format("正在下载 %1$d%2$s", progress, "%"));
                        manager.notify(notifyId, builder.build());
                    }
                });
            }

            @Override
            public void onDownloadFailed() {
                WuhunThread.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = webLauncher(downloadUrl);
                        PendingIntent intent = PendingIntent.getActivity(UpdateService.this, 0, i,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentText("下载失败(点击浏览器下载)");
                        builder.setContentIntent(intent);
                        builder.setProgress(0, 0, false);
                        builder.setDefaults(0);
//                        Notification n = builder.build();
//                        n.contentIntent = intent;
//                        manager.notify(notifyId, n);
                        manager.notify(notifyId, builder.build());
                        stopSelf();
                    }
                });
            }
        });
    }

    private static Intent installIntent(String path) {
        Uri uri = Uri.fromFile(new File(path));
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        return installIntent;
    }

    private static Intent webLauncher(String downloadUrl) {
        Uri download = Uri.parse(downloadUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, download);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
