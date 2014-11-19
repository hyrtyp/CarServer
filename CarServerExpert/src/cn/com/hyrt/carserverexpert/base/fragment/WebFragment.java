package cn.com.hyrt.carserverexpert.base.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.WebActivity;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;

public class WebFragment extends Fragment implements View.OnKeyListener{

	private View rootView;
	private WebView mWebView;
	private String mUrl = "";
	private boolean loadError = false;
	private boolean inited = false;
	
	private static final int MSG_SHOWWEB = 101; 
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOWWEB:
				mWebView.setVisibility(View.VISIBLE);
				AlertHelper.getInstance(getActivity()).hideLoading();
				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_web, null);
		AlertHelper.getInstance(getActivity()).showLoading(null);
		initWebView();
		mWebView.setVisibility(View.INVISIBLE);
		if(mUrl != null && mUrl.trim().length() > 0){
			AlertHelper.getInstance(getActivity()).showLoading(null);
			mWebView.setVisibility(View.GONE);
			mWebView.loadUrl(mUrl);
		}
		inited = true;
		rootView.setOnKeyListener(this);
		return rootView;
	}
	
	public void setUrl(String url){
		if(url != null && url.trim().length() > 0){
			mUrl = url;
			if(inited){
				AlertHelper.getInstance(getActivity()).showLoading(null);
				mWebView.setVisibility(View.GONE);
				mWebView.loadUrl(mUrl);
			}
		}
	}

	@Override
	public boolean onKey(View view, int keyCode, KeyEvent ev) {
		if(keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()){
			loadError = false;
			mWebView.goBack();
			return true;
		}
		return false;
	}

	private void initWebView(){
		mWebView = (WebView) rootView.findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				if(!loadError){
					mWebView.setVisibility(View.VISIBLE);
				}else{
					rootView.findViewById(R.id.errortext).setVisibility(View.VISIBLE);
					mWebView.setVisibility(View.GONE);
				}
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
				Intent intent = null;
				if(url.startsWith("tel:")){
					intent = new Intent(Intent.ACTION_CALL,Uri.parse(url));  
				}else if(url.contains("211.98.71.195:8080")){
					rootView.findViewById(R.id.errortext).setVisibility(View.VISIBLE);
					mWebView.setVisibility(View.GONE);
					return true;
				}else{
					intent = new Intent();
					intent.setClass(getActivity(), WebActivity.class);
					intent.putExtra("url", url);
				}
				startActivity(intent);
				return true;
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress >= 100){
					Message msg = new Message();
					msg.what = MSG_SHOWWEB;
					mHandler.sendMessageDelayed(msg, 100);
				}
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);
			}
			
			
		});
		
	}
	
	
}
