package cn.com.hyrt.carserver.classify.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class ClassifyFragment extends Fragment{
	
	private View rootView;
	private GridView gvMyInfo;
	private GridView gvFound;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_classify, null);
		findView();
		int[] imgArray = new int[]{R.drawable.classify_wxby,R.drawable.classify_pjgz, R.drawable.classify_nwzx, 
				R.drawable.classify_djfw, R.drawable.classify_bxyh, R.drawable.classify_qcmr};
		int[] textSourceArray = new int[]{R.string.classify_wxby, R.string.classify_pjgz,R.string.classify_nwzx, 
				R.string.classify_djfw,R.string.classify_bxyh,R.string.classify_qcmr};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		gvMyInfo.setOnItemClickListener(gvMyInfoOnItemClickListener);
		
		int[] imgArrayf = new int[]{R.drawable.classify_zfw,R.drawable.classify_zsj};
		int[] textSourceArrayf = new int[]{R.string.classify_zfw, R.string.classify_zsj};
		PortalGridAdapter mfAdapter = new PortalGridAdapter(imgArrayf, textSourceArrayf, getActivity());
		gvFound.setAdapter(mfAdapter);
		gvFound.setOnItemClickListener(gvFoundOnItemClickListener);
		return rootView;
	}
	
	private AdapterView.OnItemClickListener gvMyInfoOnItemClickListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent gvMyInfointent = new Intent();
			switch(position){
			//维修保养
			case 0:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
				AlertHelper.getInstance(getActivity()).showCenterToast(getString(R.string.method_weburl)+"/cspportal/");
			    break;
			//配件改装
			case 1:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;
			//内外装饰
			case 2:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;
			//代驾服务
			case 3:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;    
			//保险优惠
			case 4:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;    
			//洗车美容
			case 5:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;    
			default:
				return;
			}
			startActivity(gvMyInfointent);
		}
		
	};
	
	private AdapterView.OnItemClickListener gvFoundOnItemClickListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			LogHelper.i("tag", ""+position); 
			Intent gvFoundintent = new Intent();
			switch(position){
			//找服务
			case 0:
				gvFoundintent.setClass(getActivity(), WebActivity.class);
				gvFoundintent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;
			//找商家
			case 1:
				gvFoundintent.setClass(getActivity(), WebActivity.class);
				gvFoundintent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;
			default:
				return;  
			}
			startActivity(gvFoundintent);
		}
		
	};
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gvkjrk);
		gvFound =(GridView) rootView.findViewById(R.id.gvServer);
	}
}
