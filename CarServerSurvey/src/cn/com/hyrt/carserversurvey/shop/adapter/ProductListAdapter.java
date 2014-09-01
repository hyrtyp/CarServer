package cn.com.hyrt.carserversurvey.shop.adapter;

import java.util.List;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.INFO_PRODUCT;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductListAdapter extends BaseAdapter{

	private List<Define.INFO_PRODUCT> products;
	private Context mContext;
	
	public ProductListAdapter(List<INFO_PRODUCT> products, Context mContext) {
		super();
		this.products = products;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return products.size();
	}

	@Override
	public Object getItem(int arg0) {
		return products.get(arg0);
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
					.inflate(R.layout.layout_product_item, null);
			mHolder = new ViewHolder();
			mHolder.ivImg = (ImageLoaderView) convertView.findViewById(R.id.iv_img);
			mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc);
			mHolder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
			mHolder.tvDiscount = (TextView) convertView.findViewById(R.id.tv_discount);
			mHolder.tvDiscount.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		Define.INFO_PRODUCT product = products.get(position);
		
		mHolder.ivImg.setImageUrl(product.attacpath);
		mHolder.tvTitle.setText(product.spname);
		mHolder.tvDesc.setText(product.sptitle);
		mHolder.tvPrice.setText(product.price);
		mHolder.tvDiscount.setText("￥"+product.discount);
		return convertView;
	}
	
	private class ViewHolder{
		public ImageLoaderView ivImg;
		public TextView tvTitle;
		public TextView tvDesc;
		public TextView tvPrice;
		public TextView tvDiscount;
	}

}
