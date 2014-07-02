package cn.com.hyrt.carserver.info.adapter;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_CAR_LIST;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCarAdapter extends BaseAdapter{

	private Define.INFO_CAR_LIST cars;
	private Context mContext;
	private View.OnClickListener mClickListener;
	private MyCarOnClickListener mListener;
	
	public MyCarAdapter(INFO_CAR_LIST cars, Context mContext) {
		super();
		this.cars = cars;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cars.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return cars.data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_my_car_item, null);
		}
		final ImageLoaderView ivFaceImg = (ImageLoaderView) convertView.findViewById(R.id.iv_face_img);
		final TextView tvCarModel = (TextView) convertView.findViewById(R.id.tv_car_model);
		final TextView tvAddTime = (TextView) convertView.findViewById(R.id.tv_add_time);
		final LinearLayout layoutcarContent =(LinearLayout)convertView.findViewById(R.id.iv_face_text); 
		final LinearLayout layoutAddCarCondition 
		= (LinearLayout) convertView.findViewById(R.id.layout_add_car_condition);
		
		if(mClickListener == null){
			mClickListener = new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					if(mListener != null){
						int id = view.getId();
						if(id == ivFaceImg.getId()){
							mListener.onClick(1);
						}else if(id == layoutAddCarCondition.getId()){
							mListener.onClick(2);
						}else{
							mListener.onClick(3);
						}
					}
					
				}
			};
		}
		ivFaceImg.setOnClickListener(mClickListener);
		//tvCarModel.setOnClickListener(mClickListener);
		//tvAddTime.setOnClickListener(mClickListener);
		layoutAddCarCondition.setOnClickListener(mClickListener);
		layoutcarContent.setOnClickListener(mClickListener);
		
		
		Define.INFO_CAR_LIST.CDATA car = cars.data.get(position);
		ivFaceImg.setImageUrl(car.imagepath);
		tvCarModel.setText(car.model);
		tvAddTime.setText(car.checkdate);
		return convertView;
	}
	
	public void setOnClickListener(MyCarOnClickListener listener){
		this.mListener = listener;
	}
	
	public static interface MyCarOnClickListener{
		/**
		 * 
		 * @param position(1:头像；2：添加车况；3:其他)
		 */
		public void onClick(int position);
	}
	
}
