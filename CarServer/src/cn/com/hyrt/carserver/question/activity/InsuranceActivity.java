package cn.com.hyrt.carserver.question.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;

public class InsuranceActivity extends BaseActivity {

	private ListView lst;
	private myAdapter ma, ma1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_question_insurance);
		lst = (ListView) findViewById(R.id.lv_Strategy);
		ma = new myAdapter(text1);
		lst.setAdapter(ma);

	}

	private String[] text1 = { "投保1", "投保2", "投保3", "投保4", "投保5", "投保6", "投保7",
			"投保8" };
	private String[] text2 = { "理赔1", "理赔2", "理赔3", "理赔4", "理赔5", "理赔6", "理赔7",
			"理赔8" };

	public void onAction(View v) {
		switch (v.getId()) {
		case R.id.btn1:
			ma = new myAdapter(text1);
			lst.setAdapter(ma);
			findViewById(R.id.btn1).setBackgroundColor(
					this.getResources().getColor(R.color.white));
			findViewById(R.id.btn2).setBackgroundColor(
					this.getResources().getColor(R.color.blue));
			((TextView) findViewById(R.id.text1)).setTextColor(this
					.getResources().getColor(R.color.black));
			((TextView) findViewById(R.id.text2)).setTextColor(this
					.getResources().getColor(R.color.white));

			break;

		case R.id.btn2:
			ma1 = new myAdapter(text2);
			lst.setAdapter(ma1);
			findViewById(R.id.btn2).setBackgroundColor(
					this.getResources().getColor(R.color.white));
			findViewById(R.id.btn1).setBackgroundColor(
					this.getResources().getColor(R.color.blue));
			((TextView) findViewById(R.id.text1)).setTextColor(this
					.getResources().getColor(R.color.white));
			((TextView) findViewById(R.id.text2)).setTextColor(this
					.getResources().getColor(R.color.black));
			break;

		}
	}

	private class myAdapter extends BaseAdapter {

		public String[] str;

		private myAdapter(String[] str) {
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
			convertView = View.inflate(InsuranceActivity.this,
					R.layout.question_insurance_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}

	}

	class ViewHolder {

		public TextView text;
	}

}
