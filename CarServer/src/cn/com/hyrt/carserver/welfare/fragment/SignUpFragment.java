package cn.com.hyrt.carserver.welfare.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.ScreenHelper;
import cn.com.hyrt.carserver.base.view.CalendarView;
import cn.com.hyrt.carserver.base.view.CalendarView.OnItemClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SignUpFragment extends Fragment{

	private View rootView;
	private CalendarView cv;
	private TextView tv_year;
	private TextView tv_day;
	private SimpleDateFormat mSimpleDateFormat1;
	private SimpleDateFormat mSimpleDateFormat2;
	private Date nowDate;
	private TextView tv_top;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_signup, null);
		findView();
		return rootView;
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
		cv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onMonthChange(Date date) {
				if(nowDate == null){
					nowDate = new Date(System.currentTimeMillis());
				}
				if(date.getMonth() == nowDate.getMonth()){
					tv_year.setText(mSimpleDateFormat1.format(date));
					tv_day.setText(mSimpleDateFormat2.format(date));
					tv_day.setVisibility(View.VISIBLE);
				}else{
					tv_year.setText(mSimpleDateFormat1.format(date));
					tv_day.setVisibility(View.GONE);
				}
			}
		});
		
		List<String> signUpDays = new ArrayList<String>();
		signUpDays.add("2014-07-26");
		signUpDays.add("2014-07-27");
		signUpDays.add("2014-07-30");
		signUpDays.add("2014-08-02");
		signUpDays.add("2014-08-04");
		signUpDays.add("2014-08-06");
		signUpDays.add("2014-08-09");
		signUpDays.add("2014-08-10");
		signUpDays.add("2014-08-11");
		signUpDays.add("2014-08-12");
		signUpDays.add("2014-08-13");
		signUpDays.add("2014-08-14");
		signUpDays.add("2014-08-31");
		signUpDays.add("2014-09-06");
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
	}
}
