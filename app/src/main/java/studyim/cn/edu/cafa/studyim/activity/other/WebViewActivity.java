package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studyim.cn.edu.cafa.studyim.R;
import studyim.cn.edu.cafa.studyim.base.BaseActivity;
import tools.com.lvliangliang.wuhuntools.net.X5WebView;

public class WebViewActivity extends BaseActivity {

    @BindView(R.id.head_bg)
    ImageView headBg;
    @BindView(R.id.body_img_menu)
    ImageView bodyLeft;
    @BindView(R.id.body_tv_title)
    TextView bodyTvTitle;

    @BindView(R.id.body_search)
    ImageView bodySearch;
    @BindView(R.id.body_ok)
    TextView bodyOk;
    @BindView(R.id.fl_webview)
    FrameLayout flWebview;

    public static final String REQUEST_URI = "uri";
    public X5WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        initView();
        getIntentData();
        initListener();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        String request_uri = intent.getStringExtra(REQUEST_URI);


        if (!TextUtils.isEmpty(request_uri)) {
            mWebView.loadUrl(request_uri);
            setWebTitle();
        }
    }

    private void setWebTitle() {
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                bodyTvTitle.setText(title);
            }
        });
    }

    private void initListener() {
        bodyLeft.setOnClickListener(mClick);
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.body_img_menu:
                    WebViewActivity.this.finish();
                    break;
            }
        }
    };

    private void initView() {
        headBg.setImageResource(R.drawable.main_bg);
        bodyLeft.setImageResource(R.drawable.icon_back);
        bodySearch.setVisibility(View.GONE);
        bodyOk.setVisibility(View.GONE);

        mWebView = new X5WebView(this, null);
        flWebview.addView(mWebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onBackPressed() {
        confirmQuit();
    }

    private void confirmQuit() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
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
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView != null) {
            mWebView.clearWebView();
        }
    }
}

