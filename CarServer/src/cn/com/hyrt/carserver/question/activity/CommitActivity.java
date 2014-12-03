package cn.com.hyrt.carserver.question.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;

/**
 * 车辆问答_提交问题
 * 
 * @author Administrator
 * 
 */
public class CommitActivity extends BaseActivity {

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				finish();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question_commit);
		Message msg = new Message();
		msg.what = 0;
		mHandler.sendMessageDelayed(msg, 10000);
	}
	
	@Override
	public void finish() {
		mHandler.removeMessages(0);
		super.finish();
	}
}
