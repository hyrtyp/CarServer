package cn.com.hyrt.carserverseller.preferential.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;

public class PreferentialDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.webview) WebView webview;
	@ViewInject(id=R.id.btn_add,click="add") Button btnAdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preferential_detail);
		Intent intent = getIntent();
		boolean isAdd = intent.getBooleanExtra("isAdd", false);
		if(isAdd){
			btnAdd.setVisibility(View.VISIBLE);
		}else{
			btnAdd.setVisibility(View.GONE);
		}
		WebSettings webseting = webview.getSettings();
		webseting.setJavaScriptEnabled(true);
		webview.loadUrl("http://www.baidu.com");
	}
	
	public void add(View view){
		finish();
	}
}
