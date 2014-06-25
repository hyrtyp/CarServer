package cn.com.hyrt.carserver.knowledge.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	}
}
