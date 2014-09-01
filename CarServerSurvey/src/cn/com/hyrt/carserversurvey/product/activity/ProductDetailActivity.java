package cn.com.hyrt.carserversurvey.product.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.INFO_PRODUCT;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.StringHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import cn.com.hyrt.carserversurvey.shop.activity.ShopActivity;
import cn.com.hyrt.carserversurvey.shop.fragment.ShopFragment;

public class ProductDetailActivity extends BaseActivity{
	
	@ViewInject(id=R.id.iv_user_img) ImageLoaderView iv_user_img;
	@ViewInject(id=R.id.tv_pro_title) TextView tv_pro_title;
	@ViewInject(id=R.id.tv_pro_data) TextView tv_pro_data;
	@ViewInject(id=R.id.tv_pro_price) TextView tv_pro_price;
	@ViewInject(id=R.id.tv_pro_discountprice) TextView tv_pro_discountprice;
	@ViewInject(id=R.id.tv_pro_content) TextView tv_pro_content;
	@ViewInject(id=R.id.btn_addgono,click="add") Button btn_addgono;
	@ViewInject(id=R.id.btn_checkinfo,click="find") Button btn_checkinfo;
	
	INFO_PRODUCT productInfo;
	
	private String id;
	private String shId = "";
	private boolean isFromAdd = true;
	private boolean isSp = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spdetail);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		shId = intent.getStringExtra("shId");
		isFromAdd = intent.getBooleanExtra("isFromAdd", true);
		isSp = intent.getBooleanExtra("isSp", true);
		if(isSp){
			setTitle("商品详情");
		}else{
			setTitle("服务详情");
		}
		if(isFromAdd){
			btn_addgono.setText("继续添加");
			btn_checkinfo.setText("查看上架");
		}else{
			btn_addgono.setText("下架商品");
			btn_checkinfo.setText("编辑商品");
		}
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}
	
	private void loadData(){
		WebServiceHelper infoWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_PRODUCT>() {

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(ProductDetailActivity.this).showCenterToast(errorMsg);
						
					}

					@Override
					public void onSuccess(INFO_PRODUCT result) {
						productInfo = result;
						tv_pro_title.setText(result.spname);
						String fbdta = StringHelper.formatDate(result.fbtime);
						tv_pro_data.setText(String.format(getString(R.string.spfbtime), fbdta));
						tv_pro_price.setText(String.format(getString(R.string.pro_price), result.price));
						tv_pro_discountprice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
						tv_pro_discountprice.setText(String.format(getString(R.string.pro_discount), result.discount));
						tv_pro_content.setText(result.sptitle);
						iv_user_img.setImageUrl(result.imagepath0);
					}
		}, this);
		infoWebServiceHelper.getMerchantComm(id);
	}
	public void add(View view){
		if(isFromAdd){
			Intent intent = new Intent();
			intent.putExtra("shId", shId);
			intent.setClass(this, ProductActivity.class);
			startActivity(intent);
		}else{
			AlertHelper.getInstance(ProductDetailActivity.this).showLoading(null);
			//下架商品
			WebServiceHelper xjSpWebServiceHelper =new WebServiceHelper(
					new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

						@Override
						public void onSuccess(BASE result) {
							AlertHelper.getInstance(ProductDetailActivity.this).hideLoading();
							AlertHelper.getInstance(ProductDetailActivity.this).showCenterToast("下架成功");
							finish();
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(ProductDetailActivity.this).hideLoading();
							AlertHelper.getInstance(ProductDetailActivity.this).showCenterToast("下架失败");
						}
			}, this);
			xjSpWebServiceHelper.saveDelMerchantCommStatus(productInfo.id, shId);
		}
	}
	public void find(View view){
		Intent intent = new Intent();
		boolean needFinish = false;
		if(isFromAdd){
//			intent.setClass(this, ShopFragment.class);
			intent.setClass(this, ShopActivity.class);
			intent.putExtra("id", id);
			intent.putExtra("shId", shId);
			needFinish = true;
		}else{
			needFinish = false;
			intent.setClass(this, ProductActivity.class);
			intent.putExtra("shId", shId);
			intent.putExtra("isAdd", false);
			intent.putExtra("vo", productInfo);
		}
		startActivity(intent);
		if(needFinish){
			finish();
		}
	}
}
