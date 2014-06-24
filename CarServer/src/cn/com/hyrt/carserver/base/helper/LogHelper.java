package cn.com.hyrt.carserver.base.helper;

import android.util.Log;

/**
 * 日志输出助手
 * @author zoe
 *
 */
public class LogHelper {
	
	private static final boolean LogEnabled = true;
	
	public static void i(String tag, String msg){
		if(LogEnabled){
			Log.i(tag, msg);
		}
	}
	
	public static void i(String tag, String msg, Throwable tr){
		if(LogEnabled){
			Log.i(tag, msg, tr);
		}
		
	}
	
	public static void d(String tag, String msg){
		if(LogEnabled){
			Log.d(tag, msg);
		}
	}
	
	public static void d(String tag, String msg, Throwable tr){
		if(LogEnabled){
			Log.d(tag, msg, tr);
		}
		
	}
	
	public static void e(String tag, String msg){
		if(LogEnabled){
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag, String msg, Throwable tr){
		if(LogEnabled){
			Log.e(tag, msg, tr);
		}
		
	}
	
	public static void v(String tag, String msg){
		if(LogEnabled){
			Log.v(tag, msg);
		}
	}
	
	public static void v(String tag, String msg, Throwable tr){
		if(LogEnabled){
			Log.v(tag, msg, tr);
		}
		
	}
	
	public static void w(String tag, String msg){
		if(LogEnabled){
			Log.w(tag, msg);
		}
	}
	
	public static void w(String tag, String msg, Throwable tr){
		if(LogEnabled){
			Log.w(tag, msg, tr);
		}
	}

}
