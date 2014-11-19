package cn.com.hyrt.carserverexpert.workbench.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.R.integer;
import cn.com.hyrt.carserverexpert.base.activity.MainActivity;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.UNREAD_NUM;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;

public class WorkBenchFragment extends Fragment{

	private View rootView;
	private Button btnNew;
	private Button btnMe;
	private Button btnCurrent;
	private TextView tvUnreadNum;
	
	private int tabPosition = 0;
	private int curPosition;
	
	private QuestionListFragment mNewQuestionListFragment;
	private QuestionListFragment mMyQuestionListFragment;
	private QuestionListFragment mCurrentQuestionListFragment;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_workbench, null);
		findView();
		setListener();
		if(mNewQuestionListFragment != null){
			getChildFragmentManager().beginTransaction().remove(mNewQuestionListFragment).commit();
		}
		if(mMyQuestionListFragment != null){
			getChildFragmentManager().beginTransaction().remove(mMyQuestionListFragment).commit();
		}
		if(mCurrentQuestionListFragment != null){
			getChildFragmentManager().beginTransaction().remove(mCurrentQuestionListFragment).commit();
		}
		mNewQuestionListFragment = null;
		mMyQuestionListFragment = null;
		mCurrentQuestionListFragment = null;
		tabPosition = 0;
		curPosition = 0;
		mNewQuestionListFragment = new QuestionListFragment(
				WebServiceHelper.QUESTION_TYPE_NEW);
		getChildFragmentManager().beginTransaction()
		.add(R.id.container, mNewQuestionListFragment).commit();
		loadData();
		return rootView;
	}
	
	private void loadData(){
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.UNREAD_NUM>() {

			@Override
			public void onSuccess(UNREAD_NUM result) {
				try{
					int unreadNum = Integer.parseInt(result.num);
					if(unreadNum > 0){
						tvUnreadNum.setText(result.num);
						tvUnreadNum.setVisibility(View.VISIBLE);
					}else{
						tvUnreadNum.setVisibility(View.GONE);
					}
				}catch(NumberFormatException e){
					tvUnreadNum.setVisibility(View.GONE);
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				tvUnreadNum.setVisibility(View.GONE);
			}
		}, getActivity()).getUnreadNum();
	}
	
	private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			focusManage(false, tabPosition);
			int id = view.getId();
			if(id == btnNew.getId()){
				tabPosition = 0;
				focusManage(true, tabPosition);
			}else if(id == btnMe.getId()){
				tabPosition = 1;
				focusManage(true, tabPosition);
			}else if(id == btnCurrent.getId()){
				tabPosition = 2;
				focusManage(true, tabPosition);
			}
		}
	};
	
	private void focusManage(boolean hasFocus, int position){
		if(hasFocus){
			changeFragment(position);
			switch (position) {
			case 0:
				btnNew.setBackgroundResource(R.drawable.bg_tab_left_focus);
				btnNew.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 1:
				btnMe.setBackgroundResource(R.drawable.bg_tab_center_focus);
				btnMe.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 2:
				btnCurrent.setBackgroundResource(R.drawable.bg_tab_right_focus);
				btnCurrent.setTextColor(getResources().getColor(android.R.color.white));
				break;
			}
		}else{
			switch (position) {
			case 0:
				btnNew.setBackgroundResource(R.drawable.bg_tab_left);
				btnNew.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 1:
				btnMe.setBackgroundResource(R.drawable.bg_tab_center);
				btnMe.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 2:
				btnCurrent.setBackgroundResource(R.drawable.bg_tab_right);
				btnCurrent.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			}
		}
		
	}
	
	private void changeFragment(int position){
		if(position == curPosition){
			return;
		}
		
		if(position == 0){
			((MainActivity)MainActivity.getMe()).ivTopRight.setVisibility(View.VISIBLE);
		}else{
			((MainActivity)MainActivity.getMe()).ivTopRight.setVisibility(View.GONE);
		}
		
		switch (position) {
		case 0:
			if(mNewQuestionListFragment == null){
				mNewQuestionListFragment = new QuestionListFragment(WebServiceHelper.QUESTION_TYPE_NEW);
				getChildFragmentManager().beginTransaction()
				.add(R.id.container, mNewQuestionListFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mNewQuestionListFragment).commit();
			}
			break;
		case 1:
			if(mMyQuestionListFragment == null){
				mMyQuestionListFragment = new QuestionListFragment(WebServiceHelper.QUESTION_TYPE_MY);
				getChildFragmentManager().beginTransaction()
				.add(R.id.container, mMyQuestionListFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mMyQuestionListFragment).commit();
			}
			
			break;
		case 2:
			if(mCurrentQuestionListFragment == null){
				mCurrentQuestionListFragment = new QuestionListFragment(WebServiceHelper.QUESTION_TYPE_AN);
				getChildFragmentManager().beginTransaction()
				.add(R.id.container, mCurrentQuestionListFragment).commit();
			}else{
				getChildFragmentManager().beginTransaction().show(mCurrentQuestionListFragment).commit();
			}
			break;
		default:
			break;
		}
		
		switch (curPosition) {
		case 0:
			if(mNewQuestionListFragment != null){
				getChildFragmentManager().beginTransaction().hide(mNewQuestionListFragment).commit();
			}
			break;
		case 1:
			if(mMyQuestionListFragment != null){
				getChildFragmentManager().beginTransaction().hide(mMyQuestionListFragment).commit();
			}
			break;
		case 2:
			if(mCurrentQuestionListFragment != null){
				getChildFragmentManager().beginTransaction().hide(mCurrentQuestionListFragment).commit();
			}
			break;
		default:
			break;
		}
		
		curPosition = position;
	}
	
	public void toggleSearch(){
		if(curPosition == 0 && mNewQuestionListFragment != null){
			mNewQuestionListFragment.toggleSearch();
		}
	}
	
	private void setListener(){
		btnNew.setOnClickListener(mTabOnClickListener);
		btnMe.setOnClickListener(mTabOnClickListener);
		btnCurrent.setOnClickListener(mTabOnClickListener);
	}
	
	private void findView(){
		btnNew = (Button) rootView.findViewById(R.id.btn_new);
		btnMe = (Button) rootView.findViewById(R.id.btn_me);
		btnCurrent = (Button) rootView.findViewById(R.id.btn_current);
		tvUnreadNum = (TextView) rootView.findViewById(R.id.tv_unread_num);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadData();
	}
}
