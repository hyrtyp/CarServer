package cn.com.hyrt.carserver.question.fragment;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_GETNEWSIMG.CDATA;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.utils.CommonUtil;
import cn.com.hyrt.carserver.base.view.RollViewPager;
import cn.com.hyrt.carserver.question.activity.BySpecialityActivity;
import cn.com.hyrt.carserver.question.activity.FindByBrandActivity;
import cn.com.hyrt.carserver.question.activity.QuestionActivity;

/**
 * 车辆问答主页
 * 
 * @author zoe
 * 
 */
public class QuestionFragment extends Fragment {

	private View rootView;
	private GridView gvQuestion, gvExperts,gvRelatedKnowledge;
	private Button questionBtn;
	private WebServiceHelper mWebServiceHelper;
	private static final String TAG = "QuestionFragment";
	private List<String> titleList = new ArrayList<String>(); //标题集合
	private List<String> imgUrlList = new ArrayList<String>(); //图片地址集合
	private List<View> dotList = new ArrayList<View>(); //点集集合
	private LinearLayout top_news_viewpager; //放置轮播图片位置
	private TextView top_news_title; //放置图片标题的位置
	private LinearLayout dots_ll; //放置图片中选中点的位置
	//存储底部条目的对应news集合  TODO
	private List<CDATA> newsList = new ArrayList<Define.QUESTION_GETNEWSIMG.CDATA>();
	private List<String> urls = new ArrayList<String>(); //图片对应链接的集合

	@SuppressLint("InflateParams")
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
		top_news_viewpager = (LinearLayout) rootView.findViewById(R.id.top_news_viewpager);
		top_news_title = (TextView) rootView.findViewById(R.id.top_news_title);
		dots_ll = (LinearLayout) rootView.findViewById(R.id.dots_ll);
		
		gvQuestion = (GridView) rootView.findViewById(R.id.gvQuestion);
		gvExperts = (GridView) rootView.findViewById(R.id.gvExperts);
		questionBtn = (Button) rootView.findViewById(R.id.btn_question);
		
		gvRelatedKnowledge = (GridView) rootView.findViewById(R.id.gvRelatedKnowledge1);
	}


	private void loadData() {
		AlertHelper.getInstance(getActivity()).showLoading(getString(R.string.loading_msg));
		if (mWebServiceHelper == null) {
			mWebServiceHelper = new WebServiceHelper(
			new WebServiceHelper.RequestCallback<Define.QUESTION_GETNEWSIMG>() {
				@Override
				public void onSuccess(QUESTION_GETNEWSIMG result) {
					LogHelper.i("tag", "result:" + result.data.size());
					AlertHelper.getInstance(getActivity()).hideLoading();
					if (result == null || result.data.size() <= 0) {
						AlertHelper.getInstance(getActivity()).showCenterToast(R.string.question_imgload_failed);
					}else{
						newsList.clear();
						titleList.clear();
						imgUrlList.clear();
						urls.clear();
						newsList.addAll(result.data);
						for (int i = 0; i < result.data.size(); i++) {	
							titleList.add(result.data.get(i).title);
							imgUrlList.add(result.data.get(i).attacpath);
							urls.add(result.data.get(i).newslink);
							System.out.println(result.data.get(i).newslink); //TODO
							System.out.println(result.data.get(i).attacpath);
							//viewpager你放到那里去都能用，组装思想
							RollViewPager rollViewPager = new RollViewPager(getActivity(),dotList,new RollViewPager.OnPageClick() {
								@Override
								public void click(String url) {
									Intent intent = new Intent();
									intent.setClass(getActivity(), WebActivity.class);
									intent.putExtra("url", url.toString());
									startActivity(intent);
								}
							}); 
							//TODO
							rollViewPager.initTitle(titleList,top_news_title);
							//传递图片对应url地址
							rollViewPager.initImgUrl(imgUrlList);
							rollViewPager.initUrls(urls);
							//初始化点操作
							initDot();
							//滚动自定义viewpager方法
							rollViewPager.startRoll();
							top_news_viewpager.removeAllViews();
							top_news_viewpager.addView(rollViewPager);
						}
					}
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					if (204==errorNo) {
						AlertHelper.getInstance(getActivity()).showCenterToast("未查询到数据！");
					}
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
//				String path = getString(R.string.method_weburl)+"/cspportal/knowledge/list?typeid=";
				String path = getString(R.string.method_weburl)+"/knowledge/list?typeid=";
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
//				System.out.println(path+"_--------------");
				gvRelatedKnowledgeIntent.putExtra("url", path);
				startActivity(gvRelatedKnowledgeIntent);
			}
			
		};

	@Override
	public void onDestroy() {
		super.onDestroy();
		AlertHelper.getInstance(getActivity()).dismissLoading();
	}
	
	private void initDot() {
		//封装点的集合
		dotList.clear();
		//如果线性布局中就有点，做移除后添加的操作
		dots_ll.removeAllViews();
		for(int i=0;i<newsList.size();i++){
			View view = new View(getActivity());
			if(i==0){
				view.setBackgroundResource(R.drawable.dot_focus);
			}else{
				view.setBackgroundResource(R.drawable.dot_normal);
			}
			//dp---->px   1dp = 1.5px   1dp = 1px  dip dp ////// dpi ppi
			//px---->dp
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					CommonUtil.dip2px(getActivity(), 6), 
					CommonUtil.dip2px(getActivity(), 6));
			layoutParams.setMargins(
					CommonUtil.dip2px(getActivity(), 4), 0, 
					CommonUtil.dip2px(getActivity(), 4), 0);
			view.setLayoutParams(layoutParams);
			
			dotList.add(view);
			dots_ll.addView(view);
		}
	}	
}
