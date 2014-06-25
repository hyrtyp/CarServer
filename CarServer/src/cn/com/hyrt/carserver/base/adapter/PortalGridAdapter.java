package cn.com.hyrt.carserver.base.adapter;

import cn.com.hyrt.carserver.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页入口表格适配器
 * @author zoe
 *
 */
public class PortalGridAdapter extends BaseAdapter{

	private int[] imgArray;
	private int[] textSourceArray;
	private Context mContext;
	
	
	
	public PortalGridAdapter(int[] imgArray, int[] textSourceArray,
			Context mContext) {
		super();
		this.imgArray = imgArray;
		this.textSourceArray = textSourceArray;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return imgArray.length;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		if(convertView == null){
			LayoutInflater mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.layout_portal_item, null);
		}
		
		if(mContext.getString(textSourceArray[index]).toString().length() <= 0){
			convertView.setBackgroundResource(android.R.color.white);
		}else{
			convertView.setBackgroundResource(R.drawable.item_bg);
		}
		
		ImageView img = (ImageView) convertView.findViewById(R.id.iv_portal_img);
		TextView text = (TextView) convertView.findViewById(R.id.tv_portal_text);
		
		img.setImageResource(imgArray[index]);
		text.setText(textSourceArray[index]);
		
		return convertView;
	}

}
