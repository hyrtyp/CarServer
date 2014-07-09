package cn.com.hyrt.carserver.base.view;

import uk.co.senab.photoview.PhotoView;
import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import cn.com.hyrt.carserver.R;

public class PhotoPopupView extends RelativeLayout{
	
	private static FinalBitmap mFinalBitmap;
	private View rootView;
	private PhotoView pvPhoto;
	private TouchCallback mCallback;
	
	
	public PhotoPopupView(Context context) {
		super(context);
		init();
	}
	
	
	public PhotoPopupView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PhotoPopupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		this.removeAllViews();
		rootView = LayoutInflater.from(getContext())
				.inflate(R.layout.layout_photopopup, null);
		pvPhoto = (PhotoView) rootView.findViewById(R.id.pv_photo);
		RelativeLayout.LayoutParams mParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		this.addView(rootView, mParams);
	}
	
	public void setImageUrl(String imageUrl){
		if(mFinalBitmap == null){
			mFinalBitmap = FinalBitmap.create(getContext());
			mFinalBitmap.configLoadingImage(R.drawable.ic_launcher);
			mFinalBitmap.configLoadfailImage(R.drawable.ic_launcher);
		}
		mFinalBitmap.display(pvPhoto, imageUrl);
	}
	
	private long downTime;
    private float downX;
    private float downY;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	if(ev.getAction() == MotionEvent.ACTION_DOWN){
    		downTime = System.currentTimeMillis();
    		downX = ev.getX();
    		downY = ev.getY();
//			LogHelper.i("tag", "按下");
		}else if(ev.getAction() == MotionEvent.ACTION_UP){
			long upTime = System.currentTimeMillis() - downTime;
			float upX = ev.getX();
			float upY = ev.getY();
			float offsetX = Math.abs(Math.abs(downX) - Math.abs(upX));
			float offsetY = Math.abs(Math.abs(downY) - Math.abs(upY));
//			LogHelper.i("tag", "弹起 upTime:"+upTime+" offsetX:"+offsetX+" offsetY:"+offsetY);
			if(upTime<150 && offsetX < 10 && offsetY < 10){
				if(mCallback != null){
					mCallback.onTouch();
				}
				return true;
			}
		}
    	return pvPhoto.dispatchTouchEvent(ev);
//    	return super.dispatchTouchEvent(ev);
    }
    
    public void setCallback(TouchCallback callback){
    	this.mCallback = callback;
    }
    
    public static interface TouchCallback{
    	public void onTouch();
    }
}
