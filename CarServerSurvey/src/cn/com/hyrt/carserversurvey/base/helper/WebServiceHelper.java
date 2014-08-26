package cn.com.hyrt.carserversurvey.base.helper;

import android.content.Context;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;

import com.google.gson.Gson;

/**
 * WebService请求助手
 * 
 * @author zoe
 * 
 */
public class WebServiceHelper extends BaseWebServiceHelper {

	public static final String SEEK_REPLY_HISTORY = "gb";
	public static final String SEEK_REPLY_QUESTION = "zx";

	public static final String REPLY_DETAIL_QUESTION = "consultationid";
	public static final String REPLY_DETAIL_HISTORY = "wtid";

	public WebServiceHelper(RequestCallback mCallback, Context context) {
		super(mCallback, context);
	}
	
	public WebServiceHelper(OnSuccessListener listener, Context context) {
		super(listener, context);
	}

	
	/**
	 * 登录
	 * @param username
	 * @param pwd
	 */
	public void login(String username, String pwd){
		String params = String.format(
				"{\"loginname\":\"%s\",\"password\":\"%s\"}",
				username, pwd);
		get(getString(R.string.method_login), params, Define.INFO_LOGIN.class);
	}
	
	/**
	 * 判断商家用户名或者全称是否已存在
	 * @param sjname
	 * @param 
	 */
	public void sjnameExist(String sjname, boolean isFullName){
		String type = isFullName ? "sjname" : "user";
		String params = String.format(
				"{\"type\":\"%s\",\"text\":\"%s\"}",
				type, sjname);
		get(getString(R.string.method_existsjname), params, Define.BASE.class);
	}
	
	/**
	 * 分类查询统一方法
	 */
	public void getCLWDfl(String id){
		String params = String.format(
				"{\"flid\":\"%s\",\"depth\":\"2;3\"}",
				id);
		get(getString(R.string.method_getCLWDfl), params, Define.BASE.class);
	}
	
	public void getCodingArea(){
		get(getString(R.string.method_getCodingArea), null, Define.BASE.class);
	}

	private String getString(int resId) {
		if (mContext == null) {
			return null;
		}
		return mContext.getString(resId);
	}
}
