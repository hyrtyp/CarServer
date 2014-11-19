package cn.com.hyrt.carserverexpert.workbench.adapter;

import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_QUESTION_LIST.CDATA;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.StringHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.workbench.activity.QuestionDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionAdapter extends BaseAdapter{

	private List<Define.INFO_QUESTION_LIST.CDATA> mData;
	private Context mContext;
	private String type;
	
	public QuestionAdapter(List<CDATA> mData, Context mContext, String type) {
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
	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder mHolder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_question_item, null);
			mHolder = new ViewHolder();
			mHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
			mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
			mHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_content);
//			mHolder.btnAccept = (Button) convertView.findViewById(R.id.btn_accept);
			mHolder.ivUnread = (ImageView) convertView.findViewById(R.id.iv_unread);
			convertView.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertView.getTag();
		}
		
		if(position%2 == 0){
			convertView.setBackgroundColor(0xfff4f4f4);
		}else{
			convertView.setBackgroundColor(0xffffffff);
		}
		
		final Define.INFO_QUESTION_LIST.CDATA questioninfo = mData.get(position);
		mHolder.tvTime.setText(StringHelper.formatDate(questioninfo.lasttime));
		mHolder.tvTitle.setText(questioninfo.zyname);
		mHolder.tvContent.setText(questioninfo.content);
		if(WebServiceHelper.QUESTION_TYPE_NEW.equals(type)){
//			mHolder.btnAccept.setVisibility(View.VISIBLE);
			mHolder.ivUnread.setVisibility(View.GONE);
		}else{
//			mHolder.btnAccept.setVisibility(View.GONE);
			if(WebServiceHelper.QUESTION_TYPE_AN.equals(type)
					&& "zjwjj".equals(questioninfo.status)){
				mHolder.ivUnread.setVisibility(View.VISIBLE);
			}else{
				mHolder.ivUnread.setVisibility(View.GONE);
			}
		}
		
		convertView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(mContext, QuestionDetailActivity.class);
				intent.putExtra("questionId", questioninfo.id);
				intent.putExtra("type", type);
				((BaseActivity)mContext).startActivityForResult(intent, 101);
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder{
		public TextView tvTitle;
		public TextView tvContent;
		public TextView tvTime;
//		public Button btnAccept;
		public ImageView ivUnread;
	}

}
