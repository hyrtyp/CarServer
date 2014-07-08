package cn.com.hyrt.carserver.knowledge.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.question.activity.CorrelationActivity;

public class RelatedQuestionActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_left) ListView lvLeft;
	@ViewInject(id=R.id.lv_right) ListView lvRight;
	private List<String> leftText = new ArrayList<String>();
	private List<String> rightText = new ArrayList<String>();
	private List<String> rightId = new ArrayList<String>();
	
	private List<Map<String, String>> oneList;
	private List<List<Map<String, String>>> twoList;
	
	private LeftAdapter mLeftAdapter;
	
	//维修自查
	public static final String TYPE_REPAIR = "repair";
	//汽车装饰
	public static final String TYPE_DECORATION = "decoration";
	
	private String type = "";
	private WebServiceHelper mWebServiceHelper;
	
	private int curIndex = 0;
	private ArrayAdapter<String> mRightAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		type = intent.getStringExtra("type");
		setContentView(R.layout.activity_related_question);
		if(TYPE_DECORATION.equals(type)){
			setTitle(getString(R.string.knowledge_decoration));
		}else{
			setTitle(getString(R.string.iknowledge_examination));
		}
		setlistener();
		AlertHelper.getInstance(this).showLoading(null);
		loadData();
	}
	
	private void loadData(){
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
				
				@Override
				public void onSuccess(String result) {
					AlertHelper.getInstance(RelatedQuestionActivity.this).hideLoading();
					ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
					classifyJsonParser.parse(result);
					oneList = classifyJsonParser.getOneList();
					 LogHelper.i("tag", "oneList:"+oneList);
					
					twoList = classifyJsonParser.getTwoList();
					 LogHelper.i("tag", "twoList:"+twoList);
					 
					 leftText.clear();
					 
					 for(int i=0,j=oneList.size(); i<j; i++){
						 leftText.add(oneList.get(i).get("name"));
					 }
					
					 if(mLeftAdapter == null){
						 mLeftAdapter = new LeftAdapter(
								 RelatedQuestionActivity.this,
								 R.layout.layout_related_question_left_item,
								 R.id.tv_name, leftText);
						 lvLeft.setAdapter(mLeftAdapter);
					 }else{
						 mLeftAdapter.notifyDataSetChanged();
					 }
					 
					 setRight(curIndex);
					 
				}
			}, this);
		}
		
		if(TYPE_DECORATION.equals(type)){
			mWebServiceHelper.getCarDecoration();
		}else{
			mWebServiceHelper.getMaintainCheck();
		}
		
	}
	
	private void setRight(int index){
		List<Map<String, String>> rightList = twoList.get(index);
		rightText.clear();
		rightId.clear();
		for(int i=0,j=rightList.size(); i<j; i++){
			rightText.add(rightList.get(i).get("name"));
			rightId.add(rightList.get(i).get("id"));
		}
		
		if(mRightAdapter == null){
			mRightAdapter = new ArrayAdapter<String>(
					this,
					R.layout.layout_related_question_right_item,
					R.id.tv_name, rightText);
			lvRight.setAdapter(mRightAdapter);
		}else{
			mRightAdapter.notifyDataSetChanged();
		}
	}
	
	private class LeftAdapter extends ArrayAdapter<String>{

		public LeftAdapter(Context context, int resource,
				int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			if(position == curIndex){
				view.setBackgroundResource(android.R.color.white);
				((TextView)view.findViewById(R.id.tv_name))
				.setTextColor(getResources().getColor(R.color.no_select_btn_color));
			}
			return view;
		}
	}
	
	private void setlistener(){
		lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				View curView = parent.getChildAt(curIndex);
				curView.setBackgroundResource(R.color.blue);
				((TextView)curView.findViewById(R.id.tv_name))
				.setTextColor(getResources().getColor(android.R.color.white));
				
				view.setBackgroundResource(android.R.color.white);
				((TextView)view.findViewById(R.id.tv_name))
				.setTextColor(getResources().getColor(R.color.no_select_btn_color));
				curIndex = position;
				setRight(curIndex);
			}
		});
		
		lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long arg3) {
				Intent intent = new Intent();
				intent.setClass(RelatedQuestionActivity.this, CorrelationActivity.class);
				intent.putExtra("id", rightId.get(position));
				startActivity(intent);
			}
		});
	}
}
