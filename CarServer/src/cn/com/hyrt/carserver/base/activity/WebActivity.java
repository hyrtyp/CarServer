package cn.com.hyrt.carserver.base.activity;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * @author zoe
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebActivity extends BaseActivity{

	private WebView mWebView;
	private String mUrl = "";
	private boolean loadError = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		AlertHelper.getInstance(this).showLoading(null);
		initWebView();
		mWebView.setVisibility(View.INVISIBLE);
		Intent intent = getIntent();
		mUrl = intent.getStringExtra("url");
		if(mUrl != null && mUrl.trim().length() > 0){
			mWebView.loadUrl(mUrl);
		}
		showBackButton(true);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
			loadError = false;
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void initWebView(){
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(!loadError){
					setTitle(mWebView.getTitle());
					mWebView.setVisibility(View.VISIBLE);
				}else{
					setTitle("网络异常");
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				loadError = true;
				mWebView.setVisibility(View.INVISIBLE);
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				mWebView.setVisibility(View.VISIBLE);
				AlertHelper.getInstance(WebActivity.this).hideLoading();
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
				setTitle(title);
			}
		});
		
	}
}
