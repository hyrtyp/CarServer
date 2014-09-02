package cn.com.hyrt.carserversurvey.base.activity;

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
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.ScreenHelper;
import cn.com.hyrt.carserversurvey.base.helper.StorageHelper;
import cn.com.hyrt.carserversurvey.info.fragment.InfoFragment;
import cn.com.hyrt.carserversurvey.info.fragment.RegistRecordFragment;
import cn.com.hyrt.carserversurvey.product.fragment.ProductFragment;
import cn.com.hyrt.carserversurvey.regist.fragment.RegistMerchantInfoFragment;
import cn.com.hyrt.carserversurvey.shop.fragment.ShopFragment;

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
	@ViewInject(id = R.id.iv_topright)
	ImageView ivTopRight;
	
	public static MainActivity meContext;
	public static boolean needFinish = false;

	private Class<?> fragmentArray[] = {
			RegistMerchantInfoFragment.class, RegistRecordFragment.class,
			InfoFragment.class };

	private int mTextArray[] = {R.string.regist_label, R.string.regist_record_label,
			R.string.info_label };

	private int mImgArray[] = {R.drawable.bg_regist_tab, R.drawable.bg_merchant_list_tab,
			R.drawable.bg_info_tab};
	
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
//		if(index == 2){
//			ivTopRight.setVisibility(View.VISIBLE);
//			ivTopRight.setImageResource(R.drawable.ic_search2);
//		}else{
//			ivTopRight.setVisibility(View.GONE);
//		}
//		if (index == 4 || index == 0 || index == 2) {
//			layout_line.setVisibility(View.GONE);
//			layoutMainTop.setVisibility(View.VISIBLE);
//		} else {
//			layout_line.setVisibility(View.VISIBLE);
//			layoutMainTop.setVisibility(View.VISIBLE);
//		}
		mainTitle.setText(mTextArray[index]);
	}
	
	private void setListener(){
		ivTopRight.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				if(curIndex == 2){
//					Intent intent = new Intent();
//					intent.setClass(MainActivity.this, KnowledgeSearchResultActivity.class);
//					intent.putExtra("str", "发动机");
//					startActivity(intent);
//				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(curIndex == 2){
			InfoFragment mInfoFragment = 
					(InfoFragment) getSupportFragmentManager()
					.findFragmentByTag(mTabHost.getCurrentTabTag());
			if(mInfoFragment != null){
				mInfoFragment.loadData();
			}
		}
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
			List<Fragment> mFragments = getSupportFragmentManager().getFragments();
			if(mFragments == null){
				return;
			}
			for(int i=0,j=mFragments.size(); i<j; i++){
				if(mFragments.get(i) instanceof RegistMerchantInfoFragment){
					((RegistMerchantInfoFragment)mFragments.get(i)).onActivityResult(arg0, arg1, arg2);
				}
			}
		}else if(curIndex == 1){
			List<Fragment> mFragments = getSupportFragmentManager().getFragments();
			if(mFragments == null){
				return;
			}
			for(int i=0,j=mFragments.size(); i<j; i++){
				if(mFragments.get(i) instanceof ProductFragment){
					((ProductFragment)mFragments.get(i)).onActivityResult(arg0, arg1, arg2);
				}
			}
		}
	}
	
	public void jump(int position){
		mTabHost.setCurrentTab(position);
	}

}
