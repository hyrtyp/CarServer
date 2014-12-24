package cn.com.hyrt.carserversurvey.info.activity;

import java.util.ArrayList;
import java.util.List;

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

/**
 * 注册记录
 * @author zoe
 *
 */
public class RegRecodeActivity  extends BaseActivity{
	
	@ViewInject(id=R.id.lv_insuranceclaim) FullListView lvclaim;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.tv_noData) TextView tvNoData;

	private List<Define.REGRECODE.CDATA> recodes = new ArrayList<Define.REGRECODE.CDATA>();
	private RegRecodeAdapter recodeAdapter;
	private int pageNo = 1;
	private boolean isLoadMore = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regrecode);
		isLoadMore = false;
		recodeAdapter = null;
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
				intent.putExtra("id", recodes.get(position).id);
				startActivity(intent);
			}
			
		});
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		String id = ((CarServerApplication)getApplicationContext()).getLoginInfo().id;
		WebServiceHelper mwebserviceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.REGRECODE>() {

						@Override
						public void onSuccess(REGRECODE result) {
							ptrv.onHeaderRefreshComplete();
							ptrv.onFooterRefreshComplete();
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(RegRecodeActivity.this).hideLoading();	
							if(result == null || result.data.size() <= 0){
								if (recodes.size()>0) {
									if (isLoadMore) {
										AlertHelper.getInstance(getApplicationContext()).showCenterToast("已加载全部...");
									} else {
										AlertHelper.getInstance(getApplicationContext()).showCenterToast("刷新失败...");
									}
								}else{
									tvNoData.setVisibility(View.VISIBLE);
									lvclaim.setVisibility(View.GONE);
								}
							}
							if (!isLoadMore) {
								recodes.clear();
							}
							recodes.addAll(result.data);
							LogHelper.i("tag", "results:"+recodes.size()+"__"+recodes);
							if(recodeAdapter == null){
								recodeAdapter = new RegRecodeAdapter(recodes, RegRecodeActivity.this);
								lvclaim.setAdapter(recodeAdapter);
							}else{
								recodeAdapter.notifyDataSetChanged();
							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							if (recodes.size()>0) {
								if (isLoadMore) {
									AlertHelper.getInstance(getApplicationContext()).showCenterToast("已加载全部...");
								} else {
									AlertHelper.getInstance(getApplicationContext()).showCenterToast("刷新失败...");
								}
							}else{
								tvNoData.setVisibility(View.VISIBLE);
								lvclaim.setVisibility(View.GONE);
							}
							ptrv.onHeaderRefreshComplete();
							ptrv.onFooterRefreshComplete();
							AlertHelper.getInstance(RegRecodeActivity.this).hideLoading();
						}
			}, this);
		
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
