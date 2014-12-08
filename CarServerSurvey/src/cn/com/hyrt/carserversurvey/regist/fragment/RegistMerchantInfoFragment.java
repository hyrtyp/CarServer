package cn.com.hyrt.carserversurvey.regist.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kobjects.base64.Base64;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import cn.com.hyrt.carserversurvey.base.activity.MainActivity;
import cn.com.hyrt.carserversurvey.base.adapter.AddPhotoGridAdapter;
import cn.com.hyrt.carserversurvey.base.adapter.CheckBoxGridAdapter;
import cn.com.hyrt.carserversurvey.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.SAVE_INFO_MERCHANT_RESULT;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserversurvey.base.helper.StringHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.regist.activity.RegistMerchantInfoActivity;
import cn.com.hyrt.carserversurvey.regist.adapter.BrandCheckAdapter;

public class RegistMerchantInfoFragment extends Fragment{

	private View rootView;
	private GridView gvMerchantPhoto;
	private GridView gvLicensePhoto;
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
	
	private List<String> services;
	
	private List<Bitmap> merchantPhotos = new  ArrayList<Bitmap>();
	private List<Bitmap> licensePhoto = new ArrayList<Bitmap>();
	private AddPhotoGridAdapter merchantAdapter;
	private AddPhotoGridAdapter licenseAdapter;
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
	private String imgBuffer;
	
	private Dialog mBrandDialog;
	private Dialog mfwClassDialog;
	private boolean photoUploadDone = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogHelper.i("tag", "onCreateView:");
		merchantPhotos.clear();
		licensePhoto.clear();
		brandCheckedId.clear();
		brandCheckedName.clear();
		fwClassCheckedId.clear();
		fwClassCheckedName.clear();
		curCityIndex = 0;
		curCountyIndex = 0;
		curProvinceIndex = 0;
		
		rootView = inflater.inflate(R.layout.fragment_regist_merchant, null);
		findView();
		release();
		loadData();
		
//		CheckBoxGridAdapter mCheckBoxGridAdapter = new CheckBoxGridAdapter(services, getActivity());
//		gvServices.setAdapter(mCheckBoxGridAdapter);
		
		if(merchantAdapter == null){
			merchantAdapter = new AddPhotoGridAdapter(merchantPhotos, getActivity());
		}
		if(licenseAdapter == null){
			licenseAdapter = new AddPhotoGridAdapter(licensePhoto, getActivity());
		}
		gvMerchantPhoto.setAdapter(merchantAdapter);
		gvLicensePhoto.setAdapter(licenseAdapter);
		
		setListener();
		
		return rootView;
	}
	
	private void release(){
		LogHelper.i("tag", "release:");
		etFullname.setText("");
		etSinglename.setText("");
		etUsername.setText("");
		etAddress.setText("");
		etContactname.setText("");
		etPhonenum.setText("");
		etTelnum.setText("");
		etDesc.setText("");
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
		}, getActivity());
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
							getActivity(),
							R.layout.layout_spinner_item,
							provinceName);
				}else{
					mProvinceArrayAdapter.notifyDataSetChanged();
				}
				
				if(mCityArrayAdapter == null){
					mCityArrayAdapter = new ArrayAdapter<String>(
							getActivity(),
							R.layout.layout_spinner_item,
							cityName);
				}
				
				if(mCountyArrayAdapter == null){
					mCountyArrayAdapter = new ArrayAdapter<String>(
							getActivity(),
							R.layout.layout_spinner_item,
							countyName);
				}
				
//				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(
//						getActivity(),
//						R.layout.layout_spinner_item,
//						getResources().getStringArray(R.array.spinnername));
				spProvince.setAdapter(mProvinceArrayAdapter);
				spCity.setAdapter(mCityArrayAdapter);
				spCounty.setAdapter(mCountyArrayAdapter);
			}
		}, getActivity());
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
		}, getActivity());
		mFWClassWebServiceHelper.getFwClassList();
	}
	
	private void setListener(){
		merchantAdapter.setCallback(new AddPhotoGridAdapter.PhotoGridCallback() {
			
			@Override
			public void onClick(int position) {
				if(position == -1){
					isMerchantSelect = true;
					addPhoto();
				}else{
					PhotoPopupHelper.showPop(merchantPhotos.get(position), getActivity());
				}
			}

			@Override
			public void onLongClick(int position) {
				if(position == -1){
					isMerchantSelect = true;
					addPhoto();
				}else{
					delPhoto(position, true);
				}
			}
		});
		
		licenseAdapter.setCallback(new AddPhotoGridAdapter.PhotoGridCallback() {
			
			@Override
			public void onClick(int position) {
				if(position == -1){
					isMerchantSelect = false;
					addPhoto();
				}else{
					PhotoPopupHelper.showPop(licensePhoto.get(position), getActivity());
				}
			}

			@Override
			public void onLongClick(int position) {
				if(position == -1){
					isMerchantSelect = false;
					addPhoto();
				}else{
					delPhoto(position, false);
				}
			}
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
		
		etFullname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(!hasFocus){
					String sjname = etFullname.getText().toString();
					if(sjname != null && !"".equals(sjname)){
						sjnameExist(sjname, true);
					}
				}
			}
		});
		
		etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(!hasFocus){
					String sjname = etUsername.getText().toString();
					if(sjname != null && !"".equals(sjname)){
						sjnameExist(sjname, false);
					}
				}
			}
		});
	}
	
	private void delPhoto(final int position, final boolean isMerchant){
		AlertDialog.Builder mDelPhotoDialog = new Builder(getActivity());
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if(isMerchant){
					merchantPhotos.remove(position);
					merchantAdapter.notifyDataSetChanged();
				}else{
					licensePhoto.remove(position);
					licenseAdapter.notifyDataSetChanged();
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
			mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
			mPhotoHelper.getPhoto();
		}else{
			AlertHelper.getInstance(getActivity()).showCenterToast(getString(R.string.no_sdcard));
		}
		/*
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
		mPhotoHelper.getPhoto();*/
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
                	merchantPhotos.add(bitmap);
                	merchantAdapter.notifyDataSetChanged();
                }else{
                	licensePhoto.add(bitmap);
                	licenseAdapter.notifyDataSetChanged();
                }
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
        	if (FileHelper.sdCardExist()) {
        		if(mPhotoHelper == null){
                    if(faceUri == null){
                        faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
                    }
                    mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
                }
                mPhotoHelper.startPhotoZoom(faceUri, 400);
        	}else{
        		AlertHelper.getInstance(getActivity()).showCenterToast(getString(R.string.no_sdcard));
        	}
        	/*
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 400);*/
        }
	}
	
	private void showBrand(){
		if(mBrandDialog != null){
			mBrandDialog.dismiss();
			mBrandDialog = null;
		}
		mBrandDialog = new Dialog(getActivity(), R.style.MyDialog);
		mBrandDialog.setContentView(R.layout.layout_list_popup);
		mBrandDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		ListView listview = (ListView) mBrandDialog
				.getWindow().findViewById(R.id.listview);
		final BrandCheckAdapter mBrandCheckAdapter = new BrandCheckAdapter(
				oneList, twoList, brandCheckedId, brandCheckedName, getActivity());
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
		mfwClassDialog = new Dialog(getActivity(), R.style.MyDialog);
		mfwClassDialog.setContentView(R.layout.layout_list_popup);
		mfwClassDialog.getWindow().setLayout(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		
		ListView listview = (ListView) mfwClassDialog
				.getWindow().findViewById(R.id.listview);
		final BrandCheckAdapter mFwClassCheckAdapter = new BrandCheckAdapter(
				fwClassOneList, fwClassTwoList, fwClassCheckedId, fwClassCheckedName, getActivity());
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
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "全称"));
			return;
		}else{
			if(!StringHelper.nameValidate(fullname)){
				AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_symbol), "全称"));
				return;
			}
		}
		String singlename = etSinglename.getText().toString();
		
		if("".equals(singlename) || singlename == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "简称"));
			return;
		}else{
			if(!StringHelper.nameValidate(singlename)){
				AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_symbol), "简称"));
				return;
			}
		}
		
		String username = etUsername.getText().toString();
		if("".equals(username) || username == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "注册账户"));
			return;
		}else{
			if(!StringHelper.isMobileNum(username)){
				AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "注册账户必须为手机号"));
				return;
			}
		}
		if(fwClassCheckedId.size() <=0){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "服务类型"));
			return;
		}
		
		if(brandCheckedId.size() <=0){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "品牌"));
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
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "所属地区"));
			return;
		}
		
		if("".equals(address) || address == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "具体地址"));
			return;
		}
		
		if("".equals(phoneNum) || phoneNum == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "手机号码"));
			return;
		}
		
		if("".equals(desc) || desc == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "商家简介"));
			return;
		}
		
		StringBuffer sjPhoto = new StringBuffer("");
		StringBuffer sjPhotoName = new StringBuffer("");
		for(int i=0,j=merchantPhotos.size(); i<j; i++){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap mBitmap = merchantPhotos.get(i);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if(i == j-1){
				sjPhoto.append(new String(Base64.encode(baos.toByteArray())));
				sjPhotoName.append("sjphoto"+(i+1)+".jpeg");
			}else{
				sjPhoto.append(new String(Base64.encode(baos.toByteArray()))+";");
				sjPhotoName.append("sjphoto"+(i+1)+".jpeg;");
			}
		}
		
		StringBuffer zzPhoto = new StringBuffer("");
		StringBuffer zzPhotoName = new StringBuffer("");
		for(int i=0,j=licensePhoto.size(); i<j; i++){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap mBitmap = licensePhoto.get(i);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if(i == j-1){
				zzPhoto.append(new String(Base64.encode(baos.toByteArray())));
				zzPhotoName.append("zzphoto"+(i+1)+".jpeg");
			}else{
				zzPhoto.append(new String(Base64.encode(baos.toByteArray()))+";");
				zzPhotoName.append("zzphoto"+(i+1)+".jpeg;");
			}
		}
		
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
		merchantInfo.sjimage = sjPhoto.toString();
		merchantInfo.imagename = sjPhotoName.toString();
		merchantInfo.zzimage = zzPhoto.toString();
		merchantInfo.zzimagename = zzPhotoName.toString();
		merchantInfo.brandid = brands.toString();
		merchantInfo.servicetype = fwClass.toString();
		
		AlertHelper.getInstance(getActivity()).showLoading(null);
		
		WebServiceHelper SaveMerchantHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.SAVE_INFO_MERCHANT_RESULT>() {

			@Override
			public void onSuccess(SAVE_INFO_MERCHANT_RESULT result) {
				if(licensePhoto.size() <= 0 && merchantPhotos.size() <= 0){
					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.regist_success);
					AlertHelper.getInstance(getActivity()).hideLoading();
					etFullname.setText("");
					etSinglename.setText("");
					etUsername.setText("");
					etAddress.setText("");
					etContactname.setText("");
					etPhonenum.setText("");
					etTelnum.setText("");
					etDesc.setText("");
					((MainActivity)getActivity()).jump(1);
					return;
				}
				uploadImage(licensePhoto.get(0), "zzPhoto.jpeg", true, result.id);
				uploadImage(merchantPhotos.get(0), "sjPhoto.jpeg", false, result.id);
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(getActivity()).showCenterToast(R.string.regist_error);
				AlertHelper.getInstance(getActivity()).hideLoading();
			}
		}, getActivity());
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
	
	public void uploadImage(Bitmap bitmap, String imageName, boolean isZZ, String id){
		WebServiceHelper mUploadImage = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {
			
			@Override
			public void onSuccess(BASE result2) {
				boolean isFinish = false;
				if(licensePhoto.size() <= 0 || merchantPhotos.size() <= 0){
					photoUploadDone = false;
					isFinish = true;
				}else if(photoUploadDone){
					photoUploadDone = false;
					isFinish = true;
				}else{
					photoUploadDone = true;
				}
				if(isFinish){
					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.regist_success);
					AlertHelper.getInstance(getActivity()).hideLoading();
					etFullname.setText("");
					etSinglename.setText("");
					etUsername.setText("");
					etAddress.setText("");
					etContactname.setText("");
					etPhonenum.setText("");
					etTelnum.setText("");
					etDesc.setText("");
					((MainActivity)getActivity()).jump(1);
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				photoUploadDone = false;
				AlertHelper.getInstance(getActivity()).showCenterToast(R.string.regist_error);
				AlertHelper.getInstance(getActivity()).hideLoading();
			}
		}, getActivity());
		String imageType = isZZ ? WebServiceHelper.IMAGE_TYPE_ZZ : WebServiceHelper.IMAGE_TYPE_SJ;
		mUploadImage.saveImage(bitmap, imageName, imageType, id);
	}
	
	public void sjnameExist(String sjname, final boolean isFullName){
		WebServiceHelper mSjNameExistWebService = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

			@Override
			public void onSuccess(BASE result) {
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				switch (errorNo) {
				case 207:
					if(isFullName){
						AlertHelper.getInstance(getActivity()).showCenterToast("商家全称重复");
					}else{
						AlertHelper.getInstance(getActivity()).showCenterToast("注册账户重复");
					}
					break;
				default:
					break;
				}
			}
		}, getActivity());
		mSjNameExistWebService.sjnameExist(sjname, isFullName);
	}
	
	private void findView(){
		gvMerchantPhoto = (GridView) rootView.findViewById(R.id.gv_merchant_photo);
		gvLicensePhoto = (GridView) rootView.findViewById(R.id.gv_license_photo);
//		gvServices = (GridView) rootView.findViewById(R.id.gv_services);
		spProvince = (Spinner) rootView.findViewById(R.id.sp_province);
		spCity = (Spinner) rootView.findViewById(R.id.sp_city);
		spCounty = (Spinner) rootView.findViewById(R.id.sp_county);
		tvSelectBrand = (TextView) rootView.findViewById(R.id.tv_select_brand);
		tvSelectFwClass = (TextView) rootView.findViewById(R.id.tv_select_fwclass);
		etFullname = (TextView) rootView.findViewById(R.id.et_fullname);
		etSinglename = (TextView) rootView.findViewById(R.id.et_singlename);
		etUsername = (TextView) rootView.findViewById(R.id.et_username);
		etAddress = (TextView) rootView.findViewById(R.id.et_address);
		etContactname = (TextView) rootView.findViewById(R.id.et_contactname);
		etPhonenum = (TextView) rootView.findViewById(R.id.et_phonenum);
		etTelnum = (TextView) rootView.findViewById(R.id.et_telnum);
		etDesc = (TextView) rootView.findViewById(R.id.et_desc);
		btnSubmit = (Button) rootView.findViewById(R.id.btn_submit);
	}
}
