package cn.com.hyrt.carserver.base.helper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;

import com.google.gson.Gson;

/**
 * WebService请求助手基类
 * @author zoe
 *
 */
public class BaseWebServiceHelper {

	private static  String NAME_SPACE = "http://webservice.csp.hyrt.com";
	private static final String END_POINT = "http://192.168.10.135:8080/CSPInterface/services/CspInterface?wsdl";
	private RequestCallback mCallback;
	private Gson mGson;
	protected Context mContext;
	
	
	
	protected BaseWebServiceHelper(RequestCallback mCallback, Context context) {
		super();
		this.mCallback = mCallback;
		this.mGson = new Gson();
		this.mContext = context;
	}

	protected void get(final String method, final Map<String, String> params, final Class<?> clazz){
		
		Thread mThread = new Thread(){
			public void run() {
				SoapObject soapObject = new SoapObject(NAME_SPACE, method);
				Iterator<String> keys = params.keySet().iterator();
				while(keys.hasNext()){
					  String key = (String) keys.next();
					   String value = params.get(key);
					soapObject.addProperty(key, value.toString());
				}
		        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		        envelope.bodyOut = soapObject;
		        envelope.dotNet = true;
		        envelope.setOutputSoapObject(soapObject);

		        HttpTransportSE ht = new HttpTransportSE(END_POINT);
		        ht.debug = true;
		        
				try {
					ht.call("urn:"+method, envelope);
					final String result = envelope.getResponse().toString();
					if(mCallback != null && result != null && mContext != null){
						((BaseActivity)mContext).runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Define.BASE base = (BASE) mGson.fromJson(result, clazz);
								if(base.code == Define.REQUEST_SUCCESS_CODE){
									mCallback.onSuccess(base);
								}else{
									mCallback.onFailure(base.code, base.message);
								}
								
							}
						});
						
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
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
}
