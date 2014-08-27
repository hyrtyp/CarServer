package cn.com.hyrt.carserversurvey.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.view.FullListView;
import cn.com.hyrt.carserversurvey.base.view.PullToRefreshView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.REGRECODE;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.info.adapter.RegRecodeAdapter;

public class RegRecodeActivity  extends BaseActivity{
	
	@ViewInject(id=R.id.lv_insuranceclaim) FullListView lvclaim;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.tv_noData) TextView tvNoData;

	private Define.REGRECODE recode ; 
	private RegRecodeAdapter recodeAdapter;
	private WebServiceHelper mwebserviceHelper;
	private int pageNo = 1;
	private boolean isLoadMore = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regrecode);
		loadData();
		setListener();
	}
	
	private void setListener() {
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				isLoadMore = false;
				loadData();
			}
		});

		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {

			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				isLoadMore = true;
				loadData();
			}
		});
		
		lvclaim.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				LogHelper.i("tag", "lvclaim:"+position);
				Intent intent = new Intent();
				intent.setClass(RegRecodeActivity.this, MerchantInfoActivity.class);
				startActivity(intent);
			}
			
		});
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		String id = ((CarServerApplication)getApplicationContext()).getLoginInfo().id;
		if(mwebserviceHelper == null){
			mwebserviceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.REGRECODE>() {

						@Override
						public void onSuccess(REGRECODE result) {
							ptrv.onHeaderRefreshComplete();
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(RegRecodeActivity.this).hideLoading();	
							if(result == null || result.data.size() <= 0){
								tvNoData.setVisibility(View.VISIBLE);
								lvclaim.setVisibility(View.GONE);
							}else{
								tvNoData.setVisibility(View.GONE);
								lvclaim.setVisibility(View.VISIBLE);
							}
							recode = result;		
							if(recodeAdapter == null){
								recodeAdapter = new RegRecodeAdapter(recode, RegRecodeActivity.this);
								lvclaim.setAdapter(recodeAdapter);
							}else{
								recodeAdapter.notifyDataSetChanged();
							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							ptrv.onHeaderRefreshComplete();
							AlertHelper.getInstance(RegRecodeActivity.this).hideLoading();
							tvNoData.setVisibility(View.VISIBLE);
							lvclaim.setVisibility(View.GONE);
						}
			}, this);
		}
		
		if (isLoadMore) {
			pageNo++;
		} else {
			pageNo = 1;
		}
		mwebserviceHelper.getRegRecode(id,pageNo);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Define.RESULT_FROM_ALTER_CAR){
			loadData();
		}
	}

}
