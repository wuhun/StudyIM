package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunPingyinTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;
import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

public class GroupDeleteFriendActivity extends BaseActivity {

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
    @BindView(R.id.tv_no_friend_hint)
    TextView tv_no_friend_hint;

    private Context mContext;
    private List<GroupMemeberModel> friendList = new ArrayList<>();

    public static final String GROUPID = "groupId";
    private String grouid;

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
        grouid = intent.getStringExtra(GROUPID);
    }

    private void initData() {
        HttpUtil.getGroupMemeberlist(grouid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) { /* 请求失败 */ }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
//                    TestLog.i("查询群成员result: " + result);
                final BaseModel<GroupMemeberModel> model = getGson().fromJson(result, new TypeToken<BaseModel<GroupMemeberModel>>() {
                }.getType());
                if (response.isSuccessful() && model != null && model.getCode() == 1) {
//                    final List<GroupMemeberModel> Gmobel = new ArrayList<>();

                    final List<GroupMemeberModel> memeber = model.getResult();

                    for (int i=0; i<memeber.size(); i++) {
                        TestLog.i("成员是否为自己？");
                        if((memeber.get(i).getUSERID()+"").equals(getSPUtil().getUSERID())) {
                            memeber.remove(i);
                        }
                    }

//                    for (int i=0;i<memeber.size();i++) {
//                        GroupMemeberModel memeberModel = memeber.get(i);
//                        if(!WuhunDataTool.isNullString(groupMasterId) && (memeberModel.getUSERID()+"").equals(groupMasterId)) {
//                            continue;
//                        }
//                        if(!WuhunDataTool.isNullString(groupManagerId) && (memeberModel.getUSERID()+"").equals(groupManagerId)) {
//                            continue;
//                        }
//                        Gmobel.add(memeberModel);
//                    }

//                        TestLog.i("循环" + memeber);
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            friendList.clear();
                            friendList = memeber;
                            adapter.setData(friendList);
                        }
                    });
                }
            }
        });
//        HttpUtil.getFriendList(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                TestLog.i("FriendGroupCreteActivity - initData: 访问好友列表失败");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String result = response.body().string();
//                FriendListModel dataList = null;
//                if(result != null)
//                    dataList = getGson().fromJson(result, FriendListModel.class);
//                if (response.isSuccessful() && friendList != null && dataList.getCode() == 1) {
//                    final FriendListModel finalDataList = dataList;
//                    WuhunThread.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            friendList = finalDataList.getResult();
//                            adapter.setData(friendList);
//                            if (friendList.size() <= 0) {
//                                tv_no_friend_hint.setVisibility(View.VISIBLE);
//                                rvAddFriend.setVisibility(View.GONE);
//                                mQib.setVisibility(View.GONE);
//                                bodyOk.setVisibility(View.GONE);
//                            } else {
//                                tv_no_friend_hint.setVisibility(View.GONE);
//                                rvAddFriend.setVisibility(View.VISIBLE);
//                                mQib.setVisibility(View.VISIBLE);
//                                bodyOk.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//                } else {
//                    TestLog.i("FriendGroupCreteActivity - initData: 获取好友列表失败");
//                }
//            }
//        });
    }

    /** 创建群组要添加的好友 */
    private List<GroupMemeberModel> groupList = new ArrayList<>();

    private void initListener() {
        headLeft.setOnClickListener(mClickListener);
        mQib.setOnLetterUpdateListener(mOnLetterUpdateListener);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
                CheckBox disSelect = helper.getView(R.id.dis_select);

                if((friendList.get(position).getUSERID()+"").equals(getSPUtil().getUSERID())) {
                    disSelect.setChecked(false);
                }

                if (disSelect.isChecked() == true) { //取消
                    disSelect.setChecked(false);
                    groupList.remove(friendList.get(position));
                } else { //选中
                    disSelect.setChecked(true);
                    groupList.add(friendList.get(position));
                }
                bodyOk.setText("删除(" + groupList.size() + ")");
            }
        });
        bodyOk.setOnClickListener(mClickListener);
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
                    GroupMemeberModel friend = data.get(i);
                    String noteName = WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getREMARKNAME()) + "";
                    if (WuhunDataTool.isNullString(noteName)) {
                        noteName = WuhunPingyinTool.getPinYinFirstCharIsLetter(friend.getNAME()) + "";
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
                    GroupDeleteFriendActivity.this.finish();
                    break;
                case R.id.body_ok://添加到群中
//                    Intent intent = new Intent(mContext, GroupCreateActivity.class);
//                    intent.putExtra("groupFriends", (Serializable) groupList);
//                    jumpToActivity(intent);
                    if(WuhunDataTool.isNullString(grouid)){
                        WuhunToast.info("没有获取到群信息").show();
                    } else{
                        deleteSelectfriendList();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /** 添加结果集合类 */
    List<String> addFriendResultList = new ArrayList<>();

    private void deleteSelectfriendList() {
        if (groupList.size() <= 0) {
            WuhunToast.info("请选择您要删除的好友").show();
        } else {
            for(GroupMemeberModel friend : groupList){
                HttpUtil.groupQuit(grouid, friend.getUSERID()+"", new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) { }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        ResultModel resultModel = getGson().fromJson(result, ResultModel.class);
                        if(response.isSuccessful()){
                            addFriendResultList.add(resultModel.getCode()+"");
                        }
                    }
                });
            }

            if(addFriendResultList.contains("0") && addFriendResultList.contains("1")) {
                WuhunToast.info("部分删除成功").show();
            }else if(!addFriendResultList.contains("0")) {
                WuhunToast.info("删除成功").show();
            }else if(!addFriendResultList.contains("1")) {
                WuhunToast.info("删除失败").show();
            }
            BroadcastManager.getInstance(mContext).sendBroadcast(Constant.UPDATE_GROUP_MEMEBER);
            setResult(RESULT_OK);
            GroupDeleteFriendActivity.this.finish();
        }
    }

    LQRAdapterForRecyclerView<GroupMemeberModel> adapter;
    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        headLeft.setImageResource(R.drawable.icon_back);
        bodyRight.setVisibility(View.GONE);
        bodyOk.setText("删除");
        bodyOk.setVisibility(View.VISIBLE);

        adapter = new LQRAdapterForRecyclerView<GroupMemeberModel>(mContext, friendList, R.layout.group_friend_list_item) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, GroupMemeberModel item, int position) {
                helper.setText(R.id.dis_friendname, item.getREMARKNAME());
                CheckBox disSelect = helper.getView(R.id.dis_select);
                disSelect.setVisibility(View.VISIBLE);
                
                if((item.getUSERID()+"").equals(getSPUtil().getUSERID())) {
                    disSelect.setVisibility(View.GONE);
                }
                
                ImageView imgAvatar = helper.getView(R.id.dis_frienduri);

                String uri = UserAvatarUtil.initUri(null, item.getAVATAR());
                String avatarUri = UserAvatarUtil.getAvatarUri(item.getUSERID() + "", item.getNICKNAME(), uri);
                UserAvatarUtil.showImage(mContext, avatarUri, imgAvatar);
                //UserAvatarUtil.showAvatar(mContext, item, Constant.HOME_URL, imgAvatar);//头像

                String str = "";
                String currentLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(item.getREMARKNAME()) + "";
                if (position == 0) {
                    str = currentLetter;//得到当前字母
                } else {
                    String preLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(friendList.get(position - 1).getREMARKNAME()) + "";
                    if (!preLetter.equalsIgnoreCase(currentLetter)) {//得到上一个字母
                        str = currentLetter;//如果和上一个字母的首字母不同则显示字母栏
                    }
                }
                int nextIndex = position + 1;
                if (nextIndex < friendList.size() - 1) {
                    //得到下一个字母
                    String nextLetter = WuhunPingyinTool.getPinYinFirstCharIsLetter(friendList.get(nextIndex).getREMARKNAME()) + "";
                    //如果和下一个字母的首字母不同则隐藏下划线
                    if (!nextLetter.equalsIgnoreCase(currentLetter)) {
                        helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                    } else {
                        helper.setViewVisibility(R.id.vLine, View.VISIBLE);
                    }
                } else {
                    helper.setViewVisibility(R.id.vLine, View.INVISIBLE);
                }
                if (position == friendList.size() - 1) {
                    helper.setViewVisibility(R.id.vLine, View.GONE);
                }
//
                //根据str是否为空决定字母栏是否显示
                if (TextUtils.isEmpty(str)) {
                    helper.setViewVisibility(R.id.dis_catalog, View.GONE);
                } else {
                    helper.setViewVisibility(R.id.dis_catalog, View.VISIBLE);
                    helper.setText(R.id.dis_catalog, str);
                }
            }
        };
        rvAddFriend.setAdapter(adapter);
    }
}