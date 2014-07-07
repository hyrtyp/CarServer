package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.FullListView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView.OnHeaderRefreshListener;
import cn.com.hyrt.carserver.info.adapter.QuestionDetailAdapter;

public class QuestionDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_replys) FullListView lv_replys;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.tv_prompt_one) TextView tvPromptOne;
	@ViewInject(id=R.id.tv_prompt_two) TextView tvPromptTwo;
	
	private int type;
	private String replyId;
	private QuestionDetailAdapter mQuestionAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", QuestionActivity.TYPE_NEW);
		replyId = intent.getStringExtra("replyId");
		ptrv.disableScroolUp();
		setListener();
		AlertHelper.getInstance(this).showLoading(null);
		
		if(type == QuestionActivity.TYPE_HISTORY){
			tvPromptOne.setVisibility(View.GONE);
			tvPromptTwo.setVisibility(View.GONE);
		}else{
			tvPromptOne.setVisibility(View.VISIBLE);
			tvPromptTwo.setVisibility(View.VISIBLE);
		}
		loadData();
	}
	
	private void loadData(){
		if(type == QuestionActivity.TYPE_HISTORY){
			mWebServiceHelper.getReplyDetail(replyId, WebServiceHelper.REPLY_DETAIL_HISTORY);
		}else{
			mWebServiceHelper.getReplyDetail(replyId, WebServiceHelper.REPLY_DETAIL_QUESTION);
		}
		
	}
	
	private WebServiceHelper mWebServiceHelper = new WebServiceHelper(
			new WebServiceHelper.RequestCallback<Define.REPLY_DETAIL>() {

				@Override
				public void onSuccess(Define.REPLY_DETAIL result) {
					AlertHelper.getInstance(QuestionDetailActivity.this).hideLoading();
					LogHelper.i("tag", "result:"+result.data.size());
					setData(result);
					ptrv.onHeaderRefreshComplete();
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					AlertHelper.getInstance(QuestionDetailActivity.this).hideLoading();
					ptrv.onHeaderRefreshComplete();
				}
	}, this);
	
	private void setData(Define.REPLY_DETAIL result){
		if(mQuestionAdapter == null){
			mQuestionAdapter = new QuestionDetailAdapter(result, this);
			lv_replys.setAdapter(mQuestionAdapter);
		}else{
			mQuestionAdapter.notifyDataSetChanged();
		}
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData();
			}
		});
	}
	
}
