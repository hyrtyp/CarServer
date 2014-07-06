package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.R.color;
import cn.com.hyrt.carserver.R.drawable;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_CLASSIFICATION;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
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

	private String title;

	private WebServiceHelper mWebServiceHelper;

	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		title = intent.getStringExtra("title");
		setTitle(title);
		setContentView(R.layout.layout_question_classification);

		initView();
		 leftLoadData();
	}

	// private String[] text1 = { "维修1", "维修1", "维修1", "维修1", "维修1"};
	private String[] text2 = { "保养1", "保养1", "保养1", "保养1", "保养1" };
	private String[] text3 = { "维修2", "维修2", "维修2", "维修2", "维修2" };
	private String[] text4 = { "保养2", "保养2", "保养2", "保养2", "保养2" };

	private void initView() {

		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);

		leftls = (ListView) findViewById(R.id.list_left);
		// leftls.setAdapter(new leftAdapter(text1));
		rightls = (ListView) findViewById(R.id.list_right);
		rightls.setAdapter(new rightAdapter(text3));

		leftBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				leftBtn.setTextColor(getResources().getColor(color.select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_select_left);
				rightBtn.setTextColor(getResources().getColor(color.no_select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_right);
				// leftls.setAdapter(new leftAdapter(text1));
				rightls.setAdapter(new rightAdapter(text3));
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
				leftls.setAdapter(new leftAdapter(text2));
				rightls.setAdapter(new rightAdapter(text4));
			}
		});
	}

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
					ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
					classifyJsonParser.parse(result);
					//一级分类
					 List<Map<String, String>> oneList =classifyJsonParser.getOneList();
					 LogHelper.i("tag", "oneList:"+oneList);
					
					//二级分类
					 List<List<Map<String, String>>> twoList = classifyJsonParser.getTwoList();
					 LogHelper.i("tag", "twoList:"+twoList);
					//三级分类
					 List<List<List<Map<String, String>>>> threeList = classifyJsonParser.getThreeList();
					 LogHelper.i("tag", "threeList:"+threeList);
				}
			} , this);
		}

		mWebServiceHelper.getMaintainFL();
//		mWebServiceHelper.setOnSuccessListener(new WebServiceHelper.OnSuccessListener()
//		{
//			@Override
//			public void onSuccess(String result)
//			{
//				ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
//				//一级分类
//				 List<Map<String, String>> oneList =classifyJsonParser.getOneList();
//				 System.out.println(oneList);
//				
//				//二级分类
//				 List<List<Map<String, String>>> twoList = classifyJsonParser.getTwoList();
//				 System.out.println(twoList);
//				//三级分类
//				 List<List<List<Map<String, String>>>> threeList = classifyJsonParser.getThreeList();
//				 System.out.println(threeList);
//			}
//		});
	}

	
	private class leftAdapter extends BaseAdapter {

		public String[] str;

		private leftAdapter(String[] str) {
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
			convertView = View.inflate(ClassificationActivity.this,
					R.layout.question_maintain_left_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}

	}

	private class rightAdapter extends BaseAdapter
	{
		public String[] str;

		private rightAdapter(String[] str) {
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
			convertView = View.inflate(ClassificationActivity.this,
					R.layout.question_maintain_right_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}
	}
}
