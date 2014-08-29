package cn.com.hyrt.carserversurvey.base.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AddPhotoGridAdapter extends BaseAdapter{

	private List<Bitmap> photos;
	private Context mContext;
	public int maxNum = 1;
	
	private PhotoGridCallback mCallback;
	
	public AddPhotoGridAdapter(List<Bitmap> photos, Context mContext) {
		super();
		this.photos = photos;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if(photos.size() < maxNum){
			if(photos.size() == 0){
				return 2;
			}
			return photos.size()+1;
		}
		return photos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return photos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_add_photo_item, null);
			mHolder = new ViewHolder();
			mHolder.img = (ImageView) convertView.findViewById(R.id.iv_img);
			mHolder.text = (TextView) convertView.findViewById(R.id.tv_text);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		if(photos.size() < maxNum && position == photos.size()){
			mHolder.img.setImageResource(R.drawable.ic_photo_add);
			mHolder.img.setVisibility(View.VISIBLE);
			mHolder.text.setVisibility(View.GONE);
		}else if(photos.size() == 0 && position == 1){
			mHolder.text.setText("添加照片");
			mHolder.text.setVisibility(View.VISIBLE);
			mHolder.img.setVisibility(View.GONE);
		}else{
			mHolder.img.setImageBitmap(photos.get(position));
			mHolder.img.setVisibility(View.VISIBLE);
			mHolder.text.setVisibility(View.GONE);
		}
		
		mHolder.img.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mCallback != null){
					if(photos.size() < maxNum && position == photos.size()){
						mCallback.onClick(-1);
					}else{
						mCallback.onClick(position);
					}
				}
			}
		});
		
		mHolder.img.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if(mCallback != null){
					if(photos.size() < maxNum && position == photos.size()){
						mCallback.onLongClick(-1);
					}else{
						mCallback.onLongClick(position);
					}
				}
				return true;
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder{
		public ImageView img;
		public TextView text;
	}
	
	public static interface PhotoGridCallback{
		
		/**
		 * 点击事件
		 * @param position （-1：增加图片, >=0：查看图片）
		 */
		public void onClick(int position);
		
		/**
		 * 长按事件
		 * @param position （-1：增加图片, >=0：查看图片）
		 */
		public void onLongClick(int position);
	}
	
	public void setCallback(PhotoGridCallback callback){
		this.mCallback = callback;
	}
	
}
