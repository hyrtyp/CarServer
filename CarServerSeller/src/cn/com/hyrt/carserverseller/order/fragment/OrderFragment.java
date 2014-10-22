package cn.com.hyrt.carserverseller.order.fragment;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.shop.fragment.ProductListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class OrderFragment extends Fragment{

	private View rootView;
	private Button btnNeworders;
	private Button btnFinishOrders;
	private Button btnOrdersHistory;
	private int tabPosition = 0;
	private int curPosition = 0;
	private OrderListFragment mNewOrderFragment;
	private OrderListFragment mAndOrderFragment;
	private OrderListFragment mHisOrderFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		tabPosition = 0;
		curPosition = 0;
		mNewOrderFragment = null;
		mAndOrderFragment = null;
		mHisOrderFragment = null;
		
		rootView = inflater.inflate(R.layout.fragment_order, null);
		findView();
		setListener();
		mNewOrderFragment = new OrderListFragment(WebServiceHelper.ORDER_TYPE_NEW);
		getChildFragmentManager()
		.beginTransaction()
		.add(R.id.container, mNewOrderFragment)
		.commit();
		return rootView;
	}
	
	private void setListener(){
		btnNeworders.setOnClickListener(mTabOnClickListener);
		btnFinishOrders.setOnClickListener(mTabOnClickListener);
		btnOrdersHistory.setOnClickListener(mTabOnClickListener);
	}
	
	private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			focusManage(false, tabPosition);
			int id = view.getId();
			if(id == btnNeworders.getId()){
				tabPosition = 0;
				focusManage(true, tabPosition);
			}else if(id == btnFinishOrders.getId()){
				tabPosition = 1;
				focusManage(true, tabPosition);
			}else if(id == btnOrdersHistory.getId()){
				tabPosition = 2;
				focusManage(true, tabPosition);
			}
		}
	};
	
	private void changeFragment(int position){
		if(position == curPosition){
			return;
		}
		switch (position) {
		case 0:
			if(mNewOrderFragment == null){
				mNewOrderFragment = new OrderListFragment(WebServiceHelper.ORDER_TYPE_NEW);
				getChildFragmentManager().beginTransaction()
				.add(R.id.container, mNewOrderFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mNewOrderFragment).commit();
			}
			break;
		case 1:
			if(mAndOrderFragment == null){
				mAndOrderFragment = new OrderListFragment(WebServiceHelper.ORDER_TYPE_AND);
				getChildFragmentManager().beginTransaction()
				.add(R.id.container, mAndOrderFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mAndOrderFragment).commit();
			}
			break;
		case 2:
			if(mHisOrderFragment == null){
				mHisOrderFragment = new OrderListFragment(WebServiceHelper.ORDER_TYPE_HIS);
				getChildFragmentManager().beginTransaction()
				.add(R.id.container, mHisOrderFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mHisOrderFragment).commit();
			}
			break;
		default:
			break;
		}
		
		switch (curPosition) {
		case 0:
			getChildFragmentManager().beginTransaction().hide(mNewOrderFragment).commit();
			break;
		case 1:
			getChildFragmentManager().beginTransaction().hide(mAndOrderFragment).commit();
			break;
		case 2:
			getChildFragmentManager().beginTransaction().hide(mHisOrderFragment).commit();
			break;
		default:
			break;
		}
		
		curPosition = position;
	}
	
	private void focusManage(boolean hasFocus, int position){
		if(hasFocus){
			changeFragment(position);
			switch (position) {
			case 0:
				btnNeworders.setBackgroundResource(R.drawable.bg_tab_left_focus);
				btnNeworders.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 1:
				btnFinishOrders.setBackgroundResource(R.drawable.bg_tab_center_focus);
				btnFinishOrders.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 2:
				btnOrdersHistory.setBackgroundResource(R.drawable.bg_tab_right_focus);
				btnOrdersHistory.setTextColor(getResources().getColor(android.R.color.white));
				break;
			}
		}else{
			switch (position) {
			case 0:
				btnNeworders.setBackgroundResource(R.drawable.bg_tab_left);
				btnNeworders.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 1:
				btnFinishOrders.setBackgroundResource(R.drawable.bg_tab_center);
				btnFinishOrders.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 2:
				btnOrdersHistory.setBackgroundResource(R.drawable.bg_tab_right);
				btnOrdersHistory.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			}
		}
	}
	
	private void findView(){
		btnNeworders = (Button) rootView.findViewById(R.id.btn_new_orders);
		btnFinishOrders = (Button) rootView.findViewById(R.id.btn_finish_orders);
		btnOrdersHistory = (Button) rootView.findViewById(R.id.btn_orders_history);
	}
}
