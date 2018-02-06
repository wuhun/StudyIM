package studyim.cn.edu.cafa.studyim.activity.other;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.GroupMemeberModel;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.ui.QuickIndexBar;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.net.WuhunNetTools;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

/**
 * 查看历史记录
 */
public class GroupAppointManagerActivity extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView headLeft;
    @BindView(R.id.body_search)
    ImageView bodyRight;
    @BindView(R.id.body_ok)
    TextView bodyOk;

    @BindView(R.id.rv_add_friend)
    WuhunRecyclerView rvAddFriend;
    @BindView(R.id.qib)
    QuickIndexBar mQib;
    @BindView(R.id.tvLetter)
    TextView tvLetter;

    private Context mContext;
    List<GroupMemeberModel> dataList = new ArrayList<>();

    public static final String GROUP_ID = "group_id";
    public static final String GROUP_MASTER_ID = "group_master_id";
    public static final String GROUP_MANAGER_ID = "group_manager_id";
    private String groupId = "";
    private String groupMasterId = "";
    private String groupManagerId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select_friend);
        ButterKnife.bind(this);

        initView();
        getIntentData();
        initData();
        initListener();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        groupId = intent.getStringExtra(GROUP_ID);
        groupMasterId = intent.getStringExtra(GROUP_MASTER_ID);
        groupManagerId = intent.getStringExtra(GROUP_MANAGER_ID);
    }

    private void initData() {
        /** 获取好友列表 */
//        final List<Friend> friends = DBManager.getmInstance().getFriends();
        if(groupId == null){
            WuhunToast.info("缺少参数").show();
            return;
        }
        if (WuhunNetTools.isAvailable(mContext)) {
            HttpUtil.getGroupMemeberlist(groupId, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { /* 请求失败 */ }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
//                    TestLog.i("查询群成员result: " + result);
                    final BaseModel<GroupMemeberModel> model = getGson().fromJson(result, new TypeToken<BaseModel<GroupMemeberModel>>() {
                    }.getType());
                    if (response.isSuccessful() && model != null && model.getCode() == 1) {
                        final List<GroupMemeberModel> Gmobel = new ArrayList<>();

                        List<GroupMemeberModel> memeber = model.getResult();
                        for (int i=0;i<memeber.size();i++) {
                            GroupMemeberModel memeberModel = memeber.get(i);
                            if(!WuhunDataTool.isNullString(groupMasterId) && (memeberModel.getUSERID()+"").equals(groupMasterId)) {
                                continue;
                            }
                            if(!WuhunDataTool.isNullString(groupManagerId) && (memeberModel.getUSERID()+"").equals(groupManagerId)) {
                                continue;
                            }
                            Gmobel.add(memeberModel);
                        }

//                        TestLog.i("循环" + memeber);
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataList.clear();
                                dataList = Gmobel;
                                adapter.setData(dataList);
                            }
                        });
                    }
                }
            });
        } else {
            new AlertDialog.Builder(mContext)
                    .setMessage("当前无网络连接，请检查网络状态")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GroupAppointManagerActivity.this.finish();
                        }
                    }).show();
        }
    }


    /** 创建群组要添加的好友 */
    private List<GroupMemeberModel> groupList = new ArrayList<>();

    private void initListener() {
        headLeft.setOnClickListener(mClickListener);
        mQib.setOnLetterUpdateListener(mOnLetterUpdateListener);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                final GroupMemeberModel item = dataList.get(position);
                String name = WuhunDataTool.isNullString(item.getREMARKNAME()) ? item.getNICKNAME() : item.getREMARKNAME();
                new AlertDialog.Builder(mContext)
                        .setMessage("是否设置"+ name + "为管理员？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setAppointManager(item.getUSERID()+"");
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });
        bodyOk.setOnClickListener(mClickListener);
    }

    private void setAppointManager(String userid) {
        if (!WuhunNetTools.isAvailable(mContext)) {
            new AlertDialog.Builder(mContext)
                    .setMessage("当前无网络连接，请检查网络状态")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GroupAppointManagerActivity.this.finish();
                        }
                    }).show();
            return;
        } else {
            if(groupId == null) {
                WuhunToast.info("缺少参数").show();
                return;
            }
            HttpUtil.GroupAppointManager(groupId, userid, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            WuhunToast.info(R.string.request_error_net).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String result = response.body().string();
                    TestLog.i("设置管理员：" + result);
                    final ResultModel model = getGson().fromJson(result, ResultModel.class);
                    if (response.isSuccessful() && model.getCode() == 1) {
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                WuhunToast.info("设置成功").show();
                                GroupAppointManagerActivity.this.finish();
                            }
                        });
                    } else {
                        WuhunThread.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (!WuhunDataTool.isNullString(model.getMsg())) {
                                    WuhunToast.info(model.getMsg()).show();
                                } else {
                                    WuhunToast.info("接口访问失败").show();
                                }
                            }
                        });
                    }
                }
            });
        }
    }


    private QuickIndexBar.OnLetterUpdateListener mOnLetterUpdateListener = new QuickIndexBar.OnLetterUpdateListener() {
        @Override
        public void onLetterUpdate(String letter) {
            //显示对话框
            tvLetter.setText(letter);
            tvLetter.setVisibility(View.VISIBLE);
            //滑动到第一个对应字母开头的联系人
            if ("↑".equalsIgnoreCase(letter)) {
                rvAddFriend.moveToPosition(0);//集合中移除
            } else if ("☆".equalsIgnoreCase(letter)) {
                rvAddFriend.moveToPosition(0);//集合中移除
            } else {
//                List<Friend> data = ((LQRAdapterForRecyclerView) ((LQRHeaderAndFooterAdapter) rvAddFriend.getAdapter()).getInnerAdapter()).getData();
                List<GroupMemeberModel> data = ((LQRAdapterForRecyclerView)rvAddFriend.getAdapter()).getData();
                for (int i = 0; i < data.size(); i++) {
                    GroupMemeberModel model = data.get(i);
                    String noteName = WuhunPingyinTool.getPinYinFirstCharIsLetter(model.getREMARKNAME()) + "";
                    if (WuhunDataTool.isNullString(noteName)) {
                        noteName = WuhunPingyinTool.getPinYinFirstCharIsLetter(model.getNICKNAME()) + "";
                    }
                    if (noteName.equalsIgnoreCase(letter)) {
                        rvAddFriend.moveToPosition(i);//移动到制定位置
                        break;
                    }
                }
            }
        }

        @Override
        public void onLetterCancel() {
            tvLetter.setVisibility(View.GONE);
        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.body_img_menu://左侧按钮
                    GroupAppointManagerActivity.this.finish();
                    break;
                case R.id.body_ok://创建群组
                    Intent intent = new Intent(mContext, GroupCreateActivity.class);
                    intent.putExtra("groupFriends", (Serializable) groupList);
                    jumpToActivity(intent);
                    GroupAppointManagerActivity.this.finish();
                    break;
                default:
                    break;
            }
        }
    };

    LQRAdapterForRecyclerView<GroupMemeberModel> adapter;
    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        headLeft.setImageResource(R.drawable.icon_back);
        bodyRight.setVisibility(View.GONE);
        bodyOk.setText(R.string.rc_action_bar_ok);
        bodyOk.setVisibility(View.GONE);

        adapter = new LQRAdapterForRecyclerView<GroupMemeberModel>(mContext, dataList, R.layout.group_friend_list_item){

            @Override
            public void convert(LQRViewHolderForRecyclerView helper, GroupMemeberModel item, int position) {
                GroupMemeberModel model = dataList.get(position);
                String name = WuhunDataTool.isNullString(model.getREMARKNAME()) ? model.getNICKNAME() : model.getREMARKNAME();
                helper.setText(R.id.dis_friendname, name);

                CheckBox disSelect = helper.getView(R.id.dis_select);
                disSelect.setVisibility(View.GONE);

                ImageView imgAvatar = helper.getView(R.id.dis_frienduri);
                String uri = UserAvatarUtil.initUri(Constant.HOME_URL, model.getAVATAR());
                String avatarUri = UserAvatarUtil.getAvatarUri(model.getUSERID() + "", name, uri);
                UserAvatarUtil.showImage(mContext, avatarUri, imgAvatar);

                //根据str是否为空决定字母栏是否显示
                helper.setViewVisibility(R.id.dis_catalog, View.GONE);
            }
        };
        rvAddFriend.setAdapter(adapter);
    }
}
