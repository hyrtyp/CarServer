package cn.com.hyrt.carserverseller.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_PRODUCT_LIST;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.PullToRefreshView;
import cn.com.hyrt.carserverseller.product.activity.ProductDetailActivity;
import cn.com.hyrt.carserverseller.shop.adapter.ProductListAdapter;

public class ProductListFragment extends Fragment{

	private View rootView;
	private PullToRefreshView ptrv;
	private ListView listview;
	private TextView tvNodata;
	private List<Define.INFO_PRODUCT_LIST.CDATA> mData
	= new ArrayList<Define.INFO_PRODUCT_LIST.CDATA>();
	private ProductListAdapter mAdapter;
	
	private boolean isFw;
	private int page = 1;
	
	public ProductListFragment(boolean isFw) {
		super();
		this.isFw = isFw;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_product_list, null);
		findView();
		setListener();
		loadData(false);
		return rootView;
	}
	
	private void loadData(final boolean isMore){
		if(!isMore){
			page = 1;
		}else{
			page++;
		}
		WebServiceHelper mProductListWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_PRODUCT_LIST>() {

					@Override
					public void onSuccess(INFO_PRODUCT_LIST result) {
						if(result.data.size() <= 0){
							if(mData.size() > 0){
								if(isMore){
									AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部...");
								}else{
									AlertHelper.getInstance(getActivity()).showCenterToast("刷新失败...");
								}
							}else{
								tvNodata.setVisibility(View.VISIBLE);
								listview.setVisibility(View.GONE);
							}
						}
						if(!isMore){
							mData.clear();
						}
						mData.addAll(result.data);
						if(mAdapter == null){
							mAdapter = new ProductListAdapter(mData, getActivity());
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
							tvNodata.setVisibility(View.VISIBLE);
							listview.setVisibility(View.GONE);
						}
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
					}
		}, getActivity());
		AlertHelper.getInstance(getActivity()).showLoading(null);
		if(isFw){
			mProductListWebServiceHelper.getProductInfoList(
					WebServiceHelper.PRODUCT_TYPE_FW, page);
		}else{
			mProductListWebServiceHelper.getProductInfoList(
					WebServiceHelper.PRODUCT_TYPE_SP, page);
		}
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
				Intent intent = new Intent();
				intent.setClass(getActivity(), ProductDetailActivity.class);
				intent.putExtra("vo", mData.get(position));
				intent.putExtra("isProduct", !isFw);
				startActivityForResult(intent, 101);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == 101){
			loadData(false);
		}
	}
	
	private void findView(){
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		listview = (ListView) rootView.findViewById(R.id.listview);
		tvNodata = (TextView) rootView.findViewById(R.id.tv_nodata);
	}
}
