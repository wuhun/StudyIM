package studyim.cn.edu.cafa.studyim.app;

import android.app.Application;

import com.tencent.smtt.sdk.QbSdk;

import org.litepal.LitePal;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ContactNotificationMessage;

import static tools.com.lvliangliang.wuhuntools.app.WuhunApplication.getCurProcessName;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/12/6 0006
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class RongApplication extends Application implements RongIMClient.OnReceiveMessageListener {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化litepal数据库管理
        LitePal.initialize(getApplicationContext());
        // 初始化融云
        initRongCloud();
        initTBS();
    }

    private void initTBS() {
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }

    private void initRongCloud() {
        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))
                || "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
            RongIMClient.init(this);
            RongIM.init(this);
        }
        //监听
        RongIM.setOnReceiveMessageListener(this); // 接收消息的监听器
    }

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
//        TestLog.i("onMessageRecalled _ 消息监听");
        if (messageContent instanceof ContactNotificationMessage) {
            ContactNotificationMessage contactNotificationMessage = (ContactNotificationMessage) messageContent;
        }
        return false;
    }
}
