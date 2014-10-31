package cn.com.hyrt.carserverseller.shop.adapter;

import java.util.List;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_PRODUCT_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter{

	private List<Define.INFO_PRODUCT_LIST.CDATA> mData;
	private Context mContext;
	
	public ProductListAdapter(List<CDATA> mData, Context mContext) {
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
		ViewHolder mHolder;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_product_item, null);
			mHolder = new ViewHolder();
			mHolder.ivFace = (ImageLoaderView) convertView.findViewById(R.id.iv_face);
			mHolder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
			mHolder.tvDiscount = (TextView) convertView.findViewById(R.id.tv_discount);
			mHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.tvDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			mHolder.tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		Define.INFO_PRODUCT_LIST.CDATA productInfo = mData.get(position);
		mHolder.ivFace.setImageUrl(productInfo.attacpath);
		mHolder.tvDesc.setText(productInfo.sptitle);
		mHolder.tvDiscount.setText("￥"+productInfo.discount);
		mHolder.tvPrice.setText("￥"+productInfo.price);
		mHolder.tvTitle.setText(productInfo.spname);
//		if("ysj".equals(productInfo.status)){
//			mHolder.tvStatus.setText(Html.fromHtml("<font color='#ff7e0c'>已审核</font>"));
//			mHolder.tvStatus.setVisibility(View.VISIBLE);
//		}else if("bh".equals(productInfo.status)){
//			mHolder.tvStatus.setText(Html.fromHtml("<font color='#939393'>已驳回</font>"));
//			mHolder.tvStatus.setVisibility(View.VISIBLE);
//		}else{
//			mHolder.tvStatus.setVisibility(View.GONE);
//		}
		
		return convertView;
	}
	
	private class ViewHolder{
		ImageLoaderView ivFace;
		TextView tvTitle;
		TextView tvDesc;
		TextView tvPrice;
		TextView tvDiscount;
		TextView tvStatus;
	}
	

}
