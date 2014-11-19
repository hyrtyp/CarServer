package cn.com.hyrt.carserverexpert.welfare.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_SIGN_UP;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.ScreenHelper;
import cn.com.hyrt.carserverexpert.base.helper.StringHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.CalendarView;

public class WelfareFragment extends Fragment{

	private View rootView;
	private CalendarView cv;
	private TextView tv_year;
	private TextView tv_day;
	private TextView tv_top;
	private ImageButton ibSignUp;
	
	private SimpleDateFormat mSimpleDateFormat1;
	private SimpleDateFormat mSimpleDateFormat2;
	private Date nowDate;
	private List<String> signUpDays = new ArrayList<String>();
	private String mEndTime;
	private WebServiceHelper mGetSignUpDaysHelper;
	private WebServiceHelper mGetSignUpStatusHelper;
	private WebServiceHelper mSaveSignUpStatusHelper;
	private boolean signUpDone = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_welfare, null);
		findView();
		setListener();
		initDate();
		return rootView;
	}
	
	private void initDate(){
		if(signUpDone){
			setSignUpStatus(signUpDone);
			return;
		}
		if(mGetSignUpStatusHelper == null){
			mGetSignUpStatusHelper = new WebServiceHelper(mSignUpStatusCallback, getActivity());
		}
		mGetSignUpStatusHelper.getSignUpStatus();
	}
	
	private void findView(){
		cv = (CalendarView) rootView.findViewById(R.id.cv);
		tv_year = (TextView) rootView.findViewById(R.id.tv_year);
		tv_day = (TextView) rootView.findViewById(R.id.tv_day);
		Date curDate = cv.getCurDate();
		mSimpleDateFormat1 = new SimpleDateFormat("yyyy年MM月");
		mSimpleDateFormat2 = new SimpleDateFormat("MM月dd号\tE");
		tv_year.setText(mSimpleDateFormat1.format(curDate));
		tv_day.setText(mSimpleDateFormat2.format(curDate));
		cv.setOnItemClickListener(mCalendarListener);
		cv.setSignUpDay(signUpDays);
		
		tv_top = (TextView) rootView.findViewById(R.id.tv_top);
		SpannableString mSpannableString = new SpannableString(
				String.format(getActivity()
						.getString(R.string.welfare_signup_title), 10));
		mSpannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#fea700")), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		mSpannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 6, 9, Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
		mSpannableString.setSpan(new UnderlineSpan(), 6, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
		mSpannableString.setSpan(new AbsoluteSizeSpan(ScreenHelper.dip2px(getActivity(), 18)), 6, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		tv_top.setText(mSpannableString);
		ibSignUp = (ImageButton) rootView.findViewById(R.id.ib_signup);
	}
	
	private void setListener(){
		ibSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if(!signUpDone){
					AlertHelper.getInstance(getActivity()).showLoading(getActivity().getString(R.string.signup_load_msg));
					if(mSaveSignUpStatusHelper == null){
						mSaveSignUpStatusHelper = new WebServiceHelper(mSaveSignUpStatusCallback, getActivity());
					}
					mSaveSignUpStatusHelper.setSignUp();
				}
			}
		});
	}
	
	private CalendarView.OnItemClickListener mCalendarListener = new CalendarView.OnItemClickListener() {
		
		@Override
		public void onMonthChange(Date date) {
			if(nowDate == null){
				nowDate = new Date(System.currentTimeMillis());
			}
			if(date.getMonth() == nowDate.getMonth() && date.getYear() == nowDate.getYear()){
				tv_year.setText(mSimpleDateFormat1.format(date));
				tv_day.setText(mSimpleDateFormat2.format(date));
				tv_day.setVisibility(View.VISIBLE);
			}else{
				tv_year.setText(mSimpleDateFormat1.format(date));
				tv_day.setVisibility(View.GONE);
			}
		}

		@Override
		public void getSignUp(String startTime, String endTime) {
			AlertHelper.getInstance(getActivity()).showLoading(null);
			mEndTime = endTime;
			if(mGetSignUpDaysHelper == null){
				mGetSignUpDaysHelper = new WebServiceHelper(mSignUpDaysCallback, getActivity());
			}
			mGetSignUpDaysHelper.getSignUpDays(endTime, startTime);
			
		}
	};
	
	private BaseWebServiceHelper.RequestCallback<Define.INFO_SIGN_UP> mSaveSignUpStatusCallback 
	= new BaseWebServiceHelper.RequestCallback<Define.INFO_SIGN_UP>(){

		@Override
		public void onSuccess(INFO_SIGN_UP result) {
			setSignUpStatus(true);
			AlertHelper.getInstance(getActivity()).hideLoading();
		}

		@Override
		public void onFailure(int errorNo, String errorMsg) {
			AlertHelper.getInstance(getActivity()).showCenterToast("签到失败");
			AlertHelper.getInstance(getActivity()).hideLoading();
		}
		
	};
	
	private BaseWebServiceHelper.RequestCallback<Define.INFO_SIGN_UP> mSignUpStatusCallback 
	= new BaseWebServiceHelper.RequestCallback<Define.INFO_SIGN_UP>(){

		@Override
		public void onSuccess(INFO_SIGN_UP result) {
			String status = result.data.get(0).issearch;
			setSignUpStatus("y".equals(status));
		}

		@Override
		public void onFailure(int errorNo, String errorMsg) {
			
		}
		
	};
	
	private void setSignUpStatus(boolean signUpStatus){
		signUpDone = signUpStatus;
		if(signUpStatus){
			ibSignUp.setImageResource(R.drawable.ic_sign_up_done);
		}else{
			ibSignUp.setImageResource(R.drawable.ic_sign_up);
		}
	}
	
	private BaseWebServiceHelper.RequestCallback<Define.INFO_SIGN_UP> mSignUpDaysCallback 
	= new BaseWebServiceHelper.RequestCallback<Define.INFO_SIGN_UP>(){

		@Override
		public void onSuccess(INFO_SIGN_UP result) {
			if(mEndTime != null){
				cv.requestFailDate.remove(mEndTime);
			}
			List<Define.INFO_SIGN_UP.CDATA> mList = result.data;
			for(int i=0,j=mList.size(); i<j; i++){
				String strTime = StringHelper.formatDate2(mList.get(i).datetime);
				signUpDays.add(strTime);
			}
			cv.notifyDataSetChanged();
			AlertHelper.getInstance(getActivity()).hideLoading();
		}

		@Override
		public void onFailure(int errorNo, String errorMsg) {
			if(mEndTime != null && errorNo != 204){
				cv.requestFailDate.add(mEndTime);
			}
			AlertHelper.getInstance(getActivity()).hideLoading();
		}
		
	};
}
