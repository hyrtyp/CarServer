package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;

public class AlterCarActivity extends BaseActivity{
	@ViewInject(id=R.id.btn_add_photo,click="addPhoto") TextView btnAddPhoto;
	@ViewInject(id=R.id.et_carnumber) EditText etCarNumber;
	@ViewInject(id=R.id.et_brand) EditText etBrand;
	@ViewInject(id=R.id.et_model) EditText etModel;
	@ViewInject(id=R.id.et_submodel) EditText etSubModel;
	@ViewInject(id=R.id.et_cartype) EditText etCarType;
	@ViewInject(id=R.id.et_mileage) EditText etMileage;
	@ViewInject(id=R.id.et_yearcheckdate) EditText etYearCheckDate;
	@ViewInject(id=R.id.et_manufacturer) EditText etManuFacturer;
	@ViewInject(id=R.id.et_insurancedate) EditText etInsuranceDate;
	@ViewInject(id=R.id.et_insurancetype) EditText etInsuranceType;
	@ViewInject(id=R.id.et_insurancenum) EditText etInsuranceNum;
	@ViewInject(id=R.id.et_insurancecompany) EditText etInsuranceCompany;
	@ViewInject(id=R.id.btn_confirm_add,click="addCar") TextView btn_confirm_add;
	
	private boolean isAdd = true;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		intent.getBooleanExtra("type", true);
		if(isAdd){
			setTitle(getString(R.string.info_add_car));
		}else{
			setTitle(getString(R.string.info_change_car));
		}
		setContentView(R.layout.activity_alter_car);
	}
	
	public void addPhoto(View view){
		if(mPhotoHelper == null){
			if(faceUri == null){
				faceUri = Uri.fromFile(FileHelper.createFile("carphoto.jpg"));
			}
			mPhotoHelper = new PhotoHelper(this, faceUri, 247, 103);
		}
		mPhotoHelper.getPhoto();
	}
	
	public void addCar(View view){
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogHelper.i("tag", "requestCode:"+requestCode+" data:"+data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                btnAddPhoto.setBackgroundDrawable(new BitmapDrawable(bitmap));
                btnAddPhoto.setText("");
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                String uploadBuffer = new String(Base64.encode(baos.toByteArray()));

            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(AlterCarActivity.this, faceUri, 247, 103);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 247, 103);
        }
	}
	
	@Override
	public void finish() {
		setResult(Define.RESULT_FROM_ALTER_CAR);
		super.finish();
	}
}
