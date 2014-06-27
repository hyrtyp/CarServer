package cn.com.hyrt.carserver.base.activity;

import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
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
		startActivity(intent);;
		finish();
	}
}
