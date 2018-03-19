package studyim.cn.edu.cafa.studyim.fragment.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.activity.login.LoginActivity;
import studyim.cn.edu.cafa.studyim.activity.main.MainActivity;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import studyim.cn.edu.cafa.studyim.db.DBManager;
import studyim.cn.edu.cafa.studyim.model.SettingBaseModel;
import studyim.cn.edu.cafa.studyim.model.SettingModel;
import studyim.cn.edu.cafa.studyim.util.HttpUtil;
import tools.com.lvliangliang.wuhuntools.adapter.LQRAdapterForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolder;
import tools.com.lvliangliang.wuhuntools.adapter.LQRViewHolderForRecyclerView;
import tools.com.lvliangliang.wuhuntools.adapter.OnItemClickListener;
import tools.com.lvliangliang.wuhuntools.net.X5WebView;
import tools.com.lvliangliang.wuhuntools.util.WuhunDataTool;
import tools.com.lvliangliang.wuhuntools.util.WuhunThread;
import tools.com.lvliangliang.wuhuntools.widget.WuhunToast;
import tools.com.lvliangliang.wuhuntools.widget.recyclerview.WuhunRecyclerView;

import static android.app.Activity.RESULT_OK;
import static studyim.cn.edu.cafa.studyim.app.MyApplication.getGson;
import static studyim.cn.edu.cafa.studyim.app.MyApplication.getSPUtil;

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
//    private static String XS_HOME = "http://10.10.10.102/TestWebProject/upload.jsp";

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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    X5WebView mWebView;

    int mDrawerWidth;//抽屉全部拉出来时的宽度
    float scrollWidth;//抽屉被拉出部分的宽度

    LQRAdapterForRecyclerView<SettingModel> settingAdapter;
//    BaseRecycleAdapter<SettingBaseModel> settingAdapter;

    private RelativeLayout mViewParent;
//    public X5WebView mWebView;
    private List<SettingModel> settingsData;

    public int currentSettingVersion = 1;

    private static final String TONG_BU = "load_setting";
    private static final String EXIT_APP = "exit_app";

    private static final int REQUEST_ERROR = 0x01;
    private static final int REQUEST_SUCCESS = 0x02;
    private static final int VERSION_REQUEST_ERROR = 0x03;
    private final static int FILE_CHOOSER_RESULT_CODE = 10000;


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
                if (!WuhunDataTool.isNullString(url) && url.startsWith("http://")) {
                    mWebView.loadUrl(url);
                    dlLayout.closeDrawers();
                }else if(url.equals(TONG_BU)) {
                    // TODO: 2017/11/28 服务器获取设置json并添加到缓存 - 解析显示
//                    updateSetting();
                    isUpdateSetting = false;
                    requestSetting(true);
//                    WuhunToast.normal("同步预留位置").show();
                }else if(url.equals(EXIT_APP)) {
                    MainActivity main = (MainActivity) getActivity();
                    getSPUtil().setTokens("");
                    dlLayout.closeDrawers();
                    jumpToActivity(LoginActivity.class);
                    main.closeActivity();
                }
            }
        }
    };

    /** 查看版本更新 */
    private void requestSetting(final boolean isCache) {
        HttpUtil.query_setting_list(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(isCache) {
                    handler.sendEmptyMessage(VERSION_REQUEST_ERROR);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                if(response.isSuccessful() && !WuhunDataTool.isNullString(result)) {
                    final SettingBaseModel meSettings = getGson().fromJson(result, SettingBaseModel.class);
                    WuhunThread.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO: 2018/1/4 判断版本，提示更新
                            if (meSettings.getVersion() != currentSettingVersion) {
                                isUpdateSetting = true;
                                settingAdapter.notifyDataSetChangedWrapper();
                            }
                            if (isCache) {
//                        getSPUtil().setSettingList(result);
                                getSPUtil().setSettingVersion(meSettings.getVersion());
                                Message msg = handler.obtainMessage(REQUEST_SUCCESS, result);
                                handler.sendMessage(msg);
                            }
                        }
                    });
                }
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == REQUEST_ERROR) {
                WuhunToast.error(R.string.request_error).show();
            } else if(msg.what == REQUEST_SUCCESS) {
                String result = (String) msg.obj;
                settingsData.clear();
//                WuhunIOTool.mFileWrite(MyApplication.updateSettingFile(), result);
                updateSetting(result);
            }else if(msg.what == VERSION_REQUEST_ERROR) {
                WuhunToast.error(R.string.refresh_fail).show();
            }
        }
    };

//    private void updateSetting() {
//        TestLog.i("下载位置：" + MyApplication.updateSettingFile() + "\n 下载地址："+Constant.ME_FRAGMENT_UPDATE_SETTINGS);
//        DownloadUtil.getInstence().download(
//                Constant.ME_FRAGMENT_UPDATE_SETTINGS,
//                MyApplication.updateSettingFile(),
//                new DownloadUtil.OnDownloadListener() {
//                    @Override
//                    public void onDownloadSuccess() {
//                        WuhunThread.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                LoadDialog.dismiss(mActivity);
//                                WuhunToast.info("同步设置成功！").show();
//                                String str = WuhunIOTool.bufferInputRead(new File(MyApplication.updateSettingFile()));
//                                TestLog.i("设置同步：" + str);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onDownloading(final int progress) {
//                        WuhunThread.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                LoadDialog.show(mActivity, "正在同步中……" + progress + "%");
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onDownloadFailed() {
//                        WuhunThread.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                LoadDialog.dismiss(mActivity);
//                                WuhunToast.error("更新失败！").show();
//                            }
//                        });
//                    }
//                }
//        );
//    }

    @Override
    protected void initListener() {
        mWebView.setOnKeyListener(myOnKeyListener);
        bodyImgMenu.setOnClickListener(mOnClickListener);
        //必须给抽屉设置监听事件
        dlLayout.setDrawerListener(mDrawerListener);
        rv_settings.setAdapter(settingAdapter);
        settingAdapter.setOnItemClickListener(settingItemClicik);

        mWebView.setOnloding(new X5WebView.WebLoding() {
            @Override
            public void logining(int i) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(i);
            }

            @Override
            public void loginOver() {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void setTitle(String title) {}
        });

        mWebView.setImgSelect(new X5WebView.ImageSelect() {
            @Override
            public void openImageChooserActivity() {
                MainMeFragment.this.openImageChooserActivity();
            }
        });
    }

    private void openImageChooserActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (null == mWebView.uploadMessage && null == mWebView.uploadMessageAboveL) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mWebView.uploadMessageAboveL != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mWebView.uploadMessage != null) {
                mWebView.uploadMessage.onReceiveValue(result);
                mWebView.uploadMessage = null;
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != FILE_CHOOSER_RESULT_CODE || mWebView.uploadMessageAboveL == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mWebView.uploadMessageAboveL.onReceiveValue(results);
        mWebView.uploadMessageAboveL = null;
    }

    @Override
    protected void initData() {
        mWebView.loadUrl(XS_HOME);
        //本地获取json
//        String json = getSPUtil().getSettingList();
//        String json = WuhunIOTool.bufferInputRead(new File(MyApplication.updateSettingFile()));
        List<SettingModel> settings = DBManager.getmInstance().getSettings();
        if (settings == null) {
            WuhunToast.error(R.string.parsing_fail).show();
        } else {
            currentSettingVersion = getSPUtil().getSettingVersion();
            requestSetting(false);
            settingsData = settings;
        }
        addDefaultSetting();
    }

    private void updateSetting(String json) {
//        TestLog.i("json ==> " + json);
//        if(WuhunDataTool.isNullString(json)) {
//            json = "{\"version\":1,\"result\":[{\"img\":\"http://10.10.10.107:8080/test/sideicon.png\",\"reserceId\":0,\"title\":\"我的\",\"url\":\"http://www.baidu.com\"},{\"img\":\"http://10.10.10.107:8080/test/sideicon.png\",\"reserceId\":0,\"title\":\"朋友\",\"url\":\"http://www.baidu.com\"},{\"img\":\"http://10.10.10.107:8080/test/sideicon.png\",\"reserceId\":0,\"title\":\"修改密码\",\"url\":\"http://www.baidu.com\"},{\"img\":\"http://10.10.10.107:8080/test/sideicon.png\",\"reserceId\":0,\"title\":\"设置\",\"url\":\"http://www.baidu.com\"}]}";
//        }
        SettingBaseModel resultModel = getGson().fromJson(json, SettingBaseModel.class);
        if (resultModel == null) {
            WuhunToast.error(R.string.parsing_fail).show();
            addDefaultSetting();
        } else {
            requestSetting(false);
            for(SettingModel model : resultModel.getResult()){
                settingsData.add(model);
            }
            DBManager.getmInstance().clearSettins();
            DBManager.getmInstance().saveSettingsList(settingsData);
            addDefaultSetting();
            currentSettingVersion = resultModel.getVersion();
            WuhunToast.info(R.string.menu_refresh_success).show();
        }
//        settingsData.add(new SettingBaseModel(R.drawable.sideicon, "我的", "http://www.cafa.com.cn/wapcafa/xs_wd_h.htm" ));
//        settingsData.add(new SettingBaseModel(R.drawable.sideicon, "修改密码", "http://www.cafa.com.cn/wapcafa/xs_wd_h.htm" ));
//        settingsData.add(new SettingBaseModel(R.drawable.sideicon, "设置", "http://www.cafa.com.cn/wapcafa/h_about.htm" ));
//        settingsData.add(new SettingBaseModel(R.drawable.sideicon, "朋友", "http://www.cafa.com.cn/wapcafa/h_zp.htm" ));
    }

    private void addDefaultSetting() {
        settingsData.add(new SettingModel(R.drawable.sideicon, getString(R.string.syn), TONG_BU ));
        settingsData.add(new SettingModel(R.drawable.sideicon, getString(R.string.exit), EXIT_APP ));
        settingAdapter.setData(settingsData);
    }

    @Override
    protected void initView() {
        settingsData = new ArrayList<>();

        mViewParent = (RelativeLayout) mRootView.findViewById(R.id.webView1);

        View childAt = mViewParent.getChildAt(0);
        int childId = childAt.getId();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        lp.addRule(RelativeLayout.BELOW, childId);

        mWebView = new X5WebView(mActivity, null);
        mViewParent.addView(mWebView, lp);


        dlLayout = mRootView.findViewById(R.id.dl_layout);
        llContent = mRootView.findViewById(R.id.ll_content);

        dlLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);//关闭手势滑动

        bodyImgMenu = mRootView.findViewById(R.id.body_img_menu);
        rlLeftMenu = mRootView.findViewById(R.id.rl_left_menu);

        measureView(rlLeftMenu);
        mDrawerWidth = rlLeftMenu.getMeasuredWidth();

//        // settings
        initAdapter();
        dlLayout.closeDrawer(Gravity.LEFT);
    }

    /** 是否需要同步 */
    private boolean isUpdateSetting = false;

    private void initAdapter() {
        if (settingAdapter == null) {
            settingAdapter = new LQRAdapterForRecyclerView<SettingModel>(mActivity, settingsData, R.layout.me_settings_item) {
                @Override
                public void convert(LQRViewHolderForRecyclerView helper, SettingModel item, int position) {
                    if (settingsData != null && settingsData.size() > 0) {
                        helper.setText(R.id.tv_setting_text, settingsData.get(position).getTitle());
                        ImageView iv_icon = helper.getView(R.id.iv_setting_icon);

                        String url = settingsData.get(position).getUrl();
                        TextView check_version = helper.getView(R.id.check_version);
                        if (url.equals(TONG_BU) && isUpdateSetting) {
                            check_version.setVisibility(View.VISIBLE);
                        } else {
                            check_version.setVisibility(View.GONE);
                        }

                        String img = settingsData.get(position).getImg();
                        if (!WuhunDataTool.isNullString(img)) {
                            Glide.with(mActivity).load(img)
                                    .error(R.drawable.sideicon).fitCenter().into(iv_icon);
                        } else {
                            Glide.with(mActivity).load(settingsData.get(position).getReserceId())
                                    .error(R.drawable.sideicon).fitCenter().into(iv_icon);
                        }
                    }
                }
            };
        } else {
            settingAdapter.setData(settingsData);
        }
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
    public void onResume() {
        super.onResume();
        mWebView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onDestroy() {
        //wuhun
        if (mWebView != null) {
            mWebView.clearWebView();
            mWebView = null;
        }
        super.onDestroy();
    }
    /////////////////////////////////////////////////////////////////////////////////
    //// 我是华丽丽的分割线~
    /////////////////////////////////////////////////////////////////////////////////

}
