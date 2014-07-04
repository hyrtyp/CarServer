package cn.com.hyrt.carserver.question.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;

public class QuestionActivity extends BaseActivity {

	private RelativeLayout rl_teamNotifi;
	private RelativeLayout ll_teamNotifi;
	private Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question);

		initView();
	}

	private void initView() {

		rl_teamNotifi = (RelativeLayout) findViewById(R.id.rl_teamNotifi);
		ll_teamNotifi = (RelativeLayout) findViewById(R.id.rl_sysNotifi);
		mButton = (Button) findViewById(R.id.btn_commit);

		final Intent intent = new Intent();
		
		rl_teamNotifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				intent.setClass(QuestionActivity.this, PositionActivity.class);
				startActivity(intent);

			}
		});

		ll_teamNotifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(QuestionActivity.this, UpphotoActivity.class);
				startActivity(intent);

			}
		});
		
		mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(QuestionActivity.this, CommitActivity.class);
				startActivity(intent);
			}
		});
	}
}
