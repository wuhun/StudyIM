package tools.com.lvliangliang.wuhuntools.net;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
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


public class X5WebView extends WebView {

	private Context context;

	@SuppressLint("SetJavaScriptEnabled")
	public X5WebView(Context arg0, AttributeSet arg1) {
		super(arg0, arg1);
		this.context = arg0;
//		this.setWebViewClient(client);
		// this.setWebChromeClient(chromeClient);
		// WebStorage webStorage = WebStorage.getInstance();
		this.getView().setClickable(true);
		initWebView();
	}

	private WebViewClient client = new WebViewClient() {
		/**
		 * 防止加载网页时调起系统浏览器
		 */
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	};

	private void initWebView() {
		this.setWebViewClient(mWebViewClient);
		this.setWebChromeClient(mWebChromeClient);
		this.setDownloadListener(mDownloadListener);


		WebSettings webSetting = this.getSettings();
		webSetting.setAllowFileAccess(true);
		webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webSetting.setSupportZoom(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setSupportMultipleWindows(false);//新窗口
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);//自动打开窗口
		// webSetting.setLoadWithOverviewMode(true);//充满全屏
		// webSetting.setDatabaseEnabled(true);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);// 优先使用缓存
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);// 设置缓存的大小

		webSetting.setDomStorageEnabled(true);
		webSetting.setJavaScriptEnabled(true);
		webSetting.setGeolocationEnabled(true);

//		webSetting.setAppCachePath(getDir("appcache", MODE_PRIVATE).getPath());// 设置 Application Caches 缓存目录
//		webSetting.setAppCacheEnabled(true);// 开启 Application Caches 功能
//		webSetting.setDatabasePath(this.getDir("databases", MODE_PRIVATE).getPath());//数据库缓存路径
//		webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
		// webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		// webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
		// webSetting.setPreFectch(true);
	}


	private WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String s) {
			return false;//拦截
		}

		@Override
		public void onPageFinished(WebView webView, String s) {
			super.onPageFinished(webView, s);
			//结束
		}
	};

	private WebChromeClient mWebChromeClient = new WebChromeClient() {
		@Override
		public boolean onJsConfirm(WebView webView, String s, String s1, JsResult jsResult) {
			// 处理javascript中的confirm
			return super.onJsConfirm(webView, s, s1, jsResult);
		}

		@Override
		public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
			// 处理javascript中的alert
			return super.onJsAlert(webView, s, s1, jsResult);
		}

		/////////////////////////////////////【视屏全屏方法】/////////////////////////////////////
		View myVideoView;
		View myNormalView;
		IX5WebChromeClient.CustomViewCallback callback;

		@Override
		public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback customViewCallback) {
			//视频播放全屏调用
			View videoView = LayoutInflater.from(context).inflate(R.layout.webview_video_play, null);
			FrameLayout normalView = videoView.findViewById(R.id.web_video);
			ViewGroup viewGroup = (ViewGroup) normalView.getParent();
			viewGroup.removeView(normalView);
			viewGroup.addView(view);

			myVideoView = view;
			myNormalView = normalView;
			callback = customViewCallback;
		}

		@Override
		public void onHideCustomView() {
			//视频播放
			if (callback != null) {
				callback.onCustomViewHidden();
				callback = null;
			}
			if (myVideoView != null) {
				ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
				if (viewGroup!=null) {
					viewGroup.removeView(myVideoView);
					viewGroup.addView(myNormalView);
				}
			}
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
	public void clearWebView(){
		if (this != null) {
			CookieSyncManager.createInstance(context);
			CookieSyncManager.getInstance().sync();
			this.clearCache(true);
			this.clearHistory();
			this.clearFormData();
			this.destroy();
		}
	}
}
