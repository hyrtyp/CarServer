package cn.com.hyrt.carserver.welfare.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.fragment.WebFragment;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.view.HorizontalScrollTitleView;
import cn.com.hyrt.carserver.classify.fragment.ClassifyFragment;
import cn.com.hyrt.carserver.emergency.fragment.EmergencyFragment;
import cn.com.hyrt.carserver.info.fragment.InfoFragment;
import cn.com.hyrt.carserver.question.fragment.QuestionFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WelfareCenterFragment extends Fragment{

	private View rootView;
	private List<String> strTopItem = new ArrayList<String>();
	private List<String> topItemId = new ArrayList();
	
	FragmentTabHost mTabHost;
	
	private HorizontalScrollTitleView lv_top;
	private SignUpFragment mSignUpFragment;
	private WebFragment mWebFragment;
	private int curSelected = -1;
	private boolean inited = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		LogHelper.i("tag", "onCreateView:");
		rootView = inflater.inflate(R.layout.fragment_welfare_center, null);
//		FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
//		mSignUpFragment = new SignUpFragment();
//		mFragmentTransaction.add(R.id.layout_contain, mSignUpFragment);
//		mFragmentTransaction.commit();
		if(curSelected < 0){
			changeFragment(0);
		}else{
			changeFragment(curSelected);
		}
		findView();
		
		return rootView;
	}
	
	private void findView(){
		strTopItem.clear();
		strTopItem.add("签到");
		strTopItem.add("洗车");
		strTopItem.add("保险");
		strTopItem.add("商城");
		
		topItemId.clear();
		topItemId.add("1cfd3d9d6a0d4791ae5a8b08a1487cb0");
		topItemId.add("f360e6c9696445c8ba0c98face38c428");
		topItemId.add("7a0a56a8c34e438f9f566816504d6e9a");
		
		lv_top = (HorizontalScrollTitleView) rootView.findViewById(R.id.lv_top);
		lv_top.setTitles(strTopItem);
		lv_top.moveTo(curSelected);
		
		lv_top.setListener(new HorizontalScrollTitleView.TitleViewOnclickListener() {
			

			@Override
			public void onClick(int position) {
				changeFragment(position);
			}
		});
	}
	
	private void changeFragment(int position){
		if(curSelected == position){
			return;
		}
		FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
		if(position == 0){
			if(mSignUpFragment == null){
				mSignUpFragment = new SignUpFragment();
				mFragmentTransaction.add(R.id.layout_contain, mSignUpFragment);
			}else{
				mFragmentTransaction.show(mSignUpFragment);
				hideFragment(mFragmentTransaction, curSelected);
			}
		}else{
			if(curSelected == 0){
				if(mWebFragment == null){
					mWebFragment = new WebFragment();
					mFragmentTransaction.add(R.id.layout_contain, mWebFragment);
				}else{
					mFragmentTransaction.show(mWebFragment);
					hideFragment(mFragmentTransaction, curSelected);
				}
			}
			LogHelper.i("tag", "position:"+position);
			if(position > 0){
//				String url = getString(R.string.method_weburl)+"/cspportal/knowledge/fulilist?id=%s";
				String url = getString(R.string.method_weburl)+"/knowledge/fulilist?id=%s";
				String id = topItemId.get(position-1);
				LogHelper.i("tag", "id:"+id);
				mWebFragment.setUrl(String.format(url, id));
			}
			
			
		}
		mFragmentTransaction.commit();
		curSelected = position;
	}
	
	private void hideFragment(FragmentTransaction mTransaction, int position){
		switch (position) {
		case 0:
			mTransaction.hide(mSignUpFragment);
			break;
		default:
			mTransaction.hide(mWebFragment);
			break;
		}
	}
	
}
