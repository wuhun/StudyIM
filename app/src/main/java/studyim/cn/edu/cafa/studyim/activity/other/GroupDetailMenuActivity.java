package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.GroupInfoModel;
import studyim.cn.edu.cafa.studyim.model.GroupMemeberModel;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;
import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

public class GroupDetailMenuActivity extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView backActivity;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;
    @BindView(R.id.body_ok)
    TextView bodyOk;

    @BindView(R.id.rv_group_member)
    WuhunRecyclerView rvGroupMember;

    @BindView(R.id.img_group_avater)
    ImageView img_group_avater;
    @BindView(R.id.tv_groupname)
    TextView tv_groupname;
    @BindView(R.id.tv_groupmsg)
    TextView tv_groupmsg;

    @BindView(R.id.btn_group_quit)
    Button btn_group_quit;
    @BindView(R.id.btn_manager_group_quit)
    Button btn_manager_group_quit;

    @BindView(R.id.ll_group_files)
    LinearLayout ll_group_files;

    List<GroupMemeberModel> dataList;

    Context mContext;
    private String before; //头像前缀
    private String groupid;
    private String targetid;
    public static final String GROUPID = "groupId";
    public static final String TARGETID = "targetId";
    private LQRAdapterForRecyclerView<GroupMemeberModel> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail_menu);
        ButterKnife.bind(this);

        initView();
        registerBroadcast();
        getIntentData();
        initData();
        initListener();
    }

    private void initListener() {
        btn_group_quit.setOnClickListener(mClickListener);
        btn_manager_group_quit.setOnClickListener(mClickListener);
        backActivity.setOnClickListener(mClickListener);
        ll_group_files.setOnClickListener(mClickListener); // 文件下载列表

        adapter.setOnItemClickListener(mItemClickListener);

    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
            GroupMemeberModel memeberModel = dataList.get(position);
            if (memeberModel == null || WuhunDataTool.isNullString(memeberModel.getType())) return;
            if (memeberModel.getType().equals(ADD_MEMEBER_TYPE)) {
                Intent intent = new Intent(mContext, GroupAddFriendActivity.class);
                intent.putExtra(GroupAddFriendActivity.GROUPID, groupid);
                jumpToActivity(intent);
            }
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.body_img_menu:
                    GroupDetailMenuActivity.this.finish();
                    break;
                case R.id.btn_group_quit://退出群
                    HttpUtil.groupQuit(groupid, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.info(R.string.request_error).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            ResultModel resultModel = getGson().fromJson(result, ResultModel.class);
                            if (response.isSuccessful() && resultModel != null && (resultModel.getCode() == 1)) {
                                WuhunThread.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, targetid, new RongIMClient.ResultCallback<Conversation>() {
                                            @Override
                                            public void onSuccess(Conversation conversation) {
                                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, targetid, new RongIMClient.ResultCallback<Boolean>() {
                                                    @Override
                                                    public void onSuccess(Boolean aBoolean) {
                                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetid, null);
                                                    }

                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode e) {
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode e) {
                                            }
                                        });
                                        WuhunToast.info("退出成功").show();
                                        DBManager.getmInstance().deleteGroupsById(groupid);
                                        setResult(501, new Intent());
                                        GroupDetailMenuActivity.this.finish();
                                    }
                                });
                            }
                        }
                    });
                    break;
                case R.id.btn_manager_group_quit:
                    HttpUtil.groupDismiss(groupid, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.info(R.string.request_error).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            ResultModel resultModel = getGson().fromJson(result, ResultModel.class);
                            if (response.isSuccessful() && resultModel != null && (resultModel.getCode() == 1)) {
                                WuhunThread.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, targetid, new RongIMClient.ResultCallback<Conversation>() {
                                            @Override
                                            public void onSuccess(Conversation conversation) {
                                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, targetid, new RongIMClient.ResultCallback<Boolean>() {
                                                    @Override
                                                    public void onSuccess(Boolean aBoolean) {
                                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetid, null);
                                                    }

                                                    @Override
                                                    public void onError(RongIMClient.ErrorCode e) {}
                                                });
                                            }
                                            @Override
                                            public void onError(RongIMClient.ErrorCode e) {}
                                        });
                                        WuhunToast.info("退出成功").show();
                                        DBManager.getmInstance().deleteGroupsById(groupid);
                                        setResult(501, new Intent());
                                        GroupDetailMenuActivity.this.finish();
                                    }
                                });
                            }
                        }
                    });
                    break;
                case R.id.ll_group_files:
                    Intent intent = new Intent(mContext, GroupFilesActivity.class);
                    intent.putExtra(GroupFilesActivity.GROUP_ID, groupid);
                    jumpToActivity(intent);
                    break;
            }
        }
    };

    private void initData() {
        TestLog.i("更新群成员：" + groupid);
        //更新群成员
        HttpUtil.getGroupMemeberlist(groupid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                    TestLog.i("查询群成员result: " + result);
                final BaseModel<GroupMemeberModel> model = getGson().fromJson(result, new TypeToken<BaseModel<GroupMemeberModel>>() {
                }.getType());
                if (response.isSuccessful() && model != null && model.getCode() == 1) {
                    before = WuhunDataTool.isNullString(model.getBefore()) ? Constant.HOME_URL : model.getBefore();
                    final List<GroupMemeberModel> memeber = model.getResult();
//                        TestLog.i("循环" + memeber);
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dataList.clear();
                            dataList = memeber;
                            LastAddMemeber();//添加最后的添加按钮
                            adapter.setData(dataList);
                            bodyTvTitle.setText("群组信息(" + (dataList.size() - 1) + ")");
                            for (GroupMemeberModel groupitem : memeber) {
//                            TestLog.i("成员: " + groupitem);
                                if (WuhunDataTool.isNullString(groupitem.getType())) {
                                    String name = WuhunDataTool.isNullString(groupitem.getREMARKNAME()) ? groupitem.getNICKNAME() : groupitem.getREMARKNAME();
                                    String uri = UserAvatarUtil.initUri(before, groupitem.getAVATAR());
                                    String avatarUri = UserAvatarUtil.getAvatarUri(
                                            groupitem.getUSERID() + "", name, uri);
                                    UserInfo userInfo = new UserInfo(groupitem.getRCID(), name, Uri.parse(avatarUri));
                                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                                }
                            }
                        }
                    });
                } else {
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = model.getMsg();
                            if (!WuhunDataTool.isNullString(msg)) {
                                WuhunToast.info(msg).show();
                            } else {
                                WuhunToast.info("登录超时，请重新登录").show();
                            }
                        }
                    });
                }
            }
        });

        /** 获取群详情信息 */
        HttpUtil.getGroupInfo(groupid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final GroupInfoModel groupInfoModel = getGson().fromJson(result, GroupInfoModel.class);
                if (response.isSuccessful() && groupInfoModel != null && (groupInfoModel.getCode() == 1)) {
                    final GroupInfoModel.ResultBean groupInfo = groupInfoModel.getResult();
                    if (groupInfo == null)
                        return;
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!WuhunDataTool.isNullString(groupInfo.getGROUPNAME()))
                                tv_groupname.setText(groupInfo.getGROUPNAME());
                            if (!WuhunDataTool.isNullString(groupInfo.getGROUPMSG()))
                                tv_groupmsg.setText(groupInfo.getGROUPMSG());
                            String groupimage = groupInfo.getGROUPIMAGE();
                            String uri = UserAvatarUtil.initUri(Constant.HOME_URL, groupimage);
                            String avatarUri = UserAvatarUtil.getAvatarUri(groupInfo.getGROUPID() + "", groupInfo.getGROUPNAME(), uri);
                            UserAvatarUtil.showImage(mContext, avatarUri, img_group_avater);

                            String groupmanagerid = groupInfo.getGROUPMANAGERID() + "";
                            String userid = getSPUtil().getUSERID().trim();
                            if (groupmanagerid.equals(userid)) {
                                btn_group_quit.setVisibility(View.GONE);
                                btn_manager_group_quit.setVisibility(View.VISIBLE);
                            } else {
                                btn_group_quit.setVisibility(View.VISIBLE);
                                btn_manager_group_quit.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String msg = groupInfoModel.getMsg();
                            if (!WuhunDataTool.isNullString(msg)) {
                                WuhunToast.info(msg).show();
                            } else {
                                WuhunToast.info("登录超时，请重新登录").show();
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 添加好友的类型
     */
    public static final String ADD_MEMEBER_TYPE = "add_memeber_type";

    /**
     * 添加末尾的添加好友
     */
    private void LastAddMemeber() {
        GroupMemeberModel memeber = new GroupMemeberModel();
        memeber.setType(ADD_MEMEBER_TYPE);
        dataList.add(memeber);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupid = intent.getStringExtra(GROUPID);
        targetid = intent.getStringExtra(TARGETID);
    }

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        backActivity.setImageResource(R.drawable.icon_back);
        bodyTvTitle.setText("群组信息");
        bodySearch.setVisibility(View.GONE);
        bodyOk.setVisibility(View.GONE);

        dataList = new ArrayList<>();
        adapter = new LQRAdapterForRecyclerView<GroupMemeberModel>(mContext, dataList, R.layout.group_gradview_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, GroupMemeberModel item, int position) {
                //名称
                GroupMemeberModel memeberModel = dataList.get(position);
                ImageView imageAvater = helper.getView(R.id.img_avater);
                TextView tvUsername = helper.getView(R.id.tv_username);

                if (!WuhunDataTool.isNullString(memeberModel.getType())) {
                    // 添加好友,删除好友
                    if (memeberModel.getType().equals(ADD_MEMEBER_TYPE)) {
                        tvUsername.setVisibility(View.GONE);
                        imageAvater.setImageResource(R.drawable.add_group_head);
                    }
                } else {
                    String username = WuhunDataTool.isNullString(memeberModel.getREMARKNAME()) ?
                            memeberModel.getNICKNAME() : memeberModel.getREMARKNAME();
//                    helper.setText(R.id.tv_username, username);
                    tvUsername.setText(username);
                    //头像
                    String uri = UserAvatarUtil.initUri(before, memeberModel.getAVATAR());
                    String userid = WuhunDataTool.isNullString(memeberModel.getUSERID() + "") ?
                            memeberModel.getUSER_ID() + "" : memeberModel.getUSERID() + "";
                    String avatarUri = UserAvatarUtil.getAvatarUri(userid, username, uri);
                    UserAvatarUtil.showImage(mContext, avatarUri, imageAvater);
                }
            }
        };
        rvGroupMember.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void registerBroadcast() {
        BroadcastManager.getInstance(mContext).register(Constant.UPDATE_GROUP_MEMEBER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        });
    }

    public void unBroadcast() {
        BroadcastManager.getInstance(mContext).unregister(Constant.UPDATE_GROUP_MEMEBER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBroadcast();
    }
}
