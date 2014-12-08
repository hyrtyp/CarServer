package cn.com.hyrt.carserverexpert.info.activity;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.activity.MainActivity;
import cn.com.hyrt.carserverexpert.base.application.CarServerApplication;
import cn.com.hyrt.carserverexpert.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_USER;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.SAVE_INFO_RESULT;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.FileHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.PhotoHelper;
import cn.com.hyrt.carserverexpert.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.ImageLoaderView;
import cn.com.hyrt.carserverexpert.info.adapter.InfoSpecialtyAdapter;

import com.soundcloud.android.crop.Crop;

public class InfoDetailActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_name) EditText etName;
//	@ViewInject(id=R.id.et_address) EditText etAddress;
	@ViewInject(id=R.id.et_unit) EditText etUnit;
	@ViewInject(id=R.id.iv_face_photo) ImageLoaderView ivFacePhoto;
	@ViewInject(id=R.id.iv_license_photo) ImageLoaderView ivLicensePhoto;
	@ViewInject(id=R.id.tv_face_photo) TextView tvFacePhoto;
	@ViewInject(id=R.id.tv_license_photo) TextView tvLicensePhoto;
	@ViewInject(id=R.id.sp_specialty1) Spinner spSpecialty1;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	@ViewInject(id=R.id.gv_specialty) GridView gvSpecialty;
	@ViewInject(id=R.id.layout_specialty1) LinearLayout layoutSpecialty1;
	@ViewInject(id=R.id.layout_specialty2) LinearLayout layoutSpecialty2;
	@ViewInject(id=R.id.btn_reselect,click="reselect") Button btnReselect;
	@ViewInject(id=R.id.sp_province) Spinner spProvince;
	@ViewInject(id=R.id.sp_city) Spinner spCity;
	@ViewInject(id=R.id.sp_county) Spinner spCounty;
	@ViewInject(id=R.id.tv_specialty) TextView tvSpecialty;
	@ViewInject(id=R.id.et_sjname) EditText etSjName;
	@ViewInject(id=R.id.cb_select_all) CheckBox cbSelectAll;
	
	private Bitmap faceBitmap;
	private String faceImgUrl;
	private Bitmap licenseBitmap;
	private String licenseImgUrl;
	private boolean isFaceSelect;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
	private List<Map<String, String>> oneList = new ArrayList<Map<String,String>>();
	private List<List<Map<String, String>>> twoList = new ArrayList<List<Map<String,String>>>();
	
	private List<String> oneNames = new ArrayList<String>();
	private List<String> twoIds = new ArrayList<String>();
	private List<String> twoNames = new ArrayList<String>();
	private ArrayAdapter<String> mSpecialtyAdapter1;
	private InfoSpecialtyAdapter mSpecialtyAdapter2;
	
	private List<Map<String, String>> addressOneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> addressTwoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private List<List<List<Map<String, String>>>> addressThreeList
	= new ArrayList<List<List<Map<String,String>>>>();
	
	private List<String> provinceId = new ArrayList<String>();
	private List<String> provinceName = new ArrayList<String>();
	private List<String> cityId = new ArrayList<String>();
	private List<String> cityName = new ArrayList<String>();
	private List<String> countyId = new ArrayList<String>();
	private List<String> countyName = new ArrayList<String>();
	private ArrayAdapter<String> mProvinceArrayAdapter;
	private ArrayAdapter<String> mCityArrayAdapter;
	private ArrayAdapter<String> mCountyArrayAdapter;
	private int curProvinceIndex = 0;
	private int curCityIndex = -1;
	private int curCountyIndex = -1;
	private boolean photoUploadDone = false;
	private int zcstatus = 0;
	private boolean needSelectAll = true;
	
	private Define.INFO_USER mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infodetail);
		loadData();
		setListener();
	}
	
	private void loadData(){
		
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.INFO_USER>() {

			@Override
			public void onSuccess(INFO_USER result) {
				AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
				mData = result;
				getCodingArea();
				etName.setText(result.name);
				etSjName.setText(result.sjname);
				etUnit.setText(result.unitname);
				faceImgUrl = result.imagepath;
				licenseImgUrl = result.zsimagepath0;
				if(faceImgUrl != null && !"".equals(faceImgUrl)){
					ivFacePhoto.setImageUrl(faceImgUrl);
					tvFacePhoto.setVisibility(View.GONE);
				}
				if(licenseImgUrl != null && !"".equals(licenseImgUrl)){
					ivLicensePhoto.setImageUrl(licenseImgUrl);
					tvLicensePhoto.setVisibility(View.GONE);
				}
				tvSpecialty.setText(result.zcnames);
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast("获取用户信息失败");
				finish();
			}
		}, this).getUserInfo();
		
		
		
		new WebServiceHelper(new BaseWebServiceHelper.OnSuccessListener() {

			@Override
			public void onSuccess(String result) {
				AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
				ClassifyJsonParser mParser = new ClassifyJsonParser();
				mParser.parse(result);
				oneList.clear();
				twoList.clear();
				oneList.addAll(mParser.getOneList());
				twoList.addAll(mParser.getTwoList());
				
				oneNames.clear();
				twoIds.clear();
				twoNames.clear();
				for(int i=0,j=oneList.size(); i<j; i++){
					oneNames.add(oneList.get(i).get("name"));
				}
				for(int i=0,j=twoList.get(0).size(); i<j; i++){
					Map<String, String> twoMap = twoList.get(0).get(i);
					twoIds.add(twoMap.get("id"));
					twoNames.add(twoMap.get("name"));
				}
				
				if(mSpecialtyAdapter1 == null){
					mSpecialtyAdapter1 = new ArrayAdapter<String>(
							InfoDetailActivity.this,
							R.layout.layout_spinner_item,
							oneNames);
					spSpecialty1.setAdapter(mSpecialtyAdapter1);
				}else{
					mSpecialtyAdapter1.notifyDataSetChanged();
				}
				
				if(mSpecialtyAdapter2 == null){
					mSpecialtyAdapter2 = new InfoSpecialtyAdapter(
							twoNames, InfoDetailActivity.this);
					gvSpecialty.setAdapter(mSpecialtyAdapter2);
				}else{
					mSpecialtyAdapter2.notifyDataSetChanged();
				}
				
			}
		}, this).getSpecialtyType();
	}
	
	private void getCodingArea(){
		provinceId.clear();
		provinceName.clear();
		provinceId.add("-1");
		provinceName.add("省");
		
		cityId.clear();
		cityName.clear();
		cityId.add("-1");
		cityName.add("市");
		
		countyId.clear();
		countyName.clear();
		countyId.add("-1");
		countyName.add("县");
		
		WebServiceHelper mAddressWebServiceHelper = new WebServiceHelper(new BaseWebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
				ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
				classifyJsonParser.parse(result);
				
				addressOneList.clear();
				addressTwoList.clear();
				addressThreeList.clear();
				addressOneList.addAll(classifyJsonParser.getOneList());
				addressTwoList.addAll(classifyJsonParser.getTwoList());
				addressThreeList.addAll(classifyJsonParser.getThreeList());
				LogHelper.i("tag", "addressOneList:"+addressOneList);
				LogHelper.i("tag", "addressTwoList:"+addressTwoList);
				LogHelper.i("tag", "addressThreeList:"+addressThreeList);
				
//				provinceId.clear();
//				provinceName.clear();
				for(int i=0,j=addressOneList.size(); i<j; i++){
					Map<String, String> oneData = addressOneList.get(i);
					provinceId.add(oneData.get("id"));
					provinceName.add(oneData.get("name"));
				}
				
				if(mProvinceArrayAdapter == null){
					mProvinceArrayAdapter = new ArrayAdapter<String>(
							InfoDetailActivity.this,
							R.layout.layout_spinner_item,
							provinceName);
				}else{
					mProvinceArrayAdapter.notifyDataSetChanged();
				}
				
				if(mCityArrayAdapter == null){
					mCityArrayAdapter = new ArrayAdapter<String>(
							InfoDetailActivity.this,
							R.layout.layout_spinner_item,
							cityName);
				}
				
				if(mCountyArrayAdapter == null){
					mCountyArrayAdapter = new ArrayAdapter<String>(
							InfoDetailActivity.this,
							R.layout.layout_spinner_item,
							countyName);
				}
				
//				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
//						RegistInfoDetailActivity.this,
//						R.layout.layout_spinner_item_blue,
//						getResources().getStringArray(R.array.spinnername));
				spProvince.setAdapter(mProvinceArrayAdapter);
				spCity.setAdapter(mCityArrayAdapter);
				spCounty.setAdapter(mCountyArrayAdapter);
				if(mData != null){
					changeCountySelected();
				}
			}
		}, InfoDetailActivity.this);
		mAddressWebServiceHelper.getCodingArea();
	}
	
	public void submit(View view){
		Define.SAVE_INFO saveInfo = new Define.SAVE_INFO();
		saveInfo.id = mData.id;
		saveInfo.name = etName.getText().toString();
		saveInfo.merchantid = etSjName.getText().toString();
		saveInfo.loginname = mData.loginname;
		saveInfo.zcstatus = zcstatus+"";
		List<String> checkedPosition = mSpecialtyAdapter2.getCheckedPosition();
		String checkedIds = "";
		for(int i=0,j=checkedPosition.size(); i<j; i++){
			checkedIds += twoIds.get(Integer.parseInt(checkedPosition.get(i)))+";";
		}
		if(!"".equals(checkedIds)){
			checkedIds = checkedIds.substring(0, checkedIds.length()-1);
			saveInfo.professional = checkedIds;
		}else{
			saveInfo.professional = mData.zcids;
		}
		saveInfo.unitname = etUnit.getText().toString();
		if(spCounty.getSelectedItemPosition() > 0){
			saveInfo.areaid = countyId.get(spCounty.getSelectedItemPosition());
		}
		
		AlertHelper.getInstance(InfoDetailActivity.this).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.SAVE_INFO_RESULT>() {

			@Override
			public void onSuccess(SAVE_INFO_RESULT result) {
				if(faceBitmap == null && licenseBitmap == null){
					AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
					AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast("保存成功");
					if(zcstatus == 1){
						Define.INFO_LOGIN loginInfo = ((CarServerApplication)getApplication()).getLoginInfo();
						loginInfo.status = "wsh";
						((CarServerApplication)getApplication()).setLoginInfo(loginInfo);
					}
					finish();
				}else{
					if(faceBitmap != null){
						uploadImage(faceBitmap, "facePhoto.jpeg", false);
					}
					if(licenseBitmap != null){
						uploadImage(licenseBitmap, "licensePhoto.jpeg", true);
					}
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
				AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast("保存失败");
			}
		}, this).saveUserInfo(saveInfo);
	}
	
	private void setListener(){
		ivFacePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (faceBitmap == null && faceImgUrl == null) {
					isFaceSelect = true;
					addPhoto();
				} else {
					if (faceBitmap != null) {
						PhotoPopupHelper.showPop(faceBitmap,
								InfoDetailActivity.this);
					} else {
						PhotoPopupHelper.showPop(faceImgUrl,
								InfoDetailActivity.this);
					}
				}
			}
		});
		ivFacePhoto.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (faceBitmap == null && faceImgUrl == null) {
					isFaceSelect = true;
					addPhoto();
				} else {
					delPhoto(true);
				}
				return true;
			}
		});

		ivLicensePhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (licenseBitmap == null && licenseImgUrl == null) {
					isFaceSelect = false;
					addPhoto();
				} else {
					if (licenseBitmap != null) {
						PhotoPopupHelper.showPop(licenseBitmap,
								InfoDetailActivity.this);
					} else {
						PhotoPopupHelper.showPop(licenseImgUrl,
								InfoDetailActivity.this);
					}
				}
			}
		});

		ivLicensePhoto.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (licenseBitmap == null && licenseImgUrl == null) {
					isFaceSelect = false;
					addPhoto();
				} else {
					delPhoto(false);
				}
				return true;
			}
		});
		
		spSpecialty1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				twoIds.clear();
				twoNames.clear();
				for(int i=0,j=twoList.get(position).size(); i<j; i++){
					Map<String, String> twoMap = twoList.get(position).get(i);
					twoIds.add(twoMap.get("id"));
					twoNames.add(twoMap.get("name"));
				}
				mSpecialtyAdapter2.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				curProvinceIndex = position-1;
				LogHelper.i("tag", "curProvinceIndex:"+curProvinceIndex);
				
				cityId.clear();
				cityName.clear();
				cityId.add("-1");
				cityName.add("市");
				
				countyId.clear();
				countyName.clear();
				countyId.add("-1");
				countyName.add("县");
				mCountyArrayAdapter.notifyDataSetChanged();
				mCityArrayAdapter.notifyDataSetChanged();
				
				if(position <= 0){
					return;
				}
				
				List<Map<String, String>> mCitys = addressTwoList.get(position-1);
				
				spCity.setSelection(0);
				
				for(int i=0,j=mCitys.size(); i<j; i++){
					Map<String, String> mCityData = mCitys.get(i);
					cityId.add(mCityData.get("id"));
					cityName.add(mCityData.get("name"));
				}
				mCityArrayAdapter.notifyDataSetChanged();
				if(curCityIndex >= 0){
					spCity.setSelection(curCityIndex);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				curCityIndex = -1;
				countyId.clear();
				countyName.clear();
				countyId.add("-1");
				countyName.add("县");
				
				mCountyArrayAdapter.notifyDataSetChanged();
				
				if(position <= 0 || curProvinceIndex < 0){
					return;
				}
				List<Map<String, String>> mCountys = addressThreeList.get(curProvinceIndex).get(position-1);
				
				
				LogHelper.i("tag", "mCountys:"+mCountys.size());
				for(int i=0,j=mCountys.size(); i<j; i++){
					Map<String, String> mCountyData = mCountys.get(i);
					countyId.add(mCountyData.get("id"));
					countyName.add(mCountyData.get("name"));
				}
				
				mCountyArrayAdapter.notifyDataSetChanged();
				if(curCountyIndex >= 0){
					spCounty.setSelection(curCountyIndex);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				curCountyIndex = -1;
				if(position <= 0){
					return;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(mSpecialtyAdapter2 != null && needSelectAll){
					mSpecialtyAdapter2.selectAll(arg1);
				}
				needSelectAll = true;
			}
		});
	}
	
	public void selectAll(boolean checked){
		needSelectAll = false;
		cbSelectAll.setChecked(checked);
		
	}
	
	private void delPhoto(final boolean isMerchant){
		AlertDialog.Builder mDelPhotoDialog = new Builder(InfoDetailActivity.this);
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(isMerchant){
					tvFacePhoto.setVisibility(View.VISIBLE);
					faceBitmap = null;
					faceImgUrl = null;
					ivFacePhoto.setImageResource(R.drawable.ic_photo_add);
				}else{
					tvLicensePhoto.setVisibility(View.VISIBLE);
					licenseBitmap = null;
					licenseImgUrl = null;
					ivLicensePhoto.setImageResource(R.drawable.ic_photo_add);
				}
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}
	
	private void addPhoto(){
		if(faceUri == null){
			if (FileHelper.sdCardExist()) {
				faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
			}else{
				AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
			}
			//faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(InfoDetailActivity.this, faceUri, 50);
		mPhotoHelper.getPhoto();
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
		if(faceUri == null){
			if (FileHelper.sdCardExist()) {
				faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
			}else{
				AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
			}
            //faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
        }
        new Crop(source).output(faceUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
        	Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
				if(isFaceSelect){
					tvFacePhoto.setVisibility(View.GONE);
					faceImgUrl = null;
					faceBitmap = bitmap;
					ivFacePhoto.setImageBitmap(bitmap);
				}else{
					tvLicensePhoto.setVisibility(View.GONE);
					licenseBitmap = bitmap;
					licenseImgUrl = null;
					ivLicensePhoto.setImageBitmap(bitmap);
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
    
    private void changeCountySelected(){
		ok:
		if(mData.areaid != null && !"".equals(mData.areaid)){
			for(int i=0,j=addressThreeList.size(); i<j; i++){
				List<List<Map<String, String>>> list1 = addressThreeList.get(i);
				for(int a=0,b=list1.size(); a<b; a++){
					List<Map<String, String>> list2 = list1.get(a);
					for(int x=0,y=list2.size(); x<y; x++){
						Map<String, String> map1 = list2.get(x);
						LogHelper.i("tag", "id:"+map1.get("id"));
						if(mData.areaid.equals(map1.get("id"))){
							LogHelper.i("tag", "i:"+i+" a:"+a+" x:"+x);
							spProvince.setSelection(i+1);
							curCityIndex = a+1;
							curCountyIndex = x+1;
							break ok;
						}
					}
				}
			}
		}
		
	}
    
    public void reselect(View view){
    	zcstatus = 1;
    	layoutSpecialty1.setVisibility(View.GONE);
    	layoutSpecialty2.setVisibility(View.VISIBLE);
    }
    
    public void uploadImage(Bitmap bitmap, String imageName, boolean isZJ){
		WebServiceHelper mUploadImage = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {
			
			@Override
			public void onSuccess(BASE result2) {
				if(licenseBitmap == null || faceBitmap == null){
					photoUploadDone = false;
					AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast("保存成功");
					AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
					if(zcstatus == 1){
						Define.INFO_LOGIN loginInfo = ((CarServerApplication)getApplication()).getLoginInfo();
						loginInfo.status = "wsh";
						((CarServerApplication)getApplication()).setLoginInfo(loginInfo);
					}
					finish();
				}else if(photoUploadDone){
					photoUploadDone = false;
					AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast("保存成功");
					AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
					if(zcstatus == 1){
						Define.INFO_LOGIN loginInfo = ((CarServerApplication)getApplication()).getLoginInfo();
						loginInfo.status = "wsh";
						((CarServerApplication)getApplication()).setLoginInfo(loginInfo);
					}
					finish();
				}else{
					photoUploadDone = true;
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				photoUploadDone = false;
				AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast("保存失败");
				AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
			}
		}, InfoDetailActivity.this);
		String imageType = isZJ ? WebServiceHelper.IMAGE_TYPE_ZJ : WebServiceHelper.IMAGE_TYPE_USER;
		mUploadImage.saveImage(
				bitmap, imageName, imageType, mUploadImage.getUserId());
	}
}
