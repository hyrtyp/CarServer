package cn.com.hyrt.carserver.base.activity;


import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.ScreenHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.classify.fragment.ClassifyFragment;
import cn.com.hyrt.carserver.emergency.fragment.EmergencyFragment;
import cn.com.hyrt.carserver.info.fragment.InfoFragment;
import cn.com.hyrt.carserver.knowledge.fragment.KnowledgeFragment;
import cn.com.hyrt.carserver.question.fragment.QuestionFragment;

/**
 * 主界面
 * @author zoe
 *
 */
public class MainActivity extends BaseActivity {
	
	@ViewInject(id=R.id.layout_contain) FrameLayout layoutContain;
	@ViewInject(id=android.R.id.tabhost) FragmentTabHost mTabHost;
	
	private Class<?> fragmentArray[] = 
		{ClassifyFragment.class,KnowledgeFragment.class,
			QuestionFragment.class,InfoFragment.class,
			EmergencyFragment.class};
	
	private int mTextArray[] = {
			R.string.classify_label, R.string.knowledge_label,
			R.string.question_label, R.string.info_label,
			R.string.emergency_label}; 
	
	private int mImgArray[] = {
			R.drawable.bg_classify_tab, R.drawable.bg_knowledge_tab,
			R.drawable.bg_question_tab, R.drawable.bg_info_tab,
			R.drawable.bg_emergency_tab
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		showActionBar(false);
		initView();
		
	}
	
	private void initView(){
		mTabHost.setup(this, getSupportFragmentManager(), R.id.layout_contain);
		for(int i=0,j=fragmentArray.length; i<j; i++){
			TabSpec mTabSpec = mTabHost.newTabSpec(i+"")
					.setIndicator(getTabItemView(i));
			mTabHost.addTab(mTabSpec, fragmentArray[i], null);
		}
		mTabHost.setCurrentTab(StorageHelper.getInstance(this).getTabIndex());
		mTabHost.setOnTabChangedListener(mTabChangeListener);
	}
	
	private TabHost.OnTabChangeListener mTabChangeListener = new TabHost.OnTabChangeListener() {
		
		@Override
		public void onTabChanged(String arg0) {
			LogHelper.i("tag", "onChange:"+arg0);
			StorageHelper.getInstance(MainActivity.this).saveTabIndex(Integer.parseInt(arg0));
		}
	};
	
	private View getTabItemView(int index){
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View btnView = mLayoutInflater.inflate(R.layout.layout_main_btn_item, null);
		ImageView ivBtnIcon = (ImageView) btnView.findViewById(R.id.iv_btn_icon);
		ivBtnIcon.setImageResource(mImgArray[index]);
		TextView btnText = (TextView) btnView.findViewById(R.id.tv_btn_text);
		btnText.setText(getString(mTextArray[index]));
		return btnView;
	}
	

}
