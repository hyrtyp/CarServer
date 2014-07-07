package cn.com.hyrt.carserver.question.adapter;

import java.util.List;
import java.util.Map;
import cn.com.hyrt.carserver.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CorrelationAdapter extends BaseAdapter{
	
	List<Map<String, Object>> data;
	Context context;
	public CorrelationAdapter(Context context,List<Map<String, Object>> data)
	{
		this.data=data;
		this.context=context;
	}
	
	@Override
	public int getCount()
	{
		return data.size();
	}

	@Override
	public Object getItem(int position)
	{
		return data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return  position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.layout_question_search_result_item, null);
		}
//		layout_question_search_result_item ，question_correlation_item
		
//		TextView tv_linchpin=(TextView)convertView.findViewById(R.id.tv_linchpin);
//		TextView tv_other=(TextView)convertView.findViewById(R.id.tv_other);
//		ImageView img_head=(ImageView)convertView.findViewById(R.id.img_head);
		
		TextView tv_content=(TextView)convertView.findViewById(R.id.tv_content);
		TextView tv_name=(TextView)convertView.findViewById(R.id.tv_name);
		TextView tv_work=(TextView)convertView.findViewById(R.id.tv_work);
		
//		TextView tv_point=(TextView)convertView.findViewById(R.id.tv_point);
//		TextView tv_name1=(TextView)convertView.findViewById(R.id.tv_name1);
//		TextView tv_point1=(TextView)convertView.findViewById(R.id.tv_point1);
		
		tv_content.setText(data.get(position).get("content")+"");
		tv_name.setText(data.get(position).get("username")+"");
		tv_work.setText(data.get(position).get("zyname")+"");
//		tv_name.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent ie;
//				if(edit)
//				{
//					ie=new Intent(context,OfftenBridgeEditActivity.class);
//				}
//				else
//				{
//					ie=new Intent(context,BridgeDetailActivity.class);
//				}
//				ie.putExtra("objectid", data.get(position).getCol1());
//				ie.putExtra("objectname", data.get(position).getCol2());
//				ie.putExtra("edit", edit);
//				ie.putExtra("file", file);
//				ie.putExtra("segName", segName);
//				context.startActivity(ie);
//			}
//		});
		
		return convertView;
	}

}
