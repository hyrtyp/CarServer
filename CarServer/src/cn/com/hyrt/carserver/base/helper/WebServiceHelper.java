package cn.com.hyrt.carserver.base.helper;

import java.util.HashMap;
import java.util.Map;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import android.content.Context;

/**
 * WebService请求助手
 * @author zoe
 *
 */
public class WebServiceHelper extends BaseWebServiceHelper{

	public WebServiceHelper(RequestCallback mCallback, Context context) {
		super(mCallback, context);
	}

	/**
	 * 获取 用户基本信息-专业类型 编码表
	 */
	public void getTerminalUserinfoOccupationCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"TERMINAL_USERINFO_OCCUPATION\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 商家服务项目-商家提供服务 编码表
	 */
	public void getMerchantServiceCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"MERCHANT_SERVICE\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-品牌（大众） 编码表
	 */
	public void getCarBrandCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"CAR_BRAND\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-型号（捷达） 编码表
	 */
	public void getCarModelCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"CAR_MODEL\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-子型号 编码表
	 */
	public void getCarSubmodelCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"CAR_SUBMODEL\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-车辆类型 编码表
	 */
	public void getCarCartypeCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"CAR_CARTYPE\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-保险种类 编码表
	 */
	public void getCarInsurancetypeCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"CAR_INSURANCETYPE\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 咨询-咨询类型 编码表
	 */
	public void getSeekSeekTypeCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"SEEK_SEEKTYPE\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 新闻类型 编码表
	 */
	public void getNewNewTypeCode(){
		Map<String, String> params = new HashMap<String, String>();
		params.put("jsonstr", "{\"type\":\"NEW_NEWTYPE\"}");
		get(mContext.getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
}
