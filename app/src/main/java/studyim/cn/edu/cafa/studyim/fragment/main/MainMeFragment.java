package studyim.cn.edu.cafa.studyim.fragment.main;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.model.MeSettingsModel;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.net.X5WebView;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/10 0010
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MainMeFragment extends BaseFragment {

    //    private static String XS_HOME = "file:///android_res/raw/xs_home.htm";
    private static String XS_HOME = "http://www.cafa.com.cn/wapcafa/xs_home.htm";

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.dl_layout)
    DrawerLayout dlLayout;
    @BindView(R.id.body_img_menu)
    ImageView bodyImgMenu;
    @BindView(R.id.rl_left_menu)
    RelativeLayout rlLeftMenu;
    @BindView(R.id.rv_settings)
    WuhunRecyclerView rv_settings;

    int mDrawerWidth;//抽屉全部拉出来时的宽度
    float scrollWidth;//抽屉被拉出部分的宽度

    LQRAdapterForRecyclerView<MeSettingsModel> settingAdapter;
//    BaseRecycleAdapter<MeSettingsModel> settingAdapter;

    private ViewGroup mViewParent;
    public X5WebView mWebView;
    private List<MeSettingsModel> settingsData;

    private View.OnKeyListener myOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            return false;
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.body_img_menu) {
                //判断左菜单是否打开，如果没有，则的开，否则关闭左菜单
                if (!dlLayout.isDrawerOpen(Gravity.LEFT)) {
                    dlLayout.openDrawer(Gravity.LEFT);
                } else {
                    dlLayout.closeDrawer(Gravity.LEFT);
                }
            }
        }
    };
    private DrawerLayout.DrawerListener mDrawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerStateChanged(int arg0) {}

        //抽屉被拉出来或者推回去
        @Override
        public void onDrawerSlide(View arg0, float arg1) {
            //因为arg1的范围是0.0-1.0，是一个相对整个抽屉宽度的比例
            scrollWidth = arg1 * mDrawerWidth;
            //setScroll中的参数，正数表示向左移动，负数向右
            llContent.setScrollX((int) (-1 * scrollWidth));
        }

        @Override
        public void onDrawerOpened(View arg0) {}

        @Override
        public void onDrawerClosed(View arg0) {}
    };
    private OnItemClickListener settingItemClicik = new OnItemClickListener() {
        @Override
        public void onItemClick(LQRViewHolder helper, ViewGroup parent, View itemView, int position) {
            if (settingsData != null && settingsData.size()>0) {
                String url = settingsData.get(position).getUrl();
                if (!WuhunDataTool.isNullString(url)) {
                    mWebView.loadUrl(url);
                    dlLayout.closeDrawers();
                }
            }
        }
    };

    @Override
    protected void initListener() {
        mWebView.setOnKeyListener(myOnKeyListener);
        bodyImgMenu.setOnClickListener(mOnClickListener);
        //必须给抽屉设置监听事件
        dlLayout.setDrawerListener(mDrawerListener);
        rv_settings.setAdapter(settingAdapter);
        settingAdapter.setOnItemClickListener(settingItemClicik);
    }

    @Override
    protected void initData() {
        mWebView.loadUrl(XS_HOME);
        //本地获取json
        settingsData.add(new MeSettingsModel(R.drawable.sideicon, "我的", "http://www.cafa.com.cn/wapcafa/xs_wd_h.htm" ));
        settingsData.add(new MeSettingsModel(R.drawable.sideicon, "修改密码", "http://www.cafa.com.cn/wapcafa/xs_wd_h.htm" ));
        settingsData.add(new MeSettingsModel(R.drawable.sideicon, "设置", "http://www.cafa.com.cn/wapcafa/h_about.htm" ));
        settingsData.add(new MeSettingsModel(R.drawable.sideicon, "老师", "http://www.cafa.com.cn/wapcafa/h_zx.htm" ));
        settingsData.add(new MeSettingsModel(R.drawable.sideicon, "朋友", "http://www.cafa.com.cn/wapcafa/h_zp.htm" ));
        settingsData.add(new MeSettingsModel(R.drawable.sideicon, "同步", "http://www.baidu.com" ));

//        settingAdapter.refresh(settingsData);
    }

    @Override
    protected void initView() {
        settingsData = new ArrayList<>();

        mViewParent = (ViewGroup) mRootView.findViewById(R.id.webView1);
        mWebView = new X5WebView(mActivity, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));

        dlLayout = mRootView.findViewById(R.id.dl_layout);
        llContent = mRootView.findViewById(R.id.ll_content);

        dlLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//关闭手势滑动
        bodyImgMenu = mRootView.findViewById(R.id.body_img_menu);
        rlLeftMenu = mRootView.findViewById(R.id.rl_left_menu);

        measureView(rlLeftMenu);
        mDrawerWidth = rlLeftMenu.getMeasuredWidth();

//        // settings
        initAdapter();
    }

    private void initAdapter() {
        if (settingAdapter == null) {
            settingAdapter = new LQRAdapterForRecyclerView<MeSettingsModel>(mActivity, settingsData, R.layout.me_settings_item) {

                @Override
                public void convert(LQRViewHolderForRecyclerView helper, MeSettingsModel item, int position) {
                    if (settingsData != null && settingsData.size() > 0) {
                        helper.setText(R.id.tv_setting_text, settingsData.get(position).getTitle());
                        ImageView iv_icon = helper.getView(R.id.iv_setting_icon);

                        String img = settingsData.get(position).getImg();
                        if (!WuhunDataTool.isNullString(img)) {
                            Glide.with(mActivity).load(img).fitCenter().into(iv_icon);
                        } else {
                            Glide.with(mActivity).load(settingsData.get(position).getReserceId()).fitCenter().into(iv_icon);
                        }
                    }
                }
            };
        } else {
            settingAdapter.setData(settingsData);
        }
//            settingAdapter = new BaseRecycleAdapter<MeSettingsModel>(R.layout.me_settings_item, settingsData) {
//                @Override
//                protected void bindData(RecyclerView.ViewHolder holder, int position) {
//                    SettingsViewHolder h = (SettingsViewHolder) holder;
//                    if (settingsData != null && settingsData.size()>0) {
//                        h.tvSettingsText.setText(settingsData.get(position).getTitle());
////                h.ivSettingIcon
//                        String img = settingsData.get(position).getImg();
//                        if (!WuhunDataTool.isNullString(img)) {
//                            Glide.with(mActivity).load(img).fitCenter().into(h.ivSettingIcon);
//                        } else {
//                            Glide.with(mActivity).load(settingsData.get(position).getReserceId()).fitCenter().into(h.ivSettingIcon);
//                        }
//                    }
//                }
//
//                @Override
//                protected RecyclerView.ViewHolder setViweHolder(View view) {
//                    return new SettingsViewHolder(view);
//                }
//            };
//            rv_settings.setAdapter(settingAdapter);
//        } else {
//            settingAdapter.refresh(settingsData);
//        }
    }

    /**
     * 使用View对象的getMessuredHeight()获高度（单位px）
     *
     * @param child 需要测量高度和宽度的View对象，
     */
    protected void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    protected void getIntentData(Bundle arguments) {
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_me_fragment;
    }

    public void backWebView() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public boolean isGoBack() {
        if (mWebView != null && mWebView.canGoBack()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDestroy() {
        //wuhun
        if (mWebView != null) {
            mWebView.clearWebView();
        }
        super.onDestroy();
    }
    /////////////////////////////////////////////////////////////////////////////////
    //// 我是华丽丽的分割线~
    /////////////////////////////////////////////////////////////////////////////////
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        //wuhun
//        TestLog.i("settingsData_onAttach:");
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //wuhun
//        TestLog.i("settingsData_onCreate:");
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        //wuhun
//        TestLog.i("settingsData_onCreateView:");
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        //wuhun
//        TestLog.i("settingsData_onActivityCreated:" + settingsData.size());
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        //wuhun
//        TestLog.i("settingsData_onStart:" + settingsData.size());
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        //wuhun
//        TestLog.i("settingsData_onResume:" + settingsData.size());
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        dlLayout.closeDrawers();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        //wuhun
//        TestLog.i("settingsData_onStop:" + settingsData.size());
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        //wuhun
//        TestLog.i("settingsData_onDestroyView:" + settingsData.size());
//    }
//
//    @Override
//    public void onDestroyOptionsMenu() {
//        super.onDestroyOptionsMenu();
//        //wuhun
//        TestLog.i("settingsData_onDestroyOptionsMenu:" + settingsData.size());
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        //wuhun
//        TestLog.i("settingsData_onDetach:" + settingsData.size());
//    }

}
