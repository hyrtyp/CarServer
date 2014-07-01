package cn.com.hyrt.carserver.base.helper;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
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
		String params = "{\"type\":\"TERMINAL_USERINFO_OCCUPATION\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 商家服务项目-商家提供服务 编码表
	 */
	public void getMerchantServiceCode(){
		String params = "{\"type\":\"MERCHANT_SERVICE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-品牌（大众） 编码表
	 */
	public void getCarBrandCode(){
		String params = "{\"type\":\"CAR_BRAND\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-型号（捷达） 编码表
	 */
	public void getCarModelCode(){
		String params = "{\"type\":\"CAR_MODEL\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-子型号 编码表
	 */
	public void getCarSubmodelCode(){
		String params = "{\"type\":\"CAR_SUBMODEL\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-车辆类型 编码表
	 */
	public void getCarCartypeCode(){
		String params = "{\"type\":\"CAR_CARTYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 车辆基本信息-保险种类 编码表
	 */
	public void getCarInsurancetypeCode(){
		String params = "{\"type\":\"CAR_INSURANCETYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 咨询-咨询类型 编码表
	 */
	public void getSeekSeekTypeCode(){
		String params = "{\"type\":\"SEEK_SEEKTYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 获取 新闻类型 编码表
	 */
	public void getNewNewTypeCode(){
		String params = "{\"type\":\"NEW_NEWTYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}
	
	/**
	 * 登录
	 * @param loginname
	 * @param password
	 */
	public void login(String loginname, String password){
		String params = String.format("{\"loginname\":%s,\"password\":%s}", loginname, password);
		get(getString(R.string.method_login), params, Define.INFO_LOGIN.class);
	}
	
	/**
	 * 获取用户信息
	 * @param id
	 */
	public void getUserInfo(){
		String id = getUserId();
		if(id == null){
			return;
		}
		String params = String.format("{\"id\":\"%s\"}", id);
		get(mContext.getString(R.string.method_getUserInfo), params, Define.INFO.class);
	}
	
	/**
	 * 保存用户信息
	 */
	public void saveUserInfo(Define.INFO_SAVE info){
		Gson mGson = new Gson();
		String params = mGson.toJson(info);
		LogHelper.i("tag", "params:"+params);
		get(mContext.getString(R.string.method_saveUserinfo), params, Define.BASE.class);
	}
	
	/**
	 * 获取车辆列表
	 */
	public void getTerminalCarList(){
		String id = getUserId();
		if(id == null){
			return;
		}
		String params = String.format("{\"id\":\"%s\"}", id);
		get(mContext.getString(R.string.method_getTerminalCar), params, Define.INFO_CAR_LIST.class);
	}
	
	/**
	 * 添加车辆
	 */
	public void addCar(){
		
	}
	
	private String getUserId(){
		if(CarServerApplication.loginInfo == null){
			CarServerApplication.loginInfo = StorageHelper.getInstance(mContext).getLoginInfo();
		}
		return CarServerApplication.loginInfo.id;
	}
	
	
	private String getString(int resId){
		if(mContext == null){
			return null;
		}
		return mContext.getString(resId);
	}
}
