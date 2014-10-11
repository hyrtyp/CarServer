package cn.com.hyrt.carserverseller.shop.fragment;

import cn.com.hyrt.carserverseller.R;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_shop, null);
		findView();
		setListener();
		if(tabPosition != 0){
			focusManage(false, 0);
			focusManage(true, tabPosition);
		}
		return rootView;
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
	
	private void findView(){
		btnProduct = (Button) rootView.findViewById(R.id.btn_product);
		btnService = (Button) rootView.findViewById(R.id.btn_service);
		btnPreferential = (Button) rootView.findViewById(R.id.btn_preferential);
	}
}
