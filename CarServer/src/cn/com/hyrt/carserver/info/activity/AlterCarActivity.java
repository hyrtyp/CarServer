package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.kobjects.base64.Base64;

import com.soundcloud.android.crop.Crop;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;

public class AlterCarActivity extends BaseActivity{
	@ViewInject(id=R.id.btn_add_photo,click="addPhoto") TextView btnAddPhoto;
	@ViewInject(id=R.id.et_carnumber) EditText etCarNumber;
//	@ViewInject(id=R.id.et_brand) EditText etBrand;
	@ViewInject(id=R.id.spinner_brand) Spinner spinnerBrand;
//	@ViewInject(id=R.id.et_model) EditText etModel;
	@ViewInject(id=R.id.spinner_model) Spinner spinnerModel;
//	@ViewInject(id=R.id.et_submodel) EditText etSubModel;
	@ViewInject(id=R.id.et_cartype) EditText etCarType;
	@ViewInject(id=R.id.et_mileage) EditText etMileage;
	@ViewInject(id=R.id.et_yearcheckdate) EditText etYearCheckDate;
	@ViewInject(id=R.id.et_manufacturer) EditText etManuFacturer;
	@ViewInject(id=R.id.et_insurancedate) EditText etInsuranceDate;
	@ViewInject(id=R.id.et_insurancetype) EditText etInsuranceType;
	@ViewInject(id=R.id.et_insurancenum) EditText etInsuranceNum;
	@ViewInject(id=R.id.et_insurancecompany) EditText etInsuranceCompany;
	@ViewInject(id=R.id.btn_confirm_add,click="addCar") Button btn_confirm_add;
	@ViewInject(id=R.id.iv_car_img) ImageLoaderView iv_car_img;
	
	private boolean isAdd = true;
	private String carId;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
//	private String imgBuffer;
	private Bitmap imgBitmap;
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
	
	private List<List<Map<String, String>>> twoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private List<List<List<Map<String, String>>>> threeList
	= new ArrayList<List<List<Map<String,String>>>>();
	private WebServiceHelper mCarModelServiceHelper;
	
	private Map<String, List<Map<String, String>>> modeMap 
	= new HashMap<String, List<Map<String,String>>>();
	private List<String> brandName = new ArrayList<String>();
	private List<String> brandId = new ArrayList<String>();
	private List<String> modelName = new ArrayList<String>();
	private List<String> modelId = new ArrayList<String>();
	private ArrayAdapter<String> modelAdapter;
	
	private String curBrand = "";
	private String curModel = "";
	
	private boolean firstSeleted = true;
	
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
			loadBrand();
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
//							etBrand.setText(result.brand);
//							etModel.setText(result.model);
//							etSubModel.setText(result.submodel);
							curBrand = result.brand;
							curModel = result.model;
							etCarType.setText(result.cartype);
							etMileage.setText(result.mileage);
							etYearCheckDate.setText(StringHelper.formatDate(result.yearcheckdate));
							etManuFacturer.setText(result.manufacturer);
							etInsuranceDate.setText(StringHelper.formatDate(result.insurancedate));
							etInsuranceType.setText(result.insurancetype);
							etInsuranceNum.setText(result.insurancenum);
							etInsuranceCompany.setText(result.insurancecompany);
							loadBrand();
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
	
	private void loadBrand(){
		if(mCarModelServiceHelper == null){
			mCarModelServiceHelper = new WebServiceHelper(
					new WebServiceHelper.OnSuccessListener() {
				
				@Override
				public void onSuccess(String result) {
					ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
					classifyJsonParser.parse(result);
					twoList.clear();
					threeList.clear();
					twoList.addAll(classifyJsonParser.getTwoList());
					threeList.addAll(classifyJsonParser.getThreeList());
					
					brandName.clear();
					brandName.add(getString(R.string.spinner_default_label));
					brandId.clear();
					brandId.add("");
					int brandIndex = 0;
					for(int i=0,j=twoList.size(); i<j; i++){
						List<Map<String, String>> cList = twoList.get(i);
						for(int a=0,b=cList.size(); a<b; a++){
							brandId.add(cList.get(a).get("id"));
							String name = cList.get(a).get("name");
							brandName.add(name);
							if(curBrand != null && curBrand.equals(name)){
								brandIndex = a+1;
							}
						}
					}
					
					
					ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
							AlterCarActivity.this,
							android.R.layout.simple_spinner_item, brandName);
					mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					spinnerBrand.setAdapter(mArrayAdapter);
					spinnerBrand.setSelection(brandIndex);
					spinnerBrand.setOnItemSelectedListener(mBrandItemSelectedListener);
					
					
					modeMap.clear();
					for(int i=0,j=threeList.size(); i<j; i++){
						List<List<Map<String, String>>> cList = threeList.get(i);
						for(int a=0,b=cList.size(); a<b; a++){
							List<Map<String, String>> ccList = cList.get(a);
							modeMap.put(twoList.get(i).get(a).get("id"), ccList);
						}
					}
					setModel(brandId.get(brandIndex), curModel);
				}
			}, this);
		}
		mCarModelServiceHelper.getBrands();
	}
	
	private AdapterView.OnItemSelectedListener mBrandItemSelectedListener 
	= new AdapterView.OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if(firstSeleted){
				firstSeleted = false;
				return;
			}
			setModel(brandId.get(position), null);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {}
	};
	
	private void setModel(String id, String model){
		LogHelper.i("tag", "modeMap:"+modeMap);
		List<Map<String, String>> mList = modeMap.get(id);
		modelId.clear();
		modelName.clear();
		modelId.add("");
		modelName.add(getString(R.string.spinner_default_label));
		
		int modelIndex = 0;
		LogHelper.i("tag", "model:"+model +" id:"+id+" mList:"+mList);
		if(mList != null && mList.size() > 0){
			for(int i=0,j=mList.size(); i<j; i++){
				modelId.add(mList.get(i).get("id"));
				String name = mList.get(i).get("name");
				modelName.add(name);
				if(model != null && model.equals(name)){
					modelIndex = i+1;
				}
			}
		}
		
		LogHelper.i("tag", "modelIndex:"+modelIndex);
		
		if(modelAdapter == null){
			modelAdapter = new ArrayAdapter<String>(AlterCarActivity.this, android.R.layout.simple_spinner_item, modelName);
			modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerModel.setAdapter(modelAdapter);
		}else{
			modelAdapter.notifyDataSetChanged();
		}
		spinnerModel.setSelection(modelIndex);
	}
	
	public void addPhoto(View view){
		if (FileHelper.sdCardExist()) {
			if(mPhotoHelper == null){
				if(faceUri == null){
					faceUri = Uri.fromFile(FileHelper.createFile1(imageName));
				}
				mPhotoHelper = new PhotoHelper(this, faceUri, 247, 103);
			}
			mPhotoHelper.getPhoto();
		}else{
			AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
		}
		/*if(mPhotoHelper == null){
			if(faceUri == null){
				faceUri = Uri.fromFile(FileHelper.createFile(imageName));
			}
			mPhotoHelper = new PhotoHelper(this, faceUri, 247, 103);
		}
		mPhotoHelper.getPhoto();*/
	}
	
	boolean isSave = true;
	public void addCar(View view){
		if (isSave) {
			isSave = false;
			Define.INFO_CAR car = new Define.INFO_CAR();
			if(!isAdd){
				car.carid = carId;
			}
			car.carnumber = etCarNumber.getText().toString();
//			car.brand = etBrand.getText().toString();
//			car.model = etModel.getText().toString();
//			car.submodel = etSubModel.getText().toString();
			if(spinnerBrand.getSelectedItemPosition() <= 0){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_brand_not_selected);
				return;
			}else if(spinnerModel.getSelectedItemPosition() <= 0){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_model_not_selected);
				return;
			}else{
				car.brand = brandName.get(spinnerBrand.getSelectedItemPosition());
				car.model = modelName.get(spinnerModel.getSelectedItemPosition());
			}
			
			car.cartype = etCarType.getText().toString();
			car.mileage = etMileage.getText().toString();
			car.yearcheckdate = yearcheckDate;
			car.manufacturer = etManuFacturer.getText().toString();
			car.insurancedate = insuranceData;
			car.insurancetype = etInsuranceType.getText().toString();
			car.insurancenum = etInsuranceNum.getText().toString();
			car.insurancecompany = etInsuranceCompany.getText().toString();
			car.checkdate = StringHelper.getNowTime();
//			car.imagepath = imgBuffer;
			car.imagename = imageName;
			
			if(mWebServiceHelper == null){
				mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.INFO_CAR>() {

					@Override
					public void onSuccess(Define.INFO_CAR result) {
						if(!isAdd){
							uploadImg(carId);
						}else{
							uploadImg(result.id);
						}

//						setResult(Define.RESULT_FROM_ALTER_CAR);
//						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						isSave = true;
						AlertHelper.getInstance(AlterCarActivity.this).showCenterToast("保存失败");
						LogHelper.i("tag", "errorMsg:"+errorMsg);
						
					}
				}, this);
			}
			mWebServiceHelper.alterCar(car);
		}else{
			AlertHelper.getInstance(getApplicationContext()).showCenterToast("不能重复提交");
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			return;
		}
		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
			beginCrop(data.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}else if (requestCode == PhotoHelper.FROM_CAMERA) {
			beginCrop(faceUri);
        }
	}
	
	private void beginCrop(Uri source) {
		if (FileHelper.sdCardExist()) {
			if(faceUri == null){
	            faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
	        }
	        new Crop(source).output(faceUri).asSquare().start(this);
		}else{
			AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
		}
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
        	Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
				imgBitmap = bitmap;
				iv_car_img.setImageBitmap(imgBitmap);
	            btnAddPhoto.setText("");
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
	/*@Override
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
            	imgBitmap = data.getParcelableExtra("data");
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                iv_car_img.setImageBitmap(imgBitmap);
                btnAddPhoto.setText("");
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));

            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
        	if (FileHelper.sdCardExist()) {
        		if(mPhotoHelper == null){
                    if(faceUri == null){
                        faceUri = Uri.fromFile(FileHelper.createFile1(imageName));
                    }
                    mPhotoHelper = new PhotoHelper(AlterCarActivity.this, faceUri, 247, 103);
                }
                mPhotoHelper.startPhotoZoom(faceUri, 247, 103);
        	}else{
        		AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
        	}
        }
	}*/
	
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
	
	private void uploadImg(String uid){
		if(imgBitmap == null){
			setResult(Define.RESULT_FROM_ALTER_CAR);
			finish();
			return;
		}
		WebServiceHelper mUploadImgWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						setResult(Define.RESULT_FROM_ALTER_CAR);
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						isSave = true;
						AlertHelper.getInstance(AlterCarActivity.this).showCenterToast("保存失败");
						LogHelper.i("tag", "errorMsg:"+errorMsg);
					}
		}, this);
		mUploadImgWebServiceHelper.saveImage(
				imgBitmap, "carPhoto.jpeg",
				WebServiceHelper.IMAGE_TYPE_CAR,
				uid);

	}
	
	@Override
	public void finish() {
		setResult(Define.RESULT_FROM_ALTER_CAR);
		super.finish();
	}
}
