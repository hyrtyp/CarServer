package cn.com.hyrt.carserver.question.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import cn.com.hyrt.carserver.R;

/**
 * 选择部位
 * @author Administrator
 *
 */
public class PositionActivity extends Activity{
	
//	private 
	RelativeLayout rl10;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_question_position);
		
		rl10=(RelativeLayout) findViewById(R.id.rl10);
		rl10.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
//		initView();
	}
	
//	private void initView(){
//		
//		
//	}
}
