package cn.com.hyrt.carserverexpert.base.baseFunction;

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

	public static class BASE {
		public String code;
		public String message;
	}

	public static class INFO_LOGIN extends BASE {
		public String id;
		public String loginname;
		public String name;
		public String status;
	}

	public static class SAVE_INFO extends BASE {
		public String id;//登陆时传入的用户ID
		public String name;//用户姓名
		public String loginname;//登陆名
		public String password;//原密码
		public String newpassword;//新密码
		public String professional;//所属专业ID多个使用分号分隔
		public String merchantid;//专家所属商家名称
		public String unitaddress;//单位地址
		public String unitname;//所属单位
		public String repairtel;//维修电话
		public String areaid;//所属区域ID
		public String zcstatus; //"专家是否修改专长,如果修改专长则需要重新审核
		public String brandids ; //专家品牌id1;专家品牌id2
		public String zjarea ; //专家简介
	}
	
	public static class SAVE_INFO_RESULT extends BASE {
		public String id;//用户主键ID
		public String loginname;//用户登陆名
		public String name;//用户姓名
	}
	
	public static class INFO_USER extends BASE {
		public String id;//登陆用户ID
		public String loginname;//用户登陆名
		public String zcnames;//专长名称多个以分号分隔;
		public String unitname;//单位名称
		public String status;//用户状态
		public String imagepath;//用户头像图片路径
		public String name;//用户姓名
		public String areaid;//区域ID
		public String zcids;//专家专长ID多个以分号分隔
		public String sjname;//所属商家名称
		public String zsimagepath0;//咨质图证书图片路径1
		public String zsimagepath1;//咨质图证书图片路径2
		public String brandids ; //专家品牌id1;专家品牌id2
		public String brandnames ; //专家品牌1;专家品牌2
		public String zjarea ; //专家简介 
	}
	
	public static class INFO_INTEGRATION extends BASE {
		
		public ArrayList<CDATA> data;
		
		public class CDATA{
			public String id;//积分历史主键ID
			public String integrlnum;//积分数
			public String integrltime;//取得积分时间
			public String integrltype;//取得积分类型
		}
	}
	
	
	public static class INFO_BRANDIDS extends BASE {
		
		public ArrayList<CDATA> data;
		
		public class CDATA{
			public String id;//品牌主键ID
			public String name;//名牌名称
		}
	}
	
	public static class INFO_QUESTION_LIST extends BASE {
		
		public ArrayList<CDATA> data;
		
		public class CDATA{
			public String id;//问题主键ID
			public String content;//内容
			public String isreview;//是否评论
			public String seekdate;//咨询时间
			public String status;//状态
			public String terminalid;//客户版用户ID
			public String zyname;//专业名称
			public String lasttime;//最后更新时间
		}
	}
	
	public static class UNREAD_NUM extends BASE {
		public String num;
	}
	
	public static class INFO_QUESTION_DETAIL_LIST extends BASE{
		public ArrayList<INFO_QUESTION_DETAIL> data;
	}
	
	public static class INFO_QUESTION_DETAIL extends BASE {
		public String id;
		public String userterminalid;
		public String username;
		public String replytype;
		public String contenttime;
		public String attacpathname;
		public String attacpath;
		public String zcname;
		public String replycontent;
		public String sjname;
		public String levelname;
		public String answersimage;
		@Override
		public String toString() {
			return "INFO_QUESTION_DETAIL [id=" + id + ", userterminalid="
					+ userterminalid + ", username=" + username
					+ ", replytype=" + replytype + ", contenttime="
					+ contenttime + ", attacpathname=" + attacpathname
					+ ", attacpath=" + attacpath + ", zcname=" + zcname
					+ ", replycontent=" + replycontent + ", sjname=" + sjname
					+ ", levelname=" + levelname + ", answersimage="
					+ answersimage + "]";
		}
		
	}
	
	/**
	 * 签到、获取签到时间、获取是否签到公用实体
	 * @author zoe
	 *
	 */
	public static class INFO_SIGN_UP extends BASE {
		
		public ArrayList<CDATA> data;
		
		public class CDATA{
			public String sumnum;//当前用户累计积分总数
			public String searchnum;//本次签到所得积分数
			public String datetime;//已签到时间，格式2014-08-07 14:48:49.0
			public String issearch;//是否签到
		}
	}
	
	/**
	 * 专家回复内容
	 * @author zoe
	 *
	 */
	public static class INFO_REPLY extends BASE {
		public String consultationid;//回答问题ID
		public String userid;//专家用户ID
		public String type;//使用类型
		public String content;//回答内容
	}
	
	/**
	 * 车辆状况列表
	 * @author zoe
	 *
	 */
	public static class INFO_CAR_STATUS_LIST extends BASE {
		
		public ArrayList<CDATA> data;
		
		public class CDATA {
			public String insurancedate;//保险时间
			public String carnumber;//车牌号
			public String model;//型号
			public String mileage;//行驶里程
			public String insurancenum;//保险单号
			public String cartype;//车辆类型
			public String terminaluserid;//终端用户ID
			public String id;//当前车辆主键ID
			public String submodel;//子型号
			public String insurancecompany;//保险公司名称
			public String checkdate;//登记日期
			public String imagepath;//图片路径
			public String manufacturer;//生产厂家
			public String insurancetype;//保险种类
			public String brand;//品牌
			public String yearcheckdate;//年检日期
			public String prodate;//生产日期
			public String wxtime;//上次维修时间
		}
	}
	
	public static class CAR_YEAR extends BASE {
		
		public ArrayList<CDATA> data;
		
		public class CDATA {
			public String time;
		}
	}
	
	/**
	 * 维修信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_REPAIR_LIST extends BASE {

		public ArrayList<CDATA> data;

		public static class CDATA {
			public String id;// 主键ID
			public String mileage;// 维修里程数
			public String wxtime;// 维修时间
			public String reason;// 维修原因
			public String company;// 维修单位
			public String item;// 维修项目
			public String carid;// 车辆主键ID
			public String repair;// 送修人
			public String cost;// 维修费用
			public String situation;// 维修情况
			public String remarks;// 备注（保存接口）
		}
	}

	/**
	 * 保养信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_MAINTENANCE_LIST extends BASE {

		public ArrayList<CDATA> data;

		public static class CDATA {
			public String id;// 主键ID
			public String bytime;// 保养时间
			public String bypeople;// 保养人
			public String bymileage;// 保养里程数
			public String byitem;// 保养项目
			public String byunit;// 保养服务单位
			public String bycost;// 保养费用
			public String nextbymileage;// 下次保养里程数
			public String carid;// 车辆ID
			public String remarks;// 备注（保存接口）
		}
	}
	
//    "bycost": "556",
//    "byitem": "来咯没金看看",
//    "bymileage": "16698",
//    "bypeople": "某件事",
//    "bytime": "2015-01-21 12:00:00.0",
//    "byunit": "好咯那",
//    "carid": "f9aaab944af1c171014b0b0c0a1b02dd",
//    "code": "200",
//    "id": "f9aaab944af1c171014b0b427a0402f1",
//    "nextbymileage": "796",
//    "remarks": "咯喔喔喔喔喔"
	/**
	 * 显示保养详细信息
	 * @author Administrator
	 */
	public static class WXCDATA extends BASE {
		public String id;// 主键ID
		public String bytime;// 保养时间
		public String bypeople;// 保养人
		public String bymileage;// 保养里程数
		public String byitem;// 保养项目
		public String byunit;// 保养服务单位
		public String bycost;// 保养费用
		public String nextbymileage;// 下次保养里程数
		public String carid;// 车辆ID
		public String remarks;// 备注（保存接口）
	}
	
}
