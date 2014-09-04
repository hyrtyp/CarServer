package cn.com.hyrt.carserversurvey.info.activity;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.SAVE_INFO;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;

public class InfoDetailActivity extends BaseActivity{
	
	@ViewInject(id=R.id.btn_change_photo,click="changePhoto") Button btnChangeFace;
	@ViewInject(id=R.id.iv_user_img) ImageLoaderView iv_user_img;
	@ViewInject(id=R.id.tv_usercode) TextView tv_usercode;
	@ViewInject(id=R.id.tv_regrecode) TextView tv_regrecode;
	@ViewInject(id=R.id.tv_curlogin) TextView tv_curlogin;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private String imgBuffer;
	private WebServiceHelper mUserInfoWebServiceHelper;
	private WebServiceHelper mSaveUserInfoWebServiceHelper;
	private Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infodetail);
		loadData();
	}
	
	public void changePhoto(View view){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(this, faceUri, 50);
		mPhotoHelper.getPhoto();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                bitmap = data.getParcelableExtra("data");
                iv_user_img.setImageBitmap(bitmap);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
                savePhoto();
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(InfoDetailActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }else if(resultCode == Define.RESULT_FROM_ALTER_CAR){
        	AlertHelper.getInstance(this).showLoading(null);
        	loadData();
        }
	}
	
	private void loadData(){
		LogHelper.i("tag", "调我一次");
		final String id = ((CarServerApplication)getApplicationContext()).getLoginInfo().id;
		final String username = ((CarServerApplication)getApplicationContext()).getLoginInfo().loginname;
		if(mUserInfoWebServiceHelper == null){
			mUserInfoWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.SAVE_INFO>() {

						@Override
						public void onSuccess(SAVE_INFO result) {
							iv_user_img.setImageUrl(result.imagepath);
							tv_usercode.setText(String.format(getString(R.string.usercode), username));
//							tv_regrecode.setText(String.format(getString(R.string.regrecode), result.mercount+"条"));
							tv_regrecode.setText(Html.fromHtml("注册记录：<font color=\"#299fff\">"+result.mercount+"条</font>"));
							tv_curlogin.setText(String.format(getString(R.string.curlogin),result.lasttime));
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_CHANGE_INFO);
							finish();
							
						}
			}, this);
		}
		mUserInfoWebServiceHelper.getUserInfoImage(id);
	}
	
	private void savePhoto(){
//		SAVE_INFO info = new SAVE_INFO();
//		if(imgBuffer != null && !"".equals(imgBuffer)){
//			info.image = imgBuffer;
//			info.imagename = "face.jpg";
//		}
//		info.id = ((CarServerApplication)getApplicationContext()).getLoginInfo().id;
//		
//		if(mSaveUserInfoWebServiceHelper == null){
//			mSaveUserInfoWebServiceHelper = new WebServiceHelper(
//					new WebServiceHelper.RequestCallback<Define.SAVE_INFO>() {
//
//						@Override
//						public void onFailure(int errorNo, String errorMsg) {
//							AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.change_fail);
////							setResult(Define.RESULT_FROM_CHANGE_INFO);
////							finish();
//							
//						}
//
//						@Override
//						public void onSuccess(SAVE_INFO result) {
////							LogHelper.i("tag", "result:"+result.message);
//							AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.infophoto_change_success);
////							setResult(Define.RESULT_FROM_CHANGE_INFO);
//							//finish();
//						}
//			}, this);
//		}
//		mSaveUserInfoWebServiceHelper.saveUserInfo(info);
		
		WebServiceHelper mUploadImageWebService = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

			@Override
			public void onSuccess(BASE result) {
				LogHelper.i("tag", "result:"+result.message);
				AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.infophoto_change_success);
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.change_fail);
			}
		}, this);
		mUploadImageWebService.saveImage(bitmap, "face.jpeg", WebServiceHelper.IMAGE_TYPE_USER, ((CarServerApplication)getApplication()).getLoginInfo().id);
	}
	
}
