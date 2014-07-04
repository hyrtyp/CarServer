package cn.com.hyrt.carserver.base.baseFunction;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hyrt.carserver.base.helper.LogHelper;


public class ClassifyJsonParser {

	private static List<Map<String, String>> one;
	
	private List<List<Map<String, String>>> two;
	
	private List<List<List<Map<String, String>>>> three;
	
	public static void get(){
//		String json = "{\"data\":[[{\"id\":\"4028818b3da0c5ea013dc4bf58325001\",\"name\":\"美容\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325002\",\"name\":\"装潢\"}],[[{\"id\":\"4028818b3da0c5ea013dc4bf58325004\",\"name\":\"车内\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325003\",\"name\":\"车表\"}],[{\"id\":\"4028818b3da0c5ea013dc4bf58325012\",\"name\":\"挡泥板\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325011\",\"name\":\"铺地胶\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325010\",\"name\":\"全身镀膜\"}]],[[[{\"id\":\"4028818b3da0c5ea013dc4bf58325008\",\"name\":\"顶棚\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325009\",\"name\":\"地毯\"}],[{\"id\":\"4028818b3da0c5ea013dc4bf58325005\",\"name\":\"洗车\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325006\",\"name\":\"底盘清理\"}]],[[],[],[]]]],\"size\":\"3\"}";
//		JSONObject mJsonObject = new JSONObject();
//		try {
//			JSONArray mJsonArray = mJsonObject.getJSONArray("data");
//			int size = mJsonObject.getInt("size");
//			
//			JSONArray oneArray = mJsonArray.getJSONArray(0);
//			JSONArray twoArray = mJsonArray.getJSONArray(1);
//			JSONArray threeArray = mJsonArray.getJSONArray(2);
//			
//			for(int i=0,j=oneArray.length(); i<j; i++){
//			}
//			
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
	}
}
