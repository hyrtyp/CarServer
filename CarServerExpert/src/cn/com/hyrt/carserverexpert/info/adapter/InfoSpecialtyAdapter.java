package cn.com.hyrt.carserverexpert.info.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.info.activity.InfoDetailActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class InfoSpecialtyAdapter extends BaseAdapter{

	private List<String> mData;
	private Context mContext;
	private List<String> checkedPosition = new ArrayList<String>();
	private ViewGroup rootView;
	
	public InfoSpecialtyAdapter(List<String> mData, Context mContext) {
		super();
		this.mData = mData;
		this.mContext = mContext;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parentView) {
		rootView = parentView;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_checkbox_item, null);
		}
		
		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		checkbox.setChecked(checkedPosition.contains(position+""));
		checkbox.setText(mData.get(position));
		checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					checkedPosition.add(position+"");
				}else{
					((InfoDetailActivity)mContext).selectAll(false);
					checkedPosition.remove(position+"");
				}
				for(int i=0,j=checkedPosition.size(); i<j; i++){
					LogHelper.i("tag", "checked:"+checkedPosition.get(i));
				}
			}
		});
		return convertView;
	}
	
	public List<String> getCheckedPosition(){
		return checkedPosition;
	}
	
	public void selectAll(boolean checked){
		for(int i=0,j=rootView.getChildCount(); i<j; i++){
			View childView = rootView.getChildAt(i);
			CheckBox cb = (CheckBox) childView.findViewById(R.id.checkbox);
			cb.setChecked(checked);
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		checkedPosition.clear();
		super.notifyDataSetChanged();
	}

}
