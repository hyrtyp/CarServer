package cn.com.hyrt.carserver.info.adapter;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.REPLY_DETAIL;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionDetailAdapter extends BaseAdapter{

	private Define.REPLY_DETAIL replys;
	private Context mContext;
	
//	private View chatView;
//	private View chatMeView;
	
	public QuestionDetailAdapter(REPLY_DETAIL replys, Context mContext) {
		super();
		this.replys = replys;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return replys.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return replys.data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		LogHelper.i("tag", "adapter:"+position);
		Define.REPLY_DETAIL.CDATA data = replys.data.get(position);
		
		boolean needCreateView = false;
		boolean isMe = false;
		if(convertView == null){
			needCreateView = true;
		}else if(convertView.getTag() == null || !data.replytype.equals(convertView.getTag().toString())){
			needCreateView = true;
		}
		
		if("zxr".equals(data.replytype)){
			isMe = true;
		}else{
			isMe = false;
		}
		
		if(needCreateView){
			if(isMe){
//				if(chatMeView == null){
//					chatMeView = LayoutInflater.from(mContext).inflate(R.layout.layout_question_chat_me, null);
//				}
//				convertView = chatMeView;
				convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_question_chat_me, null);
			}else{
//				if(chatView == null){
//					chatView = LayoutInflater.from(mContext).inflate(R.layout.layout_question_chat, null);
//				}
//				convertView = chatView;
				convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_question_chat, null);
			}
		}
		
		convertView.setTag(data.replytype);
		
		TextView tv_time = (TextView) convertView.findViewById(R.id.tv_time);
		ImageLoaderView iv_face = (ImageLoaderView) convertView.findViewById(R.id.iv_face);
		TextView tv_content = (TextView) convertView.findViewById(R.id.tv_content);
		
		tv_time.setText(StringHelper.getFriendlydate(data.contenttime));
		iv_face.setImageUrl(data.attacpath);
		tv_content.setText(data.replycontent);
		
		if(!isMe){
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			TextView tv_replytype = (TextView) convertView.findViewById(R.id.tv_replytype);
			LogHelper.i("tag", "tv_name:"+tv_name);
			tv_name.setText(data.username);
			tv_replytype.setText(data.replytype);
		}
		
		return convertView;
	}

}
