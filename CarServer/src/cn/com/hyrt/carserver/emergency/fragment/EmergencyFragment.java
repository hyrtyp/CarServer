package cn.com.hyrt.carserver.emergency.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.emergency.activity.InsuranceClaimActivity;
import cn.com.hyrt.carserver.emergency.activity.SOSActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class EmergencyFragment extends Fragment{

	private View rootView;
	private GridView gvMyInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_emergency, null);
		findView();
		int[] imgArray = new int[]{R.drawable.emergency_yjjy, R.drawable.emergency_wzcx, R.drawable.emergency_bxlp, R.drawable.emergency_jtbj};
		int[] textSourceArray = new int[]{R.string.emergency_yjjy, R.string.emergency_wzcx, R.string.emergency_bxlp, R.string.emergency_jtbj};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		gvMyInfo.setOnItemClickListener(mOnItemClickListener);
		return rootView;
	}
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
   
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			LogHelper.i("tag", "position:"+position);
			Intent intent = new Intent();
			switch (position) {
			case 0:
				//一键救援
				intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getString(R.string.emergencyyjjy)));  
				EmergencyFragment.this.startActivity(intent);
				break;
			case 1:
				//违章查询
				AlertHelper.getInstance(getActivity()).showCenterToast("正在开发中");
				break;
			case 2:
				//保险理赔
				intent.setClass(getActivity(), InsuranceClaimActivity.class); 
				EmergencyFragment.this.startActivity(intent);
				break;
			case 3:
				//交通报警
				intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+getString(R.string.emergencyjtbj))); 
				EmergencyFragment.this.startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	private Button btn_sos;
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gv_emergency);
		btn_sos = (Button) rootView.findViewById(R.id.btn_sos);
		btn_sos.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SOSActivity.class);
				startActivity(intent);
			}
		});
	}

}
