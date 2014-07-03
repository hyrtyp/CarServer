package cn.com.hyrt.carserver.info.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.view.TableView;

public class CarConditionDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.tableview) TableView tableview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_condition_detail);
		Intent intent = getIntent();
		setTitle(intent.getStringExtra("title"));
		List<String> titles = intent.getStringArrayListExtra("titles");
		List<String> contents = intent.getStringArrayListExtra("contents");
		
		tableview.setData(titles, contents);
	}
}
