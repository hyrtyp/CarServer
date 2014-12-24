package cn.com.hyrt.carserversurvey.base.helper;

import com.soundcloud.android.crop.Crop;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.com.hyrt.carserversurvey.R;

/**
 * 照片助手
 * @author zoe
 *
 */

public class PhotoHelper {
	
	private Uri uri;
	private Context context;
	public static final int FROM_CAMERA = 2;
	public static final int PHOTO_ZOOM = 3;
	private Dialog mDialog;
	private int cropWidth;
	private int cropHeight;

	 public PhotoHelper(Context context, Uri uri, int cropWidth, int cropHeight) {
		super();
		this.uri = uri;
		this.context = context;
		this.cropWidth = cropWidth;
		this.cropHeight = cropHeight;
	}
	 
	 public PhotoHelper(Context context, Uri uri, int cropSize) {
			super();
			this.uri = uri;
			this.context = context;
			this.cropWidth = cropSize;
			this.cropHeight = cropSize;
		}
	 
	 /**
	  * 获取照片,需要在activity中监听onActivityResult方法
	  */
	 public void getPhoto(){
		 mDialog = new Dialog(context, R.style.MyDialog);
		 mDialog.setContentView(R.layout.layout_photo_select);
		 mDialog.getWindow().setLayout(
				 RelativeLayout.LayoutParams.MATCH_PARENT,
				 RelativeLayout.LayoutParams.MATCH_PARENT);
		 
		  final View click_view = mDialog.findViewById(R.id.click_view);
		  final View layout_camera = mDialog.findViewById(R.id.layout_camera);
		  final View layout_album = mDialog.findViewById(R.id.layout_album);
		  final View layout_cancle = mDialog.findViewById(R.id.layout_cancle);
		  final View layout_select = mDialog.findViewById(R.id.layout_select);
		  
		  View.OnClickListener mClickListener = new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				int id = view.getId();
				if(id == layout_camera.getId()){
					getFromCamera();
					mDialog.dismiss();
				}else if(id == layout_album.getId()){
					getFromLocal(cropWidth, cropHeight);
					mDialog.dismiss();
				}else if(id == click_view.getId()){
					mDialog.dismiss();
				}else if(id == layout_cancle.getId()){
					mDialog.dismiss();
				}
			}
		};
		
		click_view.setOnClickListener(mClickListener);
		layout_camera.setOnClickListener(mClickListener);
		layout_album.setOnClickListener(mClickListener);
		layout_cancle.setOnClickListener(mClickListener);
		layout_select.setOnClickListener(mClickListener);
		 
		 mDialog.show();
	 }

	 /**
	     * 获取本地图片，并剪切,需要在activity中监听onActivityResult方法
	     */
	    public void getFromLocal(int cropSize){
	    	getFromLocal(cropSize, cropSize);
	    }
	  /**
	   * 获取本地图片，并剪切,需要在activity中监听onActivityResult方法
	   */
	  public void getFromLocal(int cropWidth, int cropHeight){
		  Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/*");
	        try {
	        	((Activity) context).startActivityForResult(intent, Crop.REQUEST_PICK);
	        } catch (ActivityNotFoundException e) {
	            Toast.makeText(context, "没有可用的照片", Toast.LENGTH_SHORT).show();
	        }
	  }
	    
	    
	/**
     * 获取本地图片，并剪切,需要在activity中监听onActivityResult方法
     */
    public void getFromLocal1(int cropWidth, int cropHeight){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        if(cropWidth != -1 && cropHeight != -1){
        	LogHelper.i("tag", "cropWidth:"+cropWidth+" cropHeight:"+cropHeight);
        	intent.putExtra("crop", "true");
        	intent.putExtra("aspectX", cropWidth);
            intent.putExtra("aspectY", cropHeight);
        	intent.putExtra("outputX", cropWidth);
            intent.putExtra("outputY", cropHeight);
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
     * 获取照相的图片 需要在activity中监听onActivityResult方法
     * @param uri 照片保存路径
     */
    public void getFromCamera(){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        ((Activity) context).startActivityForResult(intent, FROM_CAMERA);
    }
    
    /**
     * 图片剪切功能,需要在activity中监听onActivityResult方法
     * @param paramUri 图片所在资源路径
     */
    public void startPhotoZoom(Uri paramUri, int cropSize){
    	startPhotoZoom(paramUri, cropSize, cropSize);
    }
    
    /**
     * 图片剪切功能,需要在activity中监听onActivityResult方法
     * @param paramUri 图片所在资源路径
     */
    public void startPhotoZoom(Uri paramUri, int cropWidth, int cropHeight){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(paramUri, "image/*");
        if(cropWidth == -1 || cropHeight == -1){
        	intent.putExtra("crop","false");
        }else{
        	intent.putExtra("crop","true");
            intent.putExtra("aspectX", cropWidth);
            intent.putExtra("aspectY", cropHeight);
            intent.putExtra("outputX", cropWidth);
            intent.putExtra("outputY", cropHeight);
        }
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        ((Activity) context).startActivityForResult(intent,PHOTO_ZOOM);
    }
	
}
