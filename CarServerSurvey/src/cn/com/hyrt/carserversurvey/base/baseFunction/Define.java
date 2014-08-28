package cn.com.hyrt.carserversurvey.base.baseFunction;

import java.io.Serializable;
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

	public static class LIST_INFO_PRODUCT extends BASE {

		public ArrayList<CDATA> data;

		public static class CDATA {
			public String id;// 商品/服务主键ID
			public String attacpathname0;//
			public String price;// 价格
			public String attacpath0;//
			public String spname;// 商品名称
			public String sptitle;// 商品介绍
			public String fbtime;// 上架时间
			public String type;// 类型(fw:服务 sp:商品)
			public String discount;// 折扣
		}
	}

	public static class INFO_PRODUCT extends BASE {
		public String id;// 商品主键ID
		public String price;// 价格
		public String imagepath0;
		public String imagename0;
		public String imagepath1;
		public String imagename1;
		public String imagepath2;
		public String imagename2;
		public String imagepath3;
		public String imagename3;
		public String spname;// 商品名称
		public String sptitle;// 商品介绍
		public String type;// 类型
		public String discount;// 折扣
		public String serviceid;
		public String userid;
	}

	/**
	 * 保存商户信息
	 * 
	 * @author zoe
	 * 
	 */
	public static class SAVE_INFO_MERCHANT extends BASE {
		public String sjname;// 商家名称
		public String sjtel;// 固定电话
		public String sjmanager;// 联系人员
		public String sjjc;// 简称
		public String sjaddress;// 详细地址
		public String areaid;// 所属区域ID
		public String loginname;// 商家用户登陆名
		public String coor_x;// X坐标
		public String coor_y;// Y坐标
		public String userid;// 当前登陆人ID
		public String phonenum;// 手机号码
		public String desc;// 简介
		public String sjimage;// 商家图片
		public String imagename;// 商家图片名称
		public String zzimage;// 执照图片
		public String zzimagename;// 执照图片名称
		public String id;// 商家主键id
		public String brandid;// 品牌ID
		public String servicetype;// 服务类型ID
	}

	public static class SAVE_INFO_MERCHANT_RESULT extends BASE {
		public String id;// 商家主键id
	}

	public static class SAVE_INFO extends BASE {
		public String id;
		public String password;
		public String newpassword;
		public String image;
		public String imagename;
		public String imagepath;
		public String lasttime;
		public String mercount;
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
			public String sjname;// 商家名称
			public String sjaddress; // 详细地址
			public String peopledate;// 采集时间

		}
	}

	public static class INFO_MERCHANT extends BASE {

		public ArrayList<CDATA> data;

		public class CDATA implements Serializable {
			public String id;// ID
			public String loginname;// 用户登陆名
			public String sjtel;// 服务电话/固定电话
			public String brandname;// 品牌名称
			public String sjaddress;// 详细地址
			public String sjmanager;// 联系人员
			public String attacpathname;// 图片名称
			public String attacpath;// 图片路径
			public String phonenum;// 手机号码
			public String peopledate;// 采集时间
			public String serviceTypename;// 服务类型
			public String serviceTypeid;//服务类型ID
			public String sjname;// 商家名称
			public String sjjc;// 商家简称
			public String brandid;//品牌ID
			public String areaid;//区域ID
			public String desc;//简介
			public String status;//
			public String imagepath;//商家图片路径
			public String zzimagepath;//执照图片路径
		}
	}
}
