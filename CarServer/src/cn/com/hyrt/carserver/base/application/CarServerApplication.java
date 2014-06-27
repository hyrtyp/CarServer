package cn.com.hyrt.carserver.base.application;

import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import android.app.Application;

public class CarServerApplication extends Application{
	
	public static Define.INFO_LOGIN loginInfo;

	@Override
	public void onCreate() {
		super.onCreate();
	}
}
