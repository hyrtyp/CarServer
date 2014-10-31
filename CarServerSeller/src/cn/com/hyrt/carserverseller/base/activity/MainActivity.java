package cn.com.hyrt.carserverseller.base.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.ScreenHelper;
import cn.com.hyrt.carserverseller.base.helper.StorageHelper;
import cn.com.hyrt.carserverseller.info.fragment.InfoFragment;
import cn.com.hyrt.carserverseller.order.fragment.OrderFragment;
import cn.com.hyrt.carserverseller.preferential.fragment.PreferentialFragment;
import cn.com.hyrt.carserverseller.product.fragment.ProductFragment;
import cn.com.hyrt.carserverseller.shop.fragment.ShopFragment;

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
	public FragmentTabHost mTabHost;
	@ViewInject(id = R.id.mainTitle)
	TextView mainTitle;
	@ViewInject(id = R.id.layout_main_top)
	LinearLayout layoutMainTop;
	@ViewInject(id = R.id.layout_line)
	LinearLayout layout_line;
	@ViewInject(id = R.id.iv_topright)
	ImageView ivTopRight;

	private Class<?> fragmentArray[] = { ProductFragment.class,
			OrderFragment.class, ShopFragment.class,
			PreferentialFragment.class, InfoFragment.class };

	private int mTextArray[] = { R.string.product_label,
			R.string.order_label, R.string.shop_label,
			R.string.preferential_label, R.string.info_label };

	private int mImgArray[] = { R.drawable.bg_product_tab,
			R.drawable.bg_orders_tab, R.drawable.bg_shop_tab,
			R.drawable.bg_preferential_tab, R.drawable.bg_info_tab };
	
	private static final int EXIT = 0;
	
	private boolean needExit = false;
	private int curIndex = 0;
	
	private static Context meContext;
	
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
		meContext = this;
		showActionBar(false);

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

	public void changeActionBar(int index) {
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
//		if(curIndex == 3){
//			List<Fragment> mFragments = getSupportFragmentManager().getFragments();
//			if(mFragments == null){
//				return;
//			}
//			for(int i=0,j=mFragments.size(); i<j; i++){
//				if(mFragments.get(i) instanceof InfoFragment){
//					((InfoFragment)mFragments.get(i)).loadData();
//				}
//			}
//		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(curIndex == 0){
			ProductFragment mProductFragment = 
					(ProductFragment) getSupportFragmentManager()
					.findFragmentByTag(mTabHost.getCurrentTabTag());
			if(mProductFragment != null){
				mProductFragment.onActivityResult(arg0, arg1, arg2);
			}
		}else if(curIndex == 1){
			OrderFragment mOrderFragment = 
					(OrderFragment) getSupportFragmentManager()
			.findFragmentByTag(mTabHost.getCurrentTabTag());
			if(mOrderFragment != null){
				mOrderFragment.onActivityResult(arg0, arg1, arg2);
			}
		}else if(curIndex == 2 &&
				getSupportFragmentManager()
				.findFragmentByTag(mTabHost.getCurrentTabTag()) instanceof ShopFragment){
			ShopFragment mShopFragment =
					(ShopFragment) getSupportFragmentManager()
			.findFragmentByTag(mTabHost.getCurrentTabTag());
			if(mShopFragment != null){
				mShopFragment.onActivityResult(arg0, arg1, arg2);
			}
		}else if(curIndex == 3){
			PreferentialFragment mPreferentialFragment =
					(PreferentialFragment)getSupportFragmentManager()
					.findFragmentByTag(mTabHost.getCurrentTabTag());
			if(mPreferentialFragment != null){
				mPreferentialFragment.onActivityResult(arg0, arg1, arg2);
			}
		}
	}
	
	@Override
	public void finish() {
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
	
	public void mandatoryFinish(){
		needExit = true;
		finish();
	}
	
	public static Context getMe(){
		return meContext;
	}

}
