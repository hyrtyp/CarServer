package cn.com.hyrt.carserversurvey.regist.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.adapter.AddPhotoGridAdapter;
import cn.com.hyrt.carserversurvey.base.adapter.CheckBoxGridAdapter;
import cn.com.hyrt.carserversurvey.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.regist.adapter.BrandCheckAdapter;

public class RegistMerchantInfoFragment extends Fragment{

	private View rootView;
	private GridView gvMerchantPhoto;
	private GridView gvLicensePhoto;
	private GridView gvServices;
	private Spinner spProvince;
	private Spinner spCity;
	private Spinner spCounty;
	private TextView tvSelectBrand;
	
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_regist_merchant, null);
		findView();
		loadData();
		
		CheckBoxGridAdapter mCheckBoxGridAdapter = new CheckBoxGridAdapter(services, getActivity());
		gvServices.setAdapter(mCheckBoxGridAdapter);
		
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
				if(position <= 0 || curProvinceIndex < 0){
					return;
				}
				List<Map<String, String>> mCountys = addressThreeList.get(curProvinceIndex).get(position-1);
				
				countyId.clear();
				countyName.clear();
				countyId.add("-1");
				countyName.add("县");
				
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
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 50);
		mPhotoHelper.getPhoto();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AlertHelper.getInstance(getActivity()).showCenterToast("onActivityResult:"+resultCode);
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
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }
	}
	
	private void showBrand(){
		if(mBrandDialog == null){
			mBrandDialog = new Dialog(getActivity(), R.style.MyDialog);
			mBrandDialog.setContentView(R.layout.layout_list_popup);
			mBrandDialog.getWindow().setLayout(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			mBrandDialog.getWindow().findViewById(R.id.view_bg)
			.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					mBrandDialog.dismiss();
					mBrandDialog = null;
				}
			});
			
			ListView listview = (ListView) mBrandDialog
					.getWindow().findViewById(R.id.listview);
			BrandCheckAdapter mBrandCheckAdapter = new BrandCheckAdapter(
					oneList, twoList, getActivity());
			listview.setAdapter(mBrandCheckAdapter);
		}
		mBrandDialog.show();
	}
	
	private void findView(){
		gvMerchantPhoto = (GridView) rootView.findViewById(R.id.gv_merchant_photo);
		gvLicensePhoto = (GridView) rootView.findViewById(R.id.gv_license_photo);
		gvServices = (GridView) rootView.findViewById(R.id.gv_services);
		spProvince = (Spinner) rootView.findViewById(R.id.sp_province);
		spCity = (Spinner) rootView.findViewById(R.id.sp_city);
		spCounty = (Spinner) rootView.findViewById(R.id.sp_county);
		tvSelectBrand = (TextView) rootView.findViewById(R.id.tv_select_brand);
	}
}
