package cn.com.hyrt.carserver.base.application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import cn.com.hyrt.carserver.base.baseFunction.CrashHandler;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import android.app.Application;

public class CarServerApplication extends Application{
	
	public static Define.INFO_LOGIN loginInfo;
	public static Define.INFO info;
	public static Define.QUESTION_SAVE save;
	
	@Override
	public void onCreate() {
		super.onCreate();
		//全局捕获异常
//		CrashHandler mCrashHandler = CrashHandler.getInstance();
//		mCrashHandler.init(getApplicationContext());
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        .threadPriority(Thread.NORM_PRIORITY - 2)
        .denyCacheImageMultipleSizesInMemory()
        .discCacheFileNameGenerator(new Md5FileNameGenerator())
        .tasksProcessingOrder(QueueProcessingType.LIFO)
        .build();
		ImageLoader.getInstance().init(config);
	}
}
