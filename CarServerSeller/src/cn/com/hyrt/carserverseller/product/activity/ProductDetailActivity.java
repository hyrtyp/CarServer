package cn.com.hyrt.carserverseller.product.activity;

import android.os.Bundle;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;

public class ProductDetailActivity extends BaseActivity{

	private boolean isProduct = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isProduct){
			setTitle("商品详情");
		}else{
			setTitle("服务详情");
		}
		setContentView(R.layout.activity_product_detail);
	}
}
