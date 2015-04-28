package cn.com.hyrt.carserver.question.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.ZJTJUSERINFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.ZJUSERMAIN_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;

public class ExpertDetitalActivity extends BaseActivity {
	private TextView tv_car_expert_name ;
	private ImageLoaderView iv_car_expert_face;
	private TextView tv_car_expert_zhuanchang ;
	private TextView tv_car_expert_level ;
	private TextView tv_car_expert_brands ;
	private TextView tv_car_expert_jianjie ;
	private Button bt_ask ;
	private RatingBar rb_car_expert_score;
	private Define.ZJTJUSERINFO mData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expert_detital);
		initView();
		loadData();
		setListener();
	}

	private void setListener() {
		//向某一个具体的推荐专家提问
		bt_ask.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ExpertDetitalActivity.this, QuestionActivity.class);
				intent.putExtra("zjid", mData.id);
				startActivity(intent);
			}
		});
	}

	private void initView() {
		tv_car_expert_name = (TextView) findViewById(R.id.tv_car_expert_name);
		iv_car_expert_face = (ImageLoaderView) findViewById(R.id.iv_car_expert_face);
		tv_car_expert_zhuanchang = (TextView) findViewById(R.id.tv_car_expert_zhuanchang);
		tv_car_expert_level = (TextView) findViewById(R.id.tv_car_expert_level);
		tv_car_expert_brands = (TextView) findViewById(R.id.tv_car_expert_brands);
		tv_car_expert_jianjie = (TextView) findViewById(R.id.tv_car_expert_jianjie);
		bt_ask = (Button) findViewById(R.id.bt_ask);
		rb_car_expert_score = (RatingBar) findViewById(R.id.rb_car_expert_score);
	}

	private void loadData() {
		AlertHelper.getInstance(ExpertDetitalActivity.this).showLoading(null);
		WebServiceHelper webServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<ZJTJUSERINFO>() {

			@Override
			public void onSuccess(ZJTJUSERINFO result) {
				AlertHelper.getInstance(ExpertDetitalActivity.this).hideLoading();
				mData = result ;
//				tv_car_expert_name.setText("专家名称："+result.name); 
				tv_car_expert_name.setText(Html.fromHtml("<font color='#222222'><big>"+result.name+"</big></font>"));
				iv_car_expert_face.setImageUrl(result.imagepath);
				tv_car_expert_zhuanchang.setText(Html.fromHtml("<font color='#222222'>专家专长：</font>")+result.zcnames);
				tv_car_expert_level.setText(Html.fromHtml("<font color='#222222'>专家级别：</font>")+result.level);
				tv_car_expert_brands.setText(Html.fromHtml("<font color='#222222'>专修品牌：</font>")+result.brandnames);
				tv_car_expert_jianjie.setText(Html.fromHtml("<font color='#222222'>专家简介：</font>")+result.zjarea);
				rb_car_expert_score.setIsIndicator(true);
				float f ;
				if ("".equals(result.serviceattitude) || result.serviceattitude == null) {
					f = 0;
				}else{
					f = Float.parseFloat(result.serviceattitude);
				}
				rb_car_expert_score.setRating(f);
				
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(ExpertDetitalActivity.this).hideLoading();
			}
		}, ExpertDetitalActivity.this);
		
		webServiceHelper.getZJTJUserinfo(getIntent().getStringExtra("id"));
	}
}
