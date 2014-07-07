package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.R.color;
import cn.com.hyrt.carserver.R.drawable;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;

/**
 * 通用的分类Activity
 * 
 * @author Administrator
 * 
 */
public class ClassificationActivity extends BaseActivity {

	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	
	private Button leftBtn, rightBtn;
	private ListView leftls, rightls;
	private Context context;
	private String title;
	private String type;
	String[] name=null;//一级菜单
	private String [] b = null;//二级数组
	private  String [] c = null;//二级数组
	 
	private List<Map<String, String>> oneList = null;
	private List<List<Map<String, String>>> twoList = null;
	private List<List<List<Map<String, String>>>> threeList = null;
	
	private WebServiceHelper mWebServiceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		type = intent.getStringExtra("type");
		
		setTitle(title);
		setContentView(R.layout.layout_question_classification);
		context=ClassificationActivity.this;
		initView();
		leftLoadData(type);
	}

	private void initView() 
	{
		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);
		leftls = (ListView) findViewById(R.id.list_left);
		rightls = (ListView) findViewById(R.id.list_right);

		leftBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				leftBtn.setTextColor(getResources().getColor(color.select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_select_left);
				rightBtn.setTextColor(getResources().getColor(color.no_select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_right);
				leftls.setAdapter(new leftAdapter(b,0,context));
			}
		});

		rightBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				leftBtn.setTextColor(getResources().getColor(color.no_select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_left);
				rightBtn.setTextColor(getResources().getColor(color.select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_select_right);
				leftls.setAdapter(new leftAdapter(c,1,context));
			}
		});
	}

	private void leftLoadData(String type) 
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
						name=new String[2];
						int i=0;
						for(Map<String, String> onemap : oneList)
						{
							name[i++]=onemap.get("name");
						}
						
						leftBtn.setText(name[0]);
						rightBtn.setText(name[1]);
					}
					
					//二级分类
					twoList = classifyJsonParser.getTwoList();
					int n1 =50;
					int n2 =50;
					if( twoList.get(0)!=null && twoList.get(0).size()>0 )
					{
						 n1 = twoList.get(0).size();
					}
					
					if(twoList.get(1)!=null && twoList.get(1).size()>0)
					{
						 n2 = twoList.get(1).size();
					}
					 
					 b = new String[n1];
					 c = new String[n2];
					 int j1=0,j2=0;
					 for(Map<String, String> map: twoList.get(0))
					 {
						 b[j1++] = map.get("name");
					 }
					 
					 for(Map<String, String> map: twoList.get(1))
					 {
						 c[j2++] = map.get("name");
					 }
					 
					 leftls.setAdapter(new leftAdapter(b,0,context));
					 
					//三级分类
					threeList = classifyJsonParser.getThreeList();
				}
			} , this);
		}
		String id ="";
		if(type.equals("1"))
		{
			id ="000011";
		}else if(type.equals("2")){
			id ="000012";
		}
		else if(type.equals("3")){
			id ="000013";
		}
		else if(type.equals("4")){
			id="000014";
		}
		
		mWebServiceHelper.getMaintainFL(id);
	}
	
	private class leftAdapter extends BaseAdapter 
	{
		public String[] str;
		public int flag;
		
		public Context context;
		private leftAdapter(String[] str,int flag,Context context) 
		{
			this.str = str;
			this.flag = flag;
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
			convertView = View.inflate(ClassificationActivity.this,
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
					if(threeList.get(0) != null && !threeList.get(0).equals("[]") && threeList.get(0).size()>0)
					{
						if(!threeList.get(flag).get(position).equals("[]"))
						{
							List<Map<String, String>> list = threeList.get(flag).get(position);
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
			convertView = View.inflate(ClassificationActivity.this,
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
}
