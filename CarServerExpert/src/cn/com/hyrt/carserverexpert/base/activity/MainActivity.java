package cn.com.hyrt.carserverexpert.base.activity;

import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.ScreenHelper;
import cn.com.hyrt.carserverexpert.base.helper.StorageHelper;
import cn.com.hyrt.carserverexpert.history.fragment.HistoryFragment;
import cn.com.hyrt.carserverexpert.info.fragment.InfoFragment;
import cn.com.hyrt.carserverexpert.welfare.fragment.WelfareFragment;
import cn.com.hyrt.carserverexpert.workbench.fragment.WorkBenchFragment;

/**
 * 主界面
 * 
 * @author zoe
 * 
 */
public class MainActivity extends BaseActivity {

	@ViewInject(id = R.id.layout_contain)
	FrameLayout layoutContain;
	@ViewInject(id = android.R.id.tabhost)
	FragmentTabHost mTabHost;
	@ViewInject(id = R.id.mainTitle)
	TextView mainTitle;
	@ViewInject(id = R.id.layout_main_top)
	LinearLayout layoutMainTop;
//	@ViewInject(id = R.id.layout_line)
//	LinearLayout layout_line;
	public @ViewInject(id = R.id.iv_topright)
	ImageView ivTopRight;
	
	private static MainActivity meContext;
	public static boolean needFinish = false;

	private Class<?> fragmentArray[] = {
			WorkBenchFragment.class, WelfareFragment.class,
			HistoryFragment.class, InfoFragment.class };

	private int mTextArray[] = {R.string.workbench_label, R.string.welfare_label,
			R.string.history_label, R.string.info_label };

	private int mImgArray[] = {R.drawable.bg_workbench_tab, R.drawable.bg_welfare_tab,
			R.drawable.bg_history_tab, R.drawable.bg_info_tab};
	
	private static final int EXIT = 0;
	
	private boolean needExit = false;
	private int curIndex = 0;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case EXIT:
				needExit = false;
				break;

			default:
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showActionBar(false);
		meContext = this;
		initView();
		LogHelper.i("tag", "sp:" + ScreenHelper.px2sp(this, 24) + " dp:"
				+ ScreenHelper.px2dip(this, 24));
		setListener();
	}

	private void initView() {
		mTabHost.setup(this, getSupportFragmentManager(), R.id.layout_contain);
		for (int i = 0, j = fragmentArray.length; i < j; i++) {
			TabSpec mTabSpec = mTabHost.newTabSpec(i + "").setIndicator(
					getTabItemView(i));
			mTabHost.addTab(mTabSpec, fragmentArray[i], null);
		}
		int index = StorageHelper.getInstance(this).getTabIndex();
		changeActionBar(index);
		mTabHost.setCurrentTab(index);
		mTabHost.setOnTabChangedListener(mTabChangeListener);
	}

	private TabHost.OnTabChangeListener mTabChangeListener = new TabHost.OnTabChangeListener() {

		@Override
		public void onTabChanged(String arg0) {
			LogHelper.i("tag", "onChange:" + arg0);
			int index = Integer.parseInt(arg0);
			StorageHelper.getInstance(MainActivity.this).saveTabIndex(index);
			changeActionBar(index);
		}
	};

	private View getTabItemView(int index) {
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View btnView = mLayoutInflater.inflate(R.layout.layout_main_btn_item,
				null);
		ImageView ivBtnIcon = (ImageView) btnView
				.findViewById(R.id.iv_btn_icon);
		ivBtnIcon.setImageResource(mImgArray[index]);
		TextView btnText = (TextView) btnView.findViewById(R.id.tv_btn_text);
		btnText.setText(getString(mTextArray[index]));
		return btnView;
	}

	private void changeActionBar(int index) {
		curIndex = index;
		if(index == 0 || index == 2){
			ivTopRight.setImageResource(R.drawable.ic_search);
			ivTopRight.setVisibility(View.VISIBLE);
		}else{
			ivTopRight.setVisibility(View.GONE);
		}
		mainTitle.setText(mTextArray[index]);
	}
	
	private void setListener(){
		ivTopRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(curIndex == 0){
					WorkBenchFragment mWorkBenchFragment = 
							(WorkBenchFragment) getSupportFragmentManager()
							.findFragmentByTag(mTabHost.getCurrentTabTag());
					if(mWorkBenchFragment != null){
						mWorkBenchFragment.toggleSearch();
					}
				}else if(curIndex == 2){
					HistoryFragment mHistoryFragment =
							(HistoryFragment) getSupportFragmentManager()
							.findFragmentByTag(mTabHost.getCurrentTabTag());
					if(mHistoryFragment != null){
						mHistoryFragment.toggleSearch();
					}
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		if(curIndex == 1){
//			
//		}else if(curIndex == 2){
//			HistoryFragment mHistoryFragment = 
//					(HistoryFragment) getSupportFragmentManager()
//					.findFragmentByTag(mTabHost.getCurrentTabTag());
//			if(mHistoryFragment != null){
//				mHistoryFragment.loadData(false);
//			}
//		}
	}
	
	@Override
	public void finish() {
		if(needFinish){
			needFinish = false;
			super.finish();
			return;
		}
		if(!needExit){
			needExit = true;
			AlertHelper.getInstance(MainActivity.this).showCenterToast(R.string.exit_prompt);
			Message msg = new Message();
			msg.what = EXIT;
			mHandler.sendMessageDelayed(msg, 1000);
			return;
		}
		mHandler.removeMessages(EXIT);
		super.finish();
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(curIndex == 0){
			
		}else if(curIndex == 1){
			
		}
	}
	
	public void jump(int position){
		mTabHost.setCurrentTab(position);
	}
	
	public void mandatoryFinish(){
		needExit = true;
		finish();
	}
	
	public static Context getMe(){
		return meContext;
	}

}
