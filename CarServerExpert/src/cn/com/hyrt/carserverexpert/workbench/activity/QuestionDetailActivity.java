package cn.com.hyrt.carserverexpert.workbench.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_QUESTION_DETAIL_LIST;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_REPLY;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.PullToRefreshView;
import cn.com.hyrt.carserverexpert.workbench.adapter.QuestionDetailAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class QuestionDetailActivity extends BaseActivity{

	private String questionId;
	private String type;
	private List<Define.INFO_QUESTION_DETAIL> mData
	= new ArrayList<Define.INFO_QUESTION_DETAIL>();
	private QuestionDetailAdapter mAdapter;
	
	@ViewInject(id=R.id.et_reply) EditText etReply;
	@ViewInject(id=R.id.btn_reply,click="reply") Button btnReply;
	@ViewInject(id=R.id.btn_accept,click="accept") Button btnAccept;
	@ViewInject(id=R.id.layout_reply) LinearLayout layoutReply;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	
	@ViewInject(id=R.id.listview) ListView listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question_detail);
		Intent intent = getIntent();
		questionId = intent.getStringExtra("questionId");
		type = intent.getStringExtra("type");
		if(WebServiceHelper.QUESTION_TYPE_HIS.equals(type)){
			layoutReply.setVisibility(View.GONE);
		}else{
			layoutReply.setVisibility(View.VISIBLE);
		}
		
//		if(WebServiceHelper.QUESTION_TYPE_NEW.equals(type)){
//			btnAccept.setVisibility(View.VISIBLE);
//		}else{
//			btnAccept.setVisibility(View.GONE);
//		}
		loadData();
		setListener();
	}
	
	public void accept(View view){
		
	}
	
	public void reply(View view){
		String content = etReply.getText().toString();
		if(content == null || "".equals(content.trim())){
			AlertHelper.getInstance(this).showCenterToast("请输入回答");
			return;
		}
		Define.INFO_REPLY replyInfo = new Define.INFO_REPLY();
		replyInfo.consultationid = questionId;
		replyInfo.type = type;
		replyInfo.content = content;
		AlertHelper.getInstance(this).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

			@Override
			public void onSuccess(Define.BASE result) {
				etReply.setText("");
				loadData();
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(QuestionDetailActivity.this).showCenterToast("请求失败");
			}
		}, this).saveReply(replyInfo);
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.INFO_QUESTION_DETAIL_LIST>() {

			@Override
			public void onSuccess(INFO_QUESTION_DETAIL_LIST result) {
				AlertHelper.getInstance(QuestionDetailActivity.this).hideLoading();
				mData.clear();
				mData.addAll(result.data);
				LogHelper.i("tag", mData.toString());
				if(mAdapter == null){
					mAdapter = new QuestionDetailAdapter(mData, QuestionDetailActivity.this, type);
					listview.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
				ptrv.onHeaderRefreshComplete();
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(QuestionDetailActivity.this).hideLoading();
			}
		}, this).getQuestionDetail(questionId);
	}
	
	@Override
	public void finish() {
		setResult(101);
		super.finish();
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData();
			}
		});
	}
}
