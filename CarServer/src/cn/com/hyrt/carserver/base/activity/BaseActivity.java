package cn.com.hyrt.carserver.base.activity;

import net.tsz.afinal.FinalActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.LogHelper;

/**
 * Activity基类
 * @author zoe
 *
 */
public class BaseActivity extends ActionBarActivity{
	
	private ActionBar actionBar;
	private TextView titleText;//actionBar标题
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		actionBar = getSupportActionBar();
		initActionBar();
		
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		FinalActivity.initInjectedView(this);
	}
	
	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		FinalActivity.initInjectedView(this);
	}
	
	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		FinalActivity.initInjectedView(this);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(this instanceof MainActivity){
			return false;
		}
		menu.add("default")
        .setIcon(R.drawable.ic_actionbar_right)
        .setShowAsAction(
                MenuItem.SHOW_AS_ACTION_ALWAYS);
        
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	protected void initActionBar(){
		actionBar.setCustomView(R.layout.layout_actionbar);
        actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setIcon(R.drawable.ic_back);
		View layoutActionBar = actionBar.getCustomView();
		titleText = (TextView) layoutActionBar.findViewById(R.id.tv_actionbar_title);
		if(getTitle() != null && getTitle().toString().length() > 0){
			titleText.setText(getTitle());
		}
	}
	
	protected void setTitle(String name){
		if(name == null){
			titleText.setText("");
		}else{
			titleText.setText(name);
		}
	}
	
	protected void showActionBar(boolean isShow){
		if(isShow){
			actionBar.show();
		}else{
			actionBar.hide();
		}
	}
	
	/**
	 * 显示或隐藏返回按钮
	 * @param isShow
	 */
	protected void showBackButton(boolean isShow){
		actionBar.setDisplayShowHomeEnabled(isShow);
	}
}
