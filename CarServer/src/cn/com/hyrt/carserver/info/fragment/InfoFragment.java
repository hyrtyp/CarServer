package cn.com.hyrt.carserver.info.fragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.CODE;
import cn.com.hyrt.carserver.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.info.activity.ChangeInfoActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * 我的信息主页
 * @author zoe
 *
 */
public class InfoFragment extends Fragment{
	
	private View rootView;
	private GridView gvMyInfo;
	private ImageView ivFaceImg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		findView();
		initView();
		setListener();
		
		
		Thread mThread = new Thread(){
			@Override
			public void run() {
				super.run();
				
				WebServiceHelper mServiceHelper = 
						new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.CODE>() {

							@Override
							public void onSuccess(CODE result) {
								for(int i=0,j=result.data.size(); i<j; i++){
									LogHelper.i("tag", "result:"+result.data.get(i).name);
								}
							}

							@Override
							public void onFailure(int errorNo, String errorMsg) {
								LogHelper.i("tag", "errorNo:"+errorNo+" errorMsg:"+errorMsg);
							}
				
				}, getActivity());
				mServiceHelper.getTerminalUserinfoOccupationCode();
				
				
				
				/*// 命名空间  
		        String nameSpace = "http://webservice.csp.hyrt.com";  
		        // 调用的方法名称  
		        String methodName = "getCode";  
		        // EndPoint  
		        String wsd = "http://192.168.10.135:8080/CSPInterface/services/CspInterface";  
		        // SOAP Action  
		        String soapAction = "urn:getCode";  

		        SoapObject soapObject = new SoapObject(nameSpace, methodName);
		        soapObject.addProperty("jsonstr", "{\"type\":\"TERMINAL_USERINFO_OCCUPATION\"}");
		        
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.bodyOut = soapObject;
		        envelope.dotNet = true;
		        envelope.setOutputSoapObject(soapObject);

		        HttpTransportSE ht = new HttpTransportSE(wsd);
		        ht.debug = true;
				try {
					ht.call(soapAction, envelope);
					String result = envelope.getResponse().toString();
					LogHelper.i("tag", "result:"+result);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			}
		};
		
		mThread.start();
		
		
		
		return rootView;
	}
	
	private void initView(){
		int[] imgArray = new int[]{R.drawable.ic_new_question, R.drawable.ic_question_history,
				R.drawable.ic_my_appointment, R.drawable.ic_my_experts,
				R.drawable.ic_condition, R.drawable.ic_my_car};
		int[] textSourceArray = new int[]{R.string.info_new_question, R.string.info_question_history,
				R.string.info_my_appointment, R.string.info_my_experts, R.string.info_condition,
				R.string.info_my_car};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		gvMyInfo.setOnItemClickListener(mOnItemClickListener);
	}
	
	private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			LogHelper.i("tag", "position:"+position);
			switch (position) {
			case 0:
				//最新资讯
				break;
			case 1:
				//提问历史
				break;
			case 2:
				//我的预约
				break;
			case 3:
				//我的专家
				break;
			case 4:
				//车辆状况
				break;
			case 5:
				//我的爱车
				Intent intent = new Intent();
				intent.setClass(getActivity(), ChangeInfoActivity.class);
				startActivity(intent);
				break;
			default:
				break;
			}
		}
	};
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gv_myInfo);
		ivFaceImg = (ImageView) rootView.findViewById(R.id.iv_face_img);
	}
	
	private void setListener(){
		ivFaceImg.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ChangeInfoActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
