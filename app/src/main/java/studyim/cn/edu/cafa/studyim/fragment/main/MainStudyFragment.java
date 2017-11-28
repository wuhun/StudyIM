package studyim.cn.edu.cafa.studyim.fragment.main;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.main.StudyShearchActivity;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyAllFragment;
import studyim.cn.edu.cafa.studyim.fragment.study.StudyClassFragment;
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
    @BindView(R.id.fl_friend_list)
    FrameLayout flFriendList;

    private int position = 0;

    private BaseFragment mFragment[] = {
            new StudyAllFragment(),new StudyClassFragment(),new StudyLeadFragment()
    };
    private FragmentTransaction ft;

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
                case R.id.body_search:
                    jumpToActivity(StudyShearchActivity.class);
                    break;
                default:
                    WuhunToast.error("正在建设中，敬请期待").show();
                    break;
            }
        }
    };

    private void setTabBackColor() {
        rlTabAll.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabClass.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        rlTabLead.setBackgroundResource(R.drawable.study_tab_bottom_redline_selector);
        if (position == 0) {
            rlTabAll.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 1) {
            rlTabClass.setBackgroundResource(R.drawable.study_tab_hover);
        } else if (position == 2) {
            rlTabLead.setBackgroundResource(R.drawable.study_tab_hover);
        }
    }

    private void showFragment() {
//        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fl_friend_list, mFragment[position]).commit();
//        ft.hide(mFragment[0]).hide(mFragment[1]).hide(mFragment[2]);
//        ft.show(fragment).commit();
    }

    @Override
    protected void initListener() {
        rlTabAll.setOnClickListener(mOnClick);
        rlTabClass.setOnClickListener(mOnClick);
        rlTabLead.setOnClickListener(mOnClick);
        bodySearch.setOnClickListener(mOnClick);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
//        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.fl_friend_list, mFragment[0]).commit();
//        ft.add(R.id.fl_friend_list, mFragment[0], "all")
//                .add(R.id.fl_friend_list, mFragment[1], "friend")
//                .add(R.id.fl_friend_list, mFragment[2], "lead")
//                .hide(mFragment[1])
//                .hide(mFragment[2])
//                .commit();
        setTabBackColor();
    }

    @Override
    protected void getIntentData(Bundle arguments) {
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_study_fragment;
    }

}
