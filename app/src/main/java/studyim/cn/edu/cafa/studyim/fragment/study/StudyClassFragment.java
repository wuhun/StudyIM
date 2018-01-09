package studyim.cn.edu.cafa.studyim.fragment.study;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.util.WuhunState;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

/**
 * 作者：悟魂 ————2017/11/18.
 * 版本：1.0
 * 说明：
 */

public class StudyClassFragment extends BaseFragment {

    @BindView(R.id.rc_list)
    WuhunRecyclerView rcList;
    @BindView(R.id.rc_status_bar)
    LinearLayout rcStatusBar;
    @BindView(R.id.rc_status_bar_image)
    ImageView rcStatusBarImage;
    @BindView(R.id.rc_status_bar_text)
    TextView rcStatusBarText;
    @BindView(R.id.rc_conversation_list_empty_layout)
    LinearLayout rcConversationListEmptyLayout;
    @BindView(R.id.rc_empty_tv)
    TextView rcEmptyTv;

    LQRAdapterForRecyclerView madapter = null;

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        HttpUtil.getGroupList("class", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(WuhunState.REQUEST_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(response.isSuccessful()) {
                    Message message = handler.obtainMessage(WuhunState.REQUEST_SUCCESS, result);
                    handler.sendMessage(message);
                }
            }
        });
    }

    @Override
    protected void initView() {
        rcStatusBar.setVisibility(View.GONE);
        rcEmptyTv.setText("暂无聊天信息");

//        madapter = new LQRAdapterForRecyclerView(mActivity, classDatas, R.layout.group_class_item) {
//            @Override
//            public void convert(LQRViewHolderForRecyclerView helper, Object item, int position) {
//
//            }
//
//            @Override
//            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//            }
//        };
    }

    @Override
    protected void getIntentData(Bundle arguments) {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.study_class_fragment;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == WuhunState.REQUEST_ERROR) {
                if (WuhunNetTools.isAvailable(mActivity)) {
                    WuhunToast.error(R.string.request_error_net).show();
                } else {
                    WuhunToast.error(R.string.request_error).show();
                }
            }else if(msg.what == WuhunState.REQUEST_SUCCESS) {
                String result = (String) msg.obj;
                TestLog.i("studyClassFragment:==> " + result);
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        setNotificationBarVisibility(RongIM.getInstance().getCurrentConnectionStatus());
    }

    /** 提示 */
    private void setNotificationBarVisibility(RongIMClient.ConnectionStatusListener.ConnectionStatus status) {
        if(this.getResources().getBoolean(io.rong.imkit.R.bool.rc_is_show_warning_notification)) {
            String content = null;
            if(status.equals(ConnectionStatus.NETWORK_UNAVAILABLE)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_network_unavailable);
            } else if(status.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_tick);
            } else if(status.equals(ConnectionStatus.CONNECTED)) {
                rcStatusBar.setVisibility(View.GONE);
            } else if(status.equals(ConnectionStatus.DISCONNECTED)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_disconnect);
            } else if(status.equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                content = this.getResources().getString(io.rong.imkit.R.string.rc_notice_connecting);
            }

            if(content != null && this.rcStatusBar != null) {
                final String statusText = content;
                if(rcStatusBar.getVisibility() == View.GONE) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            if(!RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                                rcStatusBar.setVisibility(View.VISIBLE);
                                rcStatusBarText.setText(statusText);
                                if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTING)) {
                                    rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_connecting_animated);
                                } else {
                                    rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_network_available);
                                }
                            }

                        }
                    }, 4000L);
                } else {
                    rcStatusBarText.setText(content);
                    if(RongIMClient.getInstance().getCurrentConnectionStatus().equals(ConnectionStatus.CONNECTING)) {
                        rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_connecting_animated);
                    } else {
                        rcStatusBarImage.setImageResource(io.rong.imkit.R.drawable.rc_notification_network_available);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
