package cn.com.hyrt.carserver.question.fragment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.WebActivity;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_GETNEWSIMG;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_GETNEWSIMG.CDATA;
import cn.com.hyrt.carserver.base.baseFunction.Define.ZJUSERMAIN_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.utils.CircleBitmapDisplayer;
import cn.com.hyrt.carserver.base.utils.CommonUtil;
import cn.com.hyrt.carserver.base.view.RollViewPager;
import cn.com.hyrt.carserver.question.activity.BySpecialityActivity;
import cn.com.hyrt.carserver.question.activity.ExpertAnsweredQuestionsActivity;
import cn.com.hyrt.carserver.question.activity.ExpertDetitalActivity;
import cn.com.hyrt.carserver.question.activity.FindByBrandActivity;
import cn.com.hyrt.carserver.question.activity.MoreExpertsActivity;
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
	private WebServiceHelper mExpertInfoWebServiceHelper;
	private WebServiceHelper mWebServiceHelper;
	private static final String TAG = "QuestionFragment";
	
	//轮播图相关
	private List<String> titleList = new ArrayList<String>(); //标题集合
	private List<String> imgUrlList = new ArrayList<String>(); //图片地址集合
	private List<View> dotList = new ArrayList<View>(); //点集集合
	private LinearLayout top_news_viewpager; //放置轮播图片位置
	private TextView top_news_title; //放置图片标题的位置
	private LinearLayout dots_ll; //放置图片中选中点的位置
	private Context mContext;
	//存储底部条目的对应news集合  TODO
	private List<CDATA> newsList = new ArrayList<Define.QUESTION_GETNEWSIMG.CDATA>();
	private List<String> urls = new ArrayList<String>(); //图片对应链接的集合
	
	//汽车专家列表相关
	private ImageView iv_expert_photo1;
	private TextView tv_expert_name1 ;
	private TextView tv_expert_jianjie1 ;
	private ImageView iv_expert_photo2;
	private TextView tv_expert_name2;
	private TextView tv_expert_jianjie2 ;
	private ImageView iv_expert_photo3;
	private TextView tv_expert_name3 ;
	private TextView tv_expert_jianjie3 ;
	private ImageView iv_expert_photo4;
	private TextView tv_expert_name4 ;
	private TextView tv_expert_jianjie4 ;
	private ImageView iv_expert_photo5;
	private TextView tv_expert_name5 ;
	private TextView tv_expert_jianjie5 ;
	private TextView tv_more_experts  ; // moreexperts
	private ImageView [] expertsphoto = {iv_expert_photo1 ,iv_expert_photo2,iv_expert_photo3,iv_expert_photo4,iv_expert_photo5};
	private TextView expert_name[] = {tv_expert_name1,tv_expert_name2,tv_expert_name3,tv_expert_name4,tv_expert_name5};
	private TextView expert_jianjie[] = {tv_expert_jianjie1,tv_expert_jianjie2,tv_expert_jianjie3,tv_expert_jianjie4,tv_expert_jianjie5};
	private Bitmap mBitmap ;
	private DisplayImageOptions options;
	private List<Define.ZJUSERMAIN_LIST.CDATA> expertList = new ArrayList<Define.ZJUSERMAIN_LIST.CDATA>();
	
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{	
		rootView = inflater.inflate(R.layout.fragment_question, null);
		findView();
		initGrid();
		loadData();
		loadDataExperts();
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
		mContext = getActivity();
		
		// 汽车专家
		expertsphoto[0] = (ImageView) rootView.findViewById(R.id.iv_expert_photo1);
		expertsphoto[1] = (ImageView) rootView.findViewById(R.id.iv_expert_photo2);
		expertsphoto[2] = (ImageView) rootView.findViewById(R.id.iv_expert_photo3);
		expertsphoto[3] = (ImageView) rootView.findViewById(R.id.iv_expert_photo4);
		expertsphoto[4] = (ImageView) rootView.findViewById(R.id.iv_expert_photo5);
		expert_name[0] =  (TextView) rootView.findViewById(R.id.tv_expert_name1);
		expert_name[1] =  (TextView) rootView.findViewById(R.id.tv_expert_name2);
		expert_name[2] =  (TextView) rootView.findViewById(R.id.tv_expert_name3);
		expert_name[3] =  (TextView) rootView.findViewById(R.id.tv_expert_name4);
		expert_name[4] =  (TextView) rootView.findViewById(R.id.tv_expert_name5);
		expert_jianjie[0] =  (TextView) rootView.findViewById(R.id.tv_expert_jianjie1);
		expert_jianjie[1] =  (TextView) rootView.findViewById(R.id.tv_expert_jianjie2);
		expert_jianjie[2] =  (TextView) rootView.findViewById(R.id.tv_expert_jianjie3);
		expert_jianjie[3] =  (TextView) rootView.findViewById(R.id.tv_expert_jianjie4);
		expert_jianjie[4] =  (TextView) rootView.findViewById(R.id.tv_expert_jianjie5);
		tv_more_experts =  (TextView) rootView.findViewById(R.id.tv_more_experts);
		
		options = new DisplayImageOptions.Builder()
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true)
		.build();
		
	}

	/**
	 * 获取Top5专家列表
	 */
	private void loadDataExperts() {
		if (mExpertInfoWebServiceHelper == null) {
			mExpertInfoWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.ZJUSERMAIN_LIST>() {

						@Override
						public void onSuccess(ZJUSERMAIN_LIST result) {
							LogHelper.i("tag", "result:" + result.data.size());
							if (result == null || result.data.size() <= 0) {
								AlertHelper.getInstance(getActivity()).showCenterToast(R.string.question_imgload_failed);
							}else{
								expertList.clear();
								expertList.addAll(result.data);				
								for (int i = 0;  i < expertList.size(); i++) {
									@SuppressWarnings("deprecation")
									DisplayImageOptions options = new DisplayImageOptions.Builder()
							        .cacheInMemory(true)
							        .showStubImage(R.drawable.expert_face2) 
							        .showImageForEmptyUri(R.drawable.expert_face2)
							        .cacheOnDisk(true)
							        .displayer(new CircleBitmapDisplayer())
							        .build();
							    ImageLoader.getInstance().displayImage(expertList.get(i).imagepath,expertsphoto[i],options); 
//							    "http://192.168.10.238:8080/CSPInterface/upload/zjimage//facePhoto-201504211604333.jpeg"
								expert_name[i].setText(expertList.get(i).name);
								expert_jianjie[i].setText(expertList.get(i).zcnames);
								}
							}
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(getActivity()).showCenterToast("未查询到数据！");
						}
					}, getActivity());
		}
		mExpertInfoWebServiceHelper.getZJUserMainList();
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
//							System.out.println(result.data.get(i).newslink); //TODO
//							System.out.println(result.data.get(i).attacpath);
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
		//车辆问答
		int[] questionImgArray = new int[] { R.drawable.ic_question_mend,
											 R.drawable.ic_question_custom,};
//											 R.drawable.ic_question_insurance,
//											 R.drawable.ic_question_cosmetology };
		int[] questionTextSourceArray = new int[] { R.string.question_mend,
													R.string.question_custmon1, };
//													/*R.string.question_insurance,
//													R.string.question_cosmetology */};
		PortalGridAdapter mQuestionAdapter = new PortalGridAdapter(
				questionImgArray, questionTextSourceArray, getActivity());
		gvQuestion.setAdapter(mQuestionAdapter);
		
		//找专家
		gvQuestion.setOnItemClickListener(questionItemClickListener);

		int[] expertsImgArray = new int[] { R.drawable.ic_question_specialty,
				R.drawable.ic_question_brand };
		int[] expertsTextSourceArray = new int[] {R.string.question_find_specialty, R.string.question_find_brand };
		PortalGridAdapter mExpertsAdapter = new PortalGridAdapter(
				expertsImgArray, expertsTextSourceArray, getActivity());
		gvExperts.setAdapter(mExpertsAdapter);
		gvExperts.setOnItemClickListener(ExpertItemClickListener);
		
		//相关知识
		int[] relatedKnowledgeImgArray = 
				new int[]{
				R.drawable.ic_knowledge_detail,/* R.drawable.ic_knowledge_insurance,*/
				R.drawable.ic_knowledge_experience/*, R.drawable.bg_blank*/};
		int[] relatedKnowledgeTextSourceArray = 
				new int[]{
				R.string.knowledge_detail, /*R.string.knowledge_insurance,*/
				R.string.knowledge_experience/*, R.string.blank_text*/};
		PortalGridAdapter mRelatedKnowledgeAdapter = 
				new PortalGridAdapter(
						relatedKnowledgeImgArray,
						relatedKnowledgeTextSourceArray,
						getActivity());
		gvRelatedKnowledge.setAdapter(mRelatedKnowledgeAdapter);
		gvRelatedKnowledge.setOnItemClickListener(gvRelatedKnowledgeListener);
	}

	private void setListener() {
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
		
		//更多专家
		tv_more_experts.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), MoreExpertsActivity.class);
				startActivity(intent);
			}
		});
		
		
		final Intent intent = new Intent(getActivity(),ExpertDetitalActivity.class);
		
		//点击专家头像查看专家详情
		expertsphoto[0].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.putExtra("id", expertList.get(0).id);
				startActivity(intent);
			}
		});
		expertsphoto[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra("id", expertList.get(1).id);
				startActivity(intent);
			}
		});
		expertsphoto[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra("id", expertList.get(2).id);
				startActivity(intent);
			}
		});
		expertsphoto[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra("id", expertList.get(3).id);
				startActivity(intent);
			}
		});
		expertsphoto[4].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent.putExtra("id", expertList.get(4).id);
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
//			intent.setClass(getActivity(), BySpecialityActivity.class);
			switch (position)
			{
			case 0:
				// 故障速查
//				intent.putExtra("title", getResources().getString(R.string.question_mend));
////				intent.putExtra("type", "1");
//				intent.putExtra("flId", "000011");
				
				intent.setClass(getActivity(), BySpecialityActivity.class);
				intent.putExtra("title", getResources().getString(R.string.question_mend));
				intent.putExtra("flId", "000011");
				intent.putExtra("isByQuestion", true);
				startActivity(intent);
				break;
			case 1:
				// 装饰改装
//				intent.putExtra("title", getResources().getString(R.string.question_custmon));
////				intent.putExtra("type", "2");
//				intent.putExtra("flId", "000012");
				
				intent.setClass(getActivity(), ExpertAnsweredQuestionsActivity.class);
				startActivity(intent);
				break;
//			case 2:
//				// 保险直通
//				intent.putExtra("title", getResources().getString(R.string.question_insurance));
////				intent.putExtra("type", "3");
//				intent.putExtra("flId", "000013");
//				break;
//			case 3:
//				// 洗车美容
//				intent.putExtra("title", getResources().getString(R.string.question_cosmetology));
////				intent.putExtra("type", "4");
//				intent.putExtra("flId", "000014");
//				break;

			default:
				return;
			}
//			intent.putExtra("isByQuestion", true);
//			startActivity(intent);
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
//				String path = getString(R.string.method_weburl)+"/knowledge/list?typeid=";
				switch(position){
				//维保详情
				case 0:
					gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
					path += "000003";
				    break;
//				//保险知识
//				case 1:
//					gvRelatedKnowledgeIntent.setClass(getActivity(), WebActivity.class);
//					path += "000004";
//				    break;
				//经验心得
//				case 2:
				case 1:
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
