package cn.com.hyrt.carserver.question.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.StartActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.ZJUSERMAIN_LIST.CDATA;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.question.activity.ExpertDetitalActivity;
import cn.com.hyrt.carserver.question.activity.MoreExpertsActivity;
import cn.com.hyrt.carserver.question.activity.QuestionActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class MoreExpertsAdapter extends BaseAdapter {
	private Context mContext ;
	private List<Define.ZJUSERMAIN_LIST.CDATA> mExpertList = new ArrayList<Define.ZJUSERMAIN_LIST.CDATA>();


	public MoreExpertsAdapter(Context mContext, List<CDATA> mExpertList) {
		this.mExpertList = mExpertList ;
		this.mContext = mContext ;
	}

	@Override
	public int getCount() {
		return mExpertList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder ;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.question_experts_list_item, null);
			holder = new ViewHolder();
			holder.iv_car_expert_face = (ImageLoaderView) convertView.findViewById(R.id.iv_car_expert_face);
			holder.tv_car_expert_name = (TextView) convertView.findViewById(R.id.tv_car_expert_name);
			holder.tv_car_expert_zhuanchang = (TextView) convertView.findViewById(R.id.tv_car_expert_zhuanchang);
			holder.rb_car_expert_score = (RatingBar) convertView.findViewById(R.id.rb_car_expert_score);
			holder.bt_ask = (Button) convertView.findViewById(R.id.bt_ask);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		CDATA cdata = mExpertList.get(position);
		holder.iv_car_expert_face.setImageUrl(cdata.imagepath);
		holder.tv_car_expert_name.setText("专家姓名："+cdata.name);
		holder.tv_car_expert_zhuanchang.setText("专家专长："+cdata.zcnames);
		float f ;
		if ("".equals(cdata.serviceattitude) || cdata.serviceattitude == null) {
			f = 0;
		}else{
			f = Float.parseFloat(cdata.serviceattitude);
		}
		holder.rb_car_expert_score.setRating(f);
		holder.rb_car_expert_score.setIsIndicator(true);
		holder.bt_ask.setText("提问");
		
		holder.bt_ask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(mContext, QuestionActivity.class);
				intent.putExtra("zjid", mExpertList.get(position).id);
				mContext.startActivity(intent);
			}
		});
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext,ExpertDetitalActivity.class);
				intent.putExtra("id", mExpertList.get(position).id);
				mContext.startActivity(intent);
			}
		});
		
		return convertView;
	}
	
	class ViewHolder{
		ImageLoaderView  iv_car_expert_face;
		TextView tv_car_expert_name;
		TextView tv_car_expert_zhuanchang;
		RatingBar rb_car_expert_score;
		Button bt_ask;
	}

}
