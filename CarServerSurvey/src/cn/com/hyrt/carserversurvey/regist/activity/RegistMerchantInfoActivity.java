package cn.com.hyrt.carserversurvey.regist.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kobjects.base64.Base64;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;
import cn.com.hyrt.carserversurvey.base.activity.MainActivity;
import cn.com.hyrt.carserversurvey.base.adapter.AddPhotoGridAdapter;
import cn.com.hyrt.carserversurvey.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.SAVE_INFO_MERCHANT_RESULT;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserversurvey.base.helper.StringHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import cn.com.hyrt.carserversurvey.regist.adapter.BrandCheckAdapter;

/**
 * 修改商户
 * @author zoe
 *
 */
public class RegistMerchantInfoActivity extends BaseActivity{

//	private GridView gvMerchantPhoto;
//	private GridView gvLicensePhoto;
//	private GridView gvServices;
	private Spinner spProvince;
	private Spinner spCity;
	private Spinner spCounty;
	private TextView tvSelectBrand;
	private TextView tvSelectFwClass;
	private TextView etFullname;
	private TextView etSinglename;
	private TextView etUsername;
	private TextView etAddress;
	private TextView etContactname;
	private TextView etPhonenum;
	private TextView etTelnum;
	private TextView etDesc;
	private Button btnSubmit;
	private ImageLoaderView ivMerchantPhoto;
	private TextView tvMerchantPhoto;
	private ImageLoaderView ivLicensePhoto;
	private TextView tvLicensePhoto;
	
	private List<String> services;
	
//	private List<Bitmap> merchantPhotos = new  ArrayList<Bitmap>();
//	private List<Bitmap> licensePhoto = new ArrayList<Bitmap>();
//	private AddPhotoGridAdapter merchantAdapter;
//	private AddPhotoGridAdapter licenseAdapter;
	private ArrayAdapter<String> mProvinceArrayAdapter;
	private ArrayAdapter<String> mCityArrayAdapter;
	private ArrayAdapter<String> mCountyArrayAdapter;
	
	private List<Map<String, String>> oneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> twoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private List<Map<String, String>> addressOneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> addressTwoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private List<List<List<Map<String, String>>>> addressThreeList
	= new ArrayList<List<List<Map<String,String>>>>();
	
	private List<Map<String, String>> fwClassOneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> fwClassTwoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private List<String> brandCheckedId = new ArrayList<String>();
	private List<String> brandCheckedName = new ArrayList<String>();
	private List<String> fwClassCheckedId = new ArrayList<String>();
	private List<String> fwClassCheckedName = new ArrayList<String>();
	
	private List<String> provinceId = new ArrayList<String>();
	private List<String> provinceName = new ArrayList<String>();
	private List<String> cityId = new ArrayList<String>();
	private List<String> cityName = new ArrayList<String>();
	private List<String> countyId = new ArrayList<String>();
	private List<String> countyName = new ArrayList<String>();
	
	private int curProvinceIndex = 0;
	private int curCityIndex = 0;
	private int curCountyIndex = 0;
	
	
	private boolean isMerchantSelect = true;//是否正在选商户照片
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
//	private String merchantImgBuffer;
	private Bitmap merchantBitmap;
	private Bitmap licenseBitmap;
	
	private Dialog mBrandDialog;
	private Dialog mfwClassDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist_merchant);
		findView();
		loadData();
		
//		if(merchantAdapter == null){
//			merchantAdapter = new AddPhotoGridAdapter(merchantPhotos, RegistMerchantInfoActivity.this);
//		}
//		if(licenseAdapter == null){
//			licenseAdapter = new AddPhotoGridAdapter(licensePhoto, RegistMerchantInfoActivity.this);
//		}
//		gvMerchantPhoto.setAdapter(merchantAdapter);
//		gvLicensePhoto.setAdapter(licenseAdapter);
		
		setListener();
	}
	
	private void loadData(){
		services = new ArrayList<String>();
		services.add("专业美容");
		services.add("专业美容");
		services.add("专业美容");
		services.add("专业美容");
		services.add("专业美容");
		services.add("专业美容");
		
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
		}, RegistMerchantInfoActivity.this);
		mBrandWebServiceHelper.getCLWDfl(getString(R.string.flid_brand));
		
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
							RegistMerchantInfoActivity.this,
							R.layout.layout_spinner_item,
							provinceName);
				}else{
					mProvinceArrayAdapter.notifyDataSetChanged();
				}
				
				if(mCityArrayAdapter == null){
					mCityArrayAdapter = new ArrayAdapter<String>(
							RegistMerchantInfoActivity.this,
							R.layout.layout_spinner_item,
							cityName);
				}
				
				if(mCountyArrayAdapter == null){
					mCountyArrayAdapter = new ArrayAdapter<String>(
							RegistMerchantInfoActivity.this,
							R.layout.layout_spinner_item,
							countyName);
				}
				
//				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
//						RegistMerchantInfoActivity.this,
//						R.layout.layout_spinner_item,
//						getResources().getStringArray(R.array.spinnername));
				spProvince.setAdapter(mProvinceArrayAdapter);
				spCity.setAdapter(mCityArrayAdapter);
				spCounty.setAdapter(mCountyArrayAdapter);
			}
		}, RegistMerchantInfoActivity.this);
		mAddressWebServiceHelper.getCodingArea();
		
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
		}, RegistMerchantInfoActivity.this);
		mFWClassWebServiceHelper.getFwClassList();
	}
	
	private void setListener(){
		ivMerchantPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(merchantBitmap == null){
					isMerchantSelect = true;
					addPhoto();
				}else{
					PhotoPopupHelper.showPop(merchantBitmap, RegistMerchantInfoActivity.this);
				}
			}
		});
		ivMerchantPhoto.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if(merchantBitmap == null){
					isMerchantSelect = true;
					addPhoto();
				}else{
					delPhoto(true);
				}
				return true;
			}
		});
		
		ivLicensePhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(licenseBitmap == null){
					isMerchantSelect = false;
					addPhoto();
				}else{
					PhotoPopupHelper.showPop(licenseBitmap, RegistMerchantInfoActivity.this);
				}
			}
		});
		
		ivLicensePhoto.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if(licenseBitmap == null){
					isMerchantSelect = false;
					addPhoto();
				}else{
					delPhoto(false);
				}
				return true;
			}
		});
		
//		merchantAdapter.setCallback(new AddPhotoGridAdapter.PhotoGridCallback() {
//			
//			@Override
//			public void onClick(int position) {
//				if(position == -1){
//					isMerchantSelect = true;
//					addPhoto();
//				}else{
//					PhotoPopupHelper.showPop(merchantPhotos.get(position), RegistMerchantInfoActivity.this);
//				}
//			}
//
//			@Override
//			public void onLongClick(int position) {
//				if(position == -1){
//					isMerchantSelect = true;
//					addPhoto();
//				}else{
//					delPhoto(position, true);
//				}
//			}
//		});
		
//		licenseAdapter.setCallback(new AddPhotoGridAdapter.PhotoGridCallback() {
//			
//			@Override
//			public void onClick(int position) {
//				if(position == -1){
//					isMerchantSelect = false;
//					addPhoto();
//				}else{
//					PhotoPopupHelper.showPop(licensePhoto.get(position), RegistMerchantInfoActivity.this);
//				}
//			}
//
//			@Override
//			public void onLongClick(int position) {
//				if(position == -1){
//					isMerchantSelect = false;
//					addPhoto();
//				}else{
//					delPhoto(position, false);
//				}
//			}
//		});
		
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
		
		spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				curProvinceIndex = position-1;
				
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
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				curCityIndex = position-1;
				
				countyId.clear();
				countyName.clear();
				countyId.add("-1");
				countyName.add("县");
				
				mCountyArrayAdapter.notifyDataSetChanged();
				
				if(position <= 0 || curProvinceIndex < 0){
					return;
				}
				List<Map<String, String>> mCountys = addressThreeList.get(curProvinceIndex).get(position-1);
				
				
				
				for(int i=0,j=mCountys.size(); i<j; i++){
					Map<String, String> mCountyData = mCountys.get(i);
					countyId.add(mCountyData.get("id"));
					countyName.add(mCountyData.get("name"));
				}
				mCountyArrayAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		spCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				curCountyIndex = position-1;
				if(position <= 0){
					return;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
	}
	
	/*private void delPhoto(final int position, final boolean isMerchant){
		AlertDialog.Builder mDelPhotoDialog = new Builder(RegistMerchantInfoActivity.this);
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(isMerchant){
//					merchantPhotos.remove(position);
//					merchantAdapter.notifyDataSetChanged();
				}else{
					licensePhoto.remove(position);
					licenseAdapter.notifyDataSetChanged();
				}
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}*/
	
	private void delPhoto(final boolean isMerchant){
		AlertDialog.Builder mDelPhotoDialog = new Builder(RegistMerchantInfoActivity.this);
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(isMerchant){
					tvMerchantPhoto.setVisibility(View.VISIBLE);
					merchantBitmap = null;
					ivMerchantPhoto.setImageResource(R.drawable.ic_photo_add);
				}else{
					tvLicensePhoto.setVisibility(View.VISIBLE);
					licenseBitmap = null;
					ivLicensePhoto.setImageResource(R.drawable.ic_photo_add);
				}
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}
	
	private void addPhoto(){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(RegistMerchantInfoActivity.this, faceUri, 50);
		mPhotoHelper.getPhoto();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                if(isMerchantSelect){
//                	merchantPhotos.add(bitmap);
//                	merchantAdapter.notifyDataSetChanged();
                	tvMerchantPhoto.setVisibility(View.GONE);
                	merchantBitmap = bitmap;
                	ivMerchantPhoto.setImageBitmap(bitmap);
                }else{
                	tvLicensePhoto.setVisibility(View.GONE);
                	licenseBitmap = bitmap;
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
                mPhotoHelper = new PhotoHelper(RegistMerchantInfoActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }
	}
	
	private void showBrand(){
		if(mBrandDialog != null){
			mBrandDialog.dismiss();
			mBrandDialog = null;
		}
		mBrandDialog = new Dialog(RegistMerchantInfoActivity.this, R.style.MyDialog);
		mBrandDialog.setContentView(R.layout.layout_list_popup);
		mBrandDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		ListView listview = (ListView) mBrandDialog
				.getWindow().findViewById(R.id.listview);
		final BrandCheckAdapter mBrandCheckAdapter = new BrandCheckAdapter(
				oneList, twoList, brandCheckedId, brandCheckedName, RegistMerchantInfoActivity.this);
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
		mfwClassDialog = new Dialog(RegistMerchantInfoActivity.this, R.style.MyDialog);
		mfwClassDialog.setContentView(R.layout.layout_list_popup);
		mfwClassDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		ListView listview = (ListView) mfwClassDialog
				.getWindow().findViewById(R.id.listview);
		final BrandCheckAdapter mFwClassCheckAdapter = new BrandCheckAdapter(
				fwClassOneList, fwClassTwoList, fwClassCheckedId, fwClassCheckedName, RegistMerchantInfoActivity.this);
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
	
	private void submit(){
		String fullname = etFullname.getText().toString();
		if("".equals(fullname) || fullname == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "全称"));
			return;
		}else{
			if(!StringHelper.nameValidate(fullname)){
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_symbol), "全称"));
				return;
			}
		}
		String singlename = etSinglename.getText().toString();
		
		if("".equals(singlename) || singlename == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "简称"));
			return;
		}else{
			if(!StringHelper.nameValidate(singlename)){
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_symbol), "简称"));
				return;
			}
		}
		
		String username = etUsername.getText().toString();
		if("".equals(username) || username == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "注册账户"));
			return;
		}else{
			if(!StringHelper.isMobileNum(username)){
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "注册账户必须为手机号"));
				return;
			}
		}
		if(fwClassCheckedId.size() <=0){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "服务类型"));
			return;
		}
		
		if(brandCheckedId.size() <=0){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "品牌"));
			return;
		}
		
		int countyPosition = spCounty.getSelectedItemPosition();
		String mCountyId = null;
		if(countyPosition > 0){
			mCountyId = countyId.get(spCounty.getSelectedItemPosition());
		}
		String address = etAddress.getText().toString();
		String contactName = etContactname.getText().toString();
		String phoneNum = etPhonenum.getText().toString();
		String telNum = etTelnum.getText().toString();
		String desc = etDesc.getText().toString();
		
		if(mCountyId == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "所属地区"));
			return;
		}
		
		if("".equals(address) || address == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "具体地址"));
			return;
		}
		
		if("".equals(phoneNum) || phoneNum == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "手机号码"));
			return;
		}
		
		if("".equals(desc) || desc == null){
			AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(String.format(getString(R.string.text_not_null), "商家简介"));
			return;
		}
		
		String sjPhoto = null;
		String sjPhotoName = null;
		if(merchantBitmap != null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			merchantBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			sjPhoto = new String(Base64.encode(baos.toByteArray()));
			sjPhotoName = "sjPhoto.jpeg";
		}
//		for(int i=0,j=merchantPhotos.size(); i<j; i++){
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			Bitmap mBitmap = merchantPhotos.get(i);
//			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			if(i == j-1){
//				sjPhoto.append(new String(Base64.encode(baos.toByteArray())));
//				sjPhotoName.append("sjphoto"+(i+1)+".jpeg");
//			}else{
//				sjPhoto.append(new String(Base64.encode(baos.toByteArray()))+";");
//				sjPhotoName.append("sjphoto"+(i+1)+".jpeg;");
//			}
//		}
		
		String zzPhoto = null;
		String zzPhotoName = null;
		if(licenseBitmap != null){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			licenseBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			zzPhoto = new String(Base64.encode(baos.toByteArray()));
			zzPhotoName = "sjPhoto.jpeg";
		}
		
//		StringBuffer zzPhoto = new StringBuffer("");
//		StringBuffer zzPhotoName = new StringBuffer("");
//		for(int i=0,j=licensePhoto.size(); i<j; i++){
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			Bitmap mBitmap = licensePhoto.get(i);
//			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			if(i == j-1){
//				zzPhoto.append(new String(Base64.encode(baos.toByteArray())));
//				zzPhotoName.append("zzphoto"+(i+1)+".jpeg");
//			}else{
//				zzPhoto.append(new String(Base64.encode(baos.toByteArray()))+";");
//				zzPhotoName.append("zzphoto"+(i+1)+".jpeg;");
//			}
//		}
		
		StringBuffer fwClass = new StringBuffer("");
		for(int i=0,j=fwClassCheckedId.size(); i<j; i++){
			if(i == j-1){
				fwClass.append(fwClassCheckedId.get(i));
			}else{
				fwClass.append(fwClassCheckedId.get(i)+";");
			}
		}
		
		StringBuffer brands = new StringBuffer("");
		for(int i=0,j=brandCheckedId.size(); i<j; i++){
			if(i == j-1){
				brands.append(brandCheckedId.get(i));
			}else{
				brands.append(brandCheckedId.get(i)+";");
			}
		}
		
		Define.SAVE_INFO_MERCHANT merchantInfo = new Define.SAVE_INFO_MERCHANT();
		merchantInfo.sjname = fullname;
		merchantInfo.sjtel = telNum;
		merchantInfo.sjmanager = contactName;
		merchantInfo.sjjc = singlename;
		merchantInfo.sjaddress = address;
		merchantInfo.areaid = mCountyId;
		merchantInfo.loginname = username;
		merchantInfo.phonenum = phoneNum;
		merchantInfo.desc = desc;
		merchantInfo.sjimage = sjPhoto;
		merchantInfo.imagename = sjPhotoName;
		merchantInfo.zzimage = zzPhoto.toString();
		merchantInfo.zzimagename = zzPhotoName.toString();
		merchantInfo.brandid = brands.toString();
		merchantInfo.servicetype = fwClass.toString();
		
		AlertHelper.getInstance(RegistMerchantInfoActivity.this).showLoading(null);
		
		WebServiceHelper SaveMerchantHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.SAVE_INFO_MERCHANT_RESULT>() {

			@Override
			public void onSuccess(SAVE_INFO_MERCHANT_RESULT result) {
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(R.string.regist_success);
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).hideLoading();
				finish();
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).showCenterToast(R.string.regist_error);
				AlertHelper.getInstance(RegistMerchantInfoActivity.this).hideLoading();
			}
		}, RegistMerchantInfoActivity.this);
		SaveMerchantHelper.saveMerchantInfo(merchantInfo);
		
		
	}
	
	@Override
	public void onStop() {
		LogHelper.i("tag", "onStop");
		
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		LogHelper.i("tag", "onDestroy");
		super.onDestroy();
	}
	
	private void findView(){
//		gvMerchantPhoto = (GridView) findViewById(R.id.gv_merchant_photo);
		ivMerchantPhoto = (ImageLoaderView) findViewById(R.id.iv_merchant_photo);
		tvMerchantPhoto = (TextView) findViewById(R.id.tv_merchant_photo);
		ivLicensePhoto = (ImageLoaderView) findViewById(R.id.iv_license_photo);
		tvLicensePhoto = (TextView) findViewById(R.id.tv_license_photo);
//		gvLicensePhoto = (GridView) findViewById(R.id.gv_license_photo);
//		gvServices = (GridView) findViewById(R.id.gv_services);
		spProvince = (Spinner) findViewById(R.id.sp_province);
		spCity = (Spinner) findViewById(R.id.sp_city);
		spCounty = (Spinner) findViewById(R.id.sp_county);
		tvSelectBrand = (TextView) findViewById(R.id.tv_select_brand);
		tvSelectFwClass = (TextView) findViewById(R.id.tv_select_fwclass);
		etFullname = (TextView) findViewById(R.id.et_fullname);
		etSinglename = (TextView) findViewById(R.id.et_singlename);
		etUsername = (TextView) findViewById(R.id.et_username);
		etAddress = (TextView) findViewById(R.id.et_address);
		etContactname = (TextView) findViewById(R.id.et_contactname);
		etPhonenum = (TextView) findViewById(R.id.et_phonenum);
		etTelnum = (TextView) findViewById(R.id.et_telnum);
		etDesc = (TextView) findViewById(R.id.et_desc);
		btnSubmit = (Button) findViewById(R.id.btn_submit);
	}
	
}
