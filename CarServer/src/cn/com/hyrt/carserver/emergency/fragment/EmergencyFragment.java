package cn.com.hyrt.carserver.emergency.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class EmergencyFragment extends Fragment{

	private View rootView;
	private GridView gvMyInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_emergency, null);
		findView();
		int[] imgArray = new int[]{R.drawable.emergency_bxlp, R.drawable.emergency_jtbj,
				R.drawable.emergency_ksqz, R.drawable.emergency_yjjy};
		int[] textSourceArray = new int[]{R.string.emergency_bxlp, R.string.emergency_jtbj,
				R.string.emergency_ksqz, R.string.emergency_yjjy};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		return rootView;
	}
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gv_myInfo);
	}

}
