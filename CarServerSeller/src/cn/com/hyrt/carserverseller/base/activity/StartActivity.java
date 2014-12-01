package cn.com.hyrt.carserverseller.base.activity;

import com.pipework.push.IIIService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
	public static final String SENDER_ID = "668240857682";//该常量不要修改 
    public static final long APP_ID = 1411261136391960007L;//必选 
	
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
		
		SharedPreferences pref=getSharedPreferences("3ispush", MODE_PRIVATE); 
        pref.edit().putString("sender_id", SENDER_ID).commit(); 
        pref.edit().putLong("app_id", APP_ID).commit();
        Intent iiis=new Intent(this, IIIService.class);
        startService(iiis); 
		
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
