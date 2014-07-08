package cn.com.hyrt.carserver.question.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;

public class PositionAdapter extends BaseAdapter{
	
	List<Define.QUESTION_POISTION.CDATA> data;
	Context context;
	public PositionAdapter(Context context,List<Define.QUESTION_POISTION.CDATA> data)
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
			convertView=LayoutInflater.from(context).inflate(R.layout.layout_question_position_item, null);
		}
		
		TextView tv_name =(TextView)convertView.findViewById(R.id.tv_name);
		
		tv_name.setText(data.get(position).name);
		
		
		
		return convertView;
	}

}
