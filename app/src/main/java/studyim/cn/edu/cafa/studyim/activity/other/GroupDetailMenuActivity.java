package studyim.cn.edu.cafa.studyim.activity.other;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.File;
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
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.model.ResultModel;
import studyim.cn.edu.cafa.studyim.ui.BottomMenuDialog;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.app.WuhunAppTool;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.permission.PermissionListener;
import tools.com.lvliangliang.wuhuntools.permission.PermissionsUtil;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunFileTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunImgTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getContext;
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

    @BindView(R.id.rl_group_files)
    RelativeLayout rl_group_files;
    @BindView(R.id.rl_appointManager)
    RelativeLayout rl_appointManager;

    @BindView(R.id.rl_group_avater)
    RelativeLayout rl_group_avater;
    @BindView(R.id.rl_group_name)
    RelativeLayout rl_group_name;
    @BindView(R.id.rl_group_introduce)
    RelativeLayout rl_group_introduce;

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
        getIntentData();
        initData();
        initListener();
    }

    private void initListener() {
        btn_group_quit.setOnClickListener(mClickListener);
        btn_manager_group_quit.setOnClickListener(mClickListener);
        backActivity.setOnClickListener(mClickListener);
        rl_group_files.setOnClickListener(mClickListener); // 文件下载列表
        rl_appointManager.setOnClickListener(mClickListener);

        adapter.setOnItemClickListener(mItemClickListener);
    }

    private OnItemClickListener mItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
            GroupMemeberModel memeberModel = dataList.get(position);
//            if (memeberModel == null || WuhunDataTool.isNullString(memeberModel.getType())) return;
            if (memeberModel != null && !WuhunDataTool.isNullString(memeberModel.getType())
                    && memeberModel.getType().equals(ADD_MEMEBER_TYPE)) {
                Intent intent = new Intent(mContext, GroupAddFriendActivity.class);
                intent.putExtra(GroupAddFriendActivity.GROUPID, groupid);
                startActivityForResult(intent, 200);
            } else {
                String userid = memeberModel.getUSERID() + "";
                TestLog.i("==> 查询id：" + userid);
                Intent intent = new Intent(mContext, FriendGetinfoActivity.class);
                intent.putExtra(FriendGetinfoActivity.GET_USERID, userid);
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
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetid, new RongIMClient.ResultCallback<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {
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
                                                    public void onError(RongIMClient.ErrorCode e) {
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode e) {
                                            }
                                        });
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetid, new RongIMClient.ResultCallback<Boolean>() {
                                            @Override
                                            public void onSuccess(Boolean aBoolean) {
                                            }

                                            @Override
                                            public void onError(RongIMClient.ErrorCode errorCode) {
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
                case R.id.rl_group_files:
                    Intent intent = new Intent(mContext, GroupFilesActivity.class);
                    intent.putExtra(GroupFilesActivity.GROUP_ID, groupid);
                    intent.putExtra(GroupFilesActivity.GROUP_MASTER_ID, groupmasterid);
                    intent.putExtra(GroupFilesActivity.GROUP_MANAGER_ID, groupmanagerid);
                    jumpToActivity(intent);
                    break;
                case R.id.rl_appointManager:
                    //设置群管理
                    Intent amIntent = new Intent(mContext, GroupAppointManagerActivity.class);
                    amIntent.putExtra(GroupAppointManagerActivity.GROUP_ID, groupid);
                    amIntent.putExtra(GroupAppointManagerActivity.GROUP_MASTER_ID, groupmasterid);
                    amIntent.putExtra(GroupAppointManagerActivity.GROUP_MANAGER_ID, groupmanagerid);
                    jumpToActivity(amIntent);
                    break;
                case R.id.rl_group_avater:
                    showPhotoDialog();
                    break;
                case R.id.rl_group_name:
                    final EditText et = new EditText(mContext);
                    et.setHint("新的群名称");
                    et.setPadding(20, 60, 20, 20);
                    new AlertDialog.Builder(mContext)
                            .setView(et)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String groupName = et.getText().toString();
                                    if (groupName.equals("")) {
                                        WuhunToast.info("群名称不能为空").show();
                                    } else {
                                        HttpUtil.groupChangeMessage(groupid, null, groupName, null, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                WuhunThread.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        WuhunToast.info("接口调用失败！").show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String result = response.body().string();
                                                ResultModel resultModel = getGson().fromJson(result, ResultModel.class);
                                                if (response.isSuccessful() && resultModel.getCode() == 1) {
                                                    WuhunThread.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            BroadcastManager.getInstance(mContext).sendBroadcast(Constant.UPDATE_GROUP_LIST);
                                                            updateGroupInfo();
                                                            WuhunToast.info("修改成功").show();
                                                        }
                                                    });
                                                } else {
                                                    WuhunThread.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            WuhunToast.info("修改失败").show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }).setNegativeButton("取消", null).show();

                    break;
                case R.id.rl_group_introduce:
                    final EditText et_group_introduce = new EditText(mContext);
                    et_group_introduce.setHint("新的群简介");
                    et_group_introduce.setPadding(20, 60, 20, 20);
                    new AlertDialog.Builder(mContext)
                            .setView(et_group_introduce)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String groupIntroduce = et_group_introduce.getText().toString();
                                    if (groupIntroduce.equals("")) {
                                        WuhunToast.info("群简介不能为空").show();
                                    } else {
                                        HttpUtil.groupChangeMessage(groupid, null, null, groupIntroduce, new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                                WuhunThread.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        WuhunToast.info("接口调用失败！").show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onResponse(Call call, Response response) throws IOException {
                                                String result = response.body().string();
                                                ResultModel resultModel = getGson().fromJson(result, ResultModel.class);
                                                if (response.isSuccessful() && resultModel.getCode() == 1) {
                                                    WuhunThread.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            BroadcastManager.getInstance(mContext).sendBroadcast(Constant.UPDATE_GROUP_LIST);
                                                            updateGroupInfo();
                                                            WuhunToast.info("修改成功").show();
                                                        }
                                                    });
                                                } else {
                                                    WuhunThread.runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            WuhunToast.info("修改失败").show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                }
                            }).setNegativeButton("取消", null).show();
                    break;
            }
        }
    };

    private void initData() {
        //更新群成员
        updateGroupMemeber();

        /** 获取群详情信息 */
        updateGroupInfo();
    }

    private String groupmanagerid; //管理员
    private String groupmasterid; //群主

    private void updateGroupInfo() {
        HttpUtil.getGroupInfo(groupid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                TestLog.i("群详情信息=>" + result);

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

                            //更新缓存
                            GroupModel groupModel = new GroupModel();
                            groupModel.setGROUPID(groupInfo.getGROUPID());
                            groupModel.setGROUPIMAGE(groupInfo.getGROUPIMAGE());
                            groupModel.setNAME(groupInfo.getGROUPNAME());
                            DBManager.getmInstance().updateGroup(groupModel);

                            groupmasterid = groupInfo.getGROUPMASTERID() + "";
                            groupmanagerid = groupInfo.getGROUPMANAGERID() + "";

                            String userid = getSPUtil().getUSERID().trim();
                            if (groupmasterid.equals(userid)) { //群组
                                btn_group_quit.setVisibility(View.GONE);
                                btn_manager_group_quit.setVisibility(View.VISIBLE);
                                rl_appointManager.setVisibility(View.VISIBLE);
                                // 群消息设置
                                rl_group_avater.setOnClickListener(mClickListener);
                                rl_group_name.setOnClickListener(mClickListener);
                                rl_group_introduce.setOnClickListener(mClickListener);
                            } else {
                                btn_group_quit.setVisibility(View.VISIBLE);
                                btn_manager_group_quit.setVisibility(View.GONE);
                                rl_appointManager.setVisibility(View.GONE);
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

    private void updateGroupMemeber() {
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
                    String userid = memeberModel.getUSERID() + "";
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /////////////////////////////////////////////////////////////////////////////////
    private Uri picUri;//拍照地址
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + WuhunAppTool.getAppName(getContext()) + File.separator + "picture" + File.separator + "/photo.jpg");
    private Uri cropUri;//剪裁地址
    private File cropFile = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + WuhunAppTool.getAppName(getContext()) + File.separator + "picture" + File.separator + "/crop_photo.jpg");
    private BottomMenuDialog dialog;

    /**
     * 弹出底部框
     */
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (PermissionsUtil.hasPermission(mContext, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    picUri = buildUri();
                    takePicture(GroupDetailMenuActivity.this, picUri, CODE_CAMERA_REQUEST);
                } else {
                    PermissionsUtil.requestPermission(mContext, new PermissionListener() {
                        @Override
                        public void permissionGranted(@NonNull String[] permission) {
                            //调用系统相机
                            picUri = buildUri();
                            takePicture(GroupDetailMenuActivity.this, picUri, CODE_CAMERA_REQUEST);
                        }

                        @Override
                        public void permissionDenied(@NonNull String[] permission) {
                            WuhunToast.info("您拒绝了拍照权限").show();
                        }
                    }, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, false, null);
                }
            }
        });
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                selectPicture();
            }
        });
        dialog.show();
    }

    /**
     * 裁剪图片成功后返回
     **/
    public static final int CODE_CROP_RESULT = 2;
    /**
     * 选择图片
     */
    public static final int CODE_SELECT_IMG = 3;
    /**
     * 拍照成功后返回
     */
    public static final int CODE_CAMERA_REQUEST = 4;

    /**
     * 构建uri
     */
    private Uri buildUri() {
        picUri = Uri.fromFile(fileUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picUri = FileProvider.getUriForFile(GroupDetailMenuActivity.this, "com.studyim.fileprovider", fileUri);
        }
        return picUri;
    }

    /**
     * 构建uri
     */
    private Uri buildCropUri() {
        cropUri = Uri.fromFile(cropFile);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropUri = FileProvider.getUriForFile(GroupDetailMenuActivity.this, "com.studyim.fileprovider", cropFile);
        }
        return cropUri;
    }

    /**
     * 自动获取相机权限
     */
    public static void takePicture(Activity activity, Uri imageUri, int requestCode) {
        //调用系统相机
        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activity.startActivityForResult(intentCamera, requestCode);
    }

    private void corp(Uri uri, Uri saveUri) {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        cropIntent.setDataAndType(uri, "image/*");
        cropIntent.putExtra("crop", "true");//发送裁剪信号
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        cropIntent.putExtra("outputX", 200);
        cropIntent.putExtra("outputY", 200);
//        cropIntent.putExtra("scale", true);
        //1-false用uri返回图片
        //2-true直接用bitmap返回图片（此种只适用于小图片，返回图片过大会报错）
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);//将剪切的图片保存到目标Uri中
        cropIntent.putExtra("return-data", false);
//        cropIntent.putExtra("noFaceDetection", true);
        cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        GroupDetailMenuActivity.this.startActivityForResult(cropIntent, CODE_CROP_RESULT);
    }

    private void selectPicture() {
        //每次选择图片吧之前的图片删除
        WuhunFileTool.deleteFileForUri(buildCropUri());

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        GroupDetailMenuActivity.this.startActivityForResult(intent, CODE_SELECT_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        TestLog.i("数据回调:" + requestCode);
        if (resultCode == 200) {
            updateGroupMemeber();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://相机回调
                    if (new File(picUri.getPath()).exists()) {
                        cropUri = buildCropUri();
                        corp(picUri, cropUri);
                    }
                    break;
                case CODE_CROP_RESULT://剪裁
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), cropUri);
                    String pathForUri = WuhunImgTool.getPathForUri(mContext, cropUri);
                    HttpUtil.groupChangeMessage(groupid, cropFile, null, null, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            WuhunThread.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    WuhunToast.info("接口调用失败！").show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String result = response.body().string();
                            ResultModel resultModel = getGson().fromJson(result, ResultModel.class);
                            if (response.isSuccessful() && resultModel.getCode() == 1) {
                                WuhunThread.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        BroadcastManager.getInstance(mContext).sendBroadcast(Constant.UPDATE_GROUP_LIST);
                                        updateGroupInfo();
                                        WuhunToast.info("修改成功").show();
                                    }
                                });
                            } else {
                                WuhunThread.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        WuhunToast.info("修改失败").show();
                                    }
                                });
                            }
                        }
                    });
                    break;
                case CODE_SELECT_IMG://相册
                    cropUri = buildCropUri();
                    if (data != null && data.getData() != null) {
                        String picturePath = WuhunImgTool.getPathForUri(mContext, data.getData());
//                        TestLog.i("picturePath： " + picturePath);
                        Uri path = Uri.parse(picturePath);
                        if (path != null) {
                            cropUri = buildCropUri();
                            corp(path, cropUri);
                        }
                    }
                    break;
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////
}
