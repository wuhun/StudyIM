package studyim.cn.edu.cafa.studyim.activity.other;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    ViewGroup flWebview;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.vMask)
    View vMask;

    @BindView(R.id.rl_bottom_menu)
    FrameLayout rl_bottom_menu;
    @BindView(R.id.ll_to_sys_web)
    LinearLayout ll_to_sys_web;

    public static final String REQUEST_URI = "uri";
    public X5WebView mWebView;
    private Context mContext;

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
        }
    }

    private void initListener() {
        bodyLeft.setOnClickListener(mClick);
        bodySearch.setOnClickListener(mClick);
        ll_to_sys_web.setOnClickListener(mClick);
        vMask.setOnClickListener(mClick);
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
            public void setTitle(String title) {
                bodyTvTitle.setText(title);
            }
        });
    }

    private View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.body_img_menu:
                    WebViewActivity.this.finish();
                    break;
                case R.id.body_search:
                    // TODO: 2018/1/16 菜单
                    if (rl_bottom_menu.getVisibility() == View.GONE) {
                        rl_bottom_menu.setVisibility(View.VISIBLE);
                    } else {
                        rl_bottom_menu.setVisibility(View.GONE);
                    }
                    break;
                case R.id.ll_to_sys_web:
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(mWebView.getUrl()));
                    jumpToActivity(intent);
                    rl_bottom_menu.setVisibility(View.GONE);
                    break;
                case R.id.vMask:
                    rl_bottom_menu.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void initView() {
        mContext = this;
        headBg.setImageResource(R.drawable.main_bg);
        bodyLeft.setImageResource(R.drawable.icon_back);
        bodySearch.setImageResource(R.drawable.add);
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

