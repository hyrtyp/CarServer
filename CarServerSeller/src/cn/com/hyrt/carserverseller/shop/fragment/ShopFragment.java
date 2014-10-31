package cn.com.hyrt.carserverseller.shop.fragment;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.preferential.fragment.PreferentialListFragment;
import cn.com.hyrt.carserverseller.product.fragment.ProductFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ShopFragment extends Fragment{

	private View rootView;
	private Button btnProduct;
	private Button btnService;
	private Button btnPreferential;
	private int tabPosition = 0;
	private int curPosition;
	
	private ProductListFragment mSpFragment;
	private ProductListFragment mFwFragment;
	private PreferentialListFragment mPreferentialListFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shop, null);
		findView();
		setListener();
		if(mSpFragment != null){
			getChildFragmentManager().beginTransaction().remove(mSpFragment).commit();
		}
		if(mFwFragment != null){
			getChildFragmentManager().beginTransaction().remove(mFwFragment).commit();
		}
		if(mPreferentialListFragment != null){
			getChildFragmentManager().beginTransaction().remove(mPreferentialListFragment).commit();
		}
		mSpFragment = null;
		mFwFragment = null;
		mPreferentialListFragment = null;
		tabPosition = 0;
		curPosition = 0;
//		if(tabPosition != 0){
//			focusManage(false, 0);
//			focusManage(true, tabPosition);
//		}else{
			mSpFragment = new ProductListFragment(false);
			getChildFragmentManager().beginTransaction()
			.add(R.id.layout_content, mSpFragment).commit();
//		}
		
		
		
		return rootView;
	}
	
	private void changeFragment(int position){
		if(position == curPosition){
			return;
		}
		switch (position) {
		case 0:
			if(mSpFragment == null){
				mSpFragment = new ProductListFragment(false);
				getChildFragmentManager().beginTransaction()
				.add(R.id.layout_content, mSpFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mSpFragment).commit();
			}
			break;
		case 1:
			if(mFwFragment == null){
				mFwFragment = new ProductListFragment(true);
				getChildFragmentManager().beginTransaction()
				.add(R.id.layout_content, mFwFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mFwFragment).commit();
			}
			
			break;
		case 2:
			if(mPreferentialListFragment == null){
				mPreferentialListFragment = new PreferentialListFragment();
				getChildFragmentManager().beginTransaction()
				.add(R.id.layout_content, mPreferentialListFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mPreferentialListFragment).commit();
			}
			break;
		default:
			break;
		}
		
		switch (curPosition) {
		case 0:
			if(mSpFragment != null){
				getChildFragmentManager().beginTransaction().hide(mSpFragment).commit();
			}
			break;
		case 1:
			if(mFwFragment != null){
				getChildFragmentManager().beginTransaction().hide(mFwFragment).commit();
			}
			break;
		case 2:
			if(mPreferentialListFragment != null){
				getChildFragmentManager().beginTransaction().hide(mPreferentialListFragment).commit();
			}
			break;
		default:
			break;
		}
		
		curPosition = position;
	}
	
	private void setListener(){
		btnProduct.setOnClickListener(mTabOnClickListener);
		btnService.setOnClickListener(mTabOnClickListener);
		btnPreferential.setOnClickListener(mTabOnClickListener);
	}
	
private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			focusManage(false, tabPosition);
			int id = view.getId();
			if(id == btnProduct.getId()){
				tabPosition = 0;
				focusManage(true, tabPosition);
			}else if(id == btnService.getId()){
				tabPosition = 1;
				focusManage(true, tabPosition);
			}else if(id == btnPreferential.getId()){
				tabPosition = 2;
				focusManage(true, tabPosition);
			}
		}
	};
	
	private void focusManage(boolean hasFocus, int position){
		if(hasFocus){
			changeFragment(position);
			switch (position) {
			case 0:
				btnProduct.setBackgroundResource(R.drawable.bg_tab_left_focus);
				btnProduct.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 1:
				btnService.setBackgroundResource(R.drawable.bg_tab_center_focus);
				btnService.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 2:
				btnPreferential.setBackgroundResource(R.drawable.bg_tab_right_focus);
				btnPreferential.setTextColor(getResources().getColor(android.R.color.white));
				break;
			}
		}else{
			switch (position) {
			case 0:
				btnProduct.setBackgroundResource(R.drawable.bg_tab_left);
				btnProduct.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 1:
				btnService.setBackgroundResource(R.drawable.bg_tab_center);
				btnService.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 2:
				btnPreferential.setBackgroundResource(R.drawable.bg_tab_right);
				btnPreferential.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			}
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(mFwFragment != null){
			mFwFragment.onActivityResult(requestCode, resultCode, data);
		}
		if(mSpFragment != null){
			mSpFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	private void findView(){
		btnProduct = (Button) rootView.findViewById(R.id.btn_product);
		btnService = (Button) rootView.findViewById(R.id.btn_service);
		btnPreferential = (Button) rootView.findViewById(R.id.btn_preferential);
	}
}
