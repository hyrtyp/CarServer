package cn.com.hyrt.carserversurvey.base.adapter;

import java.util.List;

import cn.com.hyrt.carserversurvey.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

public class CheckBoxGridAdapter extends BaseAdapter{

	private List<String> services;
	private Context mContext;
	
	public CheckBoxGridAdapter(List<String> services, Context mContext) {
		super();
		this.services = services;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return services.size();
	}

	@Override
	public Object getItem(int arg0) {
		return services.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_checkbox_item, null);
			mHolder = new ViewHolder();
			mHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		mHolder.checkbox.setText(services.get(position));
		
		return convertView;
	}
	
	private class ViewHolder{
		public CheckBox checkbox;
	}

}
