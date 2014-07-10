package cn.com.hyrt.carserver.base.activity;

import cn.com.hyrt.carserver.base.helper.LocationHelper;
import android.os.Bundle;

/**
 * 
 * @author zoe
 *
 */
public class STabActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		LocationHelper.getInstance(this).stop();
	}
	
}
