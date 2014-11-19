package cn.com.hyrt.carserverexpert.info.adapter;

import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_INTEGRATION.CDATA;
import cn.com.hyrt.carserverexpert.base.helper.StringHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class IntegrationAdapter extends BaseAdapter{

	private List<Define.INFO_INTEGRATION.CDATA> mData;
	private Context mContext;
	
	public IntegrationAdapter(List<CDATA> mData, Context mContext) {
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
					.inflate(R.layout.layout_integration_item, null);
			mHolder = new ViewHolder();
			mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(position%2 == 0){
			convertView.setBackgroundColor(0xfff4f4f4);
		}else{
			convertView.setBackgroundColor(0xffffffff);
		}
		
		Define.INFO_INTEGRATION.CDATA integrationInfo = mData.get(position);
		mHolder.tvTime.setText(StringHelper.formatDate(integrationInfo.integrltime));
		mHolder.tvContent.setText(integrationInfo.integrlnum+"积分");
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tvTime;
		private TextView tvContent;
	}

}
