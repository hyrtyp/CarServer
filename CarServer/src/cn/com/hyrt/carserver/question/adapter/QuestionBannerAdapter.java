package cn.com.hyrt.carserver.question.adapter;

import java.util.List;

import cn.com.hyrt.carserver.R;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class QuestionBannerAdapter extends PagerAdapter
{
	private List<View> views;
	
	public QuestionBannerAdapter(List<View> views)
	{
		super();
		this.views = views;
	}

	@Override
	public int getCount()
	{
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		container.removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(final ViewGroup container, final int position)
	{
		container.addView(views.get(position), 0);
		container.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				TextView url = (TextView) container.findViewById(position).findViewById(R.id.tv_url);
			}
		});
		return views.get(position);
	}
}
