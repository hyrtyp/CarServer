package cn.com.hyrt.carserver.base.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * 照片助手
 * @author zoe
 *
 */

public class PhotoHelper {
	
	private Context context;
	public static final int FROM_CAMERA = 2;
	public static final int PHOTO_ZOOM = 3;

	 public PhotoHelper(Context context) {
		super();
		this.context = context;
	}

	/**
     * 获取本地图片，并剪切
     */
    public void getFromLocal(int cropSize){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        if(cropSize != -1){
        	intent.putExtra("crop", "true");
        	intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        	intent.putExtra("outputX", cropSize);
            intent.putExtra("outputY", cropSize);
        }else{
        	intent.putExtra("crop", "false");
        }
        
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true); // no face detection
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        
        ((Activity) context).startActivityForResult(intent, PHOTO_ZOOM);
    }
    
    /**
     * 获取照相的图片 需要在活动中监听forresult方法
     * @param uri 照片保存路径
     */
    public void getFromCamera(Uri uri){
    	
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        ((Activity) context).startActivityForResult(intent, FROM_CAMERA);
    }
    
    /**
     * 图片剪切功能,需要在活动中监听forresult方法
     * @param paramUri 图片所在资源路径
     */
    public void startPhotoZoom(Uri paramUri, int cropSize){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(paramUri, "image/*");
        if(cropSize == -1){
        	intent.putExtra("crop","false");
        }else{
        	intent.putExtra("crop","true");
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
        }
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        ((Activity) context).startActivityForResult(intent,PHOTO_ZOOM);
    }
	
}
