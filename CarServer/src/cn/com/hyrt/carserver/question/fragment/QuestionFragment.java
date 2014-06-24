package cn.com.hyrt.carserver.question.fragment;

import cn.com.hyrt.carserver.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class QuestionFragment extends Fragment{

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_question, null);
		return rootView;
	}
}
