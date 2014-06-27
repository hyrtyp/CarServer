package cn.com.hyrt.carserver.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class FullListView extends ListView{

	public FullListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		 int expandSpec = MeasureSpec.makeMeasureSpec(     
	                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
