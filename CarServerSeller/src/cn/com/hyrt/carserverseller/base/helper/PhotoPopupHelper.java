package cn.com.hyrt.carserverseller.base.helper;

import android.app.Dialog;
import android.content.Context;
import android.widget.RelativeLayout;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.view.PhotoPopupView;

public class PhotoPopupHelper {
	
	private static PhotoPopupView mPopupView;
	private static Dialog mDialog;

	public static void showPop(String imageUrl, Context mContext){
		if(mPopupView == null){
			mPopupView = new PhotoPopupView(mContext);
			mPopupView.setCallback(new PhotoPopupView.TouchCallback() {
				
				@Override
				public void onTouch() {
					mDialog.dismiss();
					mDialog = null;
					mPopupView = null;
				}
			});
		}
		
		mPopupView.setImageUrl(imageUrl);
		if(mDialog == null){
			mDialog = new Dialog(mContext, R.style.MyDialog);
			
		}
		 mDialog.setContentView(mPopupView);
		 mDialog.getWindow().setLayout(
				 RelativeLayout.LayoutParams.MATCH_PARENT,
				 RelativeLayout.LayoutParams.MATCH_PARENT);
		 mDialog.show();
	}
	
	public static void hidePop(){
		if(mDialog != null){
			mDialog.dismiss();
			mDialog = null;
		}
	}

}
