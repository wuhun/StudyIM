package studyim.cn.edu.cafa.studyim.fragment.main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import butterknife.BindView;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import tools.com.lvliangliang.wuhuntools.net.X5WebView;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/10 0010
 * 版    本：1.0
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class MainResourceFragment extends BaseFragment {

//    private static String JS_HOME = "file:///android_res/raw/js_home.htm";
    private static String JS_HOME = "http://www.cafa.com.cn/wapcafa/js_home.htm";
    private ViewGroup mViewParent;
    public X5WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private View.OnKeyListener myOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void initListener() {
        mWebView.setOnKeyListener(myOnKeyListener);

        mWebView.setOnloding(new X5WebView.WebLoding() {
            @Override
            public void logining(int i) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(i);
            }

            @Override
            public void loginOver() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void setTitle(String title) {}
        });
    }


    @Override
    protected void initData() {
        mWebView.loadUrl(JS_HOME);
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
    protected void getIntentData(Bundle arguments) {

    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_resourse_fragment;
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
        if(mWebView != null) {
            mWebView.clearWebView();
        }
        super.onDestroy();
    }
}
