package cn.com.hyrt.carserver.info.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioGroup;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.info.fragment.AlterInsuranceInfoFragment;
import cn.com.hyrt.carserver.info.fragment.AlterMaintenanceInfoFragment;
import cn.com.hyrt.carserver.info.fragment.AlterRepairInfoFragment;
import cn.com.hyrt.carserver.info.fragment.AlterYearCheckInfoFragment;
import cn.com.hyrt.carserver.info.fragment.InsuranceInfoFragment;
import cn.com.hyrt.carserver.info.fragment.MaintenanceInfoFragment;
import cn.com.hyrt.carserver.info.fragment.RepairInfoFragment;
import cn.com.hyrt.carserver.info.fragment.YearCheckInfoFragment;

public class AlterConditionActivity extends BaseActivity{

	private String carid;
	
	private int curIndex;
	private List<Fragment> fragments = new ArrayList<Fragment>();
	@ViewInject(id=R.id.rg_tab) RadioGroup rgTab;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		carid = intent.getStringExtra("id");
		
		if(carid == null || "".equals(carid)){
			errorExit();
		}
		
		
		setContentView(R.layout.activity_car_condition);
		
		setListener();
		
		AlterRepairInfoFragment mAlterRepairInfoFragment = new AlterRepairInfoFragment();
		mAlterRepairInfoFragment.setCarid(carid);
		fragments.add(mAlterRepairInfoFragment);
		
		AlterMaintenanceInfoFragment mAlterMaintenanceInfoFragment = new AlterMaintenanceInfoFragment();
		mAlterMaintenanceInfoFragment.setCarid(carid);
		fragments.add(mAlterMaintenanceInfoFragment);
		
		AlterYearCheckInfoFragment mAlterYearCheckInfoFragment = new AlterYearCheckInfoFragment();
		mAlterYearCheckInfoFragment.setCarid(carid);
		fragments.add(mAlterYearCheckInfoFragment);
		
		AlterInsuranceInfoFragment mAlterInsuranceInfoFragment = new AlterInsuranceInfoFragment();
		mAlterInsuranceInfoFragment.setCarid(carid);
		fragments.add(mAlterInsuranceInfoFragment);
		
//		MaintenanceInfoFragment maintenanceInfoFragment = new MaintenanceInfoFragment();
//		maintenanceInfoFragment.setCarid(carid);
//		fragments.add(maintenanceInfoFragment);
//		
//		YearCheckInfoFragment mYearCheckInfoFragment = new YearCheckInfoFragment();
//		mYearCheckInfoFragment.setCarid(carid);
//		fragments.add(mYearCheckInfoFragment);
//		
//		InsuranceInfoFragment mInsuranceInfoFragment = new InsuranceInfoFragment();
//		mInsuranceInfoFragment.setCarid(carid);
//		fragments.add(mInsuranceInfoFragment);
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.layout_content, mAlterRepairInfoFragment);
		ft.commit();
	}
	
	private void setListener(){
		rgTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup arg0, int position) {
				switch (position) {
				case R.id.btn_wxxx:
					changeFragment(0);
					break;
				case R.id.btn_byxx:
					changeFragment(1);
					break;
				case R.id.btn_njxx:
					changeFragment(2);
					break;
				case R.id.btn_bxxx:
					changeFragment(3);
					break;

				default:
					break;
				}
			}
		});
	}
	
	private void changeFragment(int position){
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment fragment = fragments.get(position);
		getCurrentFragment().onPause();
		if(fragment.isAdded()){
//          fragment.onStart(); // 启动目标tab的onStart()
          fragment.onResume(); // 启动目标tab的onResume()
          
      }else{
          ft.add(R.id.layout_content, fragment);
      }
		ft.hide(fragments.get(curIndex));
        ft.show(fragments.get(position));
        ft.commit();
        curIndex = position;
	}
	
	public Fragment getCurrentFragment(){
        return fragments.get(curIndex);
    }
	
	private void errorExit(){
		AlertHelper.getInstance(AlterConditionActivity.this)
		.showCenterToast(R.string.info_load_fail);
		finish();
	}
}
