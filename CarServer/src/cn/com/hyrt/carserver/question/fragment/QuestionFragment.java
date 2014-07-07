package cn.com.hyrt.carserver.question.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_GETNEWSIMG;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.GetWebImageHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.question.activity.BySpecialityActivity;
import cn.com.hyrt.carserver.question.activity.ClassificationActivity;
import cn.com.hyrt.carserver.question.activity.QuestionActivity;
import cn.com.hyrt.carserver.question.adapter.QuestionBannerAdapter;

/**
 * 车辆问答主页
 * 
 * @author zoe
 * 
 */
public class QuestionFragment extends Fragment {

	private View rootView;
	private GridView gvQuestion, gvExperts;
	private ViewPager bannerPager;
	private Button questionBtn;

	private WebServiceHelper mWebServiceHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_question, null);
		findView();
		initGrid();
//		loadData();
		initBanner();
		setListener();
		return rootView;
	}

	private void findView() {
		gvQuestion = (GridView) rootView.findViewById(R.id.gvQuestion);
		gvExperts = (GridView) rootView.findViewById(R.id.gvExperts);
		bannerPager = (ViewPager) rootView.findViewById(R.id.bannerPager);

		questionBtn = (Button) rootView.findViewById(R.id.btn_question);
	}

	private void loadData() {

		final List<View> views = new ArrayList<View>();
		final LayoutInflater mInflater = LayoutInflater.from(getActivity());

		AlertHelper.getInstance(getActivity()).showLoading(
				getString(R.string.loading_msg));

		if (mWebServiceHelper == null) {
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.QUESTION_GETNEWSIMG>() {

						@Override
						public void onSuccess(QUESTION_GETNEWSIMG result) {
							LogHelper.i("tag", "result:" + result.data.size());
							AlertHelper.getInstance(getActivity()).hideLoading();
							AlertHelper.getInstance(getActivity())
									.hideLoading();

							if (result == null || result.data.size() <= 0) {

							} else {
								
								String[] image = new String[result.data.size()];
								for (int i = 0; i < result.data.size(); i++) {

									image[i] = result.data.get(i).attacpath;

									byte[] data;
									try {
										data = new GetWebImageHelper().getImage(image[i].toString());
										Bitmap bitmap = BitmapFactory
												.decodeByteArray(data, 0,
														data.length); // 生成位图

										View view = mInflater.inflate(
												R.layout.layout_banner, null);
										((ImageView) view.findViewById(R.id.iv_banner)).setImageBitmap(bitmap);

										views.add(view);
										bannerPager.setAdapter(new QuestionBannerAdapter(
														views));
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							
						}
					}, getActivity());
		}
		mWebServiceHelper.getNewsImg();
	}

	private void initBanner() {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View view1 = mInflater.inflate(R.layout.layout_banner, null);
		((ImageView) view1.findViewById(R.id.iv_banner))
				.setImageResource(R.drawable.img_question_banner);
		View view2 = mInflater.inflate(R.layout.layout_banner, null);
		((ImageView) view2.findViewById(R.id.iv_banner))
				.setImageResource(R.drawable.classify_banner);
		View view3 = mInflater.inflate(R.layout.layout_banner, null);
		((ImageView) view3.findViewById(R.id.iv_banner))
				.setImageResource(R.drawable.img_car_default);

		List<View> views = new ArrayList<View>();
		// ImageView imageview1 = new ImageView(getActivity());
		// imageview1.setImageResource(R.drawable.img_question_banner);
		// ImageView imageview2 = new ImageView(getActivity());
		// imageview2.setImageResource(R.drawable.classify_banner);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		bannerPager.setAdapter(new QuestionBannerAdapter(views));

	}

	private void initGrid() {
		int[] questionImgArray = new int[] { R.drawable.ic_question_mend,
				R.drawable.ic_question_custom,
				R.drawable.ic_question_insurance,
				R.drawable.ic_question_cosmetology };
		int[] questionTextSourceArray = new int[] { R.string.question_mend,
				R.string.question_custmon, R.string.question_insurance,
				R.string.question_cosmetology };
		PortalGridAdapter mQuestionAdapter = new PortalGridAdapter(
				questionImgArray, questionTextSourceArray, getActivity());
		gvQuestion.setAdapter(mQuestionAdapter);
		gvQuestion.setOnItemClickListener(questionItemClickListener);

		int[] expertsImgArray = new int[] { R.drawable.ic_question_specialty,
				R.drawable.ic_question_brand };
		int[] expertsTextSourceArray = new int[] {
				R.string.question_find_specialty, R.string.question_find_brand };
		PortalGridAdapter mExpertsAdapter = new PortalGridAdapter(
				expertsImgArray, expertsTextSourceArray, getActivity());
		gvExperts.setAdapter(mExpertsAdapter);
		gvExperts.setOnItemClickListener(ExpertItemClickListener);
	}

	private void setListener() {
		questionBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), QuestionActivity.class);
				startActivity(intent);
			}
		});
	}

	private AdapterView.OnItemClickListener questionItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), ClassificationActivity.class);
			switch (position) {
			case 0:
				// 维修自查
				intent.putExtra("title", "维修自查");
				intent.putExtra("type", "1");
				break;
			case 1:
				// 配件改装
				intent.putExtra("title", "配件改装");
				intent.putExtra("type", "2");
				break;
			case 2:
				// 保险直通
				intent.putExtra("title", "保险直通");
				intent.putExtra("type", "3");
				break;
			case 3:
				// 美容装潢
				intent.putExtra("title", "美容装潢");
				intent.putExtra("type", "4");
				break;

			default:
				return;
			}

			startActivity(intent);
		}
	};

	private AdapterView.OnItemClickListener ExpertItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			LogHelper.i("tag", "position:" + position);
			Intent intent = new Intent();
			switch (position) {
			case 0:
				// 按专长找
				intent.setClass(getActivity(), BySpecialityActivity.class);
				intent.putExtra("title", "按专长找");
				break;
			case 1:
				// 按品牌找
				intent.setClass(getActivity(), null);
				break;

			default:
				return;
			}
			startActivity(intent);
		}
	};
}
