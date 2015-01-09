package cn.com.hyrt.carserverseller.base.helper;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import android.content.Context;
import android.graphics.Bitmap;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ZZVERTIFICATION_DETITAL_LIST;

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
	
	public static final String IMAGE_TYPE_SJ = "sjimage";
	public static final String IMAGE_TYPE_ZZ = "zzimage";
	public static final String IMAGE_TYPE_SJSP = "sjspimage";
	public static final String IMAGE_TYPE_USER = "userimage";
	
	public static final String PRODUCT_TYPE_SP = "sp";
	public static final String PRODUCT_TYPE_FW = "fw";
	public static final String PRODUCT_TYPE_YH = "yh";
	
	public static final String ORDER_TYPE_NEW = "new";
	public static final String ORDER_TYPE_AND = "and";
	public static final String ORDER_TYPE_HIS = "his";
	public static final String ORDER_STATUS_ACCEPT = "cg";
	public static final String ORDER_STATUS_REFUSAL = "wcg";

	public WebServiceHelper(RequestCallback mCallback, Context context) {
		super(mCallback, context);
	}
	
	public WebServiceHelper(OnSuccessListener listener, Context context) {
		super(listener, context);
	}

	/**
	 * 获取 用户基本信息-专业类型 编码表
	 */
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
	/**
	 * 上传多张图片
	 */
	public void saveImageMonWithByte(Bitmap image, String imageName, String imageType, String id){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageByte = baos.toByteArray();
        String imageBuffer = new String(Base64.encode(imageByte));
		String params = String.format(
				"{\"imagename\":\"%s\",\"length\":\"%s\"," +
				"\"type\":\"%s\",\"userid\":\"%s\",\"id\":\"%s\"}",
				imageName, imageByte.length+"", imageType, getUserId(), id);
		uploadImage(getString(R.string.method_saveImageMonWithByte), params, imageBuffer, Define.BASE.class);
	}
	
	/**
	 * 上传资质时先删除旧图片
	 */
	public void deleteOldImage(String id,String imageType){
		String params = String.format(
				"{\"id\":\"%s\",\"type\":\"%s\"}",
				id,imageType);
		get(getString(R.string.method_saveAttacdel), params,Define.BASE.class);
	}
	
	/**
	 * 保存商品信息
	 */
	public void saveProductInfo(Define.INFO_PRODUCT productInfo){
		productInfo.userid = getUserId();
		productInfo.serviceid = getServiceId();
		String params = new Gson().toJson(productInfo);
		get(getString(R.string.method_saveMerchantComm), params,
				Define.SINGLE_ID.class);
	}
	
	private String getUserId(){
		return ((CarServerApplication)mContext
				.getApplicationContext()).getLoginInfo().id;
	}
	
	private String getServiceId(){
		return ((CarServerApplication)mContext
				.getApplicationContext()).getLoginInfo().serviceid;
	}

	private String getString(int resId) {
		if (mContext == null) {
			return null;
		}
		return mContext.getString(resId);
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
	 * 注册
	 * @param userName
	 * @param pwd
	 */
	public void regist(String userName, String pwd){
		String params = String.format(
				"{\"loginname\":\"%s\",\"password\":\"%s\"}",
				userName, pwd);
		get(getString(R.string.method_regist), params, Define.SINGLE_ID.class);
	}
	
	/**
	 * 
	 * @param type 类型
	 * @param page 页码
	 */
	public void getProductInfoList(String type, int page){
		String params = String.format(
				"{\"serviceid\":\"%s\",\"type\":\"%s\",\"page\":\"%s\"}"
				, getServiceId(), type, page+"");
		get(
				getString(R.string.method_getProductInfoList),
				params, Define.INFO_PRODUCT_LIST.class);
	}
	
	public void getProductInfo(String productId){
		String params = String.format(
				"{\"id\":\"%s\"}"
				, productId);
		get(
				getString(R.string.method_getMerchantComm),
				params, Define.INFO_PRODUCT_LIST.CDATA.class);
	}
	
	public void getCodingArea(){
		get(getString(R.string.method_getCodingArea), null, Define.BASE.class);
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
	
	/**
	 * 获取服务类型
	 */
	public void getFwClassList(){
		get(getString(R.string.method_getFwClassList), null, Define.BASE.class);
	}
	/**
	 * 获取服务类型
	 */
	public void getFwSpClassList(String type){
		String params = String.format(//TODO
				"{\"type\":\"%s\",\"sjid\":\"%s\"}",
				type, getServiceId());
		get(getString(R.string.method_getFwSpClassList), params, Define.BASE.class);
	}
	
	/**
	 * 保存商户信息
	 * @param merchant
	 */
	public void saveMerchant(Define.SAVE_MERCHANT merchant){
		merchant.id = getServiceId();
		merchant.userid = getUserId();
		String params = new Gson().toJson(merchant);
		LogHelper.i("tag", "params:"+params);
		get(getString(R.string.method_saveMerchant),
				params, Define.SINGLE_ID.class);
	}
	
	/**
	 * 获取商户信息
	 */
	public void getMerchantInfo(){
		String params = String.format(
				"{\"serviceid\":\"%s\"}"
				, getServiceId());
		get(
				getString(R.string.method_getMerchantInfo),
				params, Define.INFO_MERCHANT.class);
	}
	
	/**
	 * 判断商家全称是否存在
	 * @param text
	 * @param isSjName
	 */
	public void getMerchantUserinfo(String text, boolean isSjName){
		String nameType = isSjName ? "sjname" : "user";
		String params = String.format(
				"{\"text\":\"%s\",\"type\":\"%s\"}",
				text, nameType);
		get(getString(R.string.method_getMerchantUserinfo),
				params, Define.BASE.class);
	}
	
	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 */
	public void changePwd(String oldPwd, String newPwd){
		String params = String.format(
				"{\"id\":\"%s\",\"password\":\"%s\",\"newpassword\":\"%s\"}",
				getUserId(), oldPwd, newPwd);
		get(getString(R.string.method_saveUserinfo),
				params, Define.BASE.class);
	}
	
	/**
	 * 商品下架
	 * @param spId
	 */
	public void closeProduct(String spId){
		String params = String.format("{\"spid\":\"%s\"}", spId);
		get(
				getString(R.string.method_saveDelMerchantCommStatus),
				params, Define.BASE.class);
	}
	
	public void getProduct(String spId){
		String params = String.format("{\"id\":\"%s\"}", spId);
		get(
				getString(R.string.method_getMerchantComm),
				params, Define.INFO_PRODUCT_LIST.CDATA.class);
	}
	
	public void getOrders(String type, int page){
		String params = String.format(
				"{\"sjid\":\"%s\",\"type\":\"%s\"" +
				",\"page\":\"%s\"}", getServiceId(), type, page);
		get(
				getString(R.string.method_getMwpmSysTerminalUsermakeList),
				params, Define.ORDER_LIST.class);
	}
	
	/**
	 * 保存预约成功&拒绝
	 * @param id
	 * @param type
	 */
	public void saveOrderStatus(String id, String type){
		String params = String.format(
				"{\"id\":\"%s\",\"type\":\"%s\"}",
				id, type);
		get(
				getString(R.string.method_saveTerminalUsermakeStatus),
				params, Define.BASE.class);
	}
	
	/**
	 * 获取优惠列表
	 * @param page
	 */
	public void getPreferentials(int page){
		String params = String.format(
				"{\"userid\":\"%s\",\"page\":\"%s\"}",
				getUserId(), page+"");
		get(
				getString(R.string.method_getMwpmSysCarKnowList),
				params, Define.PREFERENTIAL_LIST.class);
	}
	
	/**
	 * 获取优惠类型列表
	 */
	public void getDiscountTypes(){
		get(
				getString(R.string.method_getMwpmSysCarKnowYhTypeList),
				null, Define.DISCOUNT_TYPE_LIST.class);
	}
	
	/**
	 * 保存优惠信息
	 */
	public void savePreferential(Define.INFO_PREFERENTIAL preferentialInfo){
		preferentialInfo.userid = getUserId();
		Gson mGson = new Gson();
		String params = mGson.toJson(preferentialInfo);
		get(
				getString(R.string.method_saveMwpmSysCarKnowInfo),
				params, Define.SINGLE_ID.class);	
	}
	
	/**
	 * 获取审核列表
	 * @param type
	 * @param page
	 */
	public void getAuditInfoList(String type, int page){
		String params = String.format(
				"{\"userid\":\"%s\",\"page\":\"%s\"," +
				"\"type\":\"%s\",\"sjid\":\"%s\"}",
				getUserId(), page, type, getServiceId());
		get(
				getString(R.string.method_getWorkflowbhList),
				params, Define.INFO_AUDIT_LIST.class);	
	}
	
	/**
	 * 获取认证类型
	 */
	public void getVerificationType(){
		String params = String.format(
				"{\"merchantid\":\"%s\"}",
				getServiceId());
		get(
				getString(R.string.method_getMerchantQualificImage),
				params, Define.BASE.class);	
	}
	
	/**
	 * 申请认证
	 */
	public void saveVerification(String zztype, String level, String qualificimageid){
		String params = String.format(
				"{\"userid\":\"%s\",\"id\":\"%s\"," +
				"\"zztype\":\"%s\",\"level\":\"%s\"," +
				"\"qualificimageid\":\"%s\"}",
				getUserId(), getServiceId(),
				zztype, level, qualificimageid);
		get(
				getString(R.string.method_saveMerchantQualificStatus),
				params, Define.ZZQUALIFICIMAGE_ID.class);	
	}
	
	/**
	 * 取得申请资质列表
	 */
	public void getMerchantQualificList(){
		String params = String.format(
				"{\"merchantid\":\"%s\"}",
				getServiceId());
		get(
				getString(R.string.method_getMerchantQualificList),
				params, Define.ZZVERTIFICATION_LIST.class);	
	}
	
	/**
	 * 取得申请资质详细
	 */
	public void getMerchantQualificInfo(String typeid){
		String params = String.format(
				"{\"typeid\":\"%s\"}",typeid);
		get(
				getString(R.string.method_getMerchantQualificInfo),
				params, ZZVERTIFICATION_DETITAL_LIST.class);	
	}
	
	/**
	 * 获取版本号
	 */
	public void getVersionInfoList(){
		get("getVersionInfoList",null,Define.VS_INFO_LIST.class);
	}
}
