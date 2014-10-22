package cn.com.hyrt.carserverseller.order.adapter;

import java.text.SimpleDateFormat;
import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ORDER_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter{

	private List<Define.ORDER_LIST.CDATA> mData;
	private Context mContext;
	private String type;
	
	public OrderListAdapter(List<CDATA> mData, Context mContext, String type) {
		super();
		this.mData = mData;
		this.mContext = mContext;
		this.type = type;
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
					.inflate(R.layout.layout_order_item, null);
			mHolder = new ViewHolder();
			mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			mHolder.tvTime1 = (TextView) convertView.findViewById(R.id.tv_time1);
			mHolder.tvTime2 = (TextView) convertView.findViewById(R.id.tv_time2);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(position%2 == 0){
			convertView.setBackgroundColor(0xfff4f4f4);
		}else{
			convertView.setBackgroundColor(0xffffffff);
		}
		
		Define.ORDER_LIST.CDATA orderInfo = mData.get(position);
		mHolder.tvTitle.setText("订单项目："+orderInfo.spname);
		if(WebServiceHelper.ORDER_TYPE_HIS.equals(type)){
			String status = orderInfo.status;
			if("ywc".equals(status)){
				mHolder.tvStatus.setText(
						Html.fromHtml("<font color='#ff7e0c'>已完成</font>"));
			}else if("qx".equals(status)){
				mHolder.tvStatus.setText(
						Html.fromHtml("<font color='#299fff'>已取消</font>"));
			}else{
				if(orderInfo.arrivetime != null 
						&& !"".equals(orderInfo.arrivetime) 
						&& StringHelper.beyondTime(orderInfo.arrivetime)){
					mHolder.tvStatus.setText(
							Html.fromHtml("<font color='#939393'>已过期</font>"));
				}
			}
			
			if(orderInfo.arrivetime != null && !"".equals(orderInfo.arrivetime)){
				mHolder.tvTime2.setText("到店时间："
						+StringHelper.formatDate(orderInfo.arrivetime));
			}
		}else if(WebServiceHelper.ORDER_TYPE_AND.equals(type)){
			if(orderInfo.makedate != null && !"".equals(orderInfo.makedate)){
				mHolder.tvTime2.setText("预约时间："
						+StringHelper.formatDate(orderInfo.makedate));
			}
		}
		if(orderInfo.createtime != null && !"".equals(orderInfo.createtime)){
			mHolder.tvTime1.setText("下单时间："
					+StringHelper.formatDate(orderInfo.createtime));
		}
		
		
		
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tvTitle;
		public TextView tvStatus;
		public TextView tvTime1;
		public TextView tvTime2;
	}

}
