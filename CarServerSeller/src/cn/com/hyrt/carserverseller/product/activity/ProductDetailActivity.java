package cn.com.hyrt.carserverseller.product.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_PRODUCT_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

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
	
	private Define.INFO_PRODUCT_LIST.CDATA productInfo;
	private String productId;
	
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
		loadData();
//		initView();
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
		tvShYj.setText(productInfo.shyj);
		tvShSj.setText(StringHelper.formatDate(productInfo.shbhtime));
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
}
