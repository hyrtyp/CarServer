package cn.com.hyrt.carserver.emergency.adapter;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INSURANCE_CLAIM_LIST;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class InsuranceClaimAdapter extends BaseAdapter{

	private Define.INSURANCE_CLAIM_LIST ic;
	private Context mContext;
	private MyIcOnClickListener mListener;
	
	public InsuranceClaimAdapter(INSURANCE_CLAIM_LIST ic, Context mContext) {
		super();
		this.ic = ic;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ic.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ic.data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_claim_item, null);
		}
		final ImageLoaderView ivFaceImg = (ImageLoaderView) convertView.findViewById(R.id.iv_ic_img);
		final TextView bxname = (TextView) convertView.findViewById(R.id.tv_bx_content);
		final TextView bxtel = (TextView) convertView.findViewById(R.id.tv_bx_tel);
		final ImageLoaderView bxcall =(ImageLoaderView)convertView.findViewById(R.id.tv_bx_call); 
		
			View.OnClickListener mClickListener = new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if(mListener != null){
						mListener.onClick(position);
					}
					
				}
			};
		bxcall.setOnClickListener(mClickListener);
		
		Define.INSURANCE_CLAIM_LIST.CDATA icc = ic.data.get(position);
		ivFaceImg.setImageUrl(icc.imagepath);
		bxname.setText(icc.sjname);
		bxtel.setText(icc.sjtel);
		bxcall.setImageResource(R.drawable.emergency_tel);
		bxcall.setTag(position);
		return convertView;
	}
	
	public void setOnClickListener(MyIcOnClickListener listener){
		this.mListener = listener;
	}
	
	public static interface MyIcOnClickListener{
		/**
		 * 
		 * @param position(1:拨打电话)
		 */
		public void onClick(int position);
	}
	
}
