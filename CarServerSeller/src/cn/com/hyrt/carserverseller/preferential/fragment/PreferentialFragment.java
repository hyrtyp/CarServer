package cn.com.hyrt.carserverseller.preferential.fragment;

import cn.com.hyrt.carserverseller.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PreferentialFragment extends Fragment{

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_preferential, null);
		return rootView;
	}
}
