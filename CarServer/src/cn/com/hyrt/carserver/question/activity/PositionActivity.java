package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_CORRELATION;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_POISTION;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.question.adapter.CorrelationAdapter;
import cn.com.hyrt.carserver.question.adapter.PositionAdapter;

/**
 * 选择部位
 * @author Administrator
 *
 */
public class PositionActivity extends Activity{
	
//	private 
	RelativeLayout rl10;
	
	private Context context;
	private TextView name;
	private PositionAdapter  positionAdapter;
	private WebServiceHelper mWebServiceHelper;
	
	private List<Define.QUESTION_POISTION.CDATA> data = new ArrayList<Define.QUESTION_POISTION.CDATA>();
	
	private ListView ls_correlation;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_question_position);
		AlertHelper.getInstance(this).showLoading(null);
		context =PositionActivity.this;
		rl10=(RelativeLayout) findViewById(R.id.rl10);
		name = (TextView) findViewById(R.id.tv_name);
		rl10.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		loadData();
	}
	private void loadData()
	{
		WebServiceHelper mCarInfoServiceHelper = new WebServiceHelper(
			new WebServiceHelper.RequestCallback<Define.QUESTION_POISTION>() 
			{
				@SuppressWarnings("unused")
				@Override
				public void onSuccess(QUESTION_POISTION result)
				{
					AlertHelper.getInstance(PositionActivity.this).hideLoading();
					if(result == null){
						AlertHelper.getInstance(PositionActivity.this).showCenterToast(R.string.info_load_fail);
						setResult(Define.RESULT_FROM_ALTER_CAR);
						finish();
					}
					else
					{
						if(result == null && data.size()>0){
							AlertHelper.getInstance(PositionActivity.this).showCenterToast(R.string.load_done);
						}
						
						data.clear();
						data.addAll(result.data);

						positionAdapter = new PositionAdapter(context,data);
						ls_correlation.setAdapter(positionAdapter);;
					}
				}

				@Override
				public void onFailure(int errorNo, String errorMsg)
				{
					AlertHelper.getInstance(PositionActivity.this).hideLoading();
					AlertHelper.getInstance(PositionActivity.this).showCenterToast(R.string.info_load_fail);
					setResult(Define.RESULT_FROM_ALTER_CAR);
					finish();
				}
		}, this);

		mCarInfoServiceHelper.getPoisition();
	}
}
