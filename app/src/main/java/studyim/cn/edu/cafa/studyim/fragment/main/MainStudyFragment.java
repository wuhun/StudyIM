package studyim.cn.edu.cafa.studyim.fragment.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.StudyShearchActivity;
import studyim.cn.edu.cafa.studyim.app.MyApplication;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyClassFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyCommonFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyLeadFragment;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/10 0010
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MainStudyFragment extends BaseFragment {


    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;
    @BindView(R.id.body_search)
    ImageView bodySearch;
    @BindView(R.id.rl_me_head)
    RelativeLayout rlMeHead;
    @BindView(R.id.textview1)
    TextView textview1;
    @BindView(R.id.rl_tab_all)
    RelativeLayout rlTabAll;
    @BindView(R.id.rl_tab_class)
    RelativeLayout rlTabClass;
    @BindView(R.id.rl_tab_lead)
    RelativeLayout rlTabLead;
    @BindView(R.id.rl_tab_common)
    RelativeLayout rlTabCommon;
    @BindView(R.id.fl_friend_list)
    FrameLayout flFriendList;

    private int position = 0;

    private FragmentTransaction ft;

//    private BaseFragment mFragment[] = {
//            FragmentFactory.getInstance().getstudyAllFragment(),
//            FragmentFactory.getInstance().getStudyClassFragment(),
//            FragmentFactory.getInstance().getStudyLeadFragment()
//    };
    private Fragment mFragment[] = {
            initConversationList(),
            initClassList(),
            initLearList(),
            initCommonList()
    };

    StudyCommonFragment mStudyCommonFragment;
    private Fragment initCommonList() {
//        mStudyCommonFragment = FragmentFactory.getInstance().getStudyCommonFragment();
        mStudyCommonFragment = new StudyCommonFragment();
        return mStudyCommonFragment;
    }

    StudyLeadFragment mStudyLeadFragment;
    private Fragment initLearList() {
//        mStudyLeadFragment = FragmentFactory.getInstance().getStudyLeadFragment();
        mStudyLeadFragment = new StudyLeadFragment();
        return mStudyLeadFragment;
    }

    StudyClassFragment mStudyClassFragment = null;
    private Fragment initClassList() {
//        mStudyClassFragment = FragmentFactory.getInstance().getStudyClassFragment();
        mStudyClassFragment = new StudyClassFragment();
        return mStudyClassFragment;
    }

    /** 初始化聊天记录 */
//    ConversationListFragment mConversationListFragment = null;
    private Fragment initConversationList() {
//        TestLog.i("initConversationList() - 聊天记录列表，融云提供的fragment，需要传入uri与adapter");
//        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            //listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
//            TestLog.i("==>" + MyApplication.getContext().getApplicationInfo().packageName);
            Uri uri = Uri.parse("rong://" + MyApplication.getContext().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话, 聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组, 聚合显示
//                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "true")//公共服务号， 非聚合显示
//                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "true")//订阅号， 非聚合显示
//                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统, 聚合显示
//                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组, 聚合显示
                    .build();
            listFragment.setUri(uri);
//            mConversationListFragment = listFragment;
//            return listFragment;
        return listFragment;
//        } else {
//            return mConversationListFragment;
//        }
    }

    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.rl_tab_all:
                    position = 0;
                    showFragment();
                    setTabBackColor();
                    break;
                case R.id.rl_tab_class:
                    position = 1;
                    setTabBackColor();
                    showFragment();
                    break;
                case R.id.rl_tab_lead:
                    position = 2;
                    setTabBackColor();
                    showFragment();
                    break;
                case R.id.rl_tab_common:
                    position = 3;
                    setTabBackColor();
                    showFragment();
                    break;
                case R.id.body_search:
                    jumpToActivity(StudyShearchActivity.class);
                    break;
                default:
                    WuhunToast.error(R.string.kaifa).show();
                    break;
            }
        }
    };

    private void setTabBackColor() {
        rlTabAll.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabClass.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabLead.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabCommon.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        if (position == 0) {
            rlTabAll.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 1) {
            rlTabClass.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 2) {
            rlTabLead.setBackgroundResource(R.drawable.study_tab_hover);
        }else if(position == 3) {
            rlTabCommon.setBackgroundResource(R.drawable.study_tab_hover);
        }
    }

    private void showFragment() {
        ft = getChildFragmentManager().beginTransaction();
        for(int i=0;i<mFragment.length;i++){
            ft.hide(mFragment[i]);
        }
        ft.show(mFragment[position]).commit();
//        ft.replace(R.id.fl_friend_list, mFragment[position]).commit();
    }

    @Override
    protected void initListener() {
        rlTabAll.setOnClickListener(mOnClick);
        rlTabClass.setOnClickListener(mOnClick);
        rlTabLead.setOnClickListener(mOnClick);
        rlTabCommon.setOnClickListener(mOnClick);
        bodySearch.setOnClickListener(mOnClick);
    }

    @Override
    protected void initData() {}

    @Override
    protected void initView() {
//        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft = getChildFragmentManager().beginTransaction();
        for (int i = 0; i < mFragment.length; i++) {
            if(!mFragment[i].isAdded()) {
                ft.add(R.id.fl_friend_list, mFragment[i], String.valueOf(i))
                        .hide(mFragment[i]);
            }

        }
        ft.show(mFragment[0]).commit();
//        ft.replace(R.id.fl_friend_list, mFragment[0]).commit();
        setTabBackColor();
    }

    @Override
    protected void getIntentData(Bundle arguments) {}

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_study_fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        position = 0;
    }
}
