package cn.com.hyrt.carserverexpert.info.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_INTEGRATION;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.PullToRefreshView;
import cn.com.hyrt.carserverexpert.info.adapter.IntegrationAdapter;

public class MyIntegrationActivity extends BaseActivity{
	
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.listview) ListView listview;
	@ViewInject(id=R.id.tv_nodata) TextView tvNoData;
	
	private List<Define.INFO_INTEGRATION.CDATA> mData 
	= new ArrayList<Define.INFO_INTEGRATION.CDATA>();
	
	private IntegrationAdapter mAdapter;
	
	private int page = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_integration);
		loadData(false);
		setListener();
	}
	
	private void loadData(final boolean isMore){
		if(isMore){
			page++;
		}else{
			page = 1;
		}
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.INFO_INTEGRATION>() {

			@Override
			public void onSuccess(INFO_INTEGRATION result) {
				ptrv.onHeaderRefreshComplete();
				ptrv.onFooterRefreshComplete();
				if(mData.size() > 0){
					if(isMore){
						if(result.data.size() <= 0){
							AlertHelper.getInstance(MyIntegrationActivity.this)
							.showCenterToast("已经加载全部");
						}
					}else{
						mData.clear();
						if(result.data.size() <= 0){
							AlertHelper.getInstance(MyIntegrationActivity.this)
							.showCenterToast("加载失败");
							listview.setVisibility(View.GONE);
							return;
						}
					}
				}else{
					if(result.data.size() <= 0){
						AlertHelper.getInstance(MyIntegrationActivity.this)
						.showCenterToast("加载失败");
						listview.setVisibility(View.GONE);
						return;
					}
				}
				listview.setVisibility(View.VISIBLE);
				mData.addAll(result.data);
				if(mAdapter == null){
					mAdapter = new IntegrationAdapter(mData, MyIntegrationActivity.this);
					listview.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				ptrv.onHeaderRefreshComplete();
				ptrv.onFooterRefreshComplete();
				if(isMore){
					if(mData.size() >= 0){
						AlertHelper.getInstance(MyIntegrationActivity.this)
						.showCenterToast("已经加载全部");
					}else{
						AlertHelper.getInstance(MyIntegrationActivity.this)
						.showCenterToast("加载失败");
						tvNoData.setVisibility(View.VISIBLE);
						listview.setVisibility(View.GONE);
						return;
					}
				}else{
					mData.clear();
					if(mData.size() <= 0){
						AlertHelper.getInstance(MyIntegrationActivity.this)
						.showCenterToast("加载失败");
						tvNoData.setVisibility(View.VISIBLE);
						listview.setVisibility(View.GONE);
						return;
					}
				}
			}
		}, this).getIntegration(page);
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData(false);
			}
		});
		
		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
			
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				loadData(true);
			}
		});
	}
}
