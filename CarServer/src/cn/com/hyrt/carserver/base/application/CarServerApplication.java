package cn.com.hyrt.carserver.base.application;

import android.app.Application;
import cn.com.hyrt.carserver.base.baseFunction.CrashHandler;
import cn.com.hyrt.carserver.base.baseFunction.Define;

public class CarServerApplication extends Application{
	
	public static Define.INFO_LOGIN loginInfo;
	public static Define.INFO info;
	public static Define.QUESTION_SAVE save;
	@Override
	public void onCreate() {
		super.onCreate();
		//全局捕获异常
		CrashHandler mCrashHandler = CrashHandler.getInstance();
		mCrashHandler.init(getApplicationContext());
	}
}
