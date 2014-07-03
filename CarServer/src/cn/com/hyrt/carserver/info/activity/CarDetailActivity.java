package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

/**
 * 车辆详情
 * @author zoe
 *
 */
public class CarDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.tv_carnumber) TextView tv_carnumber;
	@ViewInject(id=R.id.tv_brand) TextView tv_brand;
	@ViewInject(id=R.id.tv_model) TextView tv_model;
	@ViewInject(id=R.id.tv_submodel) TextView tv_submodel;
	@ViewInject(id=R.id.tv_cartype) TextView tv_cartype;
	@ViewInject(id=R.id.tv_mileage) TextView tv_mileage;
	@ViewInject(id=R.id.tv_yearcheckdate) TextView tv_yearcheckdate;
	@ViewInject(id=R.id.tv_manufacturer) TextView tv_manufacturer;
	@ViewInject(id=R.id.tv_insurancedate) TextView tv_insurancedate;
	@ViewInject(id=R.id.tv_insurancetype) TextView tv_insurancetype;
	@ViewInject(id=R.id.tv_insurancenum) TextView tv_insurancenum;
	@ViewInject(id=R.id.tv_insurancecompany) TextView tv_insurancecompany;
	@ViewInject(id=R.id.tv_carname) TextView tv_carname;
	@ViewInject(id=R.id.tv_addtime) TextView tv_addtime;
	
	private String carid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_detail);
		Intent intent = getIntent();
		carid = intent.getStringExtra("id");
		if(carid == null || "".equals(carid)){
			errorExit();
		}
		AlertHelper.getInstance(this).showLoading(null);
		loadData();
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO_CAR>() {

					@Override
					public void onSuccess(INFO_CAR result) {
						setData(result);
						AlertHelper.getInstance(CarDetailActivity.this).hideLoading();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						errorExit();
					}
		}, this);
		mWebServiceHelper.getCarInfo(carid);
	}
	
	private void setData(Define.INFO_CAR car){
		tv_carnumber.setText(car.carnumber);
		tv_brand.setText(car.brand);
		tv_model.setText(car.model);
		tv_submodel.setText(car.submodel);
		tv_cartype.setText(car.cartype);
		tv_mileage.setText(car.mileage);
		tv_yearcheckdate.setText(StringHelper.formatDate(car.yearcheckdate));
		tv_manufacturer.setText(car.manufacturer);
		tv_insurancedate.setText(StringHelper.formatDate(car.insurancedate));
		tv_insurancetype.setText(car.insurancetype);
		tv_insurancenum.setText(car.insurancenum);
		tv_insurancecompany.setText(car.insurancecompany);
		tv_addtime.setText(car.checkdate);
		tv_carname.setText(car.brand);
	}
	
	private void errorExit(){
		AlertHelper.getInstance(CarDetailActivity.this)
		.showCenterToast(R.string.info_load_fail);
		finish();
	}
	
}
