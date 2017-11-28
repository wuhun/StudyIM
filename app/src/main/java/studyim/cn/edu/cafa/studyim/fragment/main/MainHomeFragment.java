package studyim.cn.edu.cafa.studyim.fragment.main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.net.X5WebView;
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
public class MainHomeFragment extends BaseFragment {

    private static String HOMEPATH = "file:///android_res/raw/home.htm";
    private ViewGroup mViewParent;
    public X5WebView mWebView;


    private View.OnKeyListener myOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            WuhunToast.info("点击了返回键");
            TestLog.i("点击了返回键");

            if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack();
                mActivity.getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void initListener() {
//        mWebView.setOnKeyListener(myOnKeyListener);
    }

    @Override
    protected void initData() {
        mWebView.loadUrl(HOMEPATH);
    }

    @Override
    protected void initView() {
        mViewParent = (ViewGroup) mRootView.findViewById(R.id.webView1);
        mWebView = new X5WebView(mActivity, null);
        mViewParent.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void getIntentData(Bundle arguments) { }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_home_fragment;
    }

    @Override
    public void onDestroy() {
        if(mWebView != null) {
            mWebView.clearWebView();
        }
        super.onDestroy();
    }

    public void backWebView(){
        if(mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public boolean isGoBack(){
        if (mWebView != null && mWebView.canGoBack()) {
            return true;
        } else {
            return false;
        }
    }
}
