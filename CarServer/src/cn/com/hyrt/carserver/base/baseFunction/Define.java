package cn.com.hyrt.carserver.base.baseFunction;

import java.util.ArrayList;

/**
 * 全局定义
 * @author zoe
 *
 */
@SuppressWarnings("unused")
public class Define {
	
	public static int REQUEST_SUCCESS_CODE = 200;
	public static int REQUEST_ERROR_CODE = 500;
	
	public static int RESULT_FROM_ALTER_CAR = 101;
	
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
		public String status;//用户状态
		public String password;//密码
		public String coorX;//X坐标
		public String coorY;//Y坐标
		public String integral;//积分
		public String level;//会员级别
		public String imagepath;//图片路径
		public String name;//用户姓名
		public String goldcoin;//金币
		@Override
		public String toString() {
			return "INFO [id=" + id + ", phone=" + phone + ", sex=" + sex
					+ ", occupation=" + occupation + ", unitname=" + unitname
					+ ", status=" + status + ", password=" + password
					+ ", coorX=" + coorX + ", coorY=" + coorY + ", integral="
					+ integral + ", level=" + level + ", imagepath="
					+ imagepath + ", name=" + name + ", goldcoin=" + goldcoin
					+ "]";
		}
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
	
	/**
	 * 我的车辆列表
	 * @author zoe
	 *
	 */
	public static class INFO_CAR_LIST extends BASE{
		public ArrayList<CDATA> data;
		public class CDATA{
			public String id;//车辆ID
			public String insurancedate;//保险时间
			public String carnumber;//车牌号
			public String model;//型号
			public String mileage;//行驶里程
			public String insurancenum;//保险单号
			public String cartype;//车辆类型
			public String terminaluserid;//终端用户ID
			public String submodel;//子型号
			public String insurancecompany;//保险公司名称
			public String checkdate;//登记日期
			public String imagepath;//图片路径
			public String manufacturer;//产品厂家
			public String insurancetype;//保险种类
			public String brand;//品牌
			public String yearcheckdate;//年检日期
			
			@Override
			public String toString() {
				return "CDATA [id=" + id + ", insurancedate=" + insurancedate
						+ ", carnumber=" + carnumber + ", model=" + model
						+ ", mileage=" + mileage + ", insurancenum="
						+ insurancenum + ", cartype=" + cartype
						+ ", terminaluserid=" + terminaluserid + ", submodel="
						+ submodel + ", insurancecompany=" + insurancecompany
						+ ", checkdate=" + checkdate + ", imagepath="
						+ imagepath + ", manufacturer=" + manufacturer
						+ ", insurancetype=" + insurancetype + ", brand="
						+ brand + ", yearcheckdate=" + yearcheckdate + "]";
			}
			
		}
	}

}
