package cn.com.hyrt.carserver.base.baseFunction;

import java.util.ArrayList;
import java.util.List;

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

	public static class TEST {
		public int code;
		public String msg;
		public DATA data;
		public String aa;

		public class DATA {
			public String version;
			public String download;
		}
	}

	public static class BASE {
		public String code;
		public String message;
	}

	/**
	 * 编码表
	 * 
	 * @author zoe
	 * 
	 */
	public static class CODE extends BASE {
		public ArrayList<CDATA> data;

		public class CDATA {
			public String name;
			public String codevalue;
		}
	}

	/**
	 * 我的信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO extends BASE {
		public String id;// 用户ID
		public String phone;// 手机号码
		public String sex;// 性别
		public String occupation;// 职业
		public String unitname;// 单位名称
		public String status;// 用户状态
		public String password;// 密码
		public String coorX;// X坐标
		public String coorY;// Y坐标
		public String integral;// 积分
		public String level;// 会员级别
		public String imagepath;// 图片路径
		public String name;// 用户姓名
		public String goldcoin;// 金币

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
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_SAVE extends BASE {
		public String id;// 登陆时传入的用户ID
		public String name;// 用户姓名
		public String password;// 密码
		public String newpassword;// 新密码
		public String sex;// 性别
		public String occupation;// 职业
		public String unitname;// 单位名称
		public String image;// 用户头像
		public String imagename;// 图像名称
		public String loginname; //登录账号
	}

	/**
	 * 登陆信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_LOGIN extends BASE {
		public String id;// 用户ID
		public String loginname;// 用户登陆账号
		public String name;// 用户名称
	}

	/**
	 * 我的车辆列表
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_CAR_LIST extends BASE {
		public ArrayList<CDATA> data;

		public class CDATA {
			public String id;// 车辆ID
			public String insurancedate;// 保险时间
			public String carnumber;// 车牌号
			public String model;// 型号
			public String mileage;// 行驶里程
			public String insurancenum;// 保险单号
			public String cartype;// 车辆类型
			public String terminaluserid;// 终端用户ID
			public String submodel;// 子型号
			public String insurancecompany;// 保险公司名称
			public String checkdate;// 登记日期
			public String imagepath;// 图片路径
			public String manufacturer;// 产品厂家
			public String insurancetype;// 保险种类
			public String brand;// 品牌
			public String yearcheckdate;// 年检日期

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

	/**
	 * 保存车辆信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_CAR extends BASE {
		public String id;// 当前车辆主键ID
		public String carid;// 当前车辆主键ID
		public String terminaluserid;// 终端用户ID
		public String insurancedate;// 保险时间
		public String carnumber;// 车牌号
		public String model;// 型号
		public String mileage;// 行驶里程
		public String insurancenum;// 保险单号
		public String cartype;// 车辆类型
		public String submodel;// 子型号
		public String insurancecompany;// 保险公司名称
		public String checkdate;// 登记日期
		public String imagepath;// 图片Base64
		public String imagename;// 图像名称
		public String manufacturer;// 生产厂家
		public String insurancetype;// 保险种类
		public String brand;// 品牌
		public String yearcheckdate;// 年检日期
	}

	/**
	 * 最新咨询列表
	 * 
	 * @author zoe status （排队中pd 已关闭gb 未解决wjj）
	 */
	public static class SEEK_REPLY_LIST extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA {
			public String content;// 咨询内容
			public String consultationid;// 咨询主键ID
			public String seekdate;// 咨询时间
			public String status;// 问题状态
			public String isfree;// 是否免费
			public String classname;// 分类名称
			public String classid;// 分类ID
		}
	}

	/**
	 * 问题详情
	 * 
	 * @author zoe
	 * 
	 */
	public static class REPLY_DETAIL extends BASE {

		public ArrayList<CDATA> data;

		public static class CDATA {
			public String userterminalid;// 发言人ID
			public String id;// 回复ID
			public String username;// 发言人姓名
			public String replytype;// 发言人类型
			public String contenttime;// 发言时间
			public String attacpathname;// 头像图片名称
			public String attacpath;// 图片路径
			public String replycontent;// 发言内容
			public String zcname;//专长名称
			public String merchantid;//专家所属商家ID
			public String sjname;//专家所属商家名称
			public String answersimage;//问题回复图片二维码
			public String answersname;//问题回复图片名称
		}
	}

	public static class INFO_YEAR extends BASE {

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

	/**
	 * 保险信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_INSURANCE_LIST extends BASE {
		public ArrayList<CDATA> data;

		public static class CDATA {
			public String id;// 主键ID
			public String bxtime;// 保险时间
			public String bxtype;// 保险种类
			public String bxcost;// 保险费用
			public String bxunit1;// 保险单位
			public String bxcompany;// 保险公司
			public String carid;// 车辆ID
			public String remarks;// 备注（保存接口）
		}
	}

	/**
	 * 年检信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class INFO_YEARCHECK_LIST extends BASE {
		public ArrayList<CDATA> data;

		public static class CDATA {
			public String id;// 主键ID
			public String njtime;// 年检时间
			public String njpeople;// 送检人
			public String shouldtime;// 本车应检日期
			public String njefftime;// 年检有效期
			public String njcost;// 年检费用
			public String carid;// 车辆ID
			public String remarks;// 备注（保存接口）
		}

	}

	/**
	 * 保险理陪列表
	 * 
	 * @author jch
	 * 
	 */
	public static class INSURANCE_CLAIM_LIST extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA {
			public String sjtel;// 电话
			public String imagepath;// 图片地址
			public String sjname;// 名称
			public String website;// 网址
			public String sjaddress;// 地址
		}
	}

	/**
	 * 获取相关问题
	 * 
	 * @author 
	 * 
	 */
	public static class QUESTION_CORRELATION extends BASE
	{
		public ArrayList<CDATA> data;
		public class CDATA
		{
			public String content;
			public String id;
			public String zyname ;
			public String username;
			public String seekdate;
			public String userid;
			public String seektype;
			public String attacpathname;
			public String terminalid;
			public String attacpath;

			@Override
			public String toString() {

				return "CDATA [ content=" + content + ",id=" + id + ",zyname=" + zyname + ",username=" + username + "" +
						",seekdate=" + seekdate + ",userid=" + userid + ",seektype=" + seektype + 
						",attacpathname=" + attacpathname + ",terminalid=" + terminalid + ",attacpath=" + attacpath + "]";
			}
		}
	}
	
	/**
	 * 获取新闻图片
	 * @author gsl
	 *
	 */
	public static class QUESTION_GETNEWSIMG extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA {
			public String id;// 新闻ID
			public String title;//标题
			public String attacpathname;//图片名称
			public String attacpath;// 图片路径
			public String newslink;//连接路径
			
		}
	}


	/**
	 * 获取部件信息列表
	 * @author gsl
	 *
	 */
	public static class QUESTION_POISTION extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA {
			public String id;// 部件ID
			public String name;//部件name
			
		}
	}
	
	
	/**
	 * 保存问题
	 * 
	 * @author zoe
	 * 
	 */
	public static class QUESTION_SAVE extends BASE {
		public String consultationid;//提问问题ID（回复时需要填写）
		public String content;// 问题内容
		public String terminalid;//终端用户ID
		public String classid;//选择分类ID （提问时需要填写）
		public String image;// 图片二维码
		public String imagename;// 图片的名称
	}
	
	public static class QUESTION_SEARCH_RESULT extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA {
			public String id;// 问题ID
			public String content;// 问题内容
			public String zyname;// 专家专业名称
			public String username;// 专家姓名
			public String seekdate;// 提问时间
			public String userid;// 专家ID
			public String seektype;// 分类id
			public String attacpathname;// 图片名称
			public String terminalid;// 终端用户ID
			public String attacpath;// 图片路径
		}
	}

}
