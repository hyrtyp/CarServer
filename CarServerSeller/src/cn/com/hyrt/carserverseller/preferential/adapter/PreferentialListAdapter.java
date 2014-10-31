package cn.com.hyrt.carserverseller.preferential.adapter;

import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.PREFERENTIAL_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PreferentialListAdapter extends BaseAdapter{

	private Context mContext;
	private List<Define.PREFERENTIAL_LIST.CDATA> mData;
	
	public PreferentialListAdapter(Context mContext, List<CDATA> mData) {
		super();
		this.mContext = mContext;
		this.mData = mData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_preferential_item, null);
			mHolder = new ViewHolder();
			mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.layoutFace = (RelativeLayout) convertView.findViewById(R.id.layout_face);
			mHolder.ivFace = (ImageLoaderView) convertView.findViewById(R.id.iv_face);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		Define.PREFERENTIAL_LIST.CDATA preferentialInfo = mData.get(position);
		mHolder.tvTitle.setText(preferentialInfo.title);
		if(preferentialInfo.attacpath == null 
				|| "".equals(preferentialInfo.attacpath)){
			mHolder.layoutFace.setVisibility(View.GONE);
		}else{
			mHolder.layoutFace.setVisibility(View.VISIBLE);
			mHolder.ivFace.setImageUrl(preferentialInfo.attacpath);
		}
		
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tvTitle;
		public RelativeLayout layoutFace;
		public ImageLoaderView ivFace;
	}

}
