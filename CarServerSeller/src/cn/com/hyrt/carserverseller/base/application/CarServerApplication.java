package cn.com.hyrt.carserverseller.base.application;

import android.app.Application;

public class CarServerApplication extends Application{
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		//全局捕获异常
//		CrashHandler mCrashHandler = CrashHandler.getInstance();
//		mCrashHandler.init(getApplicationContext());
	}
}
