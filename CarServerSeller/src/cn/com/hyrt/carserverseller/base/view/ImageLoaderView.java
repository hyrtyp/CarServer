package cn.com.hyrt.carserverseller.base.view;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import cn.com.hyrt.carserverseller.R;

public class ImageLoaderView extends ImageView{
	
	private FinalBitmap mFinalBitmap;

	public ImageLoaderView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ImageLoaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ImageLoaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setImageUrl(String url){
//		if(url != null){
			if(mFinalBitmap == null){
				mFinalBitmap = FinalBitmap.create(getContext());
				mFinalBitmap.configLoadingImage(R.drawable.bg_img_default);
				mFinalBitmap.configLoadfailImage(R.drawable.bg_img_default);
			}else{
				mFinalBitmap.configLoadingImage(R.drawable.bg_img_default);
				mFinalBitmap.configLoadfailImage(R.drawable.bg_img_default);
			}
			if(url == null || "".equals(url)){
				url = "http://a";
			}
			mFinalBitmap.display(this, url);
//		}
	}
	
	public void setImageUrl(String url, int resId){
//		if(url != null){
			if(mFinalBitmap == null){
				mFinalBitmap = FinalBitmap.create(getContext());
				mFinalBitmap.configLoadingImage(resId);
				mFinalBitmap.configLoadfailImage(resId);
			}else{
				mFinalBitmap.configLoadingImage(resId);
				mFinalBitmap.configLoadfailImage(resId);
			}
			if(url == null || "".equals(url)){
				url = "http://a";
			}
			mFinalBitmap.display(this, url);
//		}
	}

}

