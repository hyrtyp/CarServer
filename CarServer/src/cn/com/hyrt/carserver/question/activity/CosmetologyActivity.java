package cn.com.hyrt.carserver.question.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.R.color;
import cn.com.hyrt.carserver.R.drawable;
import cn.com.hyrt.carserver.base.activity.BaseActivity;

/**
 * 美容装潢
 * 
 * @author Administrator
 * 
 */
public class CosmetologyActivity extends BaseActivity {

	private Button leftBtn, rightBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question_mount);

		initView();
	}

	private void initView() {

		leftBtn = (Button) findViewById(R.id.btn_left);
		rightBtn = (Button) findViewById(R.id.btn_right);

		leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftBtn.setTextColor(getResources().getColor(
						color.select_btn_color));
				leftBtn.setBackgroundDrawable(getResources().getDrawable(drawable.title_select_left));
				rightBtn.setTextColor(getResources().getColor(
						color.no_select_btn_color));
				rightBtn.setBackgroundDrawable(getResources().getDrawable(drawable.title_right));
			}
		});

		rightBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftBtn.setTextColor(getResources().getColor(
						color.no_select_btn_color));
				leftBtn.setBackgroundDrawable(getResources().getDrawable(drawable.title_left));
				rightBtn.setTextColor(getResources().getColor(
						color.select_btn_color));
				rightBtn.setBackgroundDrawable(getResources().getDrawable(drawable.title_select_right));
			}
		});
	}
}
