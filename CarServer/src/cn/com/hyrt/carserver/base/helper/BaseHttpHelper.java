package cn.com.hyrt.carserver.base.helper;

import com.google.gson.Gson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * http请求助手基类
 * @author zoe
 *
 */
public class BaseHttpHelper {
	
	private RequestCallback mCallback;
	private FinalHttp mFinalHttp;
	private AjaxCallBack<String> mAjaxCallback;
	private Class<?> clazz;
	private Gson mGson;
	private int timeOut = 10*1000;
	
	public static final int SUCCESS = 1;
	public static final int TIMEOUT = 2;
	public static final int FAILURE = 3;
	
	
	
	public BaseHttpHelper(RequestCallback<?> mCallback) {
		super();
		this.mCallback = mCallback;
		this.mGson = new Gson();
	}

	protected void get(String url, Class<?> clazz){
		get(url, null, clazz);
	}
	
	protected void get(String url, AjaxParams params, Class<?> clazz){
		this.clazz = clazz;
		createFinalHttp();
		if(mAjaxCallback == null){
			initAjaxCallback();
		}
		mFinalHttp.get(url, params, mAjaxCallback);
		
	}
	
	protected void post(String url, Class<?> clazz){
		post(url, null, clazz);
	}
	
	protected void post(String url, AjaxParams params, Class<?> clazz){
		this.clazz = clazz;
		createFinalHttp();
		if(mAjaxCallback == null){
			initAjaxCallback();
		}
		mFinalHttp.post(url, params, mAjaxCallback);
	}
	
	private void initAjaxCallback(){
		mAjaxCallback = new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				if(mCallback != null && clazz != null){
					mCallback.onSuccess(mGson.fromJson(t, clazz));
				}
			}
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				super.onFailure(t, errorNo, strMsg);
				if(mCallback != null){
					if(strMsg != null && strMsg.contains("timed out")){
						mCallback.onFailure(TIMEOUT);
					}else{
						mCallback.onFailure(FAILURE);
					}
				}
			}
		};
	}
	
	private void createFinalHttp(){
		if(mFinalHttp == null){
			mFinalHttp = new FinalHttp();
			mFinalHttp.configTimeout(timeOut);
		}
	}
	
	public static interface RequestCallback<T>{
		public void onSuccess(T result);
		public void onFailure(int errorNo);
	}
}
