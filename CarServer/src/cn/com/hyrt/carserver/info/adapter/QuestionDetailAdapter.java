package cn.com.hyrt.carserver.info.adapter;

import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.StartActivity;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.REPLY_DETAIL;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.base.view.PhotoPopupView;
import cn.com.hyrt.carserver.info.activity.QuestionDetailActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class QuestionDetailAdapter extends BaseAdapter{

	private List<Define.REPLY_DETAIL.CDATA> datas;
	private Context mContext;
	
//	private View chatView;
//	private View chatMeView;
	
	public QuestionDetailAdapter(List<Define.REPLY_DETAIL.CDATA> datas, Context mContext) {
		super();
		this.datas = datas;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		
		final Define.REPLY_DETAIL.CDATA data = datas.get(position);
		
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
		ImageLoaderView ivPhoto = (ImageLoaderView) convertView.findViewById(R.id.iv_photo);
		if(data.replycontent == null || "".equals(data.replycontent)){
			tv_content.setVisibility(View.GONE);
		}else{
			tv_content.setVisibility(View.VISIBLE);
			tv_content.setText(data.replycontent);
		}
		
		if(data.answersimage == null || "".equals(data.answersimage)){
			ivPhoto.setVisibility(View.GONE);
		}else{
			ivPhoto.setVisibility(View.VISIBLE);
			ivPhoto.setImageUrl(data.answersimage);
		}
		
		ivPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				PhotoPopupHelper.showPop(data.answersimage, mContext);
			}
		});
		
		tv_time.setText(StringHelper.getFriendlydate(data.contenttime));
		iv_face.setImageUrl(data.attacpath);
		
		if(!isMe){
			iv_face.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(mContext, WebActivity.class);
					String url = mContext.getString(R.string.method_weburl)
							+"/cspportal/expert/view?id="
//							+"/expert/view?id="
							+data.userterminalid
							+"&userid="+CarServerApplication.loginInfo.id;
					LogHelper.i("tag", "url:"+url);
					intent.putExtra("url", url);
					mContext.startActivity(intent);
				}
			});
		}
		
		
		
		if(!isMe){
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			TextView tv_replytype = (TextView) convertView.findViewById(R.id.tv_replytype);
			tv_name.setText(data.username);
			tv_replytype.setText(data.replytype);
		}
		return convertView;
	}

}
