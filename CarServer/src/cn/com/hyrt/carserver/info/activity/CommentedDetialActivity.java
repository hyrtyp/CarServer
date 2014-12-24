package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.COMMENTS;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class CommentedDetialActivity extends BaseActivity {
	@ViewInject(id=R.id.tv_comments) TextView tv_comments;
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_content);
		Intent intent = getIntent();
		id = intent.getStringExtra("questionId");
		loadData();
	}
	private void loadData() {
		WebServiceHelper getCommnets = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.COMMENTS>() {

					@Override
					public void onSuccess(COMMENTS result) {
						LogHelper.i("tag", "sussenjmksdfnhfffffffffffffffffffffff");
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						
					}
				} , CommentedDetialActivity.this);
		getCommnets.getMwpmSysTerminalReviewInfo(id);
	}
	
}
