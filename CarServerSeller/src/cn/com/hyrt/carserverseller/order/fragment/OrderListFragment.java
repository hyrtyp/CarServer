package cn.com.hyrt.carserverseller.order.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ORDER_LIST;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.PullToRefreshView;
import cn.com.hyrt.carserverseller.order.activity.OrderDetailActivity;
import cn.com.hyrt.carserverseller.order.adapter.OrderListAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class OrderListFragment extends Fragment{

	private View rootView;
	private PullToRefreshView ptrv;
	private ListView listview;
	private TextView tvNodata;
	private int page = 1;
	
	private List<Define.ORDER_LIST.CDATA> mData 
	= new ArrayList<Define.ORDER_LIST.CDATA>();
	
	private OrderListAdapter mAdapter;
	
	private String type;
	
	public OrderListFragment(String type) {
		super();
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_order_list, null);
		findView();
		if(mData.size() <= 0){
			loadData(false);
		}
		setListener();
		return rootView;
	}
	
	public void loadData(final boolean isMore){
		if(isMore){
			page++;
		}else{
			page = 1;
		}
		AlertHelper.getInstance(getActivity()).showLoading(null);
		WebServiceHelper mLoadOrderHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.ORDER_LIST>() {

					@Override
					public void onSuccess(ORDER_LIST result) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
						if(!isMore){
							mData.clear();
						}
						
						if(result.data.size() <= 0){
							if(isMore){
								if(mData.size() > 0){
									AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部数据");
								}else{
									listview.setVisibility(View.GONE);
									tvNodata.setVisibility(View.VISIBLE);
								}
							}else{
								listview.setVisibility(View.GONE);
								tvNodata.setVisibility(View.VISIBLE);
							}
						}else{
							mData.addAll(result.data);
						}
						if(mAdapter == null){
							mAdapter = new OrderListAdapter(mData, getActivity(), type);
							listview.setAdapter(mAdapter);
						}else{
							mAdapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
						if(isMore){
							if(mData.size() > 0){
								AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部数据");
							}else{
								listview.setVisibility(View.GONE);
								tvNodata.setVisibility(View.VISIBLE);
							}
						}else{
							listview.setVisibility(View.GONE);
							tvNodata.setVisibility(View.VISIBLE);
						}
					}
		}, getActivity());
		mLoadOrderHelper.getOrders(type, page);
	}
	
	private void setListener(){
		//下拉刷新
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData(false);
			}
		});
		
		//下拉加载更多
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
				intent.setClass(getActivity(), OrderDetailActivity.class);
				intent.putExtra("vo", mData.get(position));
				intent.putExtra("type", type);
				startActivityForResult(intent, 101);
			}
		});
	}
	
	private void findView(){
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		listview = (ListView) rootView.findViewById(R.id.listview);
		tvNodata = (TextView) rootView.findViewById(R.id.tv_nodata);
	}
}
