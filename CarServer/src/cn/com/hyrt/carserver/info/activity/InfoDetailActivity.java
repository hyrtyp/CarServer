package cn.com.hyrt.carserver.info.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.activity.MainActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;

public class InfoDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.iv_face_img) ImageLoaderView ivFaceImg;
	@ViewInject(id=R.id.tv_username) TextView tvUserName;
	@ViewInject(id=R.id.tv_phonenum) TextView tvPhoneNum;
	@ViewInject(id=R.id.layout_star) LinearLayout layoutStar;
	@ViewInject(id=R.id.tv_viplevel) TextView tvVipLevel;
	@ViewInject(id=R.id.lv_car) ListView lvCar;
	@ViewInject(id=R.id.btn_change_info,click="changeInfo") Button btnChangeInfo;
	
	private WebServiceHelper mUserInfoWebServiceHelper;
	private WebServiceHelper mWebServiceHelper;
	private List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_detail);
		AlertHelper.getInstance(this).showLoading(null);
		loadUserInfo();
	}
	
	public void changeInfo(View view){
		Intent intent = new Intent();
		intent.setClass(this, ChangeInfoActivity.class);
		startActivityForResult(intent, Define.RESULT_FROM_CHANGE_INFO);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == Define.RESULT_FROM_CHANGE_INFO){
			AlertHelper.getInstance(this).showLoading(null);
			loadUserInfo();
		}
	}
	
	private void loadData() {
		String level = CarServerApplication.info.level;
		tvUserName.setText(CarServerApplication.info.unitname);
		tvPhoneNum.setText(CarServerApplication.info.phone);
		tvVipLevel.setText(level+"级");
		ivFaceImg.setImageUrl(CarServerApplication.info.imagepath);
		if(level == null || "".equals(level)){
			layoutStar.setVisibility(View.GONE);
			tvVipLevel.setVisibility(View.GONE);
		}else{
			int levelNum = Integer.parseInt(level);
			if(levelNum > 0){
				layoutStar.removeAllViews();
				for(int i=0,j=Integer.parseInt(level); i<j; i++){
					ImageView iv = new ImageView(this);
					iv.setImageResource(R.drawable.ic_star);
					layoutStar.addView(iv);
				}
				tvVipLevel.setVisibility(View.VISIBLE);
				layoutStar.setVisibility(View.VISIBLE);
			}
			
		}
		
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INFO_CAR_LIST>() {

						@Override
						public void onSuccess(INFO_CAR_LIST result) {
							AlertHelper.getInstance(InfoDetailActivity.this).hideLoading();
							data.clear();
							for(int i=0,j=result.data.size(); i<j; i++){
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("model", result.data.get(i).model);
								map.put("id", result.data.get(i).id);
								data.add(map);
							}
							String[] from = new String[]{"model"};
							int[] to = new int[]{R.id.tv_model};
							SimpleAdapter mAdapter = new SimpleAdapter(
									InfoDetailActivity.this,
									data, R.layout.layout_car_item,
									from, to);
							lvCar.setAdapter(mAdapter);
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_CHANGE_INFO);
							finish();
						}
					}, this);
		}
		mWebServiceHelper.getTerminalCarList();
	}
	
	private void loadUserInfo(){
		if(mUserInfoWebServiceHelper == null){
			mUserInfoWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INFO>() {

						@Override
						public void onSuccess(INFO result) {
							CarServerApplication.info = result;
							CarServerApplication.loginInfo.id = result.id;
							CarServerApplication.loginInfo.loginname = result.unitname;
							CarServerApplication.loginInfo.name = result.name;
							StorageHelper.getInstance(InfoDetailActivity.this).saveLoginInfo(CarServerApplication.loginInfo);
							loadData();
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(InfoDetailActivity.this).showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_CHANGE_INFO);
							finish();
							
						}
			}, this);
		}
		mUserInfoWebServiceHelper.getUserInfo();
	}
	
	@Override
	public void finish() {
		setResult(Define.RESULT_FROM_CHANGE_INFO);
		super.finish();
	}
}
