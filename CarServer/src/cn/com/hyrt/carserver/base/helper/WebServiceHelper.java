package cn.com.hyrt.carserver.base.helper;

import java.lang.reflect.Type;
import java.util.LinkedList;

import android.content.Context;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.BaseWebServiceHelper.OnSuccessListener;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
	public void getTerminalUserinfoOccupationCode() {
		String params = "{\"type\":\"TERMINAL_USERINFO_OCCUPATION\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 商家服务项目-商家提供服务 编码表
	 */
	public void getMerchantServiceCode() {
		String params = "{\"type\":\"MERCHANT_SERVICE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 车辆基本信息-品牌（大众） 编码表
	 */
	public void getCarBrandCode() {
		String params = "{\"type\":\"CAR_BRAND\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 车辆基本信息-型号（捷达） 编码表
	 */
	public void getCarModelCode() {
		String params = "{\"type\":\"CAR_MODEL\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 车辆基本信息-子型号 编码表
	 */
	public void getCarSubmodelCode() {
		String params = "{\"type\":\"CAR_SUBMODEL\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 车辆基本信息-车辆类型 编码表
	 */
	public void getCarCartypeCode() {
		String params = "{\"type\":\"CAR_CARTYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 车辆基本信息-保险种类 编码表
	 */
	public void getCarInsurancetypeCode() {
		String params = "{\"type\":\"CAR_INSURANCETYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 咨询-咨询类型 编码表
	 */
	public void getSeekSeekTypeCode() {
		String params = "{\"type\":\"SEEK_SEEKTYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 获取 新闻类型 编码表
	 */
	public void getNewNewTypeCode() {
		String params = "{\"type\":\"NEW_NEWTYPE\"}";
		get(getString(R.string.method_getcode), params, Define.CODE.class);
	}

	/**
	 * 登录
	 * 
	 * @param loginname
	 * @param password
	 */
	public void login(String loginname, String password) {
		String params = String.format("{\"loginname\":%s,\"password\":%s}",
				loginname, password);
		get(getString(R.string.method_login), params, Define.INFO_LOGIN.class);
	}

	/**
	 * 获取用户信息
	 * 
	 * @param id
	 */
	public void getUserInfo() {
		String id = getUserId();
		if (id == null) {
			return;
		}
		String params = String.format("{\"id\":\"%s\"}", id);
		get(mContext.getString(R.string.method_getUserInfo), params,
				Define.INFO.class);
	}

	/**
	 * 保存用户信息
	 */
	public void saveUserInfo(Define.INFO_SAVE info) {
		Gson mGson = new Gson();
		String params = mGson.toJson(info);
		LogHelper.i("tag", "params:" + params);
		get(mContext.getString(R.string.method_saveUserinfo), params,
				Define.BASE.class);
	}

	/**
	 * 获取车辆列表
	 */
	public void getTerminalCarList() {
		String id = getUserId();
		if (id == null) {
			return;
		}
		String params = String.format("{\"id\":\"%s\"}", id);
		get(mContext.getString(R.string.method_getTerminalCar), params,
				Define.INFO_CAR_LIST.class);
	}

	/**
	 * 添加&修改车辆（carid如果有值,代表修改某个用户的所属车辆,如果没值,则新增）
	 */
	public void alterCar(Define.INFO_CAR car) {
		String id = getUserId();
		if (id == null) {
			return;
		}
		car.terminaluserid = id;
		Gson mGson = new Gson();
		String params = mGson.toJson(car);
		LogHelper.i("tag", "params:" + params);
		get(mContext.getString(R.string.method_saveTerminalCar), params,
				Define.INFO_CAR.class);
	}

	public void getCarInfo(String id) {
		String params = String.format("{\"id\":\"%s\"}", id);
		get(mContext.getString(R.string.method_getTerminalCarInfo), params,
				Define.INFO_CAR.class);
	}

	/**
	 * 获取提问回复列表
	 * 
	 * @param page
	 * @param type
	 */
	public void getSeekReplyList(int page, String type) {
		String id = getUserId();
		if (id == null) {
			return;
		}
		String params = String.format(
				"{\"userid\":\"%s\",\"page\":\"%s\",\"type\":\"%s\"}", id, page
						+ "", type);
		LogHelper.i("tag", "params:" + params);
		get(getString(R.string.method_seekReplyList), params,
				Define.SEEK_REPLY_LIST.class);
	}

	/**
	 * 获取回复详情
	 * 
	 * @param replyId
	 * @param type
	 */
	public void getReplyDetail(String replyId, String type) {
		String params = String.format("{\"%s\":\"%s\"}", type, replyId);
		get(getString(R.string.method_replyDetail), params,
				Define.REPLY_DETAIL.class);
	}

	/**
	 * 获取年份类别
	 * 
	 * @param carid
	 *            车辆ID
	 * @param type
	 *            （0：保养信息 1：维修信息 2：保险信息 3：年检信息）
	 */
	public void getConditionYear(String carid, int type) {
		String method;
		switch (type) {
		case 0:
			method = getString(R.string.method_maintenance_year);
			break;
		case 1:
			method = getString(R.string.method_repair_year);
			break;
		case 2:
			method = getString(R.string.method_insurance_year);
			break;
		case 3:
			method = getString(R.string.method_yearcheck_year);
			break;

		default:
			return;
		}

		LogHelper.i("tag", "method:" + method + " type:" + type);

		String params = String.format("{\"carid\":\"%s\"}", carid);
		get(method, params, Define.INFO_YEAR.class);
	}

	/**
	 * 获取保养信息
	 * 
	 * @param carid
	 *            车辆主键ID
	 * @param year
	 *            所选年份
	 * @param page
	 *            页码
	 */
	public void getMaintenanceInfo(String carid, String year, int page) {
		String params = String.format("{\"carid\":\"%s\","
				+ "\"year\":\"%s\",\"page\":\"%s\"}", carid, year, page + "");
		get(getString(R.string.method_maintenance_for_year), params,
				Define.INFO_MAINTENANCE_LIST.class);
	}

	/**
	 * 获取维修信息
	 * 
	 * @param carid
	 *            车辆主键ID
	 * @param year
	 *            所选年份
	 * @param page
	 *            页码
	 */
	public void getRepairInfo(String carid, String year, int page) {
		String params = String.format("{\"carid\":\"%s\","
				+ "\"year\":\"%s\",\"page\":\"%s\"}", carid, year, page + "");
		get(getString(R.string.method_repair_for_year), params,
				Define.INFO_REPAIR_LIST.class);
	}

	/**
	 * 获取保险信息
	 * 
	 * @param carid
	 *            车辆主键ID
	 * @param year
	 *            所选年份
	 * @param page
	 *            页码
	 */
	public void getInsuranceInfo(String carid, String year, int page) {
		String params = String.format("{\"carid\":\"%s\","
				+ "\"year\":\"%s\",\"page\":\"%s\"}", carid, year, page + "");
		get(getString(R.string.method_insurance_for_year), params,
				Define.INFO_INSURANCE_LIST.class);
	}

	/**
	 * 获取年检信息
	 * 
	 * @param carid
	 *            车辆主键ID
	 * @param year
	 *            所选年份
	 * @param page
	 *            页码
	 */
	public void getYearCheckInfo(String carid, String year, int page) {
		String params = String.format("{\"carid\":\"%s\","
				+ "\"year\":\"%s\",\"page\":\"%s\"}", carid, year, page + "");
		get(getString(R.string.method_yearcheck_for_year), params,
				Define.INFO_YEARCHECK_LIST.class);
	}

	/**
	 * 获取 保险理陪 列表
	 */
	public void getInsuranceClaim() {
		get(getString(R.string.method_insuranceClaim), null,
				Define.INSURANCE_CLAIM_LIST.class);
	}

	/**
	 * 获取维修保养分类
	 */
	public void getMaintainFL(String id){
		String params = 
				String.format("{\"flid\":\"%s\"}",id);
		get(getString(R.string.method_question_maintainfl), params, Define.BASE.class);
	}
	
	/**
	 * 获取相关问题
	 */
	public void getCorrelation(String id,int pageNo) {
		String params = String.format("{\"id\":\"%s\",\"pageNo\":\"%s\"}", id,pageNo);
		get(getString(R.string.method_question_correlation), params,Define.QUESTION_CORRELATION.class);
	}
	
	/**
	 * 获取部件位置
	 */
	public void getPoisition(){
		get(getString(R.string.method_question_position), null, Define.QUESTION_POISTION.class);
	}
	
	
	/**
	 * 保存问题信息
	 */
	public void saveQuestionInfo(Define.QUESTION_SAVE info) {
		Gson mGson = new Gson();
		String params = mGson.toJson(info);
		LogHelper.i("tag", "params:" + params);
		get(mContext.getString(R.string.method_question_save), params,Define.BASE.class);
	}
	
	/**
	 * 获取维修自查分类
	 */
	public void getMaintainCheck(){
		String params = String.format(
				"{\"flid\":\"%s\",\"depath\":\"3\"}",
				getString(R.string.id_question_maintainfl2));
		get(
				getString(R.string.method_question_maintainfl),
				params, Define.BASE.class);
	}
	
	/**
	 * 获取车辆装饰分类
	 */
	public void getCarDecoration(){
		String params = String.format(
				"{\"flid\":\"%s\"}",
				getString(R.string.id_knowledge_decoration));
		get(
				getString(R.string.method_question_maintainfl),
				params, Define.BASE.class);
	}
	
	/**
	 * 获取品牌列表
	 */
	public void getBrands(){
		String params = String.format(
				"{\"flid\":\"%s\"}",
				getString(R.string.id_knowledge_brand));
		get(
				getString(R.string.method_question_maintainfl),
				params, Define.BASE.class);
	}
	
	/**
	 * 获取新闻图片
	 */
	public void getNewsImg(){
		get(getString(R.string.method_question_getnewsimg), null, Define.QUESTION_GETNEWSIMG.class);
	}

	/**
	 * 新增&修改 维修信息
	 * id如果有值,代表修改某个车辆的维修信息,如果没值,则新增
	 * carid必须添入该值
	 * @param repairInfo
	 */
	public void alterRepairInfo(Define.INFO_REPAIR_LIST.CDATA repairInfo){
		Gson mGson = new Gson();
		String params = mGson.toJson(repairInfo);
		get(getString(R.string.method_save_repair), params, Define.BASE.class);
	}
	
	/**
	 * 新增&修改 保险信息
	 * id如果有值,代表修改某个车辆的保险信息,如果没值,则新增
	 * carid必须添入该值
	 * @param repairInfo
	 */
	public void alterInsuranceInfo(Define.INFO_INSURANCE_LIST.CDATA repairInfo){
		Gson mGson = new Gson();
		String params = mGson.toJson(repairInfo);
		get(getString(R.string.method_save_insurance), params, Define.BASE.class);
	}
	
	/**
	 * 新增&修改 保养信息
	 * id如果有值,代表修改某个车辆的保养信息,如果没值,则新增
	 * carid必须添入该值
	 * @param repairInfo
	 */
	public void alterMaintenanceInfo(Define.INFO_MAINTENANCE_LIST.CDATA repairInfo){
		Gson mGson = new Gson();
		String params = mGson.toJson(repairInfo);
		get(getString(R.string.method_save_maintenance), params, Define.BASE.class);
	}
	
	/**
	 * 新增&修改 年检信息
	 * id如果有值,代表修改某个车辆的年检信息,如果没值,则新增
	 * carid必须添入该值
	 * @param repairInfo
	 */
	public void alterYearCheckInfo(Define.INFO_YEARCHECK_LIST.CDATA repairInfo){
		Gson mGson = new Gson();
		String params = mGson.toJson(repairInfo);
		get(getString(R.string.method_save_yearcheck), params, Define.BASE.class);
	}
	
	/**
	 * 搜索问答
	 * @param str 搜索值
	 * @param pageNo 页码
	 */
	public void searchQuestion(String str, int pageNo){
		String params = 
				String.format(
						"{\"jsname\":\"%s\";\"pageNo\":\"%s\"}",
						str, pageNo);
		
		get(
				getString(R.string.method_search_question),
				params, Define.QUESTION_SEARCH_RESULT.class);
	}
	
	/**
	 * 获取用户ID
	 * 
	 * @return
	 */
	private String getUserId() {
		if (CarServerApplication.loginInfo == null) {
			CarServerApplication.loginInfo = StorageHelper
					.getInstance(mContext).getLoginInfo();
		}
		return CarServerApplication.loginInfo.id;
	}

	private String getString(int resId) {
		if (mContext == null) {
			return null;
		}
		return mContext.getString(resId);
	}
}
