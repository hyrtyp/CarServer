package cn.com.hyrt.carserver.base.view;

import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.ScreenHelper;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class HorizontalScrollTitleView extends HorizontalScrollView{
	
	private List<String> titles;
	private int curSelected = 0;
	private int nextSelected = 0;
	private int itemWidth = ScreenHelper.dip2px(getContext(), 75);
	private int itemHeight = ScreenHelper.dip2px(getContext(), 51);
	private int screenWidth;
	
	private int focusTextColor = 0xffffffff;
	private int normalTextColor = 0xff000000;
	private int focusBgColor = 0xff00c4e9;
	private int normalBgColor = 0xffffffff;
	
	private TitleViewOnclickListener mListener;
	
	private MyAbsoluteLayout contentLayout;

	public HorizontalScrollTitleView(Context context) {
		super(context);
	}

	public HorizontalScrollTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HorizontalScrollTitleView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	private float downX;
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_DOWN){
			downX = ev.getX();
		}else if(ev.getAction() == MotionEvent.ACTION_UP){
			float offsetX = Math.abs(ev.getX()-downX);
			if(offsetX < 10){
//				nextSelected = downX%itemWidth==0 ? (int)(downX/itemWidth-1) : (int)(downX/itemWidth);
//				LogHelper.i("tag", "nextSelected:"+nextSelected);
				contentLayout.onTouchEvent(ev);
				downX = 0;
				return true;
			}
		}
		return super.onTouchEvent(ev);
	}
	
	public void setTitles(List<String> titles){
		this.titles = titles;
		if(titles == null || titles.size() == 0){
			removeAllViews();
			contentLayout = null;
			return;
		}
		if(contentLayout == null){
			removeAllViews();
			contentLayout = new MyAbsoluteLayout(getContext());
			AbsoluteLayout.LayoutParams mParams = new AbsoluteLayout.LayoutParams(
					itemWidth*titles.size(),
					itemHeight, 0, 0);
			this.addView(contentLayout, mParams);
		}
		notifyDataSetChanged();
	}
	
	public void notifyDataSetChanged(){
		contentLayout.removeAllViews();
		for(int i=0,j=titles.size(); i<j; i++){
			TextView tvTitle = new TextView(getContext());
			tvTitle.setText(titles.get(i));
			tvTitle.setGravity(Gravity.CENTER);
			if(curSelected == i){
				tvTitle.setBackgroundColor(focusBgColor);
				tvTitle.setTextColor(focusTextColor);
			}else{
				tvTitle.setBackgroundColor(normalBgColor);
				tvTitle.setTextColor(normalTextColor);
			}
			tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
			contentLayout.addView(tvTitle, getItemParams(i));
		}
	}
	
	private AbsoluteLayout.LayoutParams getItemParams(int position){
		int x = position*itemWidth;
		return new AbsoluteLayout.LayoutParams(itemWidth, itemHeight, x, 0);
	}
	
	public void moveTo(int position){
		if(nextSelected == position){
			return;
		}
		nextSelected = position;
		moveTo();
	}
	
	private void moveTo(){
		if(screenWidth <= 0){
			WindowManager mWindowManager 
			= (WindowManager) getContext()
			.getSystemService(Context.WINDOW_SERVICE);
			screenWidth = mWindowManager.getDefaultDisplay().getWidth();
		}
		int[] location = new int[2];
		TextView curView = (TextView) contentLayout.getChildAt(curSelected);
		TextView nextView = (TextView) contentLayout.getChildAt(nextSelected);
		
		if(curView != null){
			curView.setBackgroundColor(normalBgColor);
			curView.setTextColor(normalTextColor);
		}
		if(nextView != null){
			nextView.setBackgroundColor(focusBgColor);
			nextView.setTextColor(focusTextColor);
			nextView.getLocationOnScreen(location);
			if(location[0]+itemWidth > screenWidth){
				fullScroll(HorizontalScrollTitleView.FOCUS_RIGHT);
			}else if(location[0] < 0){
				fullScroll(HorizontalScrollTitleView.FOCUS_LEFT);
			}
		}
		curSelected = nextSelected;
		if(mListener != null){
			mListener.onClick(curSelected);
		}
	}
	
	private class MyAbsoluteLayout extends AbsoluteLayout{

		public MyAbsoluteLayout(Context context) {
			super(context);
		}
		
		private float downX;
		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			if(ev.getAction() == MotionEvent.ACTION_DOWN){
				downX = ev.getX();
			}else if(ev.getAction() == MotionEvent.ACTION_UP){
				nextSelected = downX%itemWidth==0 ? (int)(downX/itemWidth-1) : (int)(downX/itemWidth);
				LogHelper.i("tag", "nextSelected:"+nextSelected);
				downX = 0;
				moveTo();
				return true;
			}
			return super.onTouchEvent(ev);
		}
	}
	
	public static interface TitleViewOnclickListener{
		public void onClick(int position);
	}
	
	public void setListener(TitleViewOnclickListener listener){
		this.mListener = listener;
	}
}
