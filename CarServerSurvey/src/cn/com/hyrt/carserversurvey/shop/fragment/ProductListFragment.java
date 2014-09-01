package cn.com.hyrt.carserversurvey.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.INFO_PRODUCT_LIST;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.PullToRefreshView;
import cn.com.hyrt.carserversurvey.product.activity.ProductDetailActivity;
import cn.com.hyrt.carserversurvey.shop.adapter.ProductListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ProductListFragment extends Fragment{
	
	private View rootView;
	private PullToRefreshView ptrv;
	private ListView lvProduct;
	private TextView tv_noData;
	
	private ProductListAdapter mProductListAdapter;
	
	private int pageNo = 1;
	private boolean isLoadMore = false;
	private WebServiceHelper mWebServiceHelper;
	
	private boolean isSp = true;
	
	private List<Define.INFO_PRODUCT> products = new ArrayList<Define.INFO_PRODUCT>();
	
	public ProductListFragment(boolean isSp) {
		super();
		this.isSp = isSp;
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
		
		lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ProductDetailActivity.class);
				intent.putExtra("id", products.get(position).id);
				intent.putExtra("isSp", isSp);
				intent.putExtra("isFromAdd", false);
				startActivity(intent);
			}
		});
	}
	
	public void loadData(boolean loadMore){
		AlertHelper.getInstance(getActivity()).showLoading(null);
		isLoadMore = loadMore;
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new BaseWebServiceHelper.RequestCallback<Define.INFO_PRODUCT_LIST>() {
						
						@Override
						public void onSuccess(INFO_PRODUCT_LIST result) {
							if(!isLoadMore){
								products.clear();
							}
							products.addAll(result.data);
							if(mProductListAdapter == null){
								mProductListAdapter = new ProductListAdapter(products, getActivity());
								lvProduct.setAdapter(mProductListAdapter);
							}else{
								mProductListAdapter.notifyDataSetChanged();
							}
							ptrv.onHeaderRefreshComplete();
							ptrv.onFooterRefreshComplete();
							AlertHelper.getInstance(getActivity()).hideLoading();
							
							
							if(result.data.size() <= 0){
								if(isLoadMore){
									AlertHelper.getInstance(getActivity())
									.showCenterToast(R.string.load_done);
								}else{
									tv_noData.setVisibility(View.VISIBLE);
									lvProduct.setVisibility(View.GONE);
								}
							}else{
								tv_noData.setVisibility(View.GONE);
								lvProduct.setVisibility(View.VISIBLE);
							}
						}
						
						@Override
						public void onFailure(int errorNo, String errorMsg) {
							ptrv.onHeaderRefreshComplete();
							ptrv.onFooterRefreshComplete();
							AlertHelper.getInstance(getActivity()).hideLoading();
							if(!isLoadMore){
								products.clear();
								if(mProductListAdapter == null){
									mProductListAdapter = new ProductListAdapter(products, getActivity());
									lvProduct.setAdapter(mProductListAdapter);
								}else{
									mProductListAdapter.notifyDataSetChanged();
								}
								tv_noData.setVisibility(View.VISIBLE);
								lvProduct.setVisibility(View.GONE);
							}else{
								AlertHelper.getInstance(getActivity())
								.showCenterToast(R.string.load_done);
							}
						}
					}, getActivity());
		}
		//getActivity().getIntent().getStringExtra("id")
		if(isLoadMore){
			pageNo++;
		}else{
			pageNo = 1;
		}
		mWebServiceHelper.getMerchantCommList("1", isSp, pageNo+"");
	}
	
	private void findView(){
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		lvProduct = (ListView) rootView.findViewById(R.id.lv_product);
		tv_noData = (TextView) rootView.findViewById(R.id.tv_noData);
	}
}
