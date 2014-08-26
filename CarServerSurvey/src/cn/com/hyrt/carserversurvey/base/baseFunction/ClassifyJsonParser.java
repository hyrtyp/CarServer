package cn.com.hyrt.carserversurvey.base.baseFunction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.hyrt.carserversurvey.base.helper.LogHelper;

/**
 * 分类json解析
 * @author zoe
 *
 */
public class ClassifyJsonParser {

	//一级分类
	private List<Map<String, String>> oneList = new ArrayList<Map<String,String>>();
	
	//二级分类
	private List<List<Map<String, String>>> twoList = new ArrayList<List<Map<String,String>>>();
	
	//三级分类
	private List<List<List<Map<String, String>>>> threeList = new ArrayList<List<List<Map<String,String>>>>();
	
	private int size;
	
	
	public List<Map<String, String>> getOneList() {
		return oneList;
	}



	public List<List<Map<String, String>>> getTwoList() {
		return twoList;
	}



	public List<List<List<Map<String, String>>>> getThreeList() {
		return threeList;
	}
	
	public int getSize() {
		return size;
	}



	/**
	 * 解析
	 * @param json
	 */
	public void parse(String json){
//		String json = "{\"data\":[[{\"id\":\"4028818b3da0c5ea013dc4bf58325001\",\"name\":\"美容\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325002\",\"name\":\"装潢\"}],[[{\"id\":\"4028818b3da0c5ea013dc4bf58325004\",\"name\":\"车内\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325003\",\"name\":\"车表\"}],[{\"id\":\"4028818b3da0c5ea013dc4bf58325012\",\"name\":\"挡泥板\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325011\",\"name\":\"铺地胶\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325010\",\"name\":\"全身镀膜\"}]],[[[{\"id\":\"4028818b3da0c5ea013dc4bf58325008\",\"name\":\"顶棚\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325009\",\"name\":\"地毯\"}],[{\"id\":\"4028818b3da0c5ea013dc4bf58325005\",\"name\":\"洗车\"},{\"id\":\"4028818b3da0c5ea013dc4bf58325006\",\"name\":\"底盘清理\"}]],[[],[],[]]]],\"size\":\"3\"}";
		try {
			JSONObject mJsonObject = new JSONObject(json);
			JSONArray mJsonArray = mJsonObject.getJSONArray("data");
			this.size = mJsonObject.optInt("size");
			
			JSONArray oneArray = mJsonArray.getJSONArray(0);
			JSONArray twoArray = mJsonArray.getJSONArray(1);
			
			for(int i=0,j=oneArray.length(); i<j; i++){
				JSONObject cJsonObject = oneArray.getJSONObject(i);
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", cJsonObject.getString("id"));
				map.put("name", cJsonObject.getString("name"));
				oneList.add(map);
			}
			
			for(int i=0,j=twoArray.length(); i<j; i++){
				JSONArray cJsonArray = twoArray.getJSONArray(i);
				List<Map<String, String>> cList = new ArrayList<Map<String,String>>();
				
				for(int a=0,b=cJsonArray.length(); a<b; a++){
					JSONObject cJsonObject = cJsonArray.getJSONObject(a);
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", cJsonObject.getString("id"));
					map.put("name", cJsonObject.getString("name"));
					map.put("attacpath", cJsonObject.optString("attacpath"));
					cList.add(map);
				}
				
				twoList.add(cList);
			}
			
			if(size == 3){
				JSONArray threeArray = mJsonArray.getJSONArray(2);
				for(int i=0,j=threeArray.length(); i<j; i++){
					JSONArray cJsonArray = threeArray.getJSONArray(i);
					List<List<Map<String, String>>> cList = new ArrayList<List<Map<String,String>>>();
					
					for(int a=0,b=cJsonArray.length(); a<b; a++){
						List<Map<String, String>> ccList = new ArrayList<Map<String,String>>();
						JSONArray ccJsonArray = cJsonArray.getJSONArray(a);
						
						for(int x=0,y=ccJsonArray.length(); x<y; x++){
							JSONObject ccJsonObject = ccJsonArray.getJSONObject(x);
							Map<String, String> map = new HashMap<String, String>();
							map.put("id", ccJsonObject.getString("id"));
							map.put("name", ccJsonObject.getString("name"));
							ccList.add(map);
						}
						
						cList.add(ccList);
					}
					
					threeList.add(cList);
				}
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			LogHelper.i("tag", "teste:"+e.getMessage());
		}
	}
}
