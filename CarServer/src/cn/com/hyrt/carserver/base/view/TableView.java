package cn.com.hyrt.carserver.base.view;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserver.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class TableView extends TableLayout{
	
	List<String> titles = new ArrayList<String>();
	List<String> contents = new ArrayList<String>();
	private LayoutInflater mInflater;

	public TableView(Context context) {
		super(context);
	}

	public TableView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setData(List<String> titles, List<String> contents){
		this.titles = titles;
		this.contents = contents;
		this.removeAllViews();
		if(mInflater == null){
			mInflater = LayoutInflater.from(getContext());
		}
		
		for(int i=0,j=titles.size(); i<j; i++){
			if(i == 0){
				View lineView1 = new View(getContext());
				TableLayout.LayoutParams lineViewParams1 = new TableLayout.LayoutParams(
						TableLayout.LayoutParams.MATCH_PARENT,
						getResources().getDimensionPixelOffset(R.dimen.line_size));
				lineView1.setBackgroundResource(R.color.line_color);
				this.addView(lineView1, lineViewParams1);
			}
			
			View rowView = mInflater.inflate(R.layout.layout_table_row_item, null);
			TextView tvTtitle = (TextView) rowView.findViewById(R.id.tv_title);
			TextView tvContent = (TextView) rowView.findViewById(R.id.tv_content);
			tvTtitle.setText(titles.get(i));
			tvContent.setText(contents.get(i));
			
			View lineView2 = new View(getContext());
			TableLayout.LayoutParams lineViewParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.MATCH_PARENT,
					getResources().getDimensionPixelOffset(R.dimen.line_size));
			lineView2.setBackgroundResource(R.color.line_color);
			
			this.addView(rowView);
			this.addView(lineView2, lineViewParams);
			
			
		}
	}

}
