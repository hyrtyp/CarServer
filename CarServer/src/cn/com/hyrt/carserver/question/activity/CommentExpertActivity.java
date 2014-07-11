package cn.com.hyrt.carserver.question.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.helper.LogHelper;

public class CommentExpertActivity extends BaseActivity{
	
	@ViewInject(id=R.id.rb_score) RatingBar rbScore;
	@ViewInject(id=R.id.et_content) EditText etContent;
	@ViewInject(id=R.id.btn_commit,click="commit") Button btnCommit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_expert);
	}
	
	public void commit(View view){
		float score = rbScore.getStepSize();
		LogHelper.i("tag", "score:"+score);
	}
}
