package cn.com.hyrt.carserver.info.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.CODE;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.info.activity.ChangeInfoActivity;
import cn.com.hyrt.carserver.info.activity.InfoDetailActivity;
import cn.com.hyrt.carserver.info.activity.MyCarActivity;
import cn.com.hyrt.carserver.info.activity.QuestionActivity;
import cn.com.hyrt.carserver.info.adapter.MyCarAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

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
	
	private void loadData(){
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
								if(i < j-1){
									strCars += result.data.get(i).model+"、";
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
				intent.putExtra("url", "http://192.168.10.238:8083/cspportal/");
				break;
			case 3:
				//我的专家
				intent.setClass(getActivity(), WebActivity.class);
				intent.putExtra("url", "http://www.baidu.com/");
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
				break;
			default:
				return;
			}
			startActivity(intent);
		}
	};
	
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gv_myInfo);
		ivFaceImg = (ImageLoaderView) rootView.findViewById(R.id.iv_face_img);
		ivFaceImg.setImageUrl(CarServerApplication.info.imagepath);
		tv_username = (TextView) rootView.findViewById(R.id.tv_username);
		tv_cars = (TextView) rootView.findViewById(R.id.tv_cars);
		tv_username.setText("用户名："+CarServerApplication.info.unitname);
	}
	
	private void setListener(){
		ivFaceImg.setOnClickListener(new View.OnClickListener() {
			
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
