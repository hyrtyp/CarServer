package cn.com.hyrt.carserver.base.baseFunction;

import java.util.ArrayList;

/**
 * 全局定义
 * @author zoe
 *
 */
public class Define {
	
	public static int REQUEST_SUCCESS_CODE = 200;
	public static int REQUEST_ERROR_CODE = 500;
	
	public static class TEST{
		public int code;
		public String msg;
		public DATA data;
		public String aa;
		
		public class DATA{
			public String version;
			public String download;
		}
	}
	
	public static class BASE{
		public int code;
		public String message;
	}
	
	/**
	 * 编码表
	 * @author zoe
	 *
	 */
	public static class CODE extends BASE{
		public ArrayList<CDATA> data;
		
		public class CDATA{
			public String name;
			public String codevalue;
		}
	}
	
	/**
	 * 我的信息
	 * @author zoe
	 *
	 */
	public static class INFO extends BASE{
		public String id;//用户ID
		public String phone;//手机号码
		public String sex;//性别
		public String occupation;//职业
		public String unitname;//单位名称
		public int status;//用户状态
		public String password;//密码
		public double coorX;//X坐标
		public double coorY;//Y坐标
		public int integral;//积分
		public int level;//会员级别
		public String imagepath;//图片路径
		public String name;//用户姓名
		public int goldcoin;//金币
	}
	
	/**
	 * 我的信息保存
	 * @author zoe
	 *
	 */
	public static class INFO_SAVE extends BASE{
		
	}
	
	/**
	 * 登陆信息
	 * @author zoe
	 *
	 */
	public static class INFO_LOGIN extends BASE{
		public String id;//用户ID
		public String loginname;//用户登陆账号
		public String name;//用户名称
	}
}
