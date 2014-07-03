package cn.com.hyrt.carserver.emergency.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.baseFunction.Define.INSURANCE_CLAIM_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.FullListView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.emergency.adapter.InsuranceClaimAdapter;
import cn.com.hyrt.carserver.info.activity.MyCarActivity;
import cn.com.hyrt.carserver.info.adapter.MyCarAdapter;

public class InsuranceClaimActivity extends BaseActivity{
	
	@ViewInject(id=R.id.lv_insuranceclaim) FullListView lvclaim;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.tv_noData) TextView tvNoData;
	
	private WebServiceHelper mWebServiceHelper;
	private Define.INSURANCE_CLAIM_LIST icm;
	private InsuranceClaimAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_inclaim);
		loadData();
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INSURANCE_CLAIM_LIST>() {

						@Override
						public void onSuccess(INSURANCE_CLAIM_LIST result) {
							ptrv.onHeaderRefreshComplete();
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(InsuranceClaimActivity.this).hideLoading();	
							if(result == null || result.data.size() <= 0){
								tvNoData.setVisibility(View.VISIBLE);
								lvclaim.setVisibility(View.GONE);
							}else{
								tvNoData.setVisibility(View.GONE);
								lvclaim.setVisibility(View.VISIBLE);
							}
							icm = result;		
							if(mAdapter == null){
								mAdapter = new InsuranceClaimAdapter(icm, InsuranceClaimActivity.this);
								lvclaim.setAdapter(mAdapter);
								setListener();
							}else{
								mAdapter.notifyDataSetChanged();
							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							ptrv.onHeaderRefreshComplete();
							AlertHelper.getInstance(InsuranceClaimActivity.this).hideLoading();
							tvNoData.setVisibility(View.VISIBLE);
							lvclaim.setVisibility(View.GONE);
						}
			}, this);
		}
		mWebServiceHelper.getInsuranceClaim();
		
	}
	
	private void setListener(){

		loadData();
		mAdapter.setOnClickListener(new InsuranceClaimAdapter.MyIcOnClickListener() {

			@Override
			public void onClick(int position) {
				LogHelper.i("tag", "Position = "+position);
				    
					AlertHelper.getInstance(getApplicationContext()).showCenterToast(""+icm.data.get(position).sjtel);
				}
		});
		lvclaim.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Uri uri = Uri.parse("tel:" + icm.data.get(position).sjtel);
				Intent call = new Intent(Intent.ACTION_CALL, uri); 
				startActivity(call);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Define.RESULT_FROM_ALTER_CAR){
			loadData();
		}
	}

}
