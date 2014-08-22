package cn.com.hyrt.carserverseller.base.helper;

import android.content.Context;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;

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
	 * 获取 用户基本信息-专业类型 编码表
	 */
	/*public void getTerminalUserinfoOccupationCode() {
		String params = "{\"type\":\"TERMINAL_USERINFO_OCCUPATION\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}*/

	private String getString(int resId) {
		if (mContext == null) {
			return null;
		}
		return mContext.getString(resId);
	}
}
