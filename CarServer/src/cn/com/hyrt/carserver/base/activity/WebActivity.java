package cn.com.hyrt.carserver.base.activity;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.emergency.fragment.EmergencyFragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
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
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if(mWebView.canGoBack()){
				loadError = false;
				mWebView.goBack();
				return true;
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void initWebView(){
		mWebView = (WebView) findViewById(R.id.webview);
		WebSettings webseting = mWebView.getSettings();
		webseting.setJavaScriptEnabled(true);
//		webseting.setDomStorageEnabled(true);             
//        webseting.setAppCacheMaxSize(1024*1024*8);
//        String appCacheDir = this.getApplicationContext()
//        		.getDir("cache", Context.MODE_PRIVATE).getPath();
//        webseting.setAppCachePath(appCacheDir);  
//        webseting.setAllowFileAccess(true);  
//        webseting.setAppCacheEnabled(true);  
//        webseting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
//				if(!loadError){
					setTitle(mWebView.getTitle());
					mWebView.setVisibility(View.VISIBLE);
//				}else{
//					findViewById(R.id.errortext).setVisibility(View.VISIBLE);
//					mWebView.setVisibility(View.GONE);
//					setTitle("网络异常");
//				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
				loadError = true;
				mWebView.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				LogHelper.i("tag", "url:"+url);
				if(url.startsWith("tel:")){
					Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse(url));  
					startActivity(intent);
					return true;
				}/*else if(url.contains("211.98.71.195:8080")){
					findViewById(R.id.errortext).setVisibility(View.VISIBLE);
					mWebView.setVisibility(View.GONE);
					return true;
				}*/
				return super.shouldOverrideUrlLoading(view, url);
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
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AlertHelper.getInstance(this).dismissLoading();
	}
}
