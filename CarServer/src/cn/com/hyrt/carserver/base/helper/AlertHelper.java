package cn.com.hyrt.carserver.base.helper;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * 提示框助手
 * @author zoe
 *
 */
public class AlertHelper {
	
	public static AlertHelper mAlertHelper;
	private static Context mContext;
	
	private AlertHelper(){}
	
	public static AlertHelper getInstance(Context context){
		mContext = context;
		if(mAlertHelper == null){
			mAlertHelper = new AlertHelper();
		}
		return mAlertHelper;
	}
	
	/**
	 * 屏幕中间显示Toast
	 * @param context
	 * @param msg
	 */
	public void showCenterToast(String msg){
        if(mContext == null){
            return;
        }
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
	
	/**
	 * 屏幕中间显示Toast
	 * @param context
	 * @param msgId (字符串资源ID)
	 */
	public void showCenterToast(int msgId){
        if(mContext == null){
            return;
        }
        Toast toast = Toast.makeText(
        		mContext,
        		mContext.getString(msgId),
        		Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
	
}
