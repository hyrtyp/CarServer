package cn.com.hyrt.carserverexpert.history.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.history.fragment.MaintenanceInfoFragment;
import cn.com.hyrt.carserverexpert.history.fragment.RepairInfoFragment;

public class CarStatusDetailActivity extends BaseActivity{
	
	@ViewInject(id=R.id.btn_left,click="clickLeft") Button btnLeft;
	@ViewInject(id=R.id.btn_right,click="clickRight") Button btnRight;
	
	private boolean isLeft = true;
	private String carId;
	private RepairInfoFragment mRepairInfoFragment;
	private MaintenanceInfoFragment maintenanceInfoFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		carId = getIntent().getStringExtra("carId");
		setContentView(R.layout.activity_car_status_detail);
		mRepairInfoFragment = new RepairInfoFragment(carId);
		getSupportFragmentManager().beginTransaction()
		.add(R.id.container, mRepairInfoFragment).commit();
	}
	
	
	public void clickLeft(View view){
		if(isLeft){
			return;
		}
		btnLeft.setBackgroundResource(R.drawable.bg_tab_left_focus);
		btnLeft.setTextColor(0xffffffff);
		btnRight.setBackgroundResource(R.drawable.bg_tab_right);
		btnRight.setTextColor(getResources().getColor(R.color.login_blue));
		isLeft = !isLeft;
		
		getSupportFragmentManager().beginTransaction()
		.hide(maintenanceInfoFragment).commit();
		getSupportFragmentManager().beginTransaction()
		.show(mRepairInfoFragment).commit();
	}
	
	public void clickRight(View view){
		if(!isLeft){
			return;
		}
		btnRight.setBackgroundResource(R.drawable.bg_tab_right_focus);
		btnRight.setTextColor(0xffffffff);
		btnLeft.setBackgroundResource(R.drawable.bg_tab_left);
		btnLeft.setTextColor(getResources().getColor(R.color.login_blue));
		isLeft = !isLeft;
		
		getSupportFragmentManager().beginTransaction().hide(mRepairInfoFragment).commit();
		if(maintenanceInfoFragment == null){
			maintenanceInfoFragment = new MaintenanceInfoFragment(carId);
			getSupportFragmentManager().beginTransaction()
			.add(R.id.container, maintenanceInfoFragment).commit();
		}else{
			getSupportFragmentManager().beginTransaction()
			.show(maintenanceInfoFragment).commit();
		}
	}
}
