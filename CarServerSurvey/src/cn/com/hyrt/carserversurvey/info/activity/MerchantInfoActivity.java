package cn.com.hyrt.carserversurvey.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.INFO_MERCHANT;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import cn.com.hyrt.carserversurvey.product.activity.ProductActivity;
import cn.com.hyrt.carserversurvey.regist.activity.RegistMerchantInfoActivity;
import cn.com.hyrt.carserversurvey.shop.activity.ShopActivity;

/**
 * 商户信息
 * @author zoe
 *
 */
public class MerchantInfoActivity extends BaseActivity{

	private String curInfoId = "";
	
	@ViewInject(id=R.id.tv_fullname) TextView tvFullname;
	@ViewInject(id=R.id.tv_signlename) TextView tvSignlename;
	@ViewInject(id=R.id.tv_username) TextView tvUsername;
	@ViewInject(id=R.id.tv_fwclass) TextView tvFwclass;
	@ViewInject(id=R.id.iv_photo) ImageLoaderView ivPhoto;
	@ViewInject(id=R.id.btn_change_info,click="changeInfo") Button btn_change_info;
	@ViewInject(id=R.id.btn_add_product,click="addProduct") Button btn_add_product;
	@ViewInject(id=R.id.btn_go_shop,click="goShop") Button btnGoShop;
	
	private INFO_MERCHANT.CDATA mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_merchantinfo);
		curInfoId = getIntent().getStringExtra("id");
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}
	
	private void loadData(){
		WebServiceHelper infoWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_MERCHANT>() {

					@Override
					public void onSuccess(INFO_MERCHANT result) {
						mData = result.data.get(0);
						tvFullname.setText(mData.sjname);
						tvSignlename.setText(mData.sjjc);
						tvUsername.setText(mData.loginname);
						ivPhoto.setImageUrl(mData.attacpath);
						tvFwclass.setText(mData.serviceTypename);
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						
					}
		}, this);
		infoWebServiceHelper.getMerchantInfo(curInfoId);
	}
	
	public void changeInfo(View view){
		if(mData == null){
			AlertHelper.getInstance(this).showCenterToast("用户信息获取失败");
			return;
		}
		Intent intent = new Intent();
		intent.setClass(this, RegistMerchantInfoActivity.class);
		intent.putExtra("vo", mData);
		startActivity(intent);
	}
	
	public void addProduct(View view){
		Intent intent = new Intent();
		intent.setClass(this, ProductActivity.class);
		intent.putExtra("shId", "1");
		startActivity(intent);
	}
	
	public void goShop(View view){
		Intent intent = new Intent();
		intent.setClass(this, ShopActivity.class);
		intent.putExtra("id", mData.id);
		intent.putExtra("shId", "1");
		startActivity(intent);
	}
}
