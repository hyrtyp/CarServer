package cn.com.hyrt.carserver.info.fragment;

import java.util.List;
import java.util.Map;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.SimpleAdapter;

public class InfoFragment extends Fragment{
	
	private View rootView;
	private GridView gvMyInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		findView();
		int[] imgArray = new int[]{R.drawable.ic_new_question, R.drawable.ic_question_history,
				R.drawable.ic_my_appointment, R.drawable.ic_my_experts,
				R.drawable.ic_condition, R.drawable.ic_my_car};
		int[] textSourceArray = new int[]{R.string.info_new_question, R.string.info_question_history,
				R.string.info_my_appointment, R.string.info_my_experts, R.string.info_condition,
				R.string.info_my_car};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		return rootView;
	}
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gv_myInfo);
	}

}
