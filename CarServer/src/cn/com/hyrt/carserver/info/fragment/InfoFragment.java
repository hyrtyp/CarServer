package cn.com.hyrt.carserver.info.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.info.activity.InfoDetailActivity;
import cn.com.hyrt.carserver.info.activity.MyCarActivity;
import cn.com.hyrt.carserver.info.activity.QuestionActivity;

/**
 * 我的信息主页
 * @author zoe
 *
 */
public class InfoFragment extends Fragment{
	
	private View rootView;
	private GridView gvMyInfo;
	private ImageLoaderView ivFaceImg;
	private TextView tv_username;
	private TextView tv_cars;
	private LinearLayout layout_info;
	private WebServiceHelper mCalWebServiceHelper;
	private List<String> cars = new ArrayList<String>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		
		findView();
		initView();
		setListener();
		loadData();
		
		return rootView;
	}
	
	public void loadData(){
		tv_username.setText("用户名："+CarServerApplication.info.unitname);
		AlertHelper.getInstance(getActivity()).showLoading(null);
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO>() {

					@Override
					public void onSuccess(INFO result) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						CarServerApplication.info = result;
						ivFaceImg.setImageUrl(CarServerApplication.info.imagepath);
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_load_fail);
						AlertHelper.getInstance(getActivity()).hideLoading();
					}
		}, getActivity());
		mWebServiceHelper.getUserInfo();
		
		if(mCalWebServiceHelper == null){
			mCalWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INFO_CAR_LIST>() {
						@Override
						public void onSuccess(INFO_CAR_LIST result) {
							String strCars = "";
							for(int i=0,j=result.data.size(); i<j; i++){
								cars.add(result.data.get(i).model);
								if("".equals(result.data.get(i).model)){
									continue;
								}
								if(i < j-1){
									strCars += result.data.get(i).model+"、";
								}else{
									strCars += result.data.get(i).model;
								}
								
							}
							
							tv_cars.setText("我的爱车："+strCars);
							
							
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							
						}
						
			}, getActivity());
		}
		mCalWebServiceHelper.getTerminalCarList();
	}
	
	private void initView(){
		int[] imgArray = new int[]{R.drawable.ic_new_question, R.drawable.ic_question_history,
				R.drawable.ic_my_appointment, R.drawable.ic_my_experts,
				R.drawable.ic_condition, R.drawable.ic_my_car, R.drawable.ic_info_jfdh, R.drawable.bg_blank};
		int[] textSourceArray = new int[]{R.string.info_new_question, R.string.info_question_history,
				R.string.info_my_appointment, R.string.info_my_experts, R.string.info_condition,
				R.string.info_my_car, R.string.info_jfdh, R.string.blank_text};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		gvMyInfo.setOnItemClickListener(mOnItemClickListener);
	}
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			LogHelper.i("tag", "position:"+position);
			Intent intent = new Intent();
			switch (position) {
			case 0:
				//最新资讯
				intent.setClass(getActivity(), QuestionActivity.class);
				intent.putExtra("type", QuestionActivity.TYPE_NEW);
				break;
			case 1:
				//提问历史
				intent.setClass(getActivity(), QuestionActivity.class);
				intent.putExtra("type", QuestionActivity.TYPE_HISTORY);
				break;
			case 2:
				//我的预约
				intent.setClass(getActivity(), WebActivity.class);
				intent.putExtra(
						"url",
						getString(R.string.method_weburl)
//						+"/cspportal/appoint/list?userId="
						+"/appoint/list?userId="
								+CarServerApplication.loginInfo.id);
				LogHelper.i("tag", getString(R.string.method_weburl)+"/appoint/list?userId="+CarServerApplication.loginInfo.id);
				break;
			case 3:
				//我的专家
				intent.setClass(getActivity(), WebActivity.class);
				intent.putExtra(
						"url",
						getString(R.string.method_weburl)
//						+"/cspportal/expert/list?userId="
						+"/expert/list?userId="
								+CarServerApplication.loginInfo.id);
				LogHelper.i("tag", getString(R.string.method_weburl)+"/expert/list?userId="+CarServerApplication.loginInfo.id);
				break;
			case 4:
				//车辆状况
				intent.setClass(getActivity(), MyCarActivity.class);
				intent.putExtra("isMyCar", false);
				break;
			case 5:
				//我的爱车
				intent.setClass(getActivity(), MyCarActivity.class);
				break;
			case 6:
				//积分兑换
				AlertHelper.getInstance(getActivity()).showCenterToast("正在开发中");
				return;
			default:
				return;
			}
			startActivity(intent);
		}
	};
	
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gv_myInfo);
		ivFaceImg = (ImageLoaderView) rootView.findViewById(R.id.iv_face_img);
		LogHelper.i("tag", "CarServerApplication.info:"+CarServerApplication.info);
		ivFaceImg.setImageUrl(CarServerApplication.info.imagepath);
		tv_username = (TextView) rootView.findViewById(R.id.tv_username);
		tv_cars = (TextView) rootView.findViewById(R.id.tv_cars);
		layout_info = (LinearLayout) rootView.findViewById(R.id.layout_info);
	}
	
	private void setListener(){
		layout_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), InfoDetailActivity.class);
				startActivityForResult(intent, Define.RESULT_FROM_CHANGE_INFO);
			}
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Define.RESULT_FROM_CHANGE_INFO){
			ivFaceImg.setImageUrl(CarServerApplication.info.imagepath);
		}
	}
	
}
