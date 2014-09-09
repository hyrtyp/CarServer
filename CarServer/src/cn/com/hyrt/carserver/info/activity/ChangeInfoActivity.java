package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_SAVE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.question.activity.QuestionActivity;

public class ChangeInfoActivity extends BaseActivity{
	
	private static final String TAG = "ChangeInfoActivity";
	@ViewInject(id=R.id.et_username) EditText etUserName;
	@ViewInject(id=R.id.et_old_pwd) EditText etOldPwd;
	@ViewInject(id=R.id.et_new_pwd) EditText etNewPwd;
	@ViewInject(id=R.id.et_confirm_pwd) EditText etConfirmPwd;
	@ViewInject(id=R.id.btn_change_face,click="changeFace") TextView btnChangeFace;
	@ViewInject(id=R.id.iv_face_img) ImageLoaderView ivFaceImg;
	@ViewInject(id=R.id.lv_car) ListView lvCar;
	@ViewInject(id=R.id.btn_add_car,click="addCar") ImageView btnAddCar;
	@ViewInject(id=R.id.btn_save,click="saveUserInfo") Button btnSave;
	private List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
//	private String imgBuffer;
	private Bitmap imgBitmap;
	private WebServiceHelper mWebServiceHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_info);
		
		LogHelper.i("tag", "info:"+CarServerApplication.info);
		if(CarServerApplication.loginInfo == null){
			CarServerApplication.loginInfo = StorageHelper.getInstance(this).getLoginInfo();
		}
		etUserName.setText(CarServerApplication.loginInfo.loginname);
		ivFaceImg.setImageUrl(CarServerApplication.info.imagepath);
		AlertHelper.getInstance(this).showLoading(null);
		loadData();
		setListener();
	}
	
	private String beforeText;

	private void setListener() {
		lvCar.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(ChangeInfoActivity.this, AlterCarActivity.class);
				intent.putExtra("id", (String) data.get(position).get("id"));
				startActivityForResult(intent, Define.RESULT_FROM_ALTER_CAR);
			}
		});
		
		etUserName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etUserName.getText().toString();
				if (content.length() > 25) {
					AlertHelper.getInstance(ChangeInfoActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etUserName.setText(beforeText);
						etUserName.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});

		etOldPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etOldPwd.getText().toString();
				if (content.length() > 32) {
					AlertHelper.getInstance(ChangeInfoActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etOldPwd.setText(beforeText);
						etOldPwd.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});

		etNewPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etNewPwd.getText().toString();
				if (content.length() > 32) {
					AlertHelper.getInstance(ChangeInfoActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etNewPwd.setText(beforeText);
						etNewPwd.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});

		etConfirmPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etConfirmPwd.getText().toString();
				if (content.length() > 32) {
					AlertHelper.getInstance(ChangeInfoActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etConfirmPwd.setText(beforeText);
						etConfirmPwd.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});
	}
	
	private void loadData() {
		
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INFO_CAR_LIST>() {

						@Override
						public void onSuccess(INFO_CAR_LIST result) {
							AlertHelper.getInstance(ChangeInfoActivity.this).hideLoading();
							data.clear();
							for(int i=0,j=result.data.size(); i<j; i++){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("model", result.data.get(i).model);
								map.put("id", result.data.get(i).id);
								data.add(map);
							}
							String[] from = new String[]{"model"};
							int[] to = new int[]{R.id.tv_model};
							SimpleAdapter mAdapter = new SimpleAdapter(
									ChangeInfoActivity.this,
									data, R.layout.layout_car_item,
									from, to);
							lvCar.setAdapter(mAdapter);
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(ChangeInfoActivity.this).hideLoading();
						}
					}, this);
		}
		mWebServiceHelper.getTerminalCarList();
	}
	
	public void addCar(View view){
		Intent intent = new Intent();
		intent.setClass(this, AlterCarActivity.class);
		startActivityForResult(intent, Define.RESULT_FROM_ALTER_CAR);
	}
	
	public void changeFace(View view){
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
            	imgBitmap = data.getParcelableExtra("data");
                ivFaceImg.setImageBitmap(imgBitmap);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(ChangeInfoActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }else if(resultCode == Define.RESULT_FROM_ALTER_CAR){
        	AlertHelper.getInstance(this).showLoading(null);
        	loadData();
        }
	}
	
	public void saveUserInfo(View view){
		if(CarServerApplication.loginInfo == null){
			CarServerApplication.loginInfo = StorageHelper.getInstance(this).getLoginInfo();
		}
		INFO_SAVE info  = new INFO_SAVE();
		info.id = CarServerApplication.loginInfo.id;
		
		String oldPwd = etOldPwd.getText().toString();
		String newPwd = etNewPwd.getText().toString();
		String confirmPwd = etConfirmPwd.getText().toString();
		String userName = etUserName.getText().toString();
		
		if(!"".equals(oldPwd) || !"".equals(newPwd) || !"".equals(confirmPwd)){
			if("".equals(oldPwd)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_oldpwd_is_null);
				return;
			}else if("".equals(newPwd)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_newpwd_is_null);
				return;
			}else if("".equals(confirmPwd)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_confirmpwd_is_null);
				return;
			}else{
				if(!newPwd.equals(confirmPwd)){
					AlertHelper.getInstance(this).showCenterToast(R.string.info_confirmpwd_not_tally);
					return;
				}else{
					info.newpassword = newPwd;
					info.password = oldPwd;
				}
			}
		}
		
		if("".equals(userName)){
			AlertHelper.getInstance(this).showCenterToast(R.string.info_username_is_null);
			return;
		}else{
			info.unitname = userName;
		}
		
//		if(imgBuffer != null && !"".equals(imgBuffer)){
//			info.image = imgBuffer;
//			info.imagename = "face.jpg";
//		}
		
		
		WebServiceHelper mWebServiceHelper = 
				new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						LogHelper.i("tag", "result:"+result.message);
						uploadImg();
						
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						LogHelper.i("tag", "onFailure:"+errorMsg);
						AlertHelper.getInstance(ChangeInfoActivity.this).showCenterToast(errorMsg);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						finish();
					}
		}, this);
		mWebServiceHelper.saveUserInfo(info);
		
	}
	
	private void uploadImg(){
		if(imgBitmap == null){
			AlertHelper.getInstance(ChangeInfoActivity.this).showCenterToast(R.string.info_change_success);
			setResult(Define.RESULT_FROM_CHANGE_INFO);
			finish();
			return;
		}
		WebServiceHelper mUploadImgWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						AlertHelper.getInstance(ChangeInfoActivity.this).showCenterToast(R.string.info_change_success);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						LogHelper.i("tag", "onFailure:"+errorMsg);
						AlertHelper.getInstance(ChangeInfoActivity.this).showCenterToast(errorMsg);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						finish();
					}
		}, this);
		mUploadImgWebServiceHelper.saveImage(
				imgBitmap, "face.jpeg",
				WebServiceHelper.IMAGE_TYPE_USER,
				CarServerApplication.loginInfo.id);

	}

}
