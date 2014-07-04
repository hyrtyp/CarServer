package cn.com.hyrt.carserver.knowledge.adapter;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_SEARCH_RESULT;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionResultAdapter extends BaseAdapter{

	private Define.QUESTION_SEARCH_RESULT result;
	private Context mContext;
	
	
	
	public QuestionResultAdapter(QUESTION_SEARCH_RESULT result, Context mContext) {
		super();
		this.result = result;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return result.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return result.data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		
		ViewHolder mHolder = null;
		if(convertview == null){
			convertview = LayoutInflater.from(mContext)
					.inflate(R.layout.layout_question_result_item, null);
			mHolder = new ViewHolder();
			mHolder.iv_face_img = (ImageLoaderView) convertview.findViewById(R.id.iv_face_img);
			mHolder.tv_title = (TextView) convertview.findViewById(R.id.tv_title);
			mHolder.tv_name = (TextView) convertview.findViewById(R.id.tv_name);
			mHolder.tv_work = (TextView) convertview.findViewById(R.id.tv_work);
			mHolder.tv_content = (TextView) convertview.findViewById(R.id.tv_content);
			convertview.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertview.getTag();
		}
		
		Define.QUESTION_SEARCH_RESULT.CDATA data = result.data.get(position);
		
		mHolder.iv_face_img.setImageUrl(data.attacpath);
		return convertview;
	}
	
	private class ViewHolder{
		ImageLoaderView iv_face_img;
		TextView tv_title;
		TextView tv_name;
		TextView tv_work;
		TextView tv_content;
	}

}
