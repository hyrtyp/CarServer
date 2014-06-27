package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;

public class AlterCarActivity extends BaseActivity{
	@ViewInject(id=R.id.btn_add_photo,click="addPhoto") ImageButton btnAddPhoto;
	@ViewInject(id=R.id.et_carnumber) EditText etCarNumber;
	@ViewInject(id=R.id.et_brand) EditText etBrand;
	@ViewInject(id=R.id.et_model) EditText etModel;
	@ViewInject(id=R.id.et_submodel) EditText etSubModel;
	@ViewInject(id=R.id.et_cartype) EditText etCarType;
	@ViewInject(id=R.id.et_mileage) EditText etMileage;
	@ViewInject(id=R.id.et_yearcheckdate) EditText etYearCheckDate;
	@ViewInject(id=R.id.et_manufacturer) EditText etManuFacturer;
	@ViewInject(id=R.id.et_insurancedate) EditText etInsuranceDate;
	@ViewInject(id=R.id.et_insurancetype) EditText etInsuranceType;
	@ViewInject(id=R.id.et_insurancenum) EditText etInsuranceNum;
	@ViewInject(id=R.id.et_insurancecompany) EditText etInsuranceCompany;
	@ViewInject(id=R.id.btn_confirm_add) Button btn_confirm_add;
	
	private boolean isAdd = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		intent.getBooleanExtra("type", true);
		if(isAdd){
			setTitle(getString(R.string.info_add_car));
		}else{
			setTitle(getString(R.string.info_change_car));
		}
		setContentView(R.layout.activity_alter_car);
	}
	
	public void addPhoto(View view){
		
	}
}
