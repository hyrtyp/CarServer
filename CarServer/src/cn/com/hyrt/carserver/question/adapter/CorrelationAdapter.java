package cn.com.hyrt.carserver.question.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.info.activity.QuestionDetailActivity;

public class CorrelationAdapter extends BaseAdapter {

	List<Map<String, Object>> data;
	Context context;
	String name;

	public CorrelationAdapter(Context context, List<Map<String, Object>> data,
			String name) {
		this.data = data;
		this.context = context;
		this.name = name;

	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.layout_question_search_result_item, null);
		}

		ImageLoaderView iv_face_img = (ImageLoaderView) convertView
				.findViewById(R.id.iv_face_img);

		TextView tv_content = (TextView) convertView
				.findViewById(R.id.tv_content);
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tv_work = (TextView) convertView.findViewById(R.id.tv_work);
		LinearLayout llyout = (LinearLayout) convertView
				.findViewById(R.id.llyout);

		String content = data.get(position).get("content") + "";
		Spanned contenttext = StringHelper.KeywordHighlight(content, name);
		tv_content.setText(contenttext);
		tv_name.setText(data.get(position).get("username") + "-"+data.get(position).get("levelname"));
		tv_work.setText(data.get(position).get("sjname") + "");
		iv_face_img.setImageUrl(data.get(position).get("attacpath") + "");

		llyout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent ie = new Intent(context, QuestionDetailActivity.class);
				ie.putExtra("replyId", data.get(position).get("id").toString());
				ie.putExtra("type", QuestionDetailActivity.TYPE_HISTORY);
				context.startActivity(ie);
			}
		});

		return convertView;
	}
}
