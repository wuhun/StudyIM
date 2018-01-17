package studyim.cn.edu.cafa.studyim.fragment.main;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.BindView;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseFragment;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;
import tools.com.lvliangliang.wuhuntools.net.X5WebView;

/**
 * ================================================
 * 作    者：悟魂(了解自己，感悟灵魂，做最好的自己)
 * 创建日期：2017/11/10 0010
 * 版    本：1.0
 * 描    述：主页 - 官网地址
 * 修订历史：
 * ================================================
 */
public class MainHomeFragment extends BaseFragment {

    private String TAG = "MainHomeFragment";

    private static String HOMEPATH = "file:///android_res/raw/home.htm";
    private ViewGroup mViewParent;
    private X5WebView mWebView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;



    private View.OnKeyListener myOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            TestLog.i("点击了返回键");

            if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                mWebView.goBack();
                mActivity.getSupportFragmentManager().popBackStack();
                return true;
            }
            return false;
        }
    };

    @Override
    protected void initListener() {
        TestLog.i(TAG + " - initListener()");
//        mWebView.setOnKeyListener(myOnKeyListener);
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
        TestLog.i(TAG + " - initData()");
        mWebView.loadUrl(HOMEPATH);
    }

    @Override
    protected void initView() {
        TestLog.i(TAG + " - initView()");
        mViewParent = (ViewGroup) mRootView.findViewById(R.id.webView1);
        View childAt = mViewParent.getChildAt(0);
        int childId = childAt.getId();
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );
        lp.addRule(RelativeLayout.BELOW, childId);

        mWebView = new X5WebView(mActivity, null);
        mViewParent.addView(mWebView, lp);
    }

    @Override
    protected void getIntentData(Bundle arguments) {
        TestLog.i(TAG + " - getIntentData()");
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.main_home_fragment;
    }

    public void backWebView() {
        TestLog.i(TAG + " - backWebView()");
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }

    public boolean isGoBack() {
        TestLog.i(TAG + " - isGoBack()");
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
        TestLog.i(TAG + " - onDestroy()");
        if (mWebView != null) {
            mWebView.clearWebView();
        }
        super.onDestroy();
    }
}
