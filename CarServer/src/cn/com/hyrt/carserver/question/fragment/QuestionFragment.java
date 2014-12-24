package cn.com.hyrt.carserver.question.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_GETNEWSIMG;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.question.activity.BySpecialityActivity;
import cn.com.hyrt.carserver.question.activity.FindByBrandActivity;
import cn.com.hyrt.carserver.question.activity.QuestionActivity;
import cn.com.hyrt.carserver.question.adapter.QuestionBannerAdapter;

/**
 * 车辆问答主页
 * 
 * @author zoe
 * 
 */
public class QuestionFragment extends Fragment {

	@ViewInject(id=R.id.iv_banner) ImageLoaderView iv_banner;
	private View rootView;
	private GridView gvQuestion, gvExperts,gvRelatedKnowledge;
	private ViewPager bannerPager;
	private Button questionBtn;
	private WebServiceHelper mWebServiceHelper;
	private List<View> views = new ArrayList<View>(); 
	
	private static final String TAG = "QuestionFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.fragment_question, null);
		findView();
		initGrid();
		loadData();
		setListener();
		return rootView;
	}

	private void findView() {
		gvQuestion = (GridView) rootView.findViewById(R.id.gvQuestion);
		gvExperts = (GridView) rootView.findViewById(R.id.gvExperts);
		bannerPager = (ViewPager) rootView.findViewById(R.id.bannerPager);
		questionBtn = (Button) rootView.findViewById(R.id.btn_question);
		
		gvRelatedKnowledge = (GridView) rootView.findViewById(R.id.gvRelatedKnowledge1);
	}


	private void loadData() {

		views.clear();
		final LayoutInflater mInflater = LayoutInflater.from(getActivity());

		AlertHelper.getInstance(getActivity()).showLoading(getString(R.string.loading_msg));
		if (mWebServiceHelper == null) 
		{
			mWebServiceHelper = new WebServiceHelper(
			new WebServiceHelper.RequestCallback<Define.QUESTION_GETNEWSIMG>() 
			{
				@Override
				public void onSuccess(QUESTION_GETNEWSIMG result) 
				{
					LogHelper.i("tag", "result:" + result.data.size());
					AlertHelper.getInstance(getActivity()).hideLoading();
				
					if (result == null || result.data.size() <= 0) 
					{
						AlertHelper.getInstance(getActivity()).showCenterToast(R.string.question_imgload_failed);
					} 
					else
					{

						
						String[] image = new String[result.data.size()];
						
						for (int i = 0; i < result.data.size(); i++) 
						{
							image[i] = result.data.get(i).attacpath;
							if(image[i] == null){
								image[i] = "";
							}
							final String url = result.data.get(i).newslink;
							LinearLayout view = (LinearLayout) mInflater.inflate(R.layout.layout_news_banner, null);
							((ImageLoaderView) view.findViewById(R.id.iv_banner)).setImageUrl(image[i]);
							ImageLoaderView  imageListner = ((ImageLoaderView) view.findViewById(R.id.iv_banner));
							
							imageListner.setOnClickListener(new OnClickListener()
							{
								@Override
								public void onClick(View v)
								{
									Intent intent = new Intent();
									intent.setClass(getActivity(), WebActivity.class);
									intent.putExtra("url", url.toString());
									startActivity(intent);
								}
							});
							
							((TextView) view.findViewById(R.id.tv_url)).setText(url);
							views.add(view);
							
						}
						bannerPager.setAdapter(new QuestionBannerAdapter(views));
					}
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
				}
			}, getActivity());
		}
		mWebServiceHelper.getNewsImg();
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
		
		//相关知识
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

	private void setListener() 
	{
		questionBtn.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent();
				intent.setClass(getActivity(), QuestionActivity.class);
				startActivity(intent);
			}
		});
		
		bannerPager.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				int index = bannerPager.getCurrentItem();
				Log.i("index=========================", index+"");
//				intent.setClass(getActivity(), QuestionActivity.class);
//				startActivity(intent);
			}
		});
	}

	private AdapterView.OnItemClickListener questionItemClickListener = new AdapterView.OnItemClickListener()
	{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3)
		{
			Intent intent = new Intent();
			intent.setClass(getActivity(), BySpecialityActivity.class);
			switch (position)
			{
			case 0:
				// 故障速查
				intent.putExtra("title", getResources().getString(R.string.question_mend));
//				intent.putExtra("type", "1");
				intent.putExtra("flId", "000011");
				break;
			case 1:
				// 装饰改装
				intent.putExtra("title", getResources().getString(R.string.question_custmon));
//				intent.putExtra("type", "2");
				intent.putExtra("flId", "000012");
				break;
			case 2:
				// 保险直通
				intent.putExtra("title", getResources().getString(R.string.question_insurance));
//				intent.putExtra("type", "3");
				intent.putExtra("flId", "000013");
				break;
			case 3:
				// 洗车美容
				intent.putExtra("title", getResources().getString(R.string.question_cosmetology));
//				intent.putExtra("type", "4");
				intent.putExtra("flId", "000014");
				break;

			default:
				return;
			}
			intent.putExtra("isByQuestion", true);
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
				intent.putExtra("title", getResources().getString(R.string.question_find_specialty));
				break;
			case 1:
				// 按品牌找
				intent.setClass(getActivity(), FindByBrandActivity.class);
				break;

			default:
				return;
			}
			startActivity(intent);
		}
	};
	
	
	//相关知识
	private AdapterView.OnItemClickListener gvRelatedKnowledgeListener = new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent gvRelatedKnowledgeIntent = new Intent();
				String path = getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid=";
				switch(position){
				//维保详情
				case 0:
					gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
					path += "000003";
				    break;
				//保险知识
				case 1:
					gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
					path += "000004";
				    break;
				//经验心得
				case 2:
					gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
					path += "000005";
					break;
				default:
					return;  
				}
				LogHelper.i(TAG, "path:"+path);
				gvRelatedKnowledgeIntent.putExtra("url", path);
				startActivity(gvRelatedKnowledgeIntent);
			}
			
		};

	@Override
	public void onDestroy() {
		super.onDestroy();
		AlertHelper.getInstance(getActivity()).dismissLoading();
	}
		
		
}
