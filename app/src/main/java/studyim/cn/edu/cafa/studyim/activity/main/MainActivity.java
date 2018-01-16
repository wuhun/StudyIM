package studyim.cn.edu.cafa.studyim.activity.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.common.Constant;
import studyim.cn.edu.cafa.studyim.fragment.main.MainContactFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainHomeFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainMeFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainResourceFragment;
import studyim.cn.edu.cafa.studyim.fragment.main.MainStudyFragment;
import studyim.cn.edu.cafa.studyim.model.UserGetInfo;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import studyim.cn.edu.cafa.studyim.util.Manager.FragmentFactory;
import studyim.cn.edu.cafa.studyim.util.UserAvatarUtil;
import tools.com.lvliangliang.wuhuntools.adapter.lazyViewPgaerAdapter.LazyFragmentPagerAdapter;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.manager.BroadcastManager;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;

import static studyim.cn.edu.cafa.studyim.app.MyApplication.getContext;
import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;
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

    ImageView dot_study, dot_resource, dot_contact, dot_home, dot_me;
    private ImageView mDotimg[] = {dot_study, dot_resource, dot_contact, dot_home, dot_me};

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
        initEvent(); //选中位置
        mTabHost.setCurrentTab(3);
        initData();
    }

    private void initData() {
        // TODO: 2017/12/25 我的广播加载我的提醒红点
        BroadcastManager.getInstance(getContext()).register(Constant.ME_SHOW_RED, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mDotimg[4].setVisibility(View.VISIBLE);
            }
        });
        // 更新个人信息
        BroadcastManager.getInstance(getContext()).register(Constant.USER_GETINFO_REFRESH, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initUserInfo(getSPUtil().getRctoken());
            }
        });

        //  会话聊天界面，未读消息提醒
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        RongIM.getInstance().addUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int count) {
                unreadMsg(count);
            }
        }, conversationTypes);
    }

    private void unreadMsg(int count) {
//        TestLog.i("MainActivity: 未读消息：" + count);
        if (count == 0) {
            mDotimg[0].setVisibility(View.GONE);
        } else {
            mDotimg[0].setVisibility(View.VISIBLE);
        }
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
                initUserInfo(s);
//                WuhunToast.normal("连接成功").show);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                WuhunToast.normal("连接融云服务器失败").show();
            }
        });
    }

    private void initUserInfo(final String token) {
        HttpUtil.userGetInfo(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                WuhunToast.normal(R.string.request_error).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (WuhunDataTool.isNullString(result))
                    WuhunToast.normal(R.string.request_error).show();
                UserGetInfo userGetInfo = getGson().fromJson(result, UserGetInfo.class);
                if (response.isSuccessful() && userGetInfo != null) {
                    if (userGetInfo.getCode() == 1) {
//                        Message message = handler.obtainMessage(REQUEST_SUCCESS, userGetInfo);
//                        handler.sendMessage(message);
                        UserGetInfo.ResultBean user = userGetInfo.getResult();
                        if (user != null) {
                            Uri parse = Uri.parse(UserAvatarUtil.getAvatarUri(user.getUserId() + "", user.getNickName(), user.getAvatar()));
                            if (parse != null) {
                                UserInfo info = new UserInfo(token, user.getNickName(), parse);
                                RongIM.getInstance().refreshUserInfoCache(info);
                            }
                        }
                    } else {
                        Message message = handler.obtainMessage(REQUEST_FAIL, userGetInfo);
                        handler.sendMessage(message);
                    }
                } else {
                    WuhunToast.normal(R.string.request_error).show();
                }
            }
        });
    }

    private static final int REQUEST_SUCCESS = 0x01;
    private static final int REQUEST_FAIL = 0x02;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == REQUEST_FAIL) {
                UserGetInfo info = (UserGetInfo) msg.obj;
                if (WuhunDataTool.isNullString(info.getMsg()))
                    WuhunToast.error(R.string.request_fail).show();
                WuhunToast.error(info.getMsg()).show();
            }
            super.handleMessage(msg);
        }
    };

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
        mViewPager.setOffscreenPageLimit(0);//预加载界面
    }

    /**
     * 获取当前tab标签
     */
    private View getTabView(int index) {
//        TestLog.i("getTabView调用次数：" + index);
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item, null);

        ImageView image = (ImageView) view.findViewById(R.id.image);
        TextView title = (TextView) view.findViewById(R.id.title);

        mDotimg[index] = (ImageView) view.findViewById(R.id.img_red_dot);

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
                //屏幕滚动过程中不断被调用
            }

            @Override
            public void onPageSelected(int position) { //当前界面 position
                mTabHost.setCurrentTab(position);
                //取消红点
                ImageView img = (ImageView) mTabHost.getCurrentTabView().findViewById(R.id.img_red_dot);
                img.setVisibility(View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //手指操作屏幕 1(按下) , 2(抬起) ，0（结束）
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(new IUnReadMessageObserver() {
            @Override
            public void onCountChanged(int i) {
                unreadMsg(i);
            }
        });

        BroadcastManager.getInstance(getContext()).unregister(Constant.ME_SHOW_RED);
    }
}
