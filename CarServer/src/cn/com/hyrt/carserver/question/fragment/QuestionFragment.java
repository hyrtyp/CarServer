package cn.com.hyrt.carserver.question.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.question.adapter.QuestionBannerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * 车辆问答主页
 * @author zoe
 *
 */
public class QuestionFragment extends Fragment{

	private View rootView;
	private GridView gvQuestion, gvExperts;
	private ViewPager bannerPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_question, null);
		findView();
		initGrid();
		initBanner();
		return rootView;
	}
	
	private void findView(){
		gvQuestion = (GridView) rootView.findViewById(R.id.gvQuestion);
		gvExperts = (GridView) rootView.findViewById(R.id.gvExperts);
		bannerPager = (ViewPager) rootView.findViewById(R.id.bannerPager);
	}
	
	private void initBanner(){
		List<View> views = new ArrayList<View>();
		ImageView imageview1 = new ImageView(getActivity());
		imageview1.setImageResource(R.drawable.img_question_banner);
		ImageView imageview2 = new ImageView(getActivity());
		imageview2.setImageResource(R.drawable.img_question_banner);
		views.add(imageview1);
		views.add(imageview2);
		bannerPager.setAdapter(new QuestionBannerAdapter(views));
		
	}
	
	private void initGrid(){
		int[] questionImgArray = new int[]{R.drawable.ic_question_mend, R.drawable.ic_question_custom,
				R.drawable.ic_question_insurance, R.drawable.ic_question_cosmetology};
		int[] questionTextSourceArray = new int[]{R.string.question_mend, R.string.question_custmon,
				R.string.question_insurance, R.string.question_cosmetology};
		PortalGridAdapter mQuestionAdapter = new PortalGridAdapter(questionImgArray, questionTextSourceArray, getActivity());
		gvQuestion.setAdapter(mQuestionAdapter);
		
		int[] expertsImgArray = new int[]{R.drawable.ic_question_specialty, R.drawable.ic_question_mend};
		int[] expertsTextSourceArray = new int[]{R.string.question_find_specialty, R.string.question_mend};
		PortalGridAdapter mExpertsAdapter = new PortalGridAdapter(expertsImgArray, expertsTextSourceArray, getActivity());
		gvExperts.setAdapter(mExpertsAdapter);
	}
}
