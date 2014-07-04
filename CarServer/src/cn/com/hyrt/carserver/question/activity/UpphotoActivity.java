package cn.com.hyrt.carserver.question.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import cn.com.hyrt.carserver.R;

/**
 * 上传照片
 * @author Administrator
 *
 */
public class UpphotoActivity extends Activity{
	
//	private 
	LinearLayout ll_cancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_photo_select);
		
		ll_cancel = (LinearLayout) findViewById(R.id.layout_cancle);
		
		ll_cancel.setOnClickListener(new OnClickListener() {
			
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
