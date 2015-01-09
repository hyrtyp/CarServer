package cn.com.hyrt.carserverseller.info.activity;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ZZVERTIFICATION_LIST;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.info.adapter.VerificationAdapter;

public class VerificationResultActivity extends BaseActivity {
	private TextView tv_zzstatus;
	private TextView tv_zztype;
	private TextView tv_nodata;
	private RatingBar item_rating;
	private RelativeLayout rl_newstzz;
	private LinearLayout ll_content;
	private LinearLayout history_layout;
	private LinearLayout history_content;
	private ImageView mArrow;
	private ListView lv_verification;
	private VerificationAdapter adapter;
	private Define.ZZVERTIFICATION_LIST verfitication;
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_verification_result);
		initView();
		loadData();
		setListener();
	}
	
	
	/**
	 * initView
	 */
	private void initView() {
		history_layout = (LinearLayout)findViewById(R.id.history_layout);
		history_content = (LinearLayout) findViewById(R.id.history_content);
		history_content.getLayoutParams().height = 0;
		mArrow = (ImageView)findViewById(R.id.safe_arrow);
		mArrow.setTag(false);
		lv_verification = (ListView)findViewById(R.id.lv_verification);
		//TODO
		rl_newstzz = (RelativeLayout)findViewById(R.id.rl_newstzz);
		ll_content = (LinearLayout)findViewById(R.id.ll_content);
		tv_nodata = (TextView)findViewById(R.id.tv_nodata);
		tv_zzstatus = (TextView)findViewById(R.id.tv_zzstatus);
		tv_zztype = (TextView)findViewById(R.id.tv_zztype1);
		item_rating = (RatingBar)findViewById(R.id.item_rating);
	}
	
	
	/**
	 * 点击监听
	 */
	private void setListener() {
		// 点击展开与闭合
		history_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.history_layout:
						expand();
						break;
					default:
						break;
				}
			}
		});
		
		// 点击查看详情
		rl_newstzz.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				{"typeid":"商家申请资质类型主键ID"}
				Intent intent = new Intent();
				intent.setClass(VerificationResultActivity.this, VerificationDetitalActivity.class);
				intent.putExtra("typeid", id);
				startActivity(intent);
			}
		});
	}
	
	
	/**
	 * 数据加载
	 */
	private void loadData() {
		AlertHelper.getInstance(VerificationResultActivity.this).showLoading(null);
		WebServiceHelper helper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.ZZVERTIFICATION_LIST>() {

					@Override
					public void onSuccess(ZZVERTIFICATION_LIST result) {
						AlertHelper.getInstance(VerificationResultActivity.this).dismissLoading();
						ll_content.setVisibility(View.VISIBLE);
						tv_nodata.setVisibility(View.GONE);
						// 取第一个
						verfitication = result;
						id = verfitication.data.get(0).id;
						if ("ysh".equals(verfitication.data.get(0).status)) {
							tv_zzstatus.setText("通过");
						}else{
							tv_zzstatus.setText("驳回");
						}
						tv_zztype.setText(verfitication.data.get(0).zztypename);
						if ("1".equals(verfitication.data.get(0).zztype)) {
							item_rating.setRating(4);
						}else{
							item_rating.setRating(5);
						}
						
						if (adapter == null) {
							adapter = new VerificationAdapter(VerificationResultActivity.this, verfitication.data,id);
							lv_verification.setAdapter(adapter);
						}else{
							adapter.notifyDataSetChanged();
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(VerificationResultActivity.this).dismissLoading();
						if (500==errorNo) {
							AlertHelper.getInstance(getApplicationContext()).showCenterToast("发生异常！");
						}
						ll_content.setVisibility(View.GONE);
						tv_nodata.setVisibility(View.VISIBLE);
					}
		},this);
		helper.getMerchantQualificList();
	}
	
	
	/**
	 * 形变
	 */
	protected void expand() {
		final LayoutParams params = history_content.getLayoutParams();
		int targetHeight;
		int height = history_content.getMeasuredHeight();
		boolean flag = (Boolean) mArrow.getTag();
		if (flag) {
			mArrow.setTag(false);
			targetHeight = 0;
		} else {
			mArrow.setTag(true);
			targetHeight = measureContentHeight();
		}

		ValueAnimator va = ValueAnimator.ofInt(height, targetHeight);
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator va) {
				params.height = (Integer) va.getAnimatedValue();
				history_content.setLayoutParams(params);
			}
		});
		
		
		va.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				boolean flag = (Boolean) mArrow.getTag();
				mArrow.setImageResource(flag ? R.drawable.arrow_up : R.drawable.arrow_down);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		va.setDuration(300);
		va.start();
	}
	
	
	/**
	 * 高度测量
	 */
	private int measureContentHeight() {
		int width = history_content.getMeasuredWidth();
		history_content.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
		history_content.measure(widthMeasureSpec, heightMeasureSpec);
		return history_content.getMeasuredHeight();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
}
