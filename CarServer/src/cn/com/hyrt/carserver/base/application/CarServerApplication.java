package cn.com.hyrt.carserver.base.application;

import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import android.app.Application;

public class CarServerApplication extends Application{
	
	public static Define.INFO_LOGIN loginInfo;
	public static Define.INFO info;
	public static Define.QUESTION_SAVE save;
	@Override
	public void onCreate() {
		super.onCreate();
	}
}
