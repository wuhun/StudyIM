package tools.com.lvliangliang.wuhuntools.net;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import tools.com.lvliangliang.wuhuntools.R;
import tools.com.lvliangliang.wuhuntools.exception.TestLog;


public class X5WebView extends WebView {


    private Context context;

    @SuppressLint("SetJavaScriptEnabled")
    public X5WebView(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
        this.context = arg0;
        // WebStorage webStorage = WebStorage.getInstance();
        initClient();
        initWebView();
        this.getView().setClickable(true);
    }

    private void initClient() {
        this.setWebViewClient(mWebViewClient);
        this.setWebChromeClient(mWebChromeClient);
        this.setDownloadListener(mDownloadListener);
    }

    private void initWebView() {
        WebSettings webSetting = this.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//自动打开窗口
        webSetting.setAllowFileAccess(true);
//        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//自适应屏幕
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);//适应内容大小
        webSetting.setSupportZoom(true);//支持缩放
        webSetting.setBuiltInZoomControls(true);//出现缩放工具
        webSetting.setUseWideViewPort(true);//推荐使用的窗口
        webSetting.setSupportMultipleWindows(true);//新窗口
        // webSetting.setLoadWithOverviewMode(true);//充满全屏(webview加载的页面的模式)
        webSetting.setAppCacheEnabled(true);// 开启 Application Caches 功能
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);// 设置缓存的大小
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);// 优先使用缓存
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        /** 防止加载网页时调起系统浏览器 */
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);//防止加载网页时调起系统浏览器
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            TestLog.i("webview退出时执行");
//				if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 16)
//					changGoForwardButton(view);
        }
    };


    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
            Log.i("wuhun","confirm对话框");
            return super.onJsConfirm(arg0, arg1, arg2, arg3);
        }

        @Override
        public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
            Log.i("wuhun","alert对话框");
            return super.onJsAlert(null, arg1, arg2, arg3);
        }

        /////////////////////////////////////【视屏全屏方法】/////////////////////////////////////
        View myVideoView;
        View myNormalView;
        IX5WebChromeClient.CustomViewCallback callback;

        /** 全屏播放配置 */
        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
            Log.i("wuhun","全屏播放");
            FrameLayout normalView = (FrameLayout) findViewById(R.id.frame_web_video);
            ViewGroup viewGroup = (ViewGroup) normalView.getParent();
            viewGroup.removeView(normalView);
            viewGroup.addView(view);
            myVideoView = view;
            myNormalView = normalView;
            callback = customViewCallback;
        }

        @Override
        public void onHideCustomView() {
            Log.i("wuhun","关闭全屏播放");
            if (callback != null) {
                callback.onCustomViewHidden();
                callback = null;
            }
            if (myVideoView != null) {
                ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                viewGroup.removeView(myVideoView);
                viewGroup.addView(myNormalView);
            }
        }

        /////////////////////////////////////【加载对话框】/////////////////////////////////////
        @Override
        public void onProgressChanged(WebView webView, int i) {
            if (i < 100) {
                // 加载中
                loding.logining(i);
            } else {
                // 加载完成
                loding.loginOver();
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
//            bodyTvTitle.setText(title);
            loding.setTitle(title);
        }
    };


    private DownloadListener mDownloadListener = new DownloadListener() {
        @Override
        public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            //s:下载地址
            new AlertDialog.Builder(context)
                    .setTitle("allow to download？")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO: 2017/11/15 下载任务
                                    Log.i("wuhun", "下载任务：" + url);
                                }
                            })
                    .setNegativeButton("取消", null)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            // TODO 点击窗口以外地方执行的方法
                            Toast.makeText(context, "fake message: refuse download...",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).show();
        }
    };

    /////////////////////////////////////【】/////////////////////////////////////
    public void clearWebView() {
        if (this != null) {
            CookieSyncManager.createInstance(context);
            CookieSyncManager.getInstance().sync();
            this.clearCache(true);
            this.clearHistory();
            this.clearFormData();
            this.destroy();
        }
    }

    private WebLoding loding;


    public void setOnloding(WebLoding loding) {
        this.loding = loding;
    }

    public interface WebLoding {
        void logining(int i);

        void loginOver();

        void setTitle(String title);
    }

}
