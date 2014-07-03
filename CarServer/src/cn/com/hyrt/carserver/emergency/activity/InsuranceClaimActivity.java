package cn.com.hyrt.carserver.emergency.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.FullListView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.info.adapter.MyCarAdapter;

public class InsuranceClaimActivity extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_inclaim);
		
	}

}
