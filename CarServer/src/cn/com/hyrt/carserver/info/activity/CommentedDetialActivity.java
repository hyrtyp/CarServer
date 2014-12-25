package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.COMMENTS;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
/**
 * 评论详情
 * @author awen
 *
 */
public class CommentedDetialActivity extends BaseActivity {
	@ViewInject(id=R.id.tv_comments_time) TextView tv_comments_time;
	@ViewInject(id=R.id.tv_comments_levels) TextView tv_comments_levels;
	@ViewInject(id=R.id.tv_comments_content) TextView tv_comments_content;
	@ViewInject(id=R.id.ll_comments) LinearLayout ll_comments;
	@ViewInject(id=R.id.ll_nodata) LinearLayout ll_nodata;
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
						ll_comments.setVisibility(View.VISIBLE);
						ll_nodata.setVisibility(View.GONE);
						tv_comments_time.setText("评论时间"+" : "+result.pltime);
						if ("5".equals(result.level)) {
							tv_comments_levels.setText("评论星级"+" : "+"五颗星");
						}else if ("1".equals(result.level)) {
							tv_comments_levels.setText("评论星级"+" : "+"一颗星");
						}else if ("2".equals(result.level)) {
							tv_comments_levels.setText("评论星级"+" : "+"二颗星");
						}else if ("3".equals(result.level)) {
							tv_comments_levels.setText("评论星级"+" : "+"三颗星");
						}else if ("4".equals(result.level)) {
							tv_comments_levels.setText("评论星级"+" : "+"四颗星");
						}
						if ("".equals(result.content) || result.content == null) {
							tv_comments_content.setText("评论内容"+" : "+"该用户未做具体评价");
						}else{
							tv_comments_content.setText("评论内容"+" : "+result.content);
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(CommentedDetialActivity.this).hideLoading();
						ll_comments.setVisibility(View.GONE);
						ll_nodata.setVisibility(View.VISIBLE);
					}
				} , CommentedDetialActivity.this);
		getCommnets.getMwpmSysTerminalReviewInfo(id);
	}
	
}
