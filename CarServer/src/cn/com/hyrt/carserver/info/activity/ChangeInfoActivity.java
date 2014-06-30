package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class ChangeInfoActivity extends BaseActivity{
	
	private static final String TAG = "ChangeInfoActivity";
	@ViewInject(id=R.id.et_username) EditText etUserName;
	@ViewInject(id=R.id.et_old_pwd) EditText etOldPwd;
	@ViewInject(id=R.id.et_new_pwd) EditText etNewPwd;
	@ViewInject(id=R.id.et_confirm_pwd) EditText etConfirmPwd;
	@ViewInject(id=R.id.btn_change_face,click="changeFace") TextView btnChangeFace;
	@ViewInject(id=R.id.iv_face_img) ImageView ivFaceImg;
	@ViewInject(id=R.id.lv_car) ListView lvCar;
	@ViewInject(id=R.id.btn_add_car,click="addCar") ImageView btnAddCar;
	
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_info);
		if(CarServerApplication.loginInfo == null){
			CarServerApplication.loginInfo = StorageHelper.getInstance(this).getLoginInfo();
		}
		etUserName.setText(CarServerApplication.loginInfo.name);
		loadData();
	}
	
	private void loadData() {
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO_CAR_LIST>() {

					@Override
					public void onSuccess(INFO_CAR_LIST result) {
						LogHelper.i("tag", "result:"+result.data.get(2));
						List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
						for(int i=0,j=result.data.size(); i<j; i++){
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("model", result.data.get(i).model);
							data.add(map);
						}
						String[] from = new String[]{"model"};
						int[] to = new int[]{R.id.tv_model};
						SimpleAdapter mAdapter = new SimpleAdapter(
								ChangeInfoActivity.this,
								data, R.layout.layout_car_item,
								from, to);
						lvCar.setAdapter(mAdapter);
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {

					}
				}, this);
		LogHelper.i("tag", "CarServerApplication.loginInfo:"
				+ CarServerApplication.loginInfo);
		mWebServiceHelper.getTerminalCarList();
	}
	
	public void addCar(View view){
		Intent intent = new Intent();
		intent.setClass(this, AlterCarActivity.class);
		startActivityForResult(intent, Define.RESULT_FROM_ALTER_CAR);
	}
	
	public void changeFace(View view){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("carserver", "face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(this, faceUri, 50);
		mPhotoHelper.getPhoto();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ivFaceImg.setImageBitmap(bitmap);
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                String uploadBuffer = new String(Base64.encode(baos.toByteArray()));

            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("cnp", "face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(ChangeInfoActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }
	}

}
