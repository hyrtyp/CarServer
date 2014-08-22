package cn.com.hyrt.carserversurvey.product.fragment;

import cn.com.hyrt.carserversurvey.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProductFragment extends Fragment{

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_product, null);
		return rootView;
	}
}
