package cn.com.hyrt.carserverexpert.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.application.CarServerApplication;
import cn.com.hyrt.carserverexpert.info.activity.LoginActivity;

public class StartActivity extends Activity{
	
//	public static final String SENDER_ID = "668240857682";//该常量不要修改 
//    public static final long APP_ID = 1408040301055760000L;//必选 
	
	private static final int JUMP = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case JUMP:
				Intent intent = new Intent();
				if(((CarServerApplication)getApplicationContext()).getLoginInfo() == null){
					intent.setClass(StartActivity.this, LoginActivity.class);
				}else{
					intent.setClass(StartActivity.this, MainActivity.class);
				}
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
