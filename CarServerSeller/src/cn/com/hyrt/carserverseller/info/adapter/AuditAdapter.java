package cn.com.hyrt.carserverseller.info.adapter;

import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_AUDIT_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AuditAdapter extends BaseAdapter{

	private Context mContext;
	private List<Define.INFO_AUDIT_LIST.CDATA> mData;
	
	public AuditAdapter(Context mContext, List<CDATA> mData) {
		super();
		this.mContext = mContext;
		this.mData = mData;
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
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_audit_item, null);
			mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
			mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		Define.INFO_AUDIT_LIST.CDATA auditInfo = mData.get(position);
		mHolder.tvTitle.setText(auditInfo.title);
		mHolder.tvDesc.setText("审核意见："+auditInfo.shyj);
		mHolder.tvTime.setText("审核时间："+StringHelper.formatDate(auditInfo.shbhtime));
		
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tvTitle;
		public TextView tvDesc;
		public TextView tvTime;
	}

}
