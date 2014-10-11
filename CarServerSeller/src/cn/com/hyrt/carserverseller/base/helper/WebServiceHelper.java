package cn.com.hyrt.carserverseller.base.helper;

import java.io.ByteArrayOutputStream;

import org.kobjects.base64.Base64;

import android.content.Context;
import android.graphics.Bitmap;
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
	
	public static final String IMAGE_TYPE_SJ = "sjimage";
	public static final String IMAGE_TYPE_ZZ = "zzimage";
	public static final String IMAGE_TYPE_SJSP = "sjspimage";
	public static final String IMAGE_TYPE_USER = "userimage";

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
	 * 保存商品信息
	 */
	public void saveProductInfo(Define.INFO_PRODUCT productInfo){
		productInfo.userid = getUserId();
		String params = new Gson().toJson(productInfo);
		get(getString(R.string.method_saveMerchantComm), params,
				Define.INFO_PRODUCT.class);
	}
	
	private String getUserId(){
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
