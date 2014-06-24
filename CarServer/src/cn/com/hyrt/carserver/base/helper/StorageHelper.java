package cn.com.hyrt.carserver.base.helper;

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
	private static final String TAB_INDEX_NAME = "tabIndex";
	
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
	
	public void saveTabIndex(int index){
		Editor mEditor = mSharedPreferences.edit();
		mEditor.putInt(TAB_INDEX_NAME, index);
		mEditor.commit();
	}
	
	public int getTabIndex(){
		return mSharedPreferences.getInt(TAB_INDEX_NAME, 0);
	}
	
	
	
}
