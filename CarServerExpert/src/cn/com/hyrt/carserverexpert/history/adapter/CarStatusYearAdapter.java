package cn.com.hyrt.carserverexpert.history.adapter;

import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.CAR_YEAR;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class CarStatusYearAdapter extends BaseAdapter{

	private List<Define.CAR_YEAR.CDATA> mData;
	private Context mContext;
	public int curIndex;
	
	public CarStatusYearAdapter(List<CAR_YEAR.CDATA> mData, Context mContext) {
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
	public View getView(final int position, View convertView, final ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_car_status_year_item, null);
			mHolder = new ViewHolder();
			mHolder.btnYear = (TextView) convertView.findViewById(R.id.btn_year);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(curIndex == position){
			mHolder.btnYear.setBackgroundResource(R.drawable.bg_car_status_year_focus);
			mHolder.btnYear.setTextColor(0xff289fff);
		}else{
			mHolder.btnYear.setBackgroundResource(R.drawable.bg_car_status_year_normal);
			mHolder.btnYear.setTextColor(0xff7f7f7f);
		}
		
		mHolder.btnYear.setText(mData.get(position).time);
		
//		mHolder.btnYear.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View view) {
//				LogHelper.i("tag", "position:"+position);
//				if(curIndex == position){
//					return;
//				}
//				Button curBtnYear = (Button) arg2.getChildAt(curIndex)
//						.findViewById(R.id.btn_year);
//				curBtnYear.setBackgroundResource(R.drawable.bg_car_status_year_normal);
//				curBtnYear.setTextColor(0xff7f7f7f);
//				
//				Button nextBtnYear = (Button) view.findViewById(R.id.btn_year);
//				nextBtnYear.setBackgroundResource(R.drawable.bg_car_status_year_focus);
//				nextBtnYear.setTextColor(0xff289fff);
//				curIndex = position;
//			}
//		});
		
		return convertView;
	}
	
	private class ViewHolder {
		public TextView btnYear;
	}
}
