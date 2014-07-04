package cn.com.hyrt.carserver.knowledge.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.info.activity.QuestionActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class KnowledgeFragment extends Fragment{

	private View rootView;
	private GridView gvRelatedQuestion, gvRelatedKnowledge;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_knowledge, null);
		findView();
		initGrid();
		return rootView;
	}
	
	private void findView(){
		gvRelatedQuestion = (GridView) rootView.findViewById(R.id.gvRelatedQuestion);
		gvRelatedKnowledge = (GridView) rootView.findViewById(R.id.gvRelatedKnowledge);
	}
	
	private void initGrid(){
		int[] relatedQuestionImgArray = new int[]{R.drawable.ic_knowledge_examination, R.drawable.ic_knowledge_decoration};
		int[] relatedQuestionTextSourceArray = new int[]{R.string.question_mend, R.string.question_custmon};
		PortalGridAdapter mRelatedQuestionAdapter = 
				new PortalGridAdapter(
						relatedQuestionImgArray,
						relatedQuestionTextSourceArray,
						getActivity());
		gvRelatedQuestion.setAdapter(mRelatedQuestionAdapter);
		gvRelatedQuestion.setOnItemClickListener(gvRelatedQuestionListener);
		int[] relatedKnowledgeImgArray = 
				new int[]{
				R.drawable.ic_knowledge_detail, R.drawable.ic_knowledge_insurance,
				R.drawable.ic_knowledge_experience, R.drawable.bg_blank};
		int[] relatedKnowledgeTextSourceArray = 
				new int[]{
				R.string.knowledge_detail, R.string.knowledge_insurance,
				R.string.knowledge_experience, R.string.blank_text};
		PortalGridAdapter mRelatedKnowledgeAdapter = 
				new PortalGridAdapter(
						relatedKnowledgeImgArray,
						relatedKnowledgeTextSourceArray,
						getActivity());
		gvRelatedKnowledge.setAdapter(mRelatedKnowledgeAdapter);
		gvRelatedKnowledge.setOnItemClickListener(gvRelatedKnowledgeListener);
	}
	
	//相关问答
	private AdapterView.OnItemClickListener gvRelatedQuestionListener = new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				LogHelper.i("tag", ""+position); 
				Intent gvRelatedQuestionIntent = new Intent();
				switch(position){
				//维修自查
				case 0:
					AlertHelper.getInstance(getActivity()).showCenterToast("等待开发中");
					
				    break;
				//配件改装
				case 1:
					AlertHelper.getInstance(getActivity()).showCenterToast("等待开发中");
				    break;
				default:
					return;  
				}
				//startActivity(gvRelatedQuestionIntent);
			}
			
		};
	
	//相关知识
	private AdapterView.OnItemClickListener gvRelatedKnowledgeListener = new AdapterView.OnItemClickListener() {
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			LogHelper.i("tag", ""+position); 
			Intent gvRelatedKnowledgeIntent = new Intent();
			switch(position){
			//维保详情
			case 0:
				gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
				gvRelatedKnowledgeIntent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;
			//保险知识
			case 1:
				gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
				gvRelatedKnowledgeIntent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
			    break;
			//经验心得
			case 2:
				gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
				gvRelatedKnowledgeIntent.putExtra("url", getString(R.string.method_weburl)+"/cspportal/");
				break;
			default:
				return;  
			}
			startActivity(gvRelatedKnowledgeIntent);
		}
		
	};
	
	
}
