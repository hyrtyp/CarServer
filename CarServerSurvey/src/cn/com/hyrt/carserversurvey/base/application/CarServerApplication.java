package cn.com.hyrt.carserversurvey.base.application;

import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.helper.StorageHelper;
import android.app.Application;

public class CarServerApplication extends Application{
	
	public static Define.INFO_LOGIN mLoginInfo;
	
	public Define.INFO_LOGIN getLoginInfo() {
		if(mLoginInfo == null){
			mLoginInfo = StorageHelper.getInstance(getApplicationContext()).getLoginInfo();
		}
		return mLoginInfo;
	}

	public void setLoginInfo(Define.INFO_LOGIN mLoginInfo) {
		this.mLoginInfo = mLoginInfo;
		StorageHelper.getInstance(getApplicationContext()).saveLoginInfo(mLoginInfo);
	}



	@Override
	public void onCreate() {
		super.onCreate();
		//全局捕获异常
//		CrashHandler mCrashHandler = CrashHandler.getInstance();
//		mCrashHandler.init(getApplicationContext());
	}
}
