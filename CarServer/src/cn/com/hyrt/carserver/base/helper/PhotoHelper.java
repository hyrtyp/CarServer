package cn.com.hyrt.carserver.base.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;


public class PhotoHelper {
	
	private Context context;
	
	public static final int PHOTO_ZOOM = 3;

	 public PhotoHelper(Context context) {
		super();
		this.context = context;
	}

	/**
     * 获取本地图片，并剪切
     */
    public void getFromLocal(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 50);
        intent.putExtra("outputY", 50);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        ((Activity) context).startActivityForResult(intent, PHOTO_ZOOM);
    }
	
}
