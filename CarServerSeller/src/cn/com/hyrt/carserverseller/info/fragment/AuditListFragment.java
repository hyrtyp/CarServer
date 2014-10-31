package cn.com.hyrt.carserverseller.info.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_AUDIT_LIST;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.PullToRefreshView;
import cn.com.hyrt.carserverseller.info.adapter.AuditAdapter;
import cn.com.hyrt.carserverseller.preferential.activity.PreferentialDetailActivity;
import cn.com.hyrt.carserverseller.product.activity.ProductDetailActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class AuditListFragment extends Fragment{

	private View rootView;
	private PullToRefreshView ptrv;
	private ListView listview;
	private TextView tvNoData;
	private List<Define.INFO_AUDIT_LIST.CDATA> mData 
	= new ArrayList<Define.INFO_AUDIT_LIST.CDATA>();
	private AuditAdapter mAdapter;
	
	private String type;
	private int page = 1;
	
	public AuditListFragment(String type) {
		super();
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_product_list, null);
		findView();
		loadData(false);
		setListener();
		return rootView;
	}
	
	public void loadData(final boolean isMore){
		if(!isMore){
			page = 1;
		}else{
			page++;
		}
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_AUDIT_LIST>() {

					@Override
					public void onSuccess(INFO_AUDIT_LIST result) {
						if(result.data.size() <= 0){
							if(mData.size() > 0){
								if(isMore){
									AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部...");
								}else{
									AlertHelper.getInstance(getActivity()).showCenterToast("刷新失败...");
								}
							}else{
								tvNoData.setVisibility(View.VISIBLE);
								listview.setVisibility(View.GONE);
							}
						}
						if(!isMore){
							mData.clear();
						}
						mData.addAll(result.data);
						if(mAdapter == null){
							mAdapter = new AuditAdapter(getActivity(), mData);
							listview.setAdapter(mAdapter);
						}else{
							mAdapter.notifyDataSetChanged();
						}
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
						AlertHelper.getInstance(getActivity()).hideLoading();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						if(mData.size() > 0){
							if(isMore){
								AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部...");
							}else{
								AlertHelper.getInstance(getActivity()).showCenterToast("刷新失败...");
							}
						}else{
							tvNoData.setVisibility(View.VISIBLE);
							listview.setVisibility(View.GONE);
						}
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
					}
		}, getActivity());
		AlertHelper.getInstance(getActivity()).showLoading(null);
		mWebServiceHelper.getAuditInfoList(type, page);
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
		
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), ProductDetailActivity.class);
//				intent.putExtra("vo", mData.get(position));
//				intent.putExtra("isProduct", !isFw);
//				startActivityForResult(intent, 101);
				
				if(WebServiceHelper.PRODUCT_TYPE_SP.equals(type)
						|| WebServiceHelper.PRODUCT_TYPE_FW.equals(type)){
					Intent intent = new Intent();
					intent.setClass(getActivity(), ProductDetailActivity.class);
					intent.putExtra("productId", mData.get(position).businessid);
					boolean isProduct = WebServiceHelper.PRODUCT_TYPE_SP.equals(type);
					intent.putExtra("isProduct", isProduct);
					startActivityForResult(intent, 101);
				}else{
					Intent intent = new Intent();
					intent.setClass(getActivity(), PreferentialDetailActivity.class);
					startActivity(intent);
				}
			}
		});
	}
	
	private void findView(){
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		listview = (ListView) rootView.findViewById(R.id.listview);
		tvNoData = (TextView) rootView.findViewById(R.id.tv_nodata);
	}
}
