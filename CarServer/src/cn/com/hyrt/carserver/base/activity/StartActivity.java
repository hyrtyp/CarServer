package cn.com.hyrt.carserver.base.activity;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.helper.LocationHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.info.activity.LoginActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class StartActivity extends Activity{
	
	private static final int JUMP = 0;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JUMP:
				Intent intent = new Intent();
				CarServerApplication.loginInfo = StorageHelper
						.getInstance(StartActivity.this).getLoginInfo();
				if(CarServerApplication.loginInfo == null){
					intent.setClass(StartActivity.this, LoginActivity.class);
					startActivity(intent);
					finish();
				}else{
					loadData();
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		
//		LocationHelper mLocationHelper = new LocationHelper(this);
//		mLocationHelper.setLocationCallback(new LocationHelper.LocationCallback() {
//			
//			@Override
//			public void onLocation(double lon, double lat, String city) {
//				StorageHelper.getInstance(StartActivity.this)
//				.saveLocation(lon+"", lat+"", city);
//			}
//		});
//		mLocationHelper.start();
		
		Message msg = new Message();
		msg.what = JUMP;
		mHandler.sendMessageDelayed(msg, 500);
		
		
		
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO>() {

					@Override
					public void onSuccess(INFO result) {
						LogHelper.i("tag", "result:"+result);
						CarServerApplication.info = result;
						jump();
						
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
