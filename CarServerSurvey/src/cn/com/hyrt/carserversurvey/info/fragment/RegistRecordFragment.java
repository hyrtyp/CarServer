package cn.com.hyrt.carserversurvey.info.fragment;

import net.tsz.afinal.annotation.view.ViewInject;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.REGRECODE;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.FullListView;
import cn.com.hyrt.carserversurvey.base.view.PullToRefreshView;
import cn.com.hyrt.carserversurvey.info.activity.MerchantInfoActivity;
import cn.com.hyrt.carserversurvey.info.activity.RegRecodeActivity;
import cn.com.hyrt.carserversurvey.info.adapter.RegRecodeAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

public class RegistRecordFragment extends Fragment{

	private View rootView;
	private FullListView lvclaim;
	private PullToRefreshView ptrv;
	private TextView tvNoData;

	private Define.REGRECODE recode ; 
	private RegRecodeAdapter recodeAdapter;
	private WebServiceHelper mwebserviceHelper;
	private int pageNo = 1;
	private boolean isLoadMore = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_regrecode, null);
		findView();
		loadData();
		setListener();
		return rootView;
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
				intent.setClass(getActivity(), MerchantInfoActivity.class);
				intent.putExtra("id", recode.data.get(position).id);
				startActivity(intent);
			}
			
		});
	}
	
	private void loadData(){
		AlertHelper.getInstance(getActivity()).showLoading(getString(R.string.loading_msg));
		String id = ((CarServerApplication)getActivity().getApplicationContext()).getLoginInfo().id;
		if(mwebserviceHelper == null){
			mwebserviceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.REGRECODE>() {

						@Override
						public void onSuccess(REGRECODE result) {
							ptrv.onHeaderRefreshComplete();
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(getActivity()).hideLoading();	
							if(result == null || result.data.size() <= 0){
								tvNoData.setVisibility(View.VISIBLE);
								lvclaim.setVisibility(View.GONE);
							}else{
								tvNoData.setVisibility(View.GONE);
								lvclaim.setVisibility(View.VISIBLE);
							}
							recode = result;		
							if(recodeAdapter == null){
								recodeAdapter = new RegRecodeAdapter(recode, getActivity());
								lvclaim.setAdapter(recodeAdapter);
							}else{
								lvclaim.setAdapter(recodeAdapter);
								recodeAdapter.notifyDataSetChanged();
							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							ptrv.onHeaderRefreshComplete();
							AlertHelper.getInstance(getActivity()).hideLoading();
							tvNoData.setVisibility(View.VISIBLE);
							lvclaim.setVisibility(View.GONE);
						}
			}, getActivity());
		}
		
		if (isLoadMore) {
			pageNo++;
		} else {
			pageNo = 1;
		}
		mwebserviceHelper.getRegRecode(id,pageNo);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == Define.RESULT_FROM_ALTER_CAR){
//			loadData();
//		}
	}
	
	private void findView(){
		lvclaim = (FullListView) rootView.findViewById(R.id.lv_insuranceclaim);
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		tvNoData = (TextView) rootView.findViewById(R.id.tv_noData);
	}
}
