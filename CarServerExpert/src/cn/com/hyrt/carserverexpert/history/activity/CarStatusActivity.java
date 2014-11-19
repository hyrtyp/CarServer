package cn.com.hyrt.carserverexpert.history.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_CAR_STATUS_LIST;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.PullToRefreshView;
import cn.com.hyrt.carserverexpert.history.adapter.CarStatusAdapter;

public class CarStatusActivity extends BaseActivity{

	@ViewInject(id=R.id.listview) ListView listview;
	@ViewInject(id=R.id.tv_nodata) TextView tvNoData;
	private String id;
	
	private CarStatusAdapter mAdapter;
	private List<Define.INFO_CAR_STATUS_LIST.CDATA> mData 
	= new ArrayList<Define.INFO_CAR_STATUS_LIST.CDATA>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		id = getIntent().getStringExtra("id");
		setContentView(R.layout.activity_car_status);
		loadData();
		setListener();
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.INFO_CAR_STATUS_LIST>() {

			@Override
			public void onSuccess(INFO_CAR_STATUS_LIST result) {
				AlertHelper.getInstance(CarStatusActivity.this).hideLoading();
				mData.clear();
				mData.addAll(result.data);
				if(mData.size() <= 0){
					tvNoData.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
				}else{
					tvNoData.setVisibility(View.GONE);
					listview.setVisibility(View.VISIBLE);
				}
				if(mAdapter == null){
					mAdapter = new CarStatusAdapter(mData, CarStatusActivity.this);
					listview.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				tvNoData.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
			}
		}, this).getCarStatusList(id);
	}
	
	private void setListener(){
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(CarStatusActivity.this, CarStatusDetailActivity.class);
				intent.putExtra("carId", mData.get(position).id);
				startActivity(intent);
			}
		});
	}
}
