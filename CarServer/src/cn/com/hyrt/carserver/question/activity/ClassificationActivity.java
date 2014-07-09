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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
	
	private Button leftBtn, rightBtn;
	private ListView leftls, rightls;
	private Context context;
	private String title;
	private String type;
	String[] name=null;//一级菜单
	private String [] b = null;//二级数组
	private  String [] c = null;//二级数组
	 
	private int curIndex = 0;
	private int falg = 0;
	private LeftAdapter leftAdapter;
	private RightAdapter rightAdapter;
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
		setlistener();
		leftLoadData(type);
	}

	private void initView() 
	{
		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);
		leftls = (ListView) findViewById(R.id.lv_left);
		rightls = (ListView) findViewById(R.id.lv_right);

		leftBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				falg = 0;
				curIndex =0;
				leftBtn.setTextColor(getResources().getColor(color.select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_select_left);
				rightBtn.setTextColor(getResources().getColor(color.no_select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_right);
				leftls.setAdapter(new LeftAdapter(b));
				setRight(curIndex,falg);
			}
		});

		rightBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				falg = 1;
				curIndex =0;
				leftBtn.setTextColor(getResources().getColor(color.no_select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_left);
				rightBtn.setTextColor(getResources().getColor(color.select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_select_right);
				leftls.setAdapter(new LeftAdapter(c));
				setRight(curIndex,falg);
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
					 
					 if(leftAdapter == null){
						 leftls.setAdapter(new LeftAdapter(b));
					 }else{
						 leftAdapter.notifyDataSetChanged();
					 }
					 
					//三级分类
					threeList = classifyJsonParser.getThreeList();
						
					setRight(curIndex,falg);
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

	private void setRight(int index,int flag)
	{

		String[] names = null;
		String[] ids = null;
		int j = 0;
		if(threeList.get(flag) != null && !threeList.get(flag).equals("[]") && threeList.get(flag).size()>0)
		{
			if(!threeList.get(flag).get(index).equals("[]"))
			{
				List<Map<String, String>> list = threeList.get(flag).get(index);
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
				}
				else
				{
					names = new String[1];
					ids = new String[1];
					names[0]=R.string.question_noimg+"";
					ids[0]="1";
				}
			}
		}
		if(rightAdapter == null){
			rightls.setAdapter(new RightAdapter(names,ids));
		}else{
			rightAdapter.notifyDataSetChanged();
		}
	}
	
	
	private class LeftAdapter extends BaseAdapter 
	{
		public String[] str;
		
		private LeftAdapter(String[] str) 
		{
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
		public View getView(final int position, View convertView, ViewGroup parent) 
		{
			convertView = View.inflate(ClassificationActivity.this,R.layout.question_maintain_left_item, null);
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			tv_name.setText(str[position]);
			
			if(position == curIndex){
				convertView.setBackgroundResource(android.R.color.white);
				((TextView)convertView.findViewById(R.id.tv_name))
				.setTextColor(getResources().getColor(R.color.no_select_btn_color));
			}
			
			return convertView;
		}
	}

	private class RightAdapter extends BaseAdapter
	{
		public String[] str;
		public String[] ids;
		
		private RightAdapter(String[] names,String[] ids) {
			this.str = names;
			this.ids = ids;
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
			final TextView text = (TextView) convertView.findViewById(R.id.tv_name);
			final TextView id = (TextView) convertView.findViewById(R.id.tv_id);
			
			text.setText(str[position]);
			id.setText(ids[position]);
			id.setVisibility(View.GONE);
			return convertView;
		}
	}
	
	private void setlistener()
	{
		leftls.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
				View curView = parent.getChildAt(curIndex);
				curView.setBackgroundResource(R.color.blue);
				((TextView)curView.findViewById(R.id.tv_name))
				.setTextColor(getResources().getColor(android.R.color.white));
				
				view.setBackgroundResource(android.R.color.white);
				((TextView)view.findViewById(R.id.tv_name))
				.setTextColor(getResources().getColor(R.color.no_select_btn_color));
				curIndex = position;
				setRight(curIndex,falg);
			}
		});
		
		rightls.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long arg3)
			{
				TextView tv_id =((TextView)view.findViewById(R.id.tv_id));
				TextView tv_name =((TextView)view.findViewById(R.id.tv_name));
				curIndex = position;
				Intent ie=new Intent(context,CorrelationActivity.class);
				ie.putExtra("name",tv_name.getText());
				ie.putExtra("id", tv_id.getText());
				context.startActivity(ie);
			}
		});
	}
}
