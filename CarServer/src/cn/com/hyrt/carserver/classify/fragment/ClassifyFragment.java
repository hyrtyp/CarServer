package cn.com.hyrt.carserver.classify.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LocationHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private LocationHelper mLocationHelper;
	
	private static final int STOP_LOCATION = 0;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STOP_LOCATION:
				mLocationHelper.stop();
				AlertHelper.getInstance(getActivity()).hideLoading();
				String[] location = StorageHelper.getInstance(getActivity()).getLocation();
				if(location == null || location.length <=0){
//					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.no_baidumap);
					jump((Integer) msg.obj, null, null, null);
				}else{
					jump((Integer) msg.obj, location[0], location[1], location[2]);
				}
				
				break;

			default:
				break;
			}
		};
	};

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
			//维修保养http://192.168.10.238:8083/cspportal/knowledge/list?typeid=分类iD
			case 0:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid="+000011);
			    break;
			//配件改装
			case 1:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid="+000012);
			    break;
			//内外装饰
			case 2:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid="+000013);
			    break;
			//代驾服务
			case 3:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				AlertHelper.getInstance(getActivity()).showCenterToast("正在开发中");
			    break;    
			//保险优惠
			case 4:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid="+000011);
			    break;    
			//洗车美容
			case 5:
				gvMyInfointent.setClass(getActivity(), WebActivity.class);
				gvMyInfointent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid="+000014);
			    break;    
			default:
				return;
			}
			startActivity(gvMyInfointent);
		}
		
	};
	
	private AdapterView.OnItemClickListener gvFoundOnItemClickListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int position,
				long arg3) {
//			String[] location = StorageHelper.getInstance(getActivity()).getLocation();
//			if(location == null || location.length <=0){
				AlertHelper.getInstance(getActivity()).showLoading(getActivity().getString(R.string.location_label));
				mLocationHelper = LocationHelper.getInstance(getActivity());
				Message msg = new Message();
				msg.what = STOP_LOCATION;
				msg.obj = position;
				mHandler.sendMessageDelayed(msg, 5000);
				mLocationHelper.setLocationCallback(new LocationHelper.LocationCallback() {
					
					@Override
					public void onLocation(double lon, double lat, String city) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						mHandler.removeMessages(STOP_LOCATION);
						jump(position, lon+"", lat+"", city+"");
					}
				});
				mLocationHelper.start();
//			}else{
//				jump(position, location[0], location[1], location[2]);
//			}
			
			
		}
		
	};
	
	private void jump(int position, String lon, String lat, String city){
		Intent gvFoundintent = new Intent();
		StringBuffer path = new StringBuffer("");
		switch(position){
		//找服务http://192.168.10.238:8083/cspportal/goods/search?areaname=城市名称（北京 需要经行编码UTF-8）&coorx=经度坐标&coory=纬度坐标
		case 0:
			gvFoundintent.setClass(getActivity(), WebActivity.class);
			path.append(getString(R.string.method_weburl)+"/cspportal/goods/search?");
//			gvFoundintent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/goods/search?areaname=");
		    break;
		case 1:
			gvFoundintent.setClass(getActivity(), WebActivity.class);
			if(CarServerApplication.loginInfo == null){
				CarServerApplication.loginInfo = StorageHelper.getInstance(getActivity()).getLoginInfo();
			}
			path.append(getString(R.string.method_weburl)+"/cspportal/merchant/typelist?userid="+CarServerApplication.loginInfo.id+"&");
		    break;
		default:
			return;
		}
		if(lon != null && !"".equals(lon)){
			path.append(String.format("coorx=%s&coory=%s&areaname=%s", lon, lat, city));
		}
		LogHelper.i("tag", "path:"+path);
		gvFoundintent.putExtra("url", path.toString());
		
		startActivity(gvFoundintent);
	}
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gvkjrk);
		gvFound =(GridView) rootView.findViewById(R.id.gvServer);
	}
}
