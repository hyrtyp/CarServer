package cn.com.hyrt.carserverexpert.history.adapter;

import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_CAR_STATUS_LIST.CDATA;
import cn.com.hyrt.carserverexpert.base.helper.StringHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CarStatusAdapter extends BaseAdapter{

	private List<Define.INFO_CAR_STATUS_LIST.CDATA> mData;
	private Context mContext;
	
	public CarStatusAdapter(List<CDATA> mData, Context mContext) {
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
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_car_status_item, null);
			mHolder = new ViewHolder();
			mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
			mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(position % 2 == 0){
			convertView.setBackgroundColor(0xfff4f4f4);
		}else{
			convertView.setBackgroundColor(0xffffffff);
		}
		
		Define.INFO_CAR_STATUS_LIST.CDATA carStatusInfo = mData.get(position);
		mHolder.tvName.setText("车辆名称："+carStatusInfo.model);
		mHolder.tvTime.setText("保险时间："+StringHelper.formatDate(carStatusInfo.insurancedate));
		
		
		return convertView;
	}
	
	private class ViewHolder {
		public TextView tvName;
		public TextView tvTime;
	}

}
