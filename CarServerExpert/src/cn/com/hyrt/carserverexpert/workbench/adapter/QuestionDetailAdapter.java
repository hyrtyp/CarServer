package cn.com.hyrt.carserverexpert.workbench.adapter;

import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.StartActivity;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_QUESTION_DETAIL;
import cn.com.hyrt.carserverexpert.base.helper.StringHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.ImageLoaderView;
import cn.com.hyrt.carserverexpert.history.activity.CarStatusActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class QuestionDetailAdapter extends BaseAdapter{

	private List<Define.INFO_QUESTION_DETAIL> mData;
	private Context mContext;
	private String type;
	
	public QuestionDetailAdapter(List<INFO_QUESTION_DETAIL> mData,
			Context mContext, String type) {
		super();
		this.mData = mData;
		this.mContext = mContext;
		this.type = type;
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
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_chat_item, null);
			mHolder = new ViewHolder();
			mHolder.layoutChatLeft = (LinearLayout) convertView
					.findViewById(R.id.layout_chat_left);
			mHolder.ivFaceLeft = (ImageLoaderView) convertView
					.findViewById(R.id.iv_face_left);
			mHolder.tvTimeLeft = (TextView) convertView
					.findViewById(R.id.tv_time_left);
			mHolder.tvContentLeft = (TextView) convertView
					.findViewById(R.id.tv_content_left);
			mHolder.layoutChatRight = (LinearLayout) convertView
					.findViewById(R.id.layout_chat_right);
			mHolder.ivFaceRight = (ImageLoaderView) convertView
					.findViewById(R.id.iv_face_right);
			mHolder.tvTimeRight = (TextView) convertView
					.findViewById(R.id.tv_time_right);
			mHolder.tvContentRight = (TextView) convertView
					.findViewById(R.id.tv_content_right);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		final Define.INFO_QUESTION_DETAIL questionDetail = mData.get(position);
		if("zj".equals(questionDetail.replytype)){
			mHolder.layoutChatLeft.setVisibility(View.GONE);
			mHolder.layoutChatRight.setVisibility(View.VISIBLE);
			mHolder.ivFaceRight.setImageUrl(questionDetail.attacpath);
			mHolder.tvTimeRight.setText(StringHelper.formatDate(questionDetail.contenttime));
			mHolder.tvContentRight.setText(questionDetail.replycontent);
		}else{
			mHolder.layoutChatLeft.setVisibility(View.VISIBLE);
			mHolder.layoutChatRight.setVisibility(View.GONE);
			mHolder.ivFaceLeft.setImageUrl(questionDetail.attacpath);
			mHolder.tvTimeLeft.setText(StringHelper.formatDate(questionDetail.contenttime));
			mHolder.tvContentLeft.setText(questionDetail.replycontent);
			if(WebServiceHelper.QUESTION_TYPE_HIS.equals(type)){
				mHolder.ivFaceLeft.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.setClass(mContext, CarStatusActivity.class);
						intent.putExtra("id", questionDetail.userterminalid);
						mContext.startActivity(intent);
					}
				});
			}
		}
		
		return convertView;
	}
	
	private class ViewHolder{
		public LinearLayout layoutChatLeft;
		public ImageLoaderView ivFaceLeft;
		public TextView tvTimeLeft;
		public TextView tvContentLeft;
		public LinearLayout layoutChatRight;
		public ImageLoaderView ivFaceRight;
		public TextView tvTimeRight;
		public TextView tvContentRight;
	}

}
