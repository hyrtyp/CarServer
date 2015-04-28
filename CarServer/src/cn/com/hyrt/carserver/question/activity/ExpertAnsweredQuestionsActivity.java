package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_TOP_PAGER_CLASSES;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_CORRELATION;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.question.adapter.CorrelationAdapter;

/***
 * 按条件展示专家回答的问题
 * @author Administrator
 *
 */
public class ExpertAnsweredQuestionsActivity extends BaseActivity {
	
	private WebServiceHelper mWebServiceHelper1;
	private WebServiceHelper mWebServiceHelper;
	private List<Map<String, String>> oneList;
	private List<List<Map<String, String>>> twoList;
	private List<String> QuestionOneId = new ArrayList<String>();
	private List<String> QuestionOneName = new ArrayList<String>();
	private List<String> QuestionTwoId = new ArrayList<String>();
	private List<String> QuestionTwoName = new ArrayList<String>();
	private List<String> QuestionThirdId = new ArrayList<String>();
	private List<String> QuestionThirdName = new ArrayList<String>();
	private ArrayAdapter<String> mAnsweredOneArrayAdapter;
	private ArrayAdapter<String> mAnsweredTwoArrayAdapter;
	private ArrayAdapter<String> mAnsweredThirdArrayAdapter;
	private CorrelationAdapter correlationAdapter;
	private int pageNo = 1;
	private boolean isLoadMore = false;
	private String Id;
	private String name;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	@ViewInject(id=R.id.sp_question_one) Spinner spQuestionOne;
	@ViewInject(id=R.id.sp_question_two) Spinner spQuestionTwo;
	@ViewInject(id=R.id.sp_question_third) Spinner spQuestionThird;
	@ViewInject(id=R.id.lv_expert_search) ListView ls_correlation;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
//	@ViewInject(id=R.id.bt_shuaxin) Button shuxin;
	
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_answered_question);
		
		loadData();
		setLisner();
	}

	private void loadData() {
		AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).showLoading(null);
		QuestionOneName.clear();
		QuestionOneId.clear();
		mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.RequestCallback<INFO_TOP_PAGER_CLASSES>() {

			@Override
			public void onSuccess(INFO_TOP_PAGER_CLASSES result) {
//				AlertHelper.getInstance(ExpertAnsweredQuestions.this).hideLoading();
				if (result != null && result.data.size()>0) {
					for (int i = 0; i < result.data.size(); i++) {
						if ("wd".equals(result.data.get(i).infotype) && "1".equals(result.data.get(i).depth)) {
							QuestionOneName.add(result.data.get(i).name); 
							QuestionOneId.add(result.data.get(i).id);
						}
					}
					
					mAnsweredOneArrayAdapter = new ArrayAdapter<String>(ExpertAnsweredQuestionsActivity.this, R.layout.layout_spinner_item, QuestionOneName);
					spQuestionOne.setAdapter(mAnsweredOneArrayAdapter);
				}
			}
			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).hideLoading();
			}
		}, ExpertAnsweredQuestionsActivity.this);
		mWebServiceHelper.getCLWDclasses();
	}

	

	private void loadData(WebServiceHelper mWebServiceHelper , String id) {
		AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).showLoading(null);
		mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).hideLoading();
				ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
				classifyJsonParser.parse(result);
				QuestionTwoName.clear();
				QuestionTwoId.clear();
				QuestionTwoName.add("请选择");
				QuestionTwoId.add("-1");
				
				oneList = classifyJsonParser.getOneList();
//				LogHelper.i("tag", "oneList1:"+oneList);
				twoList = classifyJsonParser.getTwoList();
//				LogHelper.i("tag", "twoList1:"+twoList);
				
				for (Map<String, String> map : oneList) {
					QuestionTwoName.add(map.get("name"));
					QuestionTwoId.add(map.get("id"));
				}
				
				mAnsweredTwoArrayAdapter = new ArrayAdapter<String>(ExpertAnsweredQuestionsActivity.this, R.layout.layout_spinner_item, QuestionTwoName);
				spQuestionTwo.setAdapter(mAnsweredTwoArrayAdapter);

				if (QuestionTwoName.size() >1) {
					spQuestionTwo.setSelection(1);
				}
			}
		}, ExpertAnsweredQuestionsActivity.this);
		mWebServiceHelper.getMaintainFL(id);
	}
	
	private int curOneLevelIndex = 0;
	private int curTwoLevelIndex = 0;
	private int curThirdLevelIndex;
	private void setLisner() {
		spQuestionOne.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
				curOneLevelIndex = position;
				LogHelper.i("tag", "curOneLevelIndex:"+curOneLevelIndex);
				data.clear();
				if (correlationAdapter != null) {
					correlationAdapter.notifyDataSetChanged();
				}
				loadData(mWebServiceHelper1 ,QuestionOneId.get(position));
				
				spQuestionTwo.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
						curTwoLevelIndex = position-1;
						QuestionThirdId.clear();
						QuestionThirdName.clear();
						QuestionThirdId.add("-1");
						QuestionThirdName.add("请选择");
						LogHelper.i("tag", "curTwoLevelIndex:"+curTwoLevelIndex);
						
						data.clear();
						if (correlationAdapter != null) {
							correlationAdapter.notifyDataSetChanged();
						}
						if (mAnsweredThirdArrayAdapter != null) {
							mAnsweredThirdArrayAdapter.notifyDataSetChanged();
						}
						if(position <= 0){
							return;
						}
						
						List<Map<String, String>> list = twoList.get(curTwoLevelIndex);
						LogHelper.i("tag", "list "+list);
						for (Map<String, String> map : list) {
							QuestionThirdName.add(map.get("name"));
							QuestionThirdId.add(map.get("id"));
						}
						
						mAnsweredThirdArrayAdapter = new ArrayAdapter<String>(ExpertAnsweredQuestionsActivity.this, R.layout.layout_spinner_item, QuestionThirdName);
						spQuestionThird.setAdapter(mAnsweredThirdArrayAdapter);
						if (QuestionThirdName.size() > 1) {
							spQuestionThird.setSelection(1);
						}
						
						spQuestionThird.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
								data.clear();
								if (correlationAdapter != null) {
									correlationAdapter.notifyDataSetChanged();
								}
								if(position <= 0){
									return;
								}
								name = QuestionThirdName.get(position);
								Id = QuestionThirdId.get(position);
								loadRelData();
							}

							@Override
							public void onNothingSelected(AdapterView<?> parent) {}
						});
					}
					@Override
					public void onNothingSelected(AdapterView<?> parent) {}
				});
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		
		
		
		
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				isLoadMore = false;
				loadRelData();
			}
		});

		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {

			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				isLoadMore = true;
				loadRelData();
			}
		});
		
//		shuxin.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Animation operatingAnim = AnimationUtils.loadAnimation(ExpertAnsweredQuestions.this, R.anim.rotate);  
//				LinearInterpolator lin = new LinearInterpolator();  
//				operatingAnim.setInterpolator(lin);  
//				if (operatingAnim != null) {  
//					shuxin.startAnimation(operatingAnim);  
//				}  
//				loadData();
//			}
//		});
	}

	
	
	/**
	 * 展示搜索到的问题
	 */
	protected void loadRelData() {
		AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).showLoading(null);
		WebServiceHelper mCarInfoServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.QUESTION_CORRELATION>() {
					@SuppressWarnings("unused")
					@Override
					public void onSuccess(QUESTION_CORRELATION result) {
						AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).hideLoading();
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
						if (result == null) {
							AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_ALTER_CAR);
//							finish();
						} else {
							if (result == null && data.size() > 0) {
								AlertHelper.getInstance(
										ExpertAnsweredQuestionsActivity.this)
										.showCenterToast(R.string.load_done);
							}
							if (!isLoadMore) {
								data.clear();
							} 

							isLoadMore = false;

							data.clear();
							for (int i = 0, j = result.data.size(); i < j; i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("content", result.data.get(i).content);
								map.put("zyname", result.data.get(i).zyname);
								map.put("username", result.data.get(i).username);
								map.put("seekdate", result.data.get(i).seekdate);
								map.put("userid", result.data.get(i).userid);
								map.put("seektype", result.data.get(i).seektype);
								map.put("attacpathname",
										result.data.get(i).attacpathname);
								map.put("terminalid",
										result.data.get(i).terminalid);
								map.put("attacpath",
										result.data.get(i).attacpath);
								map.put("id", result.data.get(i).id);
								map.put("levelname", result.data.get(i).levelname);
								map.put("sjname", result.data.get(i).sjname);
								data.add(map);
							}

							if (correlationAdapter == null) {
								correlationAdapter = new CorrelationAdapter(
										ExpertAnsweredQuestionsActivity.this, data, name);
								System.out.println(name+"   3name");
								ls_correlation.setAdapter(correlationAdapter);
							} else {
								correlationAdapter.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).hideLoading();
						AlertHelper.getInstance(ExpertAnsweredQuestionsActivity.this).showCenterToast(R.string.info_load_fail);
						setResult(Define.RESULT_FROM_ALTER_CAR);
						ptrv.onFooterRefreshComplete();
						ptrv.onHeaderRefreshComplete();
//						finish();
					}
				}, this);

		if (isLoadMore) {
			pageNo++;
		} else {
			pageNo = 1;
		}
		mCarInfoServiceHelper.getCorrelation(Id, pageNo);
	}

}