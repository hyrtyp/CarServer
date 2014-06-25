package cn.com.hyrt.carserver.question.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class QuestionBannerAdapter extends PagerAdapter{

	private List<View> views;
	
	public QuestionBannerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		container.removeView(views.get(position));
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(views.get(position), 0);
		return views.get(position);
	}

}
