package cn.com.hyrt.carserver.base.activity;

import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.info.activity.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StarActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		CarServerApplication.loginInfo = StorageHelper.getInstance(this).getLoginInfo();
		Intent intent = new Intent();
		if(CarServerApplication.loginInfo == null){
			intent.setClass(this, LoginActivity.class);
		}else{
			intent.setClass(this, MainActivity.class);
		}
		startActivity(intent);

		CarServerApplication.loginInfo = StorageHelper.getInstance(this).getLoginInfo();
		if(CarServerApplication.loginInfo == null){
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}else{
			loadData();
		}
		
		
		
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO>() {

					@Override
					public void onSuccess(INFO result) {
						jump();
						CarServerApplication.info = result;
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						// TODO Auto-generated method stub
						
					}
		}, this);
		mWebServiceHelper.getUserInfo();
	}
	
	private void jump(){
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
