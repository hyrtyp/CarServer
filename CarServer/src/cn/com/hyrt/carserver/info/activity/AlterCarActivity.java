package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Locale;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;

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
	@ViewInject(id=R.id.iv_car_img) ImageLoaderView iv_car_img;
	
	private boolean isAdd = true;
	private String carId;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private String imgBuffer;
	private String imageName = "carphoto.jpg";
	private Calendar mycalendar;
	private int year;
	private int month;
	private int day;
	private DatePickerDialog mDatePickerDialog;
	private boolean isInsurance;
	
	private String insuranceData;
	private String yearcheckDate;
	private WebServiceHelper mWebServiceHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		carId = intent.getStringExtra("id");
		if(carId == null || "".equals(carId)){
			isAdd = true;
		}else{
			isAdd = false;
		}
		
		setContentView(R.layout.activity_alter_car);
		setListener();
		
		if(isAdd){
			setTitle(getString(R.string.info_add_car));
			btn_confirm_add.setText(R.string.info_confirm_add);
		}else{
			setTitle(getString(R.string.info_change_car));
			btn_confirm_add.setText(R.string.info_save_change);
			AlertHelper.getInstance(AlterCarActivity.this).showLoading(null);
			loadData();
		}
		
	}
	
	private void loadData(){
		WebServiceHelper mCarInfoServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO_CAR>() {

					@Override
					public void onSuccess(INFO_CAR result) {
						AlertHelper.getInstance(AlterCarActivity.this).hideLoading();
						if(result == null){
							AlertHelper.getInstance(AlterCarActivity.this).showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_ALTER_CAR);
							finish();
						}else{
							if(result.imagepath != null && !"".equals(result.imagepath)){
								iv_car_img.setImageUrl(result.imagepath);
								btnAddPhoto.setText("");
							}
							etCarNumber.setText(result.carnumber);
							etBrand.setText(result.brand);
							etModel.setText(result.model);
							etSubModel.setText(result.submodel);
							etCarType.setText(result.cartype);
							etMileage.setText(result.mileage);
							etYearCheckDate.setText(StringHelper.formatDate(result.yearcheckdate));
							etManuFacturer.setText(result.manufacturer);
							etInsuranceDate.setText(StringHelper.formatDate(result.insurancedate));
							etInsuranceType.setText(result.insurancetype);
							etInsuranceNum.setText(result.insurancenum);
							etInsuranceCompany.setText(result.insurancecompany);
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(AlterCarActivity.this).hideLoading();
						AlertHelper.getInstance(AlterCarActivity.this).showCenterToast(R.string.info_load_fail);
						setResult(Define.RESULT_FROM_ALTER_CAR);
						finish();
					}
		}, this);
		mCarInfoServiceHelper.getCarInfo(carId);
	}
	
	public void addPhoto(View view){
		if(mPhotoHelper == null){
			if(faceUri == null){
				faceUri = Uri.fromFile(FileHelper.createFile(imageName));
			}
			mPhotoHelper = new PhotoHelper(this, faceUri, 247, 103);
		}
		mPhotoHelper.getPhoto();
	}
	
	public void addCar(View view){
		Define.INFO_CAR car = new Define.INFO_CAR();
		if(!isAdd){
			car.carid = carId;
		}
		car.carnumber = etCarNumber.getText().toString();
		car.brand = etBrand.getText().toString();
		car.model = etModel.getText().toString();
		car.submodel = etSubModel.getText().toString();
		car.cartype = etCarType.getText().toString();
		car.mileage = etMileage.getText().toString();
		car.yearcheckdate = yearcheckDate;
		car.manufacturer = etManuFacturer.getText().toString();
		car.insurancedate = insuranceData;
		car.insurancetype = etInsuranceType.getText().toString();
		car.insurancenum = etInsuranceNum.getText().toString();
		car.insurancecompany = etInsuranceCompany.getText().toString();
		car.checkdate = StringHelper.getNowTime();
		car.imagepath = imgBuffer;
		car.imagename = imageName;
		
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.BASE>() {

				@Override
				public void onSuccess(BASE result) {
					setResult(Define.RESULT_FROM_ALTER_CAR);
					finish();
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					AlertHelper.getInstance(AlterCarActivity.this).showCenterToast("保存失败");
					LogHelper.i("tag", "errorMsg:"+errorMsg);
					
				}
			}, this);
		}
		mWebServiceHelper.alterCar(car);
		
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
                iv_car_img.setImageBitmap(bitmap);
                btnAddPhoto.setText("");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imgBuffer = new String(Base64.encode(baos.toByteArray()));

            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile(imageName));
                }
                mPhotoHelper = new PhotoHelper(AlterCarActivity.this, faceUri, 247, 103);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 247, 103);
        }
	}
	
	private void setListener() {
         
		etInsuranceDate.setInputType(InputType.TYPE_NULL);
		etYearCheckDate.setInputType(InputType.TYPE_NULL);
		
		etInsuranceDate
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						LogHelper.i("tag", "arg1:"+arg1);
						if(arg1){
							showDatePickerDialog(true);
						}
					}
				});
		

		etYearCheckDate
				.setOnFocusChangeListener(new View.OnFocusChangeListener() {

					@Override
					public void onFocusChange(View arg0, boolean arg1) {
						if(arg1){
							if(arg1){
								showDatePickerDialog(false);
							}
						}
						
					}
				});
		
		etInsuranceDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(true);
			}
		});
		
		etYearCheckDate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(false);
			}
		});
	}
	
	private void showDatePickerDialog(boolean isInsurance){
		 this.isInsurance = isInsurance;
		 if(mDatePickerDialog == null){
			 mycalendar = Calendar.getInstance(Locale.CHINA);
			 year = mycalendar.get(Calendar.YEAR); //获取Calendar对象中的年
		     month = mycalendar.get(Calendar.MONTH);//获取Calendar对象中的月
		     day = mycalendar.get(Calendar.DAY_OF_MONTH);//获取这个月的第几天
		     
		     mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
					

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							String time = String.format("%s-%s-%s", year,
									monthOfYear + 1 >= 10 ? monthOfYear + 1 : "0" + (monthOfYear + 1),
									dayOfMonth >= 10 ? dayOfMonth : "0" + (dayOfMonth));
						if(AlterCarActivity.this.isInsurance){
							etInsuranceDate.setText(time);
							insuranceData = time+" 00:00:00.000";
						}else{
							etYearCheckDate.setText(time);
							yearcheckDate = time+" 00:00:00.000";
						}
						
					}
				}, year, month, day);
		 }
		 if(this.isInsurance){
			 mDatePickerDialog.setTitle(R.string.info_insurancedate_label);
		 }else{
			 mDatePickerDialog.setTitle(R.string.info_yearcheckdate_label);
		 }
		 
		 mDatePickerDialog.show();
		 
		
	}
	
	@Override
	public void finish() {
		setResult(Define.RESULT_FROM_ALTER_CAR);
		super.finish();
	}
}
