package cn.com.hyrt.carserverseller.info.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ZZVERTIFICATION_LIST;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ZZVERTIFICATION_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper.RequestCallback;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.info.activity.VerificationDetitalActivity;
import cn.com.hyrt.carserverseller.info.activity.VerificationResultActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class VerificationAdapter extends BaseAdapter{

	private Context mContext;
	private String id;
	private List<Define.ZZVERTIFICATION_LIST.CDATA> mData;
	private int positio;
	
	public VerificationAdapter(Context mContext, ArrayList<CDATA> data,String id) {
		super();
		this.mContext = mContext;
		this.mData = data;
		this.id = id;
	}

	@Override
	public int getCount() {
		return mData.size()-1;  // 排除掉最新的申请
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
					.inflate(R.layout.verification_result_item, null);
			mHolder.rl_content_item = (RelativeLayout) convertView.findViewById(R.id.rl_content_item);
			mHolder.rb = (RatingBar) convertView.findViewById(R.id.item_rating1);
			mHolder.tv_zzstatus = (TextView) convertView.findViewById(R.id.tv_zzstatus1);
			mHolder.tv_zztype = (TextView) convertView.findViewById(R.id.tv_zztype1);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		final Define.ZZVERTIFICATION_LIST.CDATA data = mData.get(position+1); // 排除掉最新的申请
		positio = position+1; 
//		mHolder.rb.setRating(5);
		mHolder.tv_zztype.setText(data.zztypename);
		if ("1".equals(data.zztype)) {
			mHolder.rb.setRating(4);
		}else{
			mHolder.rb.setRating(5);
		}
		if ("ysh".equals(data.status)) {
			mHolder.tv_zzstatus.setText("通过");
		}else{
			mHolder.tv_zzstatus.setText("驳回");
		}
//		mHolder.tv_zzstatus.setText("通过");
		mHolder.rl_content_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, VerificationDetitalActivity.class);
				intent.putExtra("typeid", data.id);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	
	private class ViewHolder{
		private RelativeLayout rl_content_item;
		private RatingBar rb;
		public TextView tv_zzstatus;
		public TextView tv_zztype;
	}

}
