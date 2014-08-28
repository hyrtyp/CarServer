package cn.com.hyrt.carserversurvey.base.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Html;
import android.text.Spanned;


/**
 * 字符串处理助手
 * @author zoe
 *
 */
public class StringHelper {

	/**
	 * 字符串时间 To 友好 (字符串时间格式：yyyy-MM-dd HH:mm:ss.SSS)
	 */
	public static String getFriendlydate(String strTime){
		Date time = string2Date(strTime);
		if(time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();
		
		//判断是否是同一天
		String curDate = dateFormater.get().format(cal.getTime());
		String paramDate = dateFormater.get().format(time);
		if(curDate.equals(paramDate)){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
			return ftime;
		}
		
		long lt = time.getTime()/86400000;
		long ct = cal.getTimeInMillis()/86400000;
		int days = (int)(ct - lt);		
		if(days == 0){
			int hour = (int)((cal.getTimeInMillis() - time.getTime())/3600000);
			if(hour == 0)
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000,1)+"分钟前";
			else 
				ftime = hour+"小时前";
		}
		/*else if(days == 1){
			ftime = "昨天";
		}
		else if(days == 2){
			ftime = "前天";
		}
		days > 2 &&*/
		else if(days <= 2){
			ftime = days+"天前";			
		}else{
			ftime = dateFormater.get().format(time);
		}
		return ftime;
	}
	
	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};
	
	/**
	 * 字符串时间 To Date   (字符串时间格式：yyyy-MM-dd HH:mm:ss.SSS)
	 * @param time
	 */
	public static Date string2Date(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static long string2Millis(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(date == null){
			return -1;
		}
		return date.getTime();
	}
	
	/**
	 * 格式化时间
	 * @param time
	 * @return
	 */
	public static String formatDate(String time){
		if(time == null || "".equals(time)){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(string2Date(time));
	}
	
	public static String getNowTime(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(date);
	}
	
	/**
	 * 关键字高亮
	 * @param str
	 * @param keyword
	 */
	public static Spanned KeywordHighlight(String str, String keyword){
		if(str == null || "".equals(str) 
				|| keyword == null || "".equals(keyword)){
			return Html.fromHtml("");
		}
		String newStr = str.replace(keyword, "<u ><font color='#00c4e9'>"+keyword+"</font></u>");
		
		return Html.fromHtml(newStr);
	}
	/**
	 * 判断是否是手机号码
	 * @param mobiles
	 */
	public static boolean isMobileNum(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }
	
	public static boolean nameValidate(String str){
		 Pattern p = Pattern
	                .compile("[\u4e00-\u9fa5A-Za-z0-99]+");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 

}
