package cn.com.hyrt.carserverseller.info.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.soundcloud.android.crop.Crop;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.activity.MainActivity;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

/**
 * 申请认证
 * @author zoe
 *
 */
public class VerificationActivity extends BaseActivity{
	
	@ViewInject(id=R.id.iv_photo1) ImageLoaderView ivPhoto1;
	@ViewInject(id=R.id.iv_photo2) ImageLoaderView ivPhoto2;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	@ViewInject(id=R.id.sp_zztype) Spinner spZzType;
	@ViewInject(id=R.id.sp_zzlevel) Spinner spZzLevel;
	
	private Bitmap merchantBitmap;
	private Bitmap licenseBitmap;
	private boolean isMerchantSelect = true;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private boolean photoUploadDone;
	
	private List<Map<String, String>> oneList 
	= new ArrayList<Map<String,String>>();
	private List<List<Map<String, String>>> twoList 
	= new ArrayList<List<Map<String,String>>>();
	private List<String> oneIds = new ArrayList<String>();
	private List<String> oneNames = new ArrayList<String>();
	private List<String> twoIds = new ArrayList<String>();
	private List<String> twoNames = new ArrayList<String>();
	private List<String> twoLevel = new ArrayList<String>();
	private ArrayAdapter<String> oneAdapter;
	private ArrayAdapter<String> twoAdapter;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification);
		loadData();
		setListener();
	}
	
	private void loadData(){
		new WebServiceHelper(
				new BaseWebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				ClassifyJsonParser jsonParser = new ClassifyJsonParser();
				jsonParser.parse(result);
				oneList.clear();
				oneList.addAll(jsonParser.getOneList());
				twoList.clear();
				twoList.addAll(jsonParser.getTwoList());
				oneIds.clear();
				oneNames.clear();
				twoIds.clear();
				twoNames.clear();
				twoLevel.clear();
				
				if(oneList.size() <= 0 || twoList.size() <= 0){
					AlertHelper.getInstance(VerificationActivity.this).showCenterToast("没有可申请的资质!");
					finish();
					return;
				}
				
				for(int i=0,j=oneList.size(); i<j; i++){
					Map<String, String> oneMap = oneList.get(i);
					oneIds.add(oneMap.get("zztype"));
					oneNames.add(oneMap.get("zztypename"));
				}
				for(int i=0,j=twoList.get(0).size(); i<j; i++){
					Map<String, String> twoMap = twoList.get(0).get(i);
					twoIds.add(twoMap.get("id"));
					twoNames.add(twoMap.get("zzlevelname"));
					twoLevel.add(twoMap.get("typelevel"));
				}
				
				if(oneAdapter == null){
					oneAdapter = new ArrayAdapter<String>(
							VerificationActivity.this,
							R.layout.layout_spinner_item,
							oneNames);
					spZzType.setAdapter(oneAdapter);
				}else{
					oneAdapter.notifyDataSetChanged();
				}
				
				if(twoAdapter == null){
					twoAdapter = new ArrayAdapter<String>(
							VerificationActivity.this,
							R.layout.layout_spinner_item,
							twoNames);
					spZzLevel.setAdapter(twoAdapter);
				}else{
					twoAdapter.notifyDataSetChanged();
				}
			}
		}, this).getVerificationType();
	}
	
	private void setListener(){
		ivPhoto1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (merchantBitmap == null) {
					isMerchantSelect = true;
					addPhoto();
				} else {
					PhotoPopupHelper.showPop(merchantBitmap,
							VerificationActivity.this);
				}
			}
		});
		ivPhoto1.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (merchantBitmap == null) {
					isMerchantSelect = true;
					addPhoto();
				} else {
					delPhoto(true);
				}
				return true;
			}
		});

		ivPhoto2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (licenseBitmap == null) {
					isMerchantSelect = false;
					addPhoto();
				} else {
					PhotoPopupHelper.showPop(licenseBitmap,
							VerificationActivity.this);
				}
			}
		});

		ivPhoto2.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (licenseBitmap == null) {
					isMerchantSelect = false;
					addPhoto();
				} else {
					delPhoto(false);
				}
				return true;
			}
		});
		
		spZzType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				twoIds.clear();
				twoNames.clear();
				twoLevel.clear();
				List<Map<String, String>> tempList = twoList.get(position);
				for(int i=0,j=tempList.size(); i<j; i++){
					Map<String, String> twoMap = tempList.get(i);
					twoIds.add(twoMap.get("id"));
					twoNames.add(twoMap.get("zzlevelname"));
					twoLevel.add(twoMap.get("typelevel"));
				}
				twoAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void addPhoto(){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(VerificationActivity.this, faceUri, 100);
		mPhotoHelper.getPhoto();
	}
	
	private void delPhoto(final boolean isMerchant){
		AlertDialog.Builder mDelPhotoDialog = new Builder(VerificationActivity.this);
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(isMerchant){
					merchantBitmap = null;
					ivPhoto1.setImageResource(R.drawable.bg_verification_photo_add);
				}else{
					licenseBitmap = null;
					ivPhoto2.setImageResource(R.drawable.bg_verification_photo_add);
				}
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			return;
		}
		
        /*if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                if(isMerchantSelect){
                	merchantBitmap = bitmap;
                	ivPhoto1.setImageBitmap(bitmap);
                }else{
                	licenseBitmap = bitmap;
                	ivPhoto2.setImageBitmap(bitmap);
                }
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(VerificationActivity.this, faceUri, 100);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 100);
        }*/
		
		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
			beginCrop(data.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}else if (requestCode == PhotoHelper.FROM_CAMERA) {
			beginCrop(faceUri);
        }
	}
	
	private void beginCrop(Uri source) {
		if(faceUri == null){
            faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
        }
        new Crop(source).output(faceUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
        	Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
				if(isMerchantSelect){
					merchantBitmap = bitmap;
					ivPhoto1.setImageBitmap(bitmap);
				}else{
					licenseBitmap = bitmap;
					ivPhoto2.setImageBitmap(bitmap);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else if (resultCode == Crop.RESULT_ERROR) {
            AlertHelper.getInstance(this).showCenterToast(Crop.getError(result).getMessage());
        }
    }
	
	public void submit(View view){
		if(merchantBitmap == null && licenseBitmap == null){
			AlertHelper.getInstance(VerificationActivity.this).showCenterToast("至少上传一张图片");
			return;
		}else{
			AlertHelper.getInstance(this).showLoading(null);
			new WebServiceHelper(
					new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

						@Override
						public void onSuccess(BASE result) {
							if(merchantBitmap != null){
								uploadImage(merchantBitmap, "photo1.jpeg", false);
							}
							if(licenseBitmap != null){
								uploadImage(licenseBitmap, "photo2.jpeg", true);
							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							// TODO Auto-generated method stub
							
						}
			}, this).saveVerification(
					oneIds.get(spZzType.getSelectedItemPosition()),
					twoLevel.get(spZzLevel.getSelectedItemPosition()),
					twoIds.get(spZzLevel.getSelectedItemPosition()));
		}
	}
	
	public void uploadImage(Bitmap bitmap, String imageName, boolean isZZ){
		WebServiceHelper mUploadImage = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {
			
			@Override
			public void onSuccess(BASE result2) {
				if(licenseBitmap == null || merchantBitmap == null){
					photoUploadDone = false;
					AlertHelper.getInstance(VerificationActivity.this).showCenterToast("提交成功");
					AlertHelper.getInstance(VerificationActivity.this).hideLoading();
					finish();
				}else if(photoUploadDone){
					photoUploadDone = false;
					AlertHelper.getInstance(VerificationActivity.this).showCenterToast("提交成功");
					AlertHelper.getInstance(VerificationActivity.this).hideLoading();
					finish();
				}else{
					photoUploadDone = true;
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				photoUploadDone = false;
				AlertHelper.getInstance(VerificationActivity.this).showCenterToast("提交失败");
				AlertHelper.getInstance(VerificationActivity.this).hideLoading();
			}
		}, VerificationActivity.this);
		AlertHelper.getInstance(VerificationActivity.this).showLoading("提交中...");
		mUploadImage.saveImage(
				bitmap, imageName, "sjrz", ((CarServerApplication)getApplication()).getLoginInfo().serviceid);
	}
}
