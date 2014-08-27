package cn.com.hyrt.carserversurvey.base.baseFunction;

import java.util.ArrayList;


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
	public static int RESULT_FROM_ALTER_CAR = 101;
	public static int RESULT_FROM_CHANGE_INFO = 102;

	public static class BASE {
		public String code;
		public String message;
	}
	
	public static class INFO_LOGIN extends BASE {
		public String id;
		public String loginname;
	}
	
	public static class SAVE_INFO extends BASE {
		public String id;
		public String password;
		public String newpassword; 
	}
	
	/**
	 * 注册记录列表
	 *
	 */
	public static class REGRECODE extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA {
			public String id;// ID
			public String recodeimagepath;// 图片地址
			public String sjname;//商家名称
			public String sjaddress; //详细地址
			public String peopledate;//采集时间 
			
		}
	}
}
