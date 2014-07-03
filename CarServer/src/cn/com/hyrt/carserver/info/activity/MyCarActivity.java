package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.FullListView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.info.adapter.MyCarAdapter;

public class MyCarActivity extends BaseActivity{
	
	@ViewInject(id=R.id.lv_my_car) FullListView lvMyCar;
	@ViewInject(id=R.id.tv_noData) TextView tvNoData;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.btn_add_car,click="addCar") ImageView btnAddCar;
	
	private MyCarAdapter mAdapter;
	private Define.INFO_CAR_LIST cars;
	private WebServiceHelper mWebServiceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_car);
		ptrv.disableScroolUp();
		loadData();
		
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INFO_CAR_LIST>() {

						@Override
						public void onSuccess(INFO_CAR_LIST result) {
							ptrv.onHeaderRefreshComplete();
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(MyCarActivity.this).hideLoading();
							if(result == null || result.data.size() <= 0){
								tvNoData.setVisibility(View.VISIBLE);
								lvMyCar.setVisibility(View.GONE);
							}else{
								tvNoData.setVisibility(View.GONE);
								lvMyCar.setVisibility(View.VISIBLE);
							}
							
							cars = result;
							if(mAdapter == null){
								mAdapter = new MyCarAdapter(cars, MyCarActivity.this);
								lvMyCar.setAdapter(mAdapter);
								setListener();
							}else{
								mAdapter.notifyDataSetChanged();
							}
							
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							ptrv.onHeaderRefreshComplete();
							AlertHelper.getInstance(MyCarActivity.this).hideLoading();
							tvNoData.setVisibility(View.VISIBLE);
							lvMyCar.setVisibility(View.GONE);
						}
			}, this);
		}
		mWebServiceHelper.getTerminalCarList();
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData();
			}
		});
		mAdapter.setOnClickListener(new MyCarAdapter.MyCarOnClickListener() {
			
			@Override
			public void onClick(int position) {
				LogHelper.i("tag", "Position = "+position);
				if(position==2){
					//进入添加车况页面
					AlertHelper.getInstance(getApplicationContext()).showCenterToast("正在开发中");
				}else{
					//进入我的车辆详细信息页面
					Intent it = new Intent();
					it.setClass(MyCarActivity.this, CarDetailActivity.class);
					it.putExtra("id", cars.data.get(position).id);
					startActivityForResult(it, Define.RESULT_FROM_ALTER_CAR);
				}
				}
		});
	}
	
	public void addCar(View view){
		Intent intent = new Intent();
		intent.setClass(this, AlterCarActivity.class);
		startActivityForResult(intent, Define.RESULT_FROM_ALTER_CAR);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Define.RESULT_FROM_ALTER_CAR){
			loadData();
		}
	}

}
