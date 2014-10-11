package cn.com.hyrt.carserverseller.base.application;

import android.app.Application;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.helper.StorageHelper;

public class CarServerApplication extends Application{
	
	public static Define.INFO_LOGIN mLoginInfo;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//全局捕获异常
//		CrashHandler mCrashHandler = CrashHandler.getInstance();
//		mCrashHandler.init(getApplicationContext());
	}
	
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
}
