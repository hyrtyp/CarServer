package cn.com.hyrt.carserversurvey.shop.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.shop.fragment.ProductListFragment;

/**
 * 店铺首页
 * @author zoe
 *
 */
public class ShopActivity extends BaseActivity{
	
	private ProductListFragment spListFragment;
	private ProductListFragment serviceListFragment;
	@ViewInject(id=R.id.tv_to_product,click="toProduct") TextView tvToProduct;
	@ViewInject(id=R.id.tv_to_service,click="toService") TextView tvToService;
	
	private boolean isSp = true;
	private String shId = "";
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_shop);
		shId = getIntent().getStringExtra("shId");
		spListFragment = new ProductListFragment(true, shId);
		getSupportFragmentManager().beginTransaction()
		.add(R.id.content, spListFragment).commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(isFirst){
			isFirst = false;
			return;
		}
		if(spListFragment != null){
			spListFragment.loadData(false);
		}
		if(serviceListFragment != null){
			serviceListFragment.loadData(false);
		}
	}
	
	public void toProduct(View view){
		LogHelper.i("tag", "toProduct");
		if(!isSp){
			tvToProduct.setTextColor(0xffffffff);
			tvToProduct.setBackgroundResource(R.drawable.bg_top_left_focus);
			tvToService.setTextColor(getResources().getColor(R.color.login_blue));
			tvToService.setBackgroundResource(R.drawable.bg_top_right);
			isSp = true;
			if(spListFragment == null){
				spListFragment = new ProductListFragment(true, shId);
				getSupportFragmentManager().beginTransaction()
				.add(R.id.content, spListFragment).commit();
			}else{
				getSupportFragmentManager().beginTransaction()
				.show(spListFragment).commit();
			}
			if(serviceListFragment != null){
				getSupportFragmentManager().beginTransaction()
				.hide(serviceListFragment).commit();
			}
		}
	}
	
	public void toService(View view){
		LogHelper.i("tag", "toService");
		if(isSp){
			tvToService.setTextColor(0xffffffff);
			tvToService.setBackgroundResource(R.drawable.bg_top_right_focus);
			tvToProduct.setTextColor(getResources().getColor(R.color.login_blue));
			tvToProduct.setBackgroundResource(R.drawable.bg_top_left);
			isSp = false;
			if(serviceListFragment == null){
				serviceListFragment = new ProductListFragment(false, shId);
				getSupportFragmentManager().beginTransaction()
				.add(R.id.content, serviceListFragment).commit();
			}else{
				getSupportFragmentManager().beginTransaction()
				.show(serviceListFragment).commit();
			}
			if(spListFragment != null){
				getSupportFragmentManager().beginTransaction()
				.hide(spListFragment).commit();
			}
		}
	}
	
}
