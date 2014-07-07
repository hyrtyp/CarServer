package cn.com.hyrt.carserver.question.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.widget.ListView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

/**
 * 按品牌找
 * @author zoe
 *
 */
public class FindByBrandActivity extends BaseActivity{
	
	@ViewInject(id=R.id.lv_brand) ListView lvBrand;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_by_brand);
		loadData();
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
			}
		}, this);
		mWebServiceHelper.getBrands();
	}
}
