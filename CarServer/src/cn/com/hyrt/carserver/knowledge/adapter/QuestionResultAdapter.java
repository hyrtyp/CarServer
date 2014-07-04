package cn.com.hyrt.carserver.knowledge.adapter;

import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_SEARCH_RESULT;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class QuestionResultAdapter extends BaseAdapter{

	private List<Define.QUESTION_SEARCH_RESULT.CDATA> result;
	private Context mContext;
	private String keyword;
	
	
	
	public QuestionResultAdapter(List<Define.QUESTION_SEARCH_RESULT.CDATA> result, String keyword, Context mContext) {
		super();
		this.result = result;
		this.keyword = keyword;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		return result.size();
	}

	@Override
	public Object getItem(int arg0) {
		return result.get(arg0);
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
					.inflate(R.layout.layout_question_search_result_item, null);
			mHolder = new ViewHolder();
			mHolder.iv_face_img = (ImageLoaderView) convertview.findViewById(R.id.iv_face_img);
			mHolder.tv_name = (TextView) convertview.findViewById(R.id.tv_name);
			mHolder.tv_work = (TextView) convertview.findViewById(R.id.tv_work);
			mHolder.tv_content = (TextView) convertview.findViewById(R.id.tv_content);
			convertview.setTag(mHolder);
		}else{
			mHolder = (ViewHolder) convertview.getTag();
		}
		
		Define.QUESTION_SEARCH_RESULT.CDATA data = result.get(position);
		
		mHolder.iv_face_img.setImageUrl(data.attacpath);
		mHolder.tv_content.setText(StringHelper.KeywordHighlight(data.content, keyword));
		mHolder.tv_name.setText(data.username+" - "+data.zyname);
		return convertview;
	}
	
	
	
	private class ViewHolder{
		ImageLoaderView iv_face_img;
		TextView tv_name;
		TextView tv_work;
		TextView tv_content;
	}

}
