package cn.com.hyrt.carserversurvey.base.baseFunction;

/**
 * 全局定义
 * 
 * @author zoe
 * 
 */

public class Define {

	public static String REQUEST_SUCCESS_CODE = "200";
	public static String REQUEST_SAVE_SUCCESS_CODE = "205";
	public static String REQUEST_ERROR_CODE = "500";


	public static class BASE {
		public String code;
		public String message;
	}
	
	public static class INFO_LOGIN extends BASE {
		public String id;
		public String loginname;
	}
}
