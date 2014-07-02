package cn.com.hyrt.carserver.base.helper;

import com.google.gson.Gson;

import cn.com.hyrt.carserver.base.baseFunction.Define;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * 储存助手(SharedPreferences & Database)
 * @author zoe
 *
 */
public class StorageHelper {
	
	private static StorageHelper mStorageHelper;
	private static Context mContext;
	private static SharedPreferences mSharedPreferences;
	
	private static final String PREFS_NAME = "appInfo";
	private static final String TAB_INDEX_NAME = "tabIndex";//首页TAB下标
	private static final String INFO_LOGIN_NAME = "loginInfo";//登录信息
	
	private Gson gson;
	
	private StorageHelper(){}
	
	public static StorageHelper getInstance(Context context){
		if(context == null){
			return null;
		}
		mContext = context;
		if(mStorageHelper == null){
			mStorageHelper = new StorageHelper();
		}
		
		if(mSharedPreferences == null){
			mSharedPreferences = mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		}
		
		return mStorageHelper;
	}
	
	/**
	 * 保存首页TAB下标
	 * @param index
	 */
	public void saveTabIndex(int index){
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(TAB_INDEX_NAME, index);
		mEditor.commit();
	}
	
	/**
	 *获取首页TAB下标 
	 * @return
	 */
	public int getTabIndex(){
		return mSharedPreferences.getInt(TAB_INDEX_NAME, 0);
	}
	
	/**
	 * 保存登录信息
	 * @param loginInfo
	 */
	public void saveLoginInfo(Define.INFO_LOGIN loginInfo){
		if(gson == null){
			gson = new Gson();
		}
		String json = gson.toJson(loginInfo);
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putString(INFO_LOGIN_NAME, json);
		mEditor.commit();
	}
	
	/**
	 * 获取登录信息
	 * @return
	 */
	public Define.INFO_LOGIN getLoginInfo(){
		String json = mSharedPreferences.getString(INFO_LOGIN_NAME, null);
		if(json == null){
			return null;
		}
		if(gson == null){
			gson = new Gson();
		}
		return gson.fromJson(json, Define.INFO_LOGIN.class);
	}
	
	/**
	 * 删除登录信息
	 */
	public void delLoginInfo(){
		mSharedPreferences.edit().remove(INFO_LOGIN_NAME);
	}
	
	public void saveTest(String test){
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putString("test", test);
		mEditor.commit();
	}
	
	
}
