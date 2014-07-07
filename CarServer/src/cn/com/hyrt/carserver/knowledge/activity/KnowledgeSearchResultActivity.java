package cn.com.hyrt.carserver.knowledge.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_SEARCH_RESULT;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.info.activity.QuestionDetailActivity;
import cn.com.hyrt.carserver.knowledge.adapter.QuestionResultAdapter;

public class KnowledgeSearchResultActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_content)ListView lv_content;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	
	private String searchStr;
	
	private List<Define.QUESTION_SEARCH_RESULT.CDATA> datas = new ArrayList<Define.QUESTION_SEARCH_RESULT.CDATA>();
	
	private int pageNo = 1;
	private boolean isLoadMore = false;

	private QuestionResultAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activitiy_knowledge_search_result);
		Intent intent = getIntent();
		searchStr = intent.getStringExtra("str");
		AlertHelper.getInstance(this).showLoading(null);
		setListener();
		loadData();
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				isLoadMore = false;
				loadData();
			}
		});
		
		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
			
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				isLoadMore = true;
				loadData();
			}
		});
		
		lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				String id = datas.get(position).id;
				Intent intent = new Intent();
				intent.putExtra("replyId", id);
				intent.putExtra("type", QuestionDetailActivity.TYPE_HISTORY);
				intent.setClass(KnowledgeSearchResultActivity.this, QuestionDetailActivity.class);
				startActivity(intent);
			}
		});
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.QUESTION_SEARCH_RESULT>() {

					@Override
					public void onSuccess(QUESTION_SEARCH_RESULT result) {
						setData(result);
						AlertHelper.getInstance(KnowledgeSearchResultActivity.this).hideLoading();
						LogHelper.i("tag", "result:"+result.data.size());
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(KnowledgeSearchResultActivity.this).hideLoading();
						if(datas.size()>0){
							AlertHelper.getInstance(KnowledgeSearchResultActivity.this).showCenterToast(R.string.load_done);
							ptrv.onFooterRefreshComplete();
							return;
						}
						AlertHelper.getInstance(KnowledgeSearchResultActivity.this).showCenterToast(R.string.info_load_fail);
						
					}
		}, this);
//		mWebServiceHelper.setOnSuccessListener(new WebServiceHelper.OnSuccessListener() {
//			
//			@Override
//			public void onSuccess(String result) {
//				ClassifyJsonParser mClassifyJsonParser = new ClassifyJsonParser();
//				mClassifyJsonParser.parse(result);
//				//一级分类
//				List<Map<String, String>> oneList = mClassifyJsonParser.getOneList();
//				
//				//二级分类
//				List<List<Map<String, String>>> twoList = mClassifyJsonParser.getTwoList();
//				
//				//三级分类(如果没有三级分类 threeList size为空)
//				List<List<List<Map<String, String>>>> threeList = mClassifyJsonParser.getThreeList();
//			}
//		});
		if(isLoadMore){
			pageNo++;
		}else{
			pageNo = 1;
		}
		mWebServiceHelper.searchQuestion(searchStr, pageNo);
	}
	
	private void setData(QUESTION_SEARCH_RESULT result){
		
		if(result == null && datas.size()>0){
			AlertHelper.getInstance(this).showCenterToast(R.string.load_done);
		}
		
		if(!isLoadMore){
			datas.clear();
			ptrv.onHeaderRefreshComplete();
		}else{
			ptrv.onFooterRefreshComplete();
			
		}
		
		
		isLoadMore = false;
		datas.addAll(result.data);
		if(mAdapter == null){
			mAdapter = new QuestionResultAdapter(datas, searchStr, this);
			lv_content.setAdapter(mAdapter);;
		}else{
			mAdapter.notifyDataSetChanged();
		}
		
	}
}

