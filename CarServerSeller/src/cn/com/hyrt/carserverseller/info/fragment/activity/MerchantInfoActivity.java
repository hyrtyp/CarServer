package cn.com.hyrt.carserverseller.info.fragment.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.adapter.CheckBoxGridAdapter;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverseller.base.view.FullGridView;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

public class MerchantInfoActivity extends BaseActivity{
	
	@ViewInject(id=R.id.gv_type) FullGridView gvType;
	@ViewInject(id=R.id.et_shopname) EditText etShopName;
	@ViewInject(id=R.id.iv_merchant_photo) ImageLoaderView ivMerchantPhoto;
	@ViewInject(id=R.id.tv_merchant_photo) TextView tvMerchantPhoto;
	@ViewInject(id=R.id.iv_license_photo) ImageLoaderView ivLicensePhoto;
	@ViewInject(id=R.id.tv_license_photo) TextView tvLicensePhoto;
	@ViewInject(id=R.id.tv_select_brand) TextView tvSelectBrand;
	@ViewInject(id=R.id.sp_province) Spinner spProvince;
	@ViewInject(id=R.id.sp_city) Spinner spCity;
	@ViewInject(id=R.id.sp_county) Spinner spCounty;
	@ViewInject(id=R.id.tv_select_position) TextView tvSelectPosition;
	@ViewInject(id=R.id.et_contact) EditText etContact;
	@ViewInject(id=R.id.et_phonenum) EditText etPhoneNum;
	@ViewInject(id=R.id.et_telnum) EditText etTelNum;
	@ViewInject(id=R.id.et_desc) EditText etDesc;
	@ViewInject(id=R.id.btn_submit) Button btnSubmit;
	
	private Bitmap merchantBitmap;
	private Bitmap licenseBitmap;
	private String merchantImgUrl;
	private String licenseImgUrl;
	private boolean isMerchantSelect;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchant_info);
		
		loadData();
		setListener();
	}

	private void loadData(){
		List<String> services = new ArrayList<String>();
		services.add("类型1");
		services.add("类型2");
		services.add("类型3");
		services.add("类型4");
		services.add("类型5");
		services.add("类型6");
		CheckBoxGridAdapter mTypeGridAdapter = new CheckBoxGridAdapter(services, this);
		gvType.setAdapter(mTypeGridAdapter);
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
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(MerchantInfoActivity.this, faceUri, 50);
		mPhotoHelper.getPhoto();
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
        }
	}
}
