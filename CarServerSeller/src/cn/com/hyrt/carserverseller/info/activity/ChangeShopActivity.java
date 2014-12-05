package cn.com.hyrt.carserverseller.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_MERCHANT;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

/**
 * 修改店铺
 * @author zoe
 *
 */
public class ChangeShopActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_shopname) EditText etShopName;
	@ViewInject(id=R.id.iv_sjphoto,click="addSjPhoto") ImageLoaderView ivSjPhoto;
	@ViewInject(id=R.id.iv_zzphoto,click="addZzPhoto") ImageLoaderView ivZzPhoto;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	
	private boolean isMerchantSelect;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private Bitmap merchantBitmap;
	private Bitmap licenseBitmap;
	private Define.INFO_MERCHANT mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_shop);
		Intent intent = getIntent();
		mData = (INFO_MERCHANT) intent.getSerializableExtra("vo");
		loadData();
	}
	
	private void loadData(){
		Define.INFO_MERCHANT.CDATA merchantInfo = mData.data.get(0);
		etShopName.setText(merchantInfo.sjname);
		ivSjPhoto.setImageUrl(merchantInfo.imagepath);
		ivZzPhoto.setImageUrl(merchantInfo.zzimagepath);
	}
	
	public void addSjPhoto(View view){
		isMerchantSelect = true;
		addPhoto();
	}
	
	public void addZzPhoto(View view){
		isMerchantSelect = false;
		addPhoto();
	}
	
	private void addPhoto(){
		if (FileHelper.sdCardExist()) {
			if(faceUri == null){
				faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
			}
			mPhotoHelper = new PhotoHelper(this, faceUri, 50);
			mPhotoHelper.getPhoto();
		}else{
			AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
		}
		/*if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(this, faceUri, 50);
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
//                	merchantPhotos.add(bitmap);
//                	merchantAdapter.notifyDataSetChanged();
                	merchantBitmap = bitmap;
                	ivSjPhoto.setImageBitmap(bitmap);
                }else{
                	licenseBitmap = bitmap;
                	ivZzPhoto.setImageBitmap(bitmap);
//                	licensePhoto.add(bitmap);
//                	licenseAdapter.notifyDataSetChanged();
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
                    mPhotoHelper = new PhotoHelper(ChangeShopActivity.this, faceUri, 50);
                }
                mPhotoHelper.startPhotoZoom(faceUri, 50);
			}else{
				AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
			}
            /*if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(ChangeShopActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);*/
        }
	}
	
	public void submit(View view){
		
	}
}
