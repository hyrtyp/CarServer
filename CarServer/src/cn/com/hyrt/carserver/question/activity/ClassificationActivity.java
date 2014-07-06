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
	private ClassifyJsonParser jsonParser;

	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
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

		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftBtn.setTextColor(getResources().getColor(
						color.select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_select_left);
				rightBtn.setTextColor(getResources().getColor(
						color.no_select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_right);

				// leftls.setAdapter(new leftAdapter(text1));
				rightls.setAdapter(new rightAdapter(text3));
			}
		});

		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftBtn.setTextColor(getResources().getColor(
						color.no_select_btn_color));
				leftBtn.setBackgroundResource(drawable.title_left);

				rightBtn.setTextColor(getResources().getColor(
						color.select_btn_color));
				rightBtn.setBackgroundResource(drawable.title_select_right);

				leftls.setAdapter(new leftAdapter(text2));
				rightls.setAdapter(new rightAdapter(text4));
			}
		});
	}

	private void leftLoadData() {
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		if (mWebServiceHelper == null) {
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.QUESTION_CLASSIFICATION>() {

						@Override
						public void onSuccess(QUESTION_CLASSIFICATION result) {
							ptrv.onHeaderRefreshComplete();
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(ClassificationActivity.this).hideLoading();
							data.clear();
							System.out.println("=========" + result.data.size());
							for (int i = 0, j = result.data.size(); i < j; i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("id", result.data.get(i).id);
								map.put("name", result.data.get(i).name);
								System.out.println("========="
										+ result.data.get(i).id);
								System.out.println("result.data.get(i).name========="+ result.data.get(i).name);
								System.out.println();
								data.add(map);
							}
							String[] from = new String[] { "name" };
							int[] to = new int[] { R.id.text };
							SimpleAdapter mAdapter = new SimpleAdapter(
									ClassificationActivity.this, data,
									R.layout.question_maintain_left_item, from,
									to);
							leftls.setAdapter(mAdapter);

							// jsonParser.parse(result);
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {

						}

					}, this);
		}

		mWebServiceHelper.getMaintainFL();

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
			convertView = View.inflate(ClassificationActivity.this,
					R.layout.question_maintain_left_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
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
			convertView = View.inflate(ClassificationActivity.this,
					R.layout.question_maintain_right_item, null);
			TextView text = (TextView) convertView.findViewById(R.id.text);
			text.setText(str[position]);
			return convertView;
		}

	}
}
