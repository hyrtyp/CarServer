package cn.com.hyrt.carserverseller.product.activity;

import java.util.regex.Pattern;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_PRODUCT_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;
import cn.com.hyrt.carserverseller.product.adapter.SHYJAdapter;

public class ProductDetailActivity extends BaseActivity{

	private boolean isProduct = true;
	@ViewInject(id=R.id.tv_title) TextView tvTitle;
	@ViewInject(id=R.id.iv_photo) ImageLoaderView ivPhoto;
	@ViewInject(id=R.id.tv_price) TextView tvPrice;
	@ViewInject(id=R.id.tv_preferential) TextView tvPreferential;
	@ViewInject(id=R.id.btn_change) Button btnChange;
	@ViewInject(id=R.id.tv_desc) TextView tvDesc;
	@ViewInject(id=R.id.iv_face) ImageLoaderView ivFace;
	@ViewInject(id=R.id.tv_user_name) TextView tvUserName;
	@ViewInject(id=R.id.tv_time) TextView tvTime;
	@ViewInject(id=R.id.btn_close,click="close") Button btnClose;
	@ViewInject(id=R.id.btn_edit,click="edit") Button btnEdit;
	@ViewInject(id=R.id.tv_shyj) TextView tvShYj;
	@ViewInject(id=R.id.tv_shsj) TextView tvShSj;
	
	@ViewInject(id=R.id.history_layout) LinearLayout history_layout;
	@ViewInject(id=R.id.history_content) LinearLayout history_content;
	@ViewInject(id=R.id.safe_arrow1) ImageView safe_arrow;
	@ViewInject(id=R.id.lv_lssh) ListView lv_lssh;  // 历史审核条目 
	@ViewInject(id=R.id.tv_nodatad) TextView tv_nodatad;  // 暂无数据
	@ViewInject(id=R.id.tv_shlsts1) TextView tv_shlsts1;  // 历史审核条数
	private SHYJAdapter shyjAdapter;
	@ViewInject(id=R.id.sv) ScrollView sv;
	
	private Define.INFO_PRODUCT_LIST.CDATA productInfo;
	private String productId;
	private String[] shyjs;
	private String[] shbhtimes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
//		productInfo = (CDATA) intent.getSerializableExtra("vo");
		productId = intent.getStringExtra("productId");
		isProduct = intent.getBooleanExtra("isProduct", true);
		if(isProduct){
			setTitle("商品详情");
		}else{
			setTitle("服务详情");
		}
		setContentView(R.layout.activity_product_detail);
		history_content.getLayoutParams().height = 0;
		safe_arrow.setTag(false);
		loadData();
		
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
				//  scrollview 与listview 滚动事件冲突解决
				lv_lssh.setOnTouchListener(new View.OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if(event.getAction() == MotionEvent.ACTION_UP){  
		                    sv.requestDisallowInterceptTouchEvent(false);  
		                }else{  
		                    sv.requestDisallowInterceptTouchEvent(true);  
		                }  
		                return false;  
					}
				}); 
	}
	
	private void loadData(){
		WebServiceHelper mGetProductHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_PRODUCT_LIST.CDATA>() {

					@Override
					public void onSuccess(CDATA result) {
						productInfo = result;
						AlertHelper.getInstance(ProductDetailActivity.this).hideLoading();
						initView();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(ProductDetailActivity.this).hideLoading();
						AlertHelper.getInstance(ProductDetailActivity.this).showCenterToast("信息获取失败");
						finish();
					}
		}, ProductDetailActivity.this);
		AlertHelper.getInstance(ProductDetailActivity.this).showLoading(null);
		mGetProductHelper.getProductInfo(productId);
	}
	
	public void close(View view){
		AlertHelper.getInstance(ProductDetailActivity.this).showLoading(null);
		WebServiceHelper mProductCloseHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						AlertHelper.getInstance(ProductDetailActivity.this).hideLoading();
						AlertHelper.getInstance(ProductDetailActivity.this).showCenterToast("下架成功");
						setResult(101);
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(ProductDetailActivity.this).hideLoading();
						AlertHelper.getInstance(ProductDetailActivity.this).showCenterToast("下架失败");
					}
		}, this);
		mProductCloseHelper.closeProduct(productInfo.id);
	}
	
	public void edit(View view){
		Intent intent = new Intent();
		intent.setClass(this, ProductActivity.class);
		intent.putExtra("isProduct", isProduct);
		intent.putExtra("vo", productInfo);
		startActivityForResult(intent, 101);
	}
	
	private void initView(){
		if("yxj".equals(productInfo.status)){
			btnClose.setVisibility(View.GONE);
		}else{
			btnClose.setVisibility(View.VISIBLE);
		}
		tvTitle.setText(productInfo.spname);
		tvDesc.setText(productInfo.sptitle);
		tvPrice.setText("￥"+productInfo.price);
		tvPreferential.setText("￥"+productInfo.discount);
		ivPhoto.setImageUrl(productInfo.imagepath0);
		tvTime.setText("   "+StringHelper.formatDate(productInfo.fbtime));
		//分割字符串
		String pattern=";";  
		Pattern pat=Pattern.compile(pattern);  
		shyjs = pat.split(productInfo.shyj);
		shbhtimes = pat.split(productInfo.shbhtime); 
		if (shbhtimes.length<=1) {
			tv_nodatad.setVisibility(View.VISIBLE);
			lv_lssh.setVisibility(View.GONE);
		}else{
			tv_nodatad.setVisibility(View.GONE);
			lv_lssh.setVisibility(View.VISIBLE);
		}
		tv_shlsts1.setText("共 "+shbhtimes.length+" 次");
		tvShYj.setText(shyjs[0]);
		tvShSj.setText(StringHelper.formatDate(shbhtimes[0]));
		if (shyjAdapter == null) {
			shyjAdapter = new SHYJAdapter(ProductDetailActivity.this, shyjs,shbhtimes);
			lv_lssh.setAdapter(shyjAdapter);
		}else{
			shyjAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == 101){
//			setResult(101);
//			finish();
			loadData();
		}
	}
	
	@Override
	public void finish() {
		setResult(101);
		super.finish();
	}
	
	/**
	 * 形变
	 */
	protected void expand() {
		final LayoutParams params = history_content.getLayoutParams();
		int targetHeight;
		int height = history_content.getMeasuredHeight();
		boolean flag = (Boolean) safe_arrow.getTag();
		if (flag) {
			safe_arrow.setTag(false);
			targetHeight = 0;
		} else {
			safe_arrow.setTag(true);
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
				boolean flag = (Boolean) safe_arrow.getTag();
				safe_arrow.setImageResource(flag ? R.drawable.arrow_up : R.drawable.arrow_down);
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

}
