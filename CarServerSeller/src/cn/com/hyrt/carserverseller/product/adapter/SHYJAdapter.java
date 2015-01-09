package cn.com.hyrt.carserverseller.product.adapter;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SHYJAdapter extends BaseAdapter {
	private Context mContext;
	private String[] shyjs;
	private String[] shbhtimes;
	
	
	public SHYJAdapter(Context mContext, String[] shyjs, String[] shbhtimes) {
		super();
		this.mContext = mContext;
		this.shyjs = shyjs;
		this.shbhtimes = shbhtimes;
	}

	@Override
	public int getCount() {
		return shbhtimes.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mHolder = null;
		System.out.println(position+"ddd ");
		if(convertView == null){
			mHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_lsshyj_item, null);
			mHolder.tv_shyj1 = (TextView) convertView.findViewById(R.id.tv_shyj1);
			mHolder.tv_shsj1 = (TextView) convertView.findViewById(R.id.tv_shsj1);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		mHolder.tv_shyj1.setText(shyjs[position]);
		mHolder.tv_shsj1.setText(StringHelper.formatDate(shbhtimes[position]));
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tv_shyj1;
		public TextView tv_shsj1;
	}
}
