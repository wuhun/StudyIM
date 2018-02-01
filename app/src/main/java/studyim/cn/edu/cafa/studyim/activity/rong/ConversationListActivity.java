package studyim.cn.edu.cafa.studyim.activity.rong;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.callkit.RongCallKit;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.other.GroupDetailMenuActivity;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.fragment.other.GroupActiveFragment;
import studyim.cn.edu.cafa.studyim.fragment.other.GroupGradeFragment;
import studyim.cn.edu.cafa.studyim.model.BaseModel;
import studyim.cn.edu.cafa.studyim.model.GroupMemeberModel;
import studyim.cn.edu.cafa.studyim.model.GroupModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;

public class ConversationListActivity extends BaseActivity {


    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView backActivity;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView detailUser;


    @BindView(R.id.ll_sub_title)
    LinearLayout ll_sub_title;
    @BindView(R.id.rl_tab_chat)
    RelativeLayout rlTabchat;
    @BindView(R.id.rl_tab_hint)
    RelativeLayout rlTabHint;
    @BindView(R.id.rl_tab_active)
    RelativeLayout rlTabActive;

    @BindView(R.id.conversation_list)
    FrameLayout conversation_list;

    Context mContext;

    private Fragment mFragment[] = {
            getConverstationFragment(),
            getGardeFragment(),
            getActiveFragment()
    };
    private int position = 0;

    private ConversationFragment cFragment;

    public Fragment getConverstationFragment() {
        if (cFragment == null) {
            synchronized (ConversationFragment.class) {
                if (cFragment == null) {
                    cFragment = new ConversationFragment();
                }
            }
        }
        return cFragment;
    }

    private GroupGradeFragment gradeFragment;

    public Fragment getGardeFragment() {
        if (gradeFragment == null) {
            synchronized (GroupGradeFragment.class) {
                if (gradeFragment == null) {
                    gradeFragment = new GroupGradeFragment();
                }
            }
        }
        return gradeFragment;
    }

    private GroupActiveFragment activeFragment;

    public Fragment getActiveFragment() {
        if (activeFragment == null) {
            synchronized (GroupActiveFragment.class) {
                if (activeFragment == null) {
                    activeFragment = new GroupActiveFragment();
                }
            }
        }
        return activeFragment;
    }

    private final String TextTypingTitle = "对方正在输入...";
    private final String VoiceTypingTitle = "对方正在讲话...";

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case SET_TEXT_TYPING_TITLE:
                    setTitle(TextTypingTitle);
                    break;
                case SET_VOICE_TYPING_TITLE:
                    setTitle(VoiceTypingTitle);
                    break;
                case SET_TARGET_ID_TITLE:
                    setActionBarTitle(mConversationType, mTargetId);
                    break;
                default:
                    break;
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversationlist);
        ButterKnife.bind(this);

        initView();
        getIntentDate();
        initListener();
        initInputState();
        initCallKit();
    }

    private void initCallKit() {
        //CallKit start 2
        RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
            @Override
            public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {
                mCallMemberResult = result;
                getGroupMembersForCall();
                return null;
            }
        });
        //CallKit end 2
    }

    //CallKit start 4
    private RongCallKit.OnGroupMembersResult mCallMemberResult;

    private void getGroupMembersForCall() {
        HttpUtil.getGroupMemeberlist(mGroupId, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallMemberResult.onGotMemberList(null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                ArrayList<String> userIds = new ArrayList<>();
                BaseModel<GroupMemeberModel> model = getGson().fromJson(result, new TypeToken<BaseModel<GroupMemeberModel>>(){}.getType());
                if (response.isSuccessful() && model != null && model.getCode() == 1) {
                    List<GroupMemeberModel> memeber = model.getResult();
                    for (GroupMemeberModel groupitem : memeber) {
                        if (groupitem != null) {
                            userIds.add(groupitem.getRCID());
                        }
                    }
                    mCallMemberResult.onGotMemberList(userIds);
                } else {
                    mCallMemberResult.onGotMemberList(null);
                }
            }
        });
    }
    //CallKit end 4

    private void initInputState() {
        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    int count = typingStatusSet.size();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });
    }

    private void initListener() {
        backActivity.setOnClickListener(mClick);
        detailUser.setOnClickListener(mClick);

        rlTabchat.setOnClickListener(mClick);
        rlTabHint.setOnClickListener(mClick);
        rlTabActive.setOnClickListener(mClick);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.body_img_menu) { //返回
                ConversationListActivity.this.finish();
            } else if (v.getId() == R.id.body_search) { //聊天设置
                // TODO: 2017/12/21 设置当前用户资料
                if (mGroupId != null) {
                    Intent intent = new Intent(mContext, GroupDetailMenuActivity.class);
                    intent.putExtra(GroupDetailMenuActivity.GROUPID, mGroupId);
                    intent.putExtra(GroupDetailMenuActivity.TARGETID, mTargetId);
                    startActivityForResult(intent, 500);
                }
            } else if (v.getId() == R.id.rl_tab_chat) {
                position = 0;
                showFragment();
            } else if (v.getId() == R.id.rl_tab_hint) {
                position = 1;
                showFragment();
            } else if (v.getId() == R.id.rl_tab_active) {
                position = 2;
                showFragment();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 501) {
            closeActivity();
            ConversationListActivity.this.finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        backActivity.setImageResource(R.drawable.icon_back);
        detailUser.setImageResource(R.drawable.default_user);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragment.length; i++) {
            if (!mFragment[i].isAdded()) {
                transaction.add(R.id.conversation_list, mFragment[i]);
            }
            if (i != position) {
                transaction.hide(mFragment[i]);
            }
        }
        transaction.commit();
        showFragment();
    }

    private void showFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragment.length; i++) {
            transaction.hide(mFragment[i]);
        }
        transaction.show(mFragment[position]);
        transaction.commit();
        setTabBackColor();
    }

    private void setTabBackColor() {
        rlTabchat.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabHint.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabActive.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        if (position == 0) {
            rlTabchat.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 1) {
            rlTabHint.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 2) {
            rlTabActive.setBackgroundResource(R.drawable.study_tab_hover);
        }
    }

    public String mTitle;
    public String mTargetId;
    public String mGroupId;
    //    /** 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId */
//    private String mTargetIds;
    private Conversation.ConversationType mConversationType;//会话类型

    public void getIntentDate() {
        Intent intent = getIntent();
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTitle = intent.getData().getQueryParameter("title");
        mGroupId = intent.getData().getQueryParameter("groupId");
//        TestLog.i("对话界面ConversationListActivity： id:" + mTargetId + " - title:" + mTitle);
//        mTargetIds = intent.getData().getQueryParameter("targetIds");
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        setActionBarTitle(mConversationType, mTargetId);
        bodyTvTitle.setText(mTitle);

    }

    /**
     * 设置会话页面 Title
     *
     * @param conversationType 会话类型
     * @param targetId         目标 Id
     */
    private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == null)
            return;
        ll_sub_title.setVisibility(View.GONE);
        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            setPrivateActionBar(targetId);
            TestLog.i("设置私聊界面");
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            setGroupActionBar(targetId);

            ll_sub_title.setVisibility(View.VISIBLE);
            if (mGroupId == null) {
                GroupModel groupByRCID = DBManager.getmInstance().findGroupByRCID(mTargetId);
                if (groupByRCID != null)
                    mGroupId = groupByRCID.getGROUPID() + "";
            }
            TestLog.i("设置群聊界面");
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            setDiscussionActionBar(targetId);
            TestLog.i("设置讨论组界面");
        } else if (conversationType.equals(Conversation.ConversationType.CHATROOM)) {
            setTitle(mTitle);
            TestLog.i("设置聊天室界面");
        } else if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
            setTitle("聊天室");
            TestLog.i("设置系统消息界面");
        } else if (conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
            setAppPublicServiceActionBar(targetId);
            TestLog.i("设置应用公众服务界面");
        } else if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)) {
            setPublicServiceActionBar(targetId);
            TestLog.i("设置公众服务号界面");
        } else if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            setTitle("意见反馈");
            TestLog.i("意见反馈");
        } else {
            setTitle("聊天");
            TestLog.i("一对一聊天");
        }
    }

    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(mTitle)) {
            if (mTitle.equals("null")) {
                if (!TextUtils.isEmpty(targetId)) {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    if (userInfo != null) {
                        setTitle(userInfo.getName());
                    }
                }
            } else {
                setTitle(mTitle);
            }

        } else {
            setTitle(targetId);
        }
    }

    /**
     * 设置群聊界面 ActionBar
     *
     * @param targetId 会话 Id
     */
    private void setGroupActionBar(String targetId) {
        if (!TextUtils.isEmpty(mTitle)) {
            setTitle(mTitle);
        } else {
            setTitle(targetId);
        }
    }

    /**
     * 设置讨论组界面 ActionBar
     */
    private void setDiscussionActionBar(String targetId) {

        if (targetId != null) {

            RongIM.getInstance().getDiscussion(targetId
                    , new RongIMClient.ResultCallback<Discussion>() {
                        @Override
                        public void onSuccess(Discussion discussion) {
                            setTitle(discussion.getName());
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            if (e.equals(RongIMClient.ErrorCode.NOT_IN_DISCUSSION)) {
                                setTitle("不在讨论组中");
                                supportInvalidateOptionsMenu();
                            }
                        }
                    });
        } else {
            setTitle("讨论组");
        }
    }

    /**
     * 设置应用公众服务界面 ActionBar
     */
    private void setAppPublicServiceActionBar(String targetId) {
        if (targetId == null)
            return;

        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.APP_PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置公共服务号 ActionBar
     */
    private void setPublicServiceActionBar(String targetId) {
        if (targetId == null)
            return;
        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //CallKit start 3
        RongCallKit.setGroupMemberProvider(null);
        //CallKit end 3
        RongIMClient.setTypingStatusListener(null);
    }


    //
//    private void initListener() {
//        backActivity.setOnClickListener(mClick);
//        detailUser.setOnClickListener(mClick);
//    }
//
//    private View.OnClickListener mClick = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            if(v.getId() == R.id.body_img_menu) { //返回
//                ConversationListActivity.this.finish();
//            }else if(v.getId() == R.id.body_search) { //聊天设置
//                // TODO: 2017/12/21 设置当前用户资料
//            }
//        }
//    };
//
//    private void initView() {
//        headBg.setImageResource(R.drawable.main_bg);
//        backActivity.setImageResource(R.drawable.icon_back);
//        detailUser.setImageResource(R.drawable.default_user);
//    }
//
//    private String mTitle;
//    private String mTargetId;
//    /** 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId */
//    private String mTargetIds;
//    private Conversation.ConversationType mConversationType;//会话类型
//
//    public void getIntentDate() {
//        Intent intent = getIntent();
//        mTargetId = intent.getData().getQueryParameter("targetId");
//        mTitle = intent.getData().getQueryParameter("title");
//        mTargetIds = intent.getData().getQueryParameter("targetIds");
//        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));
//
//        enterFragment(mConversationType, mTargetId);
//        bodyTvTitle.setText(mTitle);
//    }
//
//    /**
//     * 加载会话页面 ConversationFragment
//     * @param mConversationType
//     * @param mTargetId
//     */
//    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {
//
//        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);
//
//        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
//                .appendQueryParameter("targetId", mTargetId).build();
//
//        fragment.setUri(uri);
//    }
}
