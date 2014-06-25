package cn.com.hyrt.carserver.question.fragment;

import cn.com.hyrt.carserver.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * 车辆问答主页
 * @author zoe
 *
 */
public class QuestionFragment extends Fragment{

	private View rootView;
	private GridView gvQuestion, gvExperts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_question, null);
		findView();
		initGrid();
		return rootView;
	}
	
	private void findView(){
		gvQuestion = (GridView) rootView.findViewById(R.id.gvQuestion);
		gvExperts = (GridView) rootView.findViewById(R.id.gvExperts);
	}
	
	private void initGrid(){
		/*
		 * int[] imgArray = new int[]{R.drawable.ic_question_mend, R.drawable.ic_question_custom,
				R.drawable.ic_question_insurance, R.drawable.ic_question_cosmetology};
		int[] textSourceArray = new int[]{R.string.question_mend, R.string.question_custmon,
				R.string.question_insurance, R.string.question_cosmetology};*/
		int[] questionImgArray = new int[]{R.drawable.ic_new_question, R.drawable.ic_question_history,
				R.drawable.ic_my_appointment, R.drawable.ic_my_experts,
				R.drawable.ic_condition, R.drawable.ic_my_car};
		int[] questionTextSourceArray = new int[]{R.string.info_new_question, R.string.info_question_history,
				R.string.info_my_appointment, R.string.info_my_experts, R.string.info_condition,
				R.string.info_my_car};
	}
}
