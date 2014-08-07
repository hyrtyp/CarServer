package cn.com.hyrt.carserver.base.helper;

import cn.com.hyrt.carserver.R;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.Toast;

/**
 * 提示框助手
 * @author zoe
 *
 */
public class AlertHelper {
	
	public static AlertHelper mAlertHelper;
	private static Context mContext;
	MyProgressDialog mProgressDialog;
	
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
	
	/**
	 * 显示加载框
	 */
	public void showLoading(String msg){
		if(mContext == null){
			return;
		}
		if(msg == null){
			msg = mContext.getString(R.string.loading_msg);
		}
		if(mProgressDialog == null){
			mProgressDialog = new MyProgressDialog(mContext);
			mProgressDialog.setIndeterminateDrawable(
					mContext.getResources().getDrawable(R.drawable.bg_loading));
		}
		if(msg != null){
			mProgressDialog.setMessage(msg);
		}
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
	}
	
	public void hideLoading(){
		if(mProgressDialog != null 
				&& mContext != null 
				&& !((Activity)mContext).isFinishing()){
			mProgressDialog.hide();
			mProgressDialog = null;
		}
	}
	
	public void dismissLoading(){
		if(mProgressDialog != null 
				&& mContext != null 
				&& !((Activity)mContext).isFinishing()){
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}
	
	private class MyProgressDialog extends ProgressDialog{
		
		private Context mContext;

		public MyProgressDialog(Context context) {
			super(context);
			this.mContext = context;
		}

		public MyProgressDialog(Context context, int theme) {
			super(context, theme);
			this.mContext = context;
		}
		
		@Override
		public boolean dispatchKeyEvent(KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_DOWN
					&& event.getKeyCode() == KeyEvent.KEYCODE_BACK){
				if(mContext != null){
					((Activity)mContext).finish();
				}
				hideLoading();
			}
			return super.dispatchKeyEvent(event);
		}
		
	}
	
}
