package cn.com.hyrt.carserverseller.info.adapter;

import java.util.List;

import cn.com.hyrt.carserverseller.base.helper.ScreenHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class QualificationTypeAdapter extends BaseAdapter{

	private String[] imgPath;
	private Context mContext;
	
	public QualificationTypeAdapter(String[] imgPath, Context mContext) {
		super();
		this.imgPath = imgPath;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return imgPath.length;
	}

	@Override
	public Object getItem(int arg0) {
		return imgPath[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if(convertView == null){
			convertView = new ImageLoaderView(mContext);
			AbsListView.LayoutParams mLayoutParams = new AbsListView.LayoutParams(
					ScreenHelper.dip2px(mContext, 25),
					ScreenHelper.dip2px(mContext, 25));
			convertView.setLayoutParams(mLayoutParams);
		}
		((ImageLoaderView)convertView).setImageUrl(imgPath[position]);
		return convertView;
	}

}
