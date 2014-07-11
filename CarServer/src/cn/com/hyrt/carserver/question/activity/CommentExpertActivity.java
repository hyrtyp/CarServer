package cn.com.hyrt.carserver.question.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.COMMENT_EXPERT;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class CommentExpertActivity extends BaseActivity{
	
	@ViewInject(id=R.id.rb_score) RatingBar rbScore;
	@ViewInject(id=R.id.et_content) EditText etContent;
	@ViewInject(id=R.id.btn_commit,click="commit") Button btnCommit;
	
	private String replyId;
	private WebServiceHelper mWebServiceHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		replyId = intent.getStringExtra("replyId");
		if(replyId == null){
			AlertHelper.getInstance(this).showCenterToast(R.string.info_load_fail);
			finish();
			return;
		}
		setContentView(R.layout.activity_comment_expert);
	}
	
	public void commit(View view){
		Define.COMMENT_EXPERT commentExpert = new Define.COMMENT_EXPERT();
		commentExpert.seekid = replyId;
		commentExpert.level = (int)rbScore.getRating()+"";
		if(!"".equals(etContent.getText().toString())){
			commentExpert.content = etContent.getText().toString();
		}
		
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.BASE>() {

						@Override
						public void onSuccess(Define.BASE result) {
							AlertHelper.getInstance(CommentExpertActivity.this)
							.showCenterToast(R.string.comment_success);
							setResult(101);
							finish();
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(CommentExpertActivity.this)
							.showCenterToast(R.string.comment_fail);
						}
			}, this);
		}
		mWebServiceHelper.commentExpert(commentExpert);
	}
}
