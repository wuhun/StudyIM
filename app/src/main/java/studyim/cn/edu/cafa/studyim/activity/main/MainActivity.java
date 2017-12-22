package studyim.cn.edu.cafa.studyim.activity.main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.RongIMClient;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainContactFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainHomeFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainMeFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainResourceFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainStudyFragment;
import studyim.cn.edu.cafa.studyim.util.Manager.FragmentFactory;
import tools.com.lvliangliang.wuhuntools.adapter.lazyViewPgaerAdapter.LazyFragmentPagerAdapter;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

public class MainActivity extends BaseActivity {

    private FragmentTabHost mTabHost;
    private ViewPager mViewPager;
    private List<BaseFragment> mFragmentList;
    private Class mClass[] = {
            MainStudyFragment.class, MainResourceFragment.class, MainContactFragment.class, MainHomeFragment.class, MainMeFragment.class
    };
    private String mTitles[] = {"学习", "资源", "通讯录", "官网", "我的"};
    private BaseFragment mFragment[] = {
            FragmentFactory.getInstance().getMainStudyFragment(),
            FragmentFactory.getInstance().getMainResourceFragment(),
            FragmentFactory.getInstance().getMainContactFragment(),
            FragmentFactory.getInstance().getMainHomeFragment(),
            FragmentFactory.getInstance().getMainMeFragment()
    };
    private int mImages[] = {
            R.drawable.main_tab_study_icon_selecor,
            R.drawable.main_tab_resource_icon_selecor,
            R.drawable.main_tab_contact_icon_selecor,
            R.drawable.main_tab_home_icon_selecor,
            R.drawable.main_tab_me_icon_selecor,
    };
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initConnect();
        initView();
        initEvent();
        mTabHost.setCurrentTab(3);
    }

    private void initConnect() {
        RongIMClient.connect(getSPUtil().getRctoken(), new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                WuhunToast.normal("Token 错误，请重新登录").show();
            }

            @Override
            public void onSuccess(String s) {
                TestLog.i("success" + s);
                WuhunToast.normal("连接成功").show();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                WuhunToast.normal("连接融云失败").show();
            }
        });
    }

    private void initView() {
        context = this;
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mFragmentList = new ArrayList<BaseFragment>();

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);//tab分割线

        for (int i = 0; i < mFragment.length; i++) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTitles[i]).setIndicator(getTabView(i));//底部
            mTabHost.addTab(tabSpec, mClass[i], null);
            mFragmentList.add(mFragment[i]);//fragment
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.WHITE);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(0xACBDCA);//背景色
        }

        mViewPager.setAdapter(new LazyFragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            protected Fragment getItem(ViewGroup container, int position) {
                return mFragmentList.get(position);
            }
        });
//        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                return mFragmentList.get(position);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragmentList.size();
//            }
//
//            /////////////////////////////////////////////////////////////////////////////////
////            @Override
////            public Object instantiateItem(ViewGroup container, int position) {
////                BaseFragment f = (BaseFragment) super.instantiateItem(container, position);
////                return f;
////            }
//
//            @Override
//            public int getItemPosition(Object object) {
//                return PagerAdapter.POSITION_NONE;
////                return super.getItemPosition(object);
//            }
//
//            @Override
//            public void destroyItem(ViewGroup container, int position, Object object) {
//                //super.destroyItem(container, position, object);
//            }
//
//            //            @Override
////            public void destroyItem(ViewGroup container, int position, Object object) {
////                super.destroyItem(container, position, object);
//////                if(position > mDisplayViewList.size() - 1)return;
//////                container.removeView(mDisplayViewList.get(position));
//////                TestLog.i("pageAdapter中移除了"+position);
////            }
//            /////////////////////////////////////////////////////////////////////////////////
//        });
    }

    /**
     * 获取当前tab标签
     */
    private View getTabView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);

        image.setImageResource(mImages[index]);
        title.setText(mTitles[index]);

        return view;
    }

    private void initEvent() {

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                mViewPager.setCurrentItem(mTabHost.getCurrentTab());
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabHost.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int currentItem = mViewPager.getCurrentItem();
            if (mFragment[currentItem] instanceof MainHomeFragment) {
                MainHomeFragment mainHomeFragment = (MainHomeFragment) mFragment[currentItem];
                if (mainHomeFragment.isGoBack()) {
                    mainHomeFragment.backWebView();
                    return true;
                }
            }
            if (mFragment[currentItem] instanceof MainResourceFragment) {
                MainResourceFragment mainResourceFragment = (MainResourceFragment) mFragment[currentItem];
                if (mainResourceFragment.isGoBack()) {
                    mainResourceFragment.backWebView();
                    return true;
                }
            }
            if (mFragment[currentItem] instanceof MainMeFragment) {
                MainMeFragment mainMeFragment = (MainMeFragment) mFragment[currentItem];
                if (mainMeFragment.isGoBack()) {
                    mainMeFragment.backWebView();
                    return true;
                }
            }
            //退出
            if (WuhunToast.doubleClickExit()) {
                MainActivity.this.finish();
//                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
