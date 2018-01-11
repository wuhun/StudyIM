package studyim.cn.edu.cafa.studyim.fragment.study;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunState;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

/**
 * 作者：悟魂 ————2017/11/18.
 * 版本：1.0
 * 说明：班级群聊列表；
 */

public class StudyClassFragment extends BaseFragment {

    @BindView(R.id.rc_group_list)
    WuhunRecyclerView rvGroupList;
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
    List<GroupModel> groupList;

    String beforeUri = null;

    @Override
    protected void initListener() {
        madapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                GroupModel model = groupList.get(position);
                RongIM.getInstance().startGroupChat(mActivity, model.getGROUPRCID(), model.getNAME());
            }
        });
    }

    @Override
    protected void initData() {
        BroadcastManager.getInstance(mActivity).sendBroadcast(Constant.UPDATE_GROUP_LIST);
        List<GroupModel> classGroup = DBManager.getmInstance().getGroupByClass();
        groupList = classGroup;
        madapter.setData(groupList);
    }

    @Override
    protected void initView() {
        rcStatusBar.setVisibility(View.GONE);
        rcEmptyTv.setText("暂无聊天信息");

        groupList = new ArrayList<>();
        madapter = new LQRAdapterForRecyclerView(mActivity, groupList, R.layout.group_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, Object item, int position) {
                GroupModel model = groupList.get(position);
                String before = WuhunDataTool.isNullString(beforeUri) ? Constant.HOME_URL : beforeUri;
                ImageView groupuri = (ImageView) helper.getView(R.id.groupuri);
                UserAvatarUtil.showAvatar(mActivity, groupList.get(position), before, groupuri);
                helper.setText(R.id.groupname, model.getNAME());
            }
        };
        rvGroupList.setAdapter(madapter);

        BroadcastManager.getInstance(mActivity).register(Constant.UPDATE_GROUP_LIST, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                uploadGroupList();
            }
        });
    }

    @Override
    protected void getIntentData(Bundle arguments) {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.study_class_fragment;
    }


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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        BroadcastManager.getInstance(mActivity).unregister(Constant.UPDATE_GROUP_LIST);
    }

    private void uploadGroupList(){
        HttpUtil.getGroupList(null, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendEmptyMessage(WuhunState.REQUEST_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(response.isSuccessful()) {
//                    Message message = handler.obtainMessage(WuhunState.REQUEST_SUCCESS, result);
//                    handler.sendMessage(message);
                    if(result == null) return;
                    BaseModel<GroupModel> model = MyApplication.getGson().fromJson(result, new TypeToken<BaseModel<GroupModel>>(){}.getType());
                    if(model != null && model.getCode() == 1){
                        int size = DBManager.getmInstance().getAllGroup().size();
                        if(model.getResult().size() != size) {
                            DBManager.getmInstance().saveGroups(model.getResult(), model.getBefore());
                        }
                    }
                }
            }
        });
    }

    @SuppressLint("HandlerLeak")
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
                BaseModel<GroupModel> model = MyApplication.getGson().fromJson(result, new TypeToken<BaseModel<GroupModel>>(){}.getType());
                if(model != null && model.getCode() == 1){
                    DBManager.getmInstance().saveGroups(model.getResult(), model.getBefore());
                }else{
                    if (!WuhunDataTool.isNullString(model.getMsg())) {
                        WuhunToast.normal(model.getMsg()).show();
                    } else {
                        WuhunToast.normal("获取失败").show();
                    }
                }
            }
            super.handleMessage(msg);
        }
    };

}
