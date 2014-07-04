package cn.com.hyrt.carserver.question.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.R.color;
import cn.com.hyrt.carserver.R.drawable;
import cn.com.hyrt.carserver.base.activity.BaseActivity;

/**
 * 维修保养
 * 
 * @author Administrator
 * 
 */
public class MaintainActivity extends BaseActivity {

	private Button leftBtn, rightBtn;
	private ListView leftls,rightls;
//	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question_maintain);

		initView();
	}

	private String[] text1 = { "维修1", "维修1", "维修1", "维修1", "维修1"};
	private String[] text2 = { "保养1", "保养1", "保养1", "保养1", "保养1"};
	private String[] text3 = { "维修2", "维修2", "维修2", "维修2", "维修2"};
	private String[] text4 = { "保养2", "保养2", "保养2", "保养2", "保养2"};

	private void initView() {

		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);
		
		
		
		leftls = (ListView) findViewById(R.id.list_left);
		leftls.setAdapter(new leftAdapter(text1));
		rightls = (ListView) findViewById(R.id.list_right);
		rightls.setAdapter(new rightAdapter(text3));
		

		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftBtn.setTextColor(getResources().getColor(
						color.select_btn_color));
				leftBtn.setBackgroundDrawable(getResources().getDrawable(
						drawable.title_select_left));
				rightBtn.setTextColor(getResources().getColor(
						color.no_select_btn_color));
				rightBtn.setBackgroundDrawable(getResources().getDrawable(
						drawable.title_right));
				
				leftls.setAdapter(new leftAdapter(text1));
				rightls.setAdapter(new rightAdapter(text3));
			}
		});

		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftBtn.setTextColor(getResources().getColor(
						color.no_select_btn_color));
				leftBtn.setBackgroundDrawable(getResources().getDrawable(
						drawable.title_left));
				rightBtn.setTextColor(getResources().getColor(
						color.select_btn_color));
				rightBtn.setBackgroundDrawable(getResources().getDrawable(
						drawable.title_select_right));
				
				leftls.setAdapter(new leftAdapter(text2));
				rightls.setAdapter(new rightAdapter(text4));
			}
		});
	}
	
	private class leftAdapter extends BaseAdapter {

		public String[] str;

		private leftAdapter(String[] str) {
			this.str = str;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = View.inflate(MaintainActivity.this,
					R.layout.question_maintain_left_item, null);
			TextView text = (TextView)convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}

	}
	
	private class rightAdapter extends BaseAdapter {

		public String[] str;

		private rightAdapter(String[] str) {
			this.str = str;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = View.inflate(MaintainActivity.this,
					R.layout.question_maintain_right_item, null);
			TextView text = (TextView)convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}

	}
}
