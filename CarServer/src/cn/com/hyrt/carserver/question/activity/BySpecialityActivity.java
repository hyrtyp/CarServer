package cn.com.hyrt.carserver.question.activity;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.R.layout;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

/**
 * 按专长找
 * @author Administrator
 *
 */
public class BySpecialityActivity extends BaseActivity {

	private ListView lst;
	private myAdapter ma, ma1;
	private Context context;
	private LinearLayout layout;
	private String title;
	
	private String [] names = null;//左边数组菜单
	private String [] ids = null;//左边数组菜单
	private ListView leftls, rightls;
	
	private WebServiceHelper mWebServiceHelper;
	private List<Map<String, String>> oneList = null;
	private List<List<Map<String, String>>> twoList = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_question_classification);
		
		
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		setTitle(title);
		
		leftls = (ListView) findViewById(R.id.list_left);
		rightls = (ListView) findViewById(R.id.list_right);
		layout = (LinearLayout) findViewById(R.id.top_ll);
		layout.setVisibility(View.GONE);

		context=BySpecialityActivity.this;
		leftLoadData();
	}

//	public void onAction(View v) 
//	{
//		switch (v.getId())
//		{
//			case R.id.btn1:
//				ma = new myAdapter(text1);
//				lst.setAdapter(ma);
//				findViewById(R.id.btn1).setBackgroundColor(
//						this.getResources().getColor(R.color.white));
//				findViewById(R.id.btn2).setBackgroundColor(
//						this.getResources().getColor(R.color.blue));
//				((TextView) findViewById(R.id.text1)).setTextColor(this
//						.getResources().getColor(R.color.black));
//				((TextView) findViewById(R.id.text2)).setTextColor(this
//						.getResources().getColor(R.color.white));
//	
//				break;
//	
//			case R.id.btn2:
//				ma1 = new myAdapter(text2);
//				lst.setAdapter(ma1);
//				findViewById(R.id.btn2).setBackgroundColor(
//						this.getResources().getColor(R.color.white));
//				findViewById(R.id.btn1).setBackgroundColor(
//						this.getResources().getColor(R.color.blue));
//				((TextView) findViewById(R.id.text1)).setTextColor(this
//						.getResources().getColor(R.color.white));
//				((TextView) findViewById(R.id.text2)).setTextColor(this
//						.getResources().getColor(R.color.black));
//				break;
//		}
//	}
	
	private void leftLoadData() 
	{
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		if (mWebServiceHelper == null) 
		{
			mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.OnSuccessListener()
			{
				@Override
				public void onSuccess(String result)
				{
					AlertHelper.getInstance(context).hideLoading();
					ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
					classifyJsonParser.parse(result);
					
					//一级分类
					oneList =classifyJsonParser.getOneList();
					if(oneList!=null && oneList.size()>0)
					{
						names=new String[oneList.size()];
						int i=0;
						for(Map<String, String> onemap : oneList)
						{
							names[i++]=onemap.get("name");
						}
						leftls.setAdapter(new leftAdapter(names,context));
					}
					
					//二级分类
					twoList = classifyJsonParser.getTwoList();	 
				}
			} , this);
		}
		
		mWebServiceHelper.getMaintainFL("000020");
	}
	
	private class leftAdapter extends BaseAdapter 
	{
		public String[] str;
		public Context context;
		private leftAdapter(String[] str,Context context) 
		{
			this.str = str;
			this.context = context;
		}

		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			convertView = View.inflate(BySpecialityActivity.this,
					R.layout.question_maintain_left_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(str[position]);
			
			final LinearLayout llayout = (LinearLayout) convertView.findViewById(R.id.llayout);
			llayout.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v)
				{
					String[] names = null;
					String[] ids = null;
					int j = 0;
					if(twoList != null && !twoList.get(position).equals("[]") && twoList.get(position).size()>0)
					{
						List<Map<String, String>> list = twoList.get(position);
						if(!list.equals("[]") && list.size()>0)
						{
							names = new String[list.size()];
							ids = new String[list.size()];
							for(Map<String, String> map :list)
							{
								names[j] = map.get("name");
								ids[j]  = map.get("id");
								j++;
							}
							rightls.setAdapter(new rightAdapter(names,ids,context));
						}
					}
				}
			});
			return convertView;
		}
	}

	private class rightAdapter extends BaseAdapter
	{
		public String[] str;
		public String[] ids;
		public Context context;
		
		private rightAdapter(String[] names,String[] ids,Context context) {
			this.str = names;
			this.ids = ids;
			this.context = context;
		}

		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(BySpecialityActivity.this,
					R.layout.question_maintain_right_item, null);
			final TextView text = (TextView) convertView.findViewById(R.id.text);
			final TextView id = (TextView) convertView.findViewById(R.id.itemid);
			
			text.setText(str[position]);
			id.setText(ids[position]);
			id.setVisibility(View.GONE);
			LinearLayout llayout = (LinearLayout) convertView.findViewById(R.id.llayout);
			llayout.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v)
				{
					Intent ie=new Intent(context,CorrelationActivity.class);
					ie.putExtra("name", str[position]);
					ie.putExtra("id", ids[position]);
					context.startActivity(ie);
				}
			});
			return convertView;
		}
	}
	
	private class myAdapter extends BaseAdapter 
	{
		public String[] str;

		private myAdapter(String[] str) {
			this.str = str;
		}

		@Override
		public int getCount() {
			return str.length;
		}

		@Override
		public Object getItem(int position) {
			return str[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(BySpecialityActivity.this,
					R.layout.question_insurance_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}
	}
}
