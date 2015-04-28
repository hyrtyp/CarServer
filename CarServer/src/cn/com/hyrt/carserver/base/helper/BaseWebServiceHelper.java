package cn.com.hyrt.carserver.base.helper;

import java.io.IOException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;

import com.google.gson.Gson;

/**
 * WebService请求助手基类
 * @author zoe
 *
 */
public class BaseWebServiceHelper {

	public static String NAME_SPACE = "http://webservice.csp.hyrt.com";

//	private static final String END_POINT = "http://192.168.10.135:8080/CSPInterface/services/CspInterface?wsdl";
	private static final String END_POINT = "http://192.168.10.238:8080/CSPInterface/services/CspInterface?wsdl";
//	private static final String END_POINT = "http://61.233.18.68:8080/CSPInterface/services/CspInterface?wsdl";
//	private static final String END_POINT = "http://42.121.127.121:8080/CSPInterface/services/CspInterface?wsdl";
//	private static final String END_POINT = "http://121.42.43.20:82/CSPInterface/services/CspInterface?wsdl";

	private static final int TIMEOUT = 10*1000;

	private RequestCallback mCallback;
	private Gson mGson;
	protected Context mContext;
	private OnSuccessListener mListener;
	
	private static final int MESSAGE_TIMEOUT = 101;
	
	private BaseWebServiceHelper(){};
	
	protected BaseWebServiceHelper(RequestCallback mCallback, Context context) {
		super();
		this.mCallback = mCallback;
		this.mGson = new Gson();
		this.mContext = context;
	}
	
	protected BaseWebServiceHelper(OnSuccessListener listener, Context context) {
		super();
		this.mListener = listener;
		this.mGson = new Gson();
		this.mContext = context;
	}
	
	protected BaseWebServiceHelper(Context context) {
		super();
		this.mGson = new Gson();
		this.mContext = context;
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MESSAGE_TIMEOUT:
				AlertHelper.getInstance(mContext).showCenterToast(R.string.timeout);
				AlertHelper.getInstance(mContext).hideLoading();
				break;

			default:
				break;
			}
		};
	};


	protected void get(final String method, final String params, final Class<?> clazz){
		
		Thread mThread = new Thread(){
			public void run() {
				SoapObject soapObject = new SoapObject(NAME_SPACE, method);
				
				// 请求数据的参数与方法名
				LogHelper.i("tag", "params:"+params+" method:"+method);
				if(params != null){
					soapObject.addProperty("jsonstr", params);
				}
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.bodyOut = soapObject;
		        envelope.dotNet = true;
		        envelope.setOutputSoapObject(soapObject);
		        
		        HttpTransportSE ht = new HttpTransportSE(END_POINT, TIMEOUT);
		        LogHelper.i("tag", "END_POINT:"+END_POINT);
		        ht.debug = true;
		        
				try {
					ht.call("urn:"+method, envelope);
					final String result = envelope.getResponse().toString();
					
					//网络请求到的数据
					LogHelper.i("tag", "result:"+result);
					if(result != null && mContext != null){
						((Activity)mContext).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mListener != null){
									mListener.onSuccess(result);
								}
								if(mCallback != null){
									Define.BASE base = (BASE) mGson.fromJson(result, clazz);
									if(Define.REQUEST_SUCCESS_CODE.equals(base.code)
											|| Define.REQUEST_SAVE_SUCCESS_CODE.equals(base.code)){
										mCallback.onSuccess(base);
									}else{
										mCallback.onFailure(Integer.parseInt(base.code), base.message);
									}
								}
							}
						});
						
					}
				} catch (IOException e) {
					LogHelper.i("tag", "e1:"+e.getMessage());
					if(e.getMessage().contains("Network is unreachable")|| e.getMessage().contains("ECONNREFUSED")){
						if(mCallback != null){
							((Activity)mContext).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									mCallback.onFailure(Integer.parseInt(Define.REQUEST_ERROR_CODE),
											mContext.getString(R.string.net_error_msg));
								}
							});
							
						}
					}else if(e.getMessage().contains("BufferedInputStream is closed")){
						((Activity)mContext).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								mHandler.sendEmptyMessage(MESSAGE_TIMEOUT);
							}
						});
					}else{
						mHandler.sendEmptyMessage(MESSAGE_TIMEOUT);
					}
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					LogHelper.i("tag", "e2:"+e.getMessage());
					e.printStackTrace();
				}
			};
		};
		mThread.start();
	}
	
	protected void uploadImage(final String method, final String params, final String image, final Class<?> clazz){
		
		Thread mThread = new Thread(){
			public void run() {
				LogHelper.i("tag", "params:"+params+" method:"+method);
				SoapObject soapObject = new SoapObject(NAME_SPACE, method);
				if(image != null){
					soapObject.addProperty("image", image);
				}
				if(params != null){
					soapObject.addProperty("jsonstr", params);
				}
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.bodyOut = soapObject;
		        envelope.dotNet = true;
		        envelope.setOutputSoapObject(soapObject);
		        
		        HttpTransportSE ht = new HttpTransportSE(END_POINT, TIMEOUT);
		        ht.debug = true;
		        
				try {
					ht.call("urn:"+method, envelope);
					final String result = envelope.getResponse().toString();
					LogHelper.i("tag", "result:"+result);
					if(result != null && mContext != null){
						((Activity)mContext).runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if(mListener != null){
									mListener.onSuccess(result);
								}
								if(mCallback != null){
									Define.BASE base = (BASE) mGson.fromJson(result, clazz);
									if(Define.REQUEST_SUCCESS_CODE.equals(base.code)
											|| Define.REQUEST_SAVE_SUCCESS_CODE.equals(base.code)){
										mCallback.onSuccess(base);
									}else{
										mCallback.onFailure(Integer.parseInt(base.code), base.message);
									}
								}
							}
						});
						
					}
				} catch (IOException e) {
					LogHelper.i("tag", "e1:"+e.getMessage());
					if(e.getMessage() == null || e.getMessage().contains("Network is unreachable")|| e.getMessage().contains("ECONNREFUSED")){
						if(mCallback != null){
							((Activity)mContext).runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									mCallback.onFailure(Integer.parseInt(Define.REQUEST_ERROR_CODE),
											mContext.getString(R.string.net_error_msg));
								}
							});
							
						}
					}else if(e.getMessage().contains("BufferedInputStream is closed")){
						((Activity)mContext).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								mHandler.sendEmptyMessage(MESSAGE_TIMEOUT);
							}
						});
					}else{
						mHandler.sendEmptyMessage(MESSAGE_TIMEOUT);
					}
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					LogHelper.e("tag", "e2:"+e.getMessage());
					e.printStackTrace();
				}
			};
		};
		mThread.start();
	}
	
	public static interface RequestCallback<T>{
		public void onSuccess(T result);
		public void onFailure(int errorNo, String errorMsg);
	}
	
	public void setOnSuccessListener(OnSuccessListener listener){
		this.mListener = listener;
	}
	
	public static interface OnSuccessListener{
		public void onSuccess(String result);
	}
}
