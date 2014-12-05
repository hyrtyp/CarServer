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
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.activity.MainActivity;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_MERCHANT;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.SINGLE_ID;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.LocationHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;
import cn.com.hyrt.carserverseller.info.adapter.BrandCheckAdapter;

public class MerchantInfoActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_shopname) EditText etShopName;
	@ViewInject(id=R.id.et_singlename) EditText etSingleName;
	@ViewInject(id=R.id.iv_merchant_photo) ImageLoaderView ivMerchantPhoto;
	@ViewInject(id=R.id.tv_merchant_photo) TextView tvMerchantPhoto;
	@ViewInject(id=R.id.iv_license_photo) ImageLoaderView ivLicensePhoto;
	@ViewInject(id=R.id.tv_license_photo) TextView tvLicensePhoto;
	@ViewInject(id=R.id.tv_select_brand) TextView tvSelectBrand;
	@ViewInject(id=R.id.tv_select_fwclass) TextView tvSelectFwClass;
	@ViewInject(id=R.id.sp_province) Spinner spProvince;
	@ViewInject(id=R.id.sp_city) Spinner spCity;
	@ViewInject(id=R.id.sp_county) Spinner spCounty;
	@ViewInject(id=R.id.tv_select_position) TextView tvSelectPosition;
	@ViewInject(id=R.id.et_contact) EditText etContact;
	@ViewInject(id=R.id.et_phonenum) EditText etPhoneNum;
	@ViewInject(id=R.id.et_telnum) EditText etTelNum;
	@ViewInject(id=R.id.et_desc) EditText etDesc;
	@ViewInject(id=R.id.btn_submit,click="save") Button btnSubmit;
	
	private Bitmap merchantBitmap;
	private Bitmap licenseBitmap;
	private String merchantImgUrl;
	private String licenseImgUrl;
	private boolean isMerchantSelect;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
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
	
	private Dialog mBrandDialog;
	private Dialog mfwClassDialog;
	
	private List<Map<String, String>> oneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> twoList 
	= new ArrayList<List<Map<String,String>>>();
	private List<String> brandCheckedId = new ArrayList<String>();
	private List<String> brandCheckedName = new ArrayList<String>();
	
	private List<Map<String, String>> fwClassOneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> fwClassTwoList 
	= new ArrayList<List<Map<String,String>>>();
	private List<String> fwClassCheckedId = new ArrayList<String>();
	private List<String> fwClassCheckedName = new ArrayList<String>();
	
	private double lng = -1;
	private double lat = -1;
	
	private static final int LOCATION_TIMEOUT = 0;
	private boolean photoUploadDone = false;
	
	private Define.INFO_MERCHANT mData;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOCATION_TIMEOUT:
				LocationHelper.getInstance(MerchantInfoActivity.this).stop();
				AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
				AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("定位失败");
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant_info);
		Intent intent = getIntent();
		mData = (INFO_MERCHANT) intent.getSerializableExtra("vo");
		if(mData != null){
			setTitle("修改信息");
		}
		initView();
		loadData();
		setListener();
	}
	
	/**
	 * 保存用户信息
	 */
	public void save(View view){
		
		LogHelper.i("tag", "merchantBitmap:"+merchantBitmap
				+" licenseBitmap:"+licenseBitmap);
		
		Define.SAVE_MERCHANT merchant = new Define.SAVE_MERCHANT();
		String shopName = etShopName.getText().toString();
		if("".equals(shopName.trim())){
			AlertHelper.getInstance(this).showCenterToast("请输入店名");
			return;
		}
		merchant.sjname = shopName;
		
		String singleName = etSingleName.getText().toString();
		if("".equals(singleName.trim())){
			AlertHelper.getInstance(this).showCenterToast("请输入简称");
			return;
		}
		merchant.sjjc = singleName;
		
		if(fwClassCheckedId.size() <= 0){
			AlertHelper.getInstance(this).showCenterToast("请选择服务");
			return;
		}
		String fwCheckIds = "";
		for(int i=0,j=fwClassCheckedId.size(); i<j; i++){
			fwCheckIds += fwClassCheckedId.get(i)+";";
		}
		fwCheckIds = fwCheckIds.substring(0, fwCheckIds.length()-1);
		merchant.servicetype = fwCheckIds;
		if(brandCheckedId.size() > 0){
			String brandIds = "";
			for(int i=0,j=brandCheckedId.size(); i<j; i++){
				brandIds += brandCheckedId.get(i)+";";
			}
			brandIds = brandIds.substring(0, brandIds.length()-1);
			merchant.brandid = brandIds;
		}
		if(spCounty.getSelectedItemPosition() > 0){
			merchant.areaid = countyId.get(spCounty.getSelectedItemPosition());
		}else{
			AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("请选择所属地区");
			return;
		}
		
		merchant.coor_x = lat+"";
		merchant.coor_y = lng+"";
		merchant.sjmanager = etContact.getText().toString();
		String phoneNum = etPhoneNum.getText().toString();
		if(!StringHelper.isMobileNum(phoneNum)){
			AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast(R.string.phone_error);
			return;
		}
		merchant.phonenum = phoneNum;
		merchant.sjtel = etTelNum.getText().toString();
		String desc = etDesc.getText().toString();
		if(desc.length() < 10){
			AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("商家简介请输入10个字以上");
			return;
		}
		merchant.desc = desc;
		
		WebServiceHelper mSaveMerchantInfoHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.SINGLE_ID>() {

					@Override
					public void onSuccess(SINGLE_ID result) {
						if(merchantBitmap == null && licenseBitmap == null){
							AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("保存成功");
							AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
							INFO_LOGIN loginInfo 
							= ((CarServerApplication)getApplication()).getLoginInfo();
							loginInfo.bcxx = "1";
							if(result.id != null && !"".equals(result.id)){
								loginInfo.serviceid = result.id;
							}
							((CarServerApplication)getApplication()).setLoginInfo(loginInfo);
							Intent intent = new Intent();
							intent.setClass(MerchantInfoActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
							return;
						}
						if(merchantBitmap != null){
							uploadImage(merchantBitmap, "sjPhoto.jpeg", false, result.id);
						}
						if(licenseBitmap != null){
							uploadImage(licenseBitmap, "zzPhoto.jpeg", true, result.id);
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
						AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("保存失败");
						
					}
		}, MerchantInfoActivity.this);
		AlertHelper.getInstance(MerchantInfoActivity.this).showLoading("保存中...");
		mSaveMerchantInfoHelper.saveMerchant(merchant);
	}
	private void loadData(){
		
		WebServiceHelper mBrandWebServiceHelper 
		= new WebServiceHelper(new BaseWebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				oneList.clear();
				twoList.clear();
				ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
				classifyJsonParser.parse(result);
				oneList.addAll(classifyJsonParser.getOneList());
//				 LogHelper.i("tag", "oneList:"+oneList);
				twoList.addAll(classifyJsonParser.getTwoList());
//				 LogHelper.i("tag", "twoList:"+twoList);
			}
		}, MerchantInfoActivity.this);
		mBrandWebServiceHelper.getCLWDfl(getString(R.string.flid_brand));
		
		WebServiceHelper mFWClassWebServiceHelper = new WebServiceHelper(new BaseWebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
				classifyJsonParser.parse(result);
				fwClassOneList.clear();
				fwClassOneList.addAll(classifyJsonParser.getOneList());
				fwClassTwoList.clear();
				fwClassTwoList.addAll(classifyJsonParser.getTwoList());
			}
		}, MerchantInfoActivity.this);
		mFWClassWebServiceHelper.getFwClassList();
		
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
							MerchantInfoActivity.this,
							R.layout.layout_spinner_item_blue,
							provinceName);
				}else{
					mProvinceArrayAdapter.notifyDataSetChanged();
				}
				
				if(mCityArrayAdapter == null){
					mCityArrayAdapter = new ArrayAdapter<String>(
							MerchantInfoActivity.this,
							R.layout.layout_spinner_item_blue,
							cityName);
				}
				
				if(mCountyArrayAdapter == null){
					mCountyArrayAdapter = new ArrayAdapter<String>(
							MerchantInfoActivity.this,
							R.layout.layout_spinner_item_blue,
							countyName);
				}
				
//				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
//						RegistMerchantInfoActivity.this,
//						R.layout.layout_spinner_item_blue,
//						getResources().getStringArray(R.array.spinnername));
				spProvince.setAdapter(mProvinceArrayAdapter);
				spCity.setAdapter(mCityArrayAdapter);
				spCounty.setAdapter(mCountyArrayAdapter);
				if(mData != null){
					changeCountySelected();
				}
			}
		}, MerchantInfoActivity.this);
		mAddressWebServiceHelper.getCodingArea();
		
	}
	
	private void initView(){
		if(mData == null){
			return;
		}
		Define.INFO_MERCHANT.CDATA merchantInfo = mData.data.get(0);
		etShopName.setText(merchantInfo.sjname);
		etSingleName.setText(merchantInfo.sjjc);
		if(merchantInfo.imagepath != null 
				&& !"".equals(merchantInfo.imagepath.trim())){
			merchantImgUrl = merchantInfo.imagepath;
			ivMerchantPhoto.setImageUrl(merchantImgUrl);
			tvMerchantPhoto.setVisibility(View.GONE);
		}
		if(merchantInfo.zzimagepath != null 
				&& !"".equals(merchantInfo.zzimagepath.trim())){
			licenseImgUrl = merchantInfo.zzimagepath;
			ivLicensePhoto.setImageUrl(licenseImgUrl);
			tvLicensePhoto.setVisibility(View.GONE);
		}
		
		if(merchantInfo.brandname != null && !"".equals(merchantInfo.brandname)){
			String[] brandids = merchantInfo.brandid.split(";");
			String[] brandNames = merchantInfo.brandname.split("、");
			brandCheckedId.clear();
			brandCheckedName.clear();
			for (String str : brandids) {
				brandCheckedId.add(str);
			}
			for (String str : brandNames) {
				brandCheckedName.add(str);
			}
			tvSelectBrand.setText(merchantInfo.brandname);
		}
		
		String[] fwClassIds = merchantInfo.serviceTypeid.split(";");
		String[] fwClassNames = merchantInfo.serviceTypename.split(";");
		fwClassCheckedId.clear();
		fwClassCheckedName.clear();
		for(String str : fwClassIds){
			fwClassCheckedId.add(str);
		}
		for(String str : fwClassNames){
			fwClassCheckedName.add(str);
		}
		tvSelectFwClass.setText(merchantInfo.serviceTypename);

		etContact.setText(merchantInfo.sjmanager);
		etPhoneNum.setText(merchantInfo.phonenum);
		etTelNum.setText(merchantInfo.sjtel);
		etDesc.setText(merchantInfo.desc);
	}
	
	private void delPhoto(final boolean isMerchant){
		AlertDialog.Builder mDelPhotoDialog = new Builder(MerchantInfoActivity.this);
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(isMerchant){
					tvMerchantPhoto.setVisibility(View.VISIBLE);
					merchantBitmap = null;
					merchantImgUrl = null;
					ivMerchantPhoto.setImageResource(R.drawable.ic_photo_add);
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
		if (FileHelper.sdCardExist()) {
			if(faceUri == null){
				faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
			}
			mPhotoHelper = new PhotoHelper(MerchantInfoActivity.this, faceUri, 50);
			mPhotoHelper.getPhoto();
		}else{
			AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
		}
		/*if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(MerchantInfoActivity.this, faceUri, 50);
		mPhotoHelper.getPhoto();*/
	}
	
	private void showBrand(){
		if(mBrandDialog != null){
			mBrandDialog.dismiss();
			mBrandDialog = null;
		}
		mBrandDialog = new Dialog(MerchantInfoActivity.this, R.style.MyDialog);
		mBrandDialog.setContentView(R.layout.layout_list_popup);
		mBrandDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		ListView listview = (ListView) mBrandDialog
				.getWindow().findViewById(R.id.listview);
		final BrandCheckAdapter mBrandCheckAdapter = new BrandCheckAdapter(
				oneList, twoList, brandCheckedId, brandCheckedName, MerchantInfoActivity.this);
		listview.setAdapter(mBrandCheckAdapter);
		
//		mBrandDialog.getWindow().findViewById(R.id.view_bg)
//		.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mBrandDialog.dismiss();
//				mBrandDialog = null;
//			}
//		});
		
		mBrandDialog.getWindow().findViewById(R.id.btn_popup_submit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				brandCheckedId.clear();
				brandCheckedId.addAll(mBrandCheckAdapter.getCheckedId());
				brandCheckedName.clear();
				brandCheckedName.addAll(mBrandCheckAdapter.getCheckedName());
				
				if(brandCheckedName.size() > 0){
					String str = "";
					for(int i=0,j=brandCheckedName.size(); i<j; i++){
						str += brandCheckedName.get(i)+"、";
					}
					tvSelectBrand.setText(str.substring(0, str.length()-1));
				}else{
					tvSelectBrand.setText("选择品牌");
				}
				
				mBrandDialog.dismiss();
				mBrandDialog = null;
			}
			
		});
		
		mBrandDialog.show();
	}
	
	/**
	 * 显示分类服务选择
	 */
	private void showFwClass(){
		if(mfwClassDialog != null){
			mfwClassDialog.dismiss();
			mfwClassDialog = null;
		}
		mfwClassDialog = new Dialog(MerchantInfoActivity.this, R.style.MyDialog);
		mfwClassDialog.setContentView(R.layout.layout_list_popup);
		mfwClassDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		ListView listview = (ListView) mfwClassDialog
				.getWindow().findViewById(R.id.listview);
		final BrandCheckAdapter mFwClassCheckAdapter = new BrandCheckAdapter(
				fwClassOneList, fwClassTwoList, fwClassCheckedId, fwClassCheckedName, MerchantInfoActivity.this);
		listview.setAdapter(mFwClassCheckAdapter);
		
//		mBrandDialog.getWindow().findViewById(R.id.view_bg)
//		.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				mBrandDialog.dismiss();
//				mBrandDialog = null;
//			}
//		});
		
		mfwClassDialog.getWindow().findViewById(R.id.btn_popup_submit)
		.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				fwClassCheckedId.clear();
				fwClassCheckedId.addAll(mFwClassCheckAdapter.getCheckedId());
				fwClassCheckedName.clear();
				fwClassCheckedName.addAll(mFwClassCheckAdapter.getCheckedName());
				
				if(fwClassCheckedName.size() > 0){
					String str = "";
					for(int i=0,j=fwClassCheckedName.size(); i<j; i++){
						str += fwClassCheckedName.get(i)+"、";
					}
					tvSelectFwClass.setText(str.substring(0, str.length()-1));
				}else{
					tvSelectFwClass.setText("选择服务");
				}
				
				mfwClassDialog.dismiss();
				mfwClassDialog = null;
			}
			
		});
		
		mfwClassDialog.show();
	}
	
	private void setListener() {
		ivMerchantPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (merchantBitmap == null && merchantImgUrl == null) {
					isMerchantSelect = true;
					addPhoto();
				} else {
					if (merchantBitmap != null) {
						PhotoPopupHelper.showPop(merchantBitmap,
								MerchantInfoActivity.this);
					} else {
						PhotoPopupHelper.showPop(merchantImgUrl,
								MerchantInfoActivity.this);
					}
				}
			}
		});
		ivMerchantPhoto.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (merchantBitmap == null && merchantImgUrl == null) {
					isMerchantSelect = true;
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
					isMerchantSelect = false;
					addPhoto();
				} else {
					if (licenseBitmap != null) {
						PhotoPopupHelper.showPop(licenseBitmap,
								MerchantInfoActivity.this);
					} else {
						PhotoPopupHelper.showPop(licenseImgUrl,
								MerchantInfoActivity.this);
					}
				}
			}
		});

		ivLicensePhoto.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (licenseBitmap == null && licenseImgUrl == null) {
					isMerchantSelect = false;
					addPhoto();
				} else {
					delPhoto(false);
				}
				return true;
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
		
		tvSelectBrand.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showBrand();
			}
		});
		
		tvSelectFwClass.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showFwClass();
			}
		});
		
		tvSelectPosition.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(lng == -1 || lat == -1){
					LocationHelper.getInstance(MerchantInfoActivity.this)
					.setLocationCallback(new LocationHelper.LocationCallback() {
						@Override
						public void onLocation(double lng, double lat, String city) {
							MerchantInfoActivity.this.lng = lng;
							MerchantInfoActivity.this.lat = lat;
							AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
							tvSelectPosition.setText("关闭定位");
							mHandler.removeMessages(LOCATION_TIMEOUT);
							LocationHelper.getInstance(MerchantInfoActivity.this).stop();
						}
					});
					AlertHelper.getInstance(MerchantInfoActivity.this).showLoading("定位中...");
					LocationHelper.getInstance(MerchantInfoActivity.this).start();
					Message msg = new Message();
					msg.what = LOCATION_TIMEOUT;
					mHandler.sendMessageDelayed(msg, 5000);
				}else{
					lat = -1;
					lng = -1;
					tvSelectPosition.setText("开启定位");
				}
				
			}
		});
		
		etShopName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(!hasFocus){
					String shopName = etShopName.getText().toString();
					if (!"".equals(shopName.trim())) {
						WebServiceHelper verNameHelper = new WebServiceHelper(
								new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

									@Override
									public void onSuccess(BASE result) {}

									@Override
									public void onFailure(int errorNo,
											String errorMsg) {
										if(errorNo == 207){
											String mShopName = etShopName.getText().toString();
											if(mData == null || !mShopName.equals(mData.data.get(0).sjname)){
												AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("店名重复，请重新输入");
											}
										}
									}
								}, MerchantInfoActivity.this);
						verNameHelper.getMerchantUserinfo(shopName, true);
					}
				}
			}
		});
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
//                	merchantPhotos.add(bitmap);
//                	merchantAdapter.notifyDataSetChanged();
                	tvMerchantPhoto.setVisibility(View.GONE);
                	merchantBitmap = bitmap;
                	merchantImgUrl = null;
                	ivMerchantPhoto.setImageBitmap(bitmap);
                }else{
                	tvLicensePhoto.setVisibility(View.GONE);
                	licenseBitmap = bitmap;
                	licenseImgUrl = null;
                	ivLicensePhoto.setImageBitmap(bitmap);
//                	licensePhoto.add(bitmap);
//                	licenseAdapter.notifyDataSetChanged();
                }
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(MerchantInfoActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
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
		if (FileHelper.sdCardExist()) {
			if(faceUri == null){
	            faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
	        }
	        new Crop(source).output(faceUri).asSquare().start(this);	
		}else{
			AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
		}
		/*if(faceUri == null){
            faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
        }
        new Crop(source).output(faceUri).asSquare().start(this);*/
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
        	Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(result));
				if(isMerchantSelect){
					tvMerchantPhoto.setVisibility(View.GONE);
					merchantImgUrl = null;
					merchantBitmap = bitmap;
					ivMerchantPhoto.setImageBitmap(bitmap);
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
	
	public void uploadImage(Bitmap bitmap, String imageName, boolean isZZ, final String serviceId){
		WebServiceHelper mUploadImage = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {
			
			@Override
			public void onSuccess(BASE result2) {
				if(licenseBitmap == null || merchantBitmap == null){
					photoUploadDone = false;
					AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("保存成功");
					AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
					INFO_LOGIN loginInfo 
					= ((CarServerApplication)getApplication()).getLoginInfo();
					loginInfo.bcxx = "1";
					if(serviceId != null && !"".equals(serviceId)){
						loginInfo.serviceid = serviceId;
					}
					((CarServerApplication)getApplication()).setLoginInfo(loginInfo);
					Intent intent = new Intent();
					intent.setClass(MerchantInfoActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else if(photoUploadDone){
					photoUploadDone = false;
					AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("保存成功");
					AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
					INFO_LOGIN loginInfo 
					= ((CarServerApplication)getApplication()).getLoginInfo();
					loginInfo.bcxx = "1";
					if(serviceId != null && !"".equals(serviceId)){
						loginInfo.serviceid = serviceId;
					}
					((CarServerApplication)getApplication()).setLoginInfo(loginInfo);
					Intent intent = new Intent();
					intent.setClass(MerchantInfoActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}else{
					photoUploadDone = true;
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				photoUploadDone = false;
				AlertHelper.getInstance(MerchantInfoActivity.this).showCenterToast("保存失败");
				AlertHelper.getInstance(MerchantInfoActivity.this).hideLoading();
			}
		}, MerchantInfoActivity.this);
		String imageType = isZZ ? WebServiceHelper.IMAGE_TYPE_ZZ : WebServiceHelper.IMAGE_TYPE_SJ;
		if(serviceId != null && !"".equals(serviceId)){
			mUploadImage.saveImage(
					bitmap, imageName, imageType, serviceId);
		}else{
			mUploadImage.saveImage(
					bitmap, imageName, imageType,
					((CarServerApplication)getApplication()).getLoginInfo().serviceid);
		}
	}
	
	private void changeCountySelected(){
		ok:
		if(mData.data.get(0).areaid != null && !"".equals(mData.data.get(0).areaid)){
			for(int i=0,j=addressThreeList.size(); i<j; i++){
				List<List<Map<String, String>>> list1 = addressThreeList.get(i);
				for(int a=0,b=list1.size(); a<b; a++){
					List<Map<String, String>> list2 = list1.get(a);
					for(int x=0,y=list2.size(); x<y; x++){
						Map<String, String> map1 = list2.get(x);
						LogHelper.i("tag", "id:"+map1.get("id"));
						if(mData.data.get(0).areaid.equals(map1.get("id"))){
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
}
