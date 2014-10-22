package cn.com.hyrt.carserverseller.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserverseller.info.activity.LoginActivity;
import cn.com.hyrt.carserverseller.info.activity.MerchantInfoActivity;

public class StartActivity extends Activity{
	
	
	private static final int JUMP = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JUMP:
				Intent intent = new Intent();
				INFO_LOGIN loginInfo = ((CarServerApplication)getApplicationContext()).getLoginInfo();
				if(loginInfo == null || loginInfo.bcxx == null || "0".equals(loginInfo.bcxx)){
					intent.setClass(StartActivity.this, LoginActivity.class);
				}else{
					intent.setClass(StartActivity.this, MainActivity.class);
				}
//				intent.setClass(StartActivity.this, MerchantInfoActivity.class);
				startActivity(intent);
				finish();
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
		
		Message msg = new Message();
		msg.what = JUMP;
		mHandler.sendMessageDelayed(msg, 500);
	}
	
	
	private void jump(){
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
