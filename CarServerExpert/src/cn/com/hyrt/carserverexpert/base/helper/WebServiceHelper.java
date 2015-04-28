package cn.com.hyrt.carserverexpert.base.helper;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import android.content.Context;
import android.graphics.Bitmap;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.application.CarServerApplication;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;

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
	
	public static final String IMAGE_TYPE_ZJ = "zjzsimage";
	public static final String IMAGE_TYPE_USER = "userimage";
	
	public static final String QUESTION_TYPE_NEW = "new";
	public static final String QUESTION_TYPE_MY = "my";
	public static final String QUESTION_TYPE_AN = "an";
	public static final String QUESTION_TYPE_HIS = "his";

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
	 * 获取专长
	 */
	public void getSpecialtyType(){
		AlertHelper.getInstance(mContext).showLoading(null);
		get(getString(R.string.method_getCLWDfl3tb), null, Define.BASE.class);
	}
	
	/**
	 * 获取用户信息
	 */
	public void getUserInfo(){
		AlertHelper.getInstance(mContext).showLoading(null);
		String params = String.format("{\"id\":\"%s\"}", getUserId());
		get(getString(R.string.method_getUserInfo), params, Define.INFO_USER.class);
	}
	
	/**
	 * 获取区域
	 */
	public void getCodingArea() {
		AlertHelper.getInstance(mContext).showLoading(null);
		get(getString(R.string.method_getCodingArea), null, Define.BASE.class);
	}
	
	
	
	
	/**
	 * 专家版-注册显示品牌列表
	 */
	public void getSelBrandList() {
		AlertHelper.getInstance(mContext).showLoading(null);
		get(getString(R.string.method_getselbrandlist), null, Define.INFO_BRANDIDS.class);
	}
	
	/**
	 * 保存用户信息
	 * @param saveInfo
	 */
	public void saveUserInfo(Define.SAVE_INFO saveInfo){
		Gson mGson = new Gson();
		String params = mGson.toJson(saveInfo);
		get(
				getString(R.string.method_saveUserinfo),
				params, Define.SAVE_INFO_RESULT.class);
	}
	
	
	/**
	 * 获取积分列表
	 * @param page
	 */
	public void getIntegration(int page){
		String params = String.format(
				"{\"userid\":\"%s\",\"page\":\"%s\"}",
				getUserId(), page+"");
		get(
				getString(R.string.method_getIntegrationList),
				params, Define.INFO_INTEGRATION.class);
	}
	
	/**
	 * 修改用户名密码
	 * @param oldPwd
	 * @param newPwd
	 */
	public void changePwd(String oldPwd, String newPwd) {
		String params = String.format(
				"{\"id\":\"%s\",\"password\":\"%s\",\"newpassword\":\"%s\"}",
				getUserId(), oldPwd, newPwd);
		get(
				getString(R.string.method_saveUserinfo),
				params, Define.BASE.class);
	}
	
	/**
	 * 获取问题列表
	 * @param type
	 * @param page
	 */
	public void getQuestionInfoList(String type, int page, String searchText){
		String params = String.format(
				"{\"userid\":\"%s\",\"type\":\"%s\",\"page\":\"%s\",\"text\":\"%s\"}",
				getUserId(), type, page, searchText);
		get(
				getString(R.string.method_getZjCLWDwdlb),
				params, Define.INFO_QUESTION_LIST.class);
	}
	
	/**
	 * 获取最新问题条数
	 */
	public void getUnreadNum(){
		String params = String.format("{\"userid\":\"%s\"}", getUserId());
		get(
				getString(R.string.method_getTerminalSeekReplyNum),
				params, Define.UNREAD_NUM.class);
	}
	
	/**
	 * 获取问题详情
	 * @param id 问题ID
	 */
	public void getQuestionDetail(String id){
		String params = String.format(
				"{\"wtid\":\"%s\"}",
				id);
		get(
				getString(R.string.method_getCLWDwd),
				params, Define.INFO_QUESTION_DETAIL_LIST.class);
	}
	
	/**
	 * 获取签到日期
	 * @param startTime 起始时间
	 * @param endTime   结束时间
	 */
	public void getSignUpDays(String startTime, String endTime){
		String params = 
				String.format(
						"{\"terminalid\":\"%s\",\"starttime\":\"%s\",\"endtime\":\"%s\"}",
						getUserId(), startTime, endTime);
		
		get(
			getString(R.string.method_get_cap_sys_user_search),
			params, Define.INFO_SIGN_UP.class);
	}
	
	/**
	 * 获取当天签到状态（y:已签到 n:未签到）
	 */
	public void getSignUpStatus(){
		String params = String.format("{\"terminalid\":\"%s\"}", getUserId());
		get(
				getString(R.string.method_get_user_search),
				params, Define.INFO_SIGN_UP.class);
	}
	
	/**
	 * 签到
	 */
	public void setSignUp(){
		String params = String.format("{\"terminalid\":\"%s\"}", getUserId());
		get(
				getString(R.string.method_save_cap_sys_user_search),
				params, Define.INFO_SIGN_UP.class);
	}
	
	/**
	 * 专家回复
	 */
	public void saveReply(Define.INFO_REPLY replyInfo){
		replyInfo.userid = getUserId();
		String params = new Gson().toJson(replyInfo);
		get(
				getString(R.string.method_saveMwpmSysTerminalSeekReply),
				params, Define.BASE.class);
	}
	
	/**
	 * 获取车辆状况列表
	 * @param id
	 */
	public void getCarStatusList(String id){
		String params = String.format("{\"id\":\"%s\"}", id);
		get(
				getString(R.string.method_getTerminalCar),
				params, Define.INFO_CAR_STATUS_LIST.class);
	}
	
	/**
	 * 获取维修信息年份
	 * @param carId
	 */
	public void getRepairYear(String carId){
		String params = String.format("{\"carid\":\"%s\"}", carId);
		get(
				getString(R.string.method_getTerminalCarStatusYear),
				params, Define.CAR_YEAR.class);
	}
	
	/**
	 * 获取保养信息年份
	 * @param carId
	 */
	public void getMaintenanceYear(String carId){
		String params = String.format("{\"carid\":\"%s\"}", carId);
		get(
				getString(R.string.method_getTerminalCarMaintenanceYear),
				params, Define.CAR_YEAR.class);
	}
	
	/**
	 * 获取维修信息
	 * @param carId
	 * @param year
	 * @param page
	 */
	public void getRepairInfoList(String carId, String year, int page){
		String params = String.format(
				"{\"carid\":\"%s\",\"year\":\"%s\",\"page\":\"%s\"}",
				carId, year, page);
		get(
				getString(R.string.method_getTerminalCarStatusYearList),
				params, Define.INFO_REPAIR_LIST.class);
	}
	
	/**
	 * 获取保养信息
	 * @param carId
	 * @param year
	 * @param page
	 */
	public void getMaintenanceInfoList(String carId, String year, int page){
		String params = String.format(
				"{\"carid\":\"%s\",\"year\":\"%s\",\"page\":\"%s\"}",
				carId, year, page);
		get(
				getString(R.string.method_getTerminalCarMaintenanceList),
				params, Define.INFO_MAINTENANCE_LIST.class);
	}
	/**
	 * 获取保养详情
	 * @param Id
	 */
	public void getTerminalCarMaintenanceInfo(String id){
		String params = String.format(
				"{\"id\":\"%s\"}",id);
		get(
				getString(R.string.method_getTerminalCarMaintenanceInfo),
				params, Define.WXCDATA.class);
	}
	
	public void saveImage(Bitmap image, String imageName, String imageType, String id){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String imageBuffer = new String(Base64.encode(imageByte));
		String params = String.format(
				"{\"imagename\":\"%s\",\"length\":\"%s\"," +
				"\"type\":\"%s\",\"userid\":\"%s\",\"id\":\"%s\"}",
				imageName, imageByte.length+"", imageType, getUserId(), id);
		uploadImage(getString(R.string.method_saveImageWithByte), params, imageBuffer, Define.BASE.class);
	}
	
	public String getUserId(){
		return ((CarServerApplication)mContext
				.getApplicationContext()).getLoginInfo().id;
	}

	private String getString(int resId) {
		if (mContext == null) {
			return null;
		}
		return mContext.getString(resId);
	}
	
}

