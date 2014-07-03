package cn.com.hyrt.carserver.emergency.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INSURANCE_CLAIM_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class InsuranceClaimActivity extends BaseActivity{
	
	@ViewInject(id=R.id.iv_ic_img) ImageView icimg;
	@ViewInject(id=R.id.tv_bx_content) TextView bxcontent;
	@ViewInject(id=R.id.tv_bx_tel) TextView bxtel;
	
	private WebServiceHelper mWebServiceHelper;
	private Define.INSURANCE_CLAIM_LIST icm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergency_inclaim);
		loadData();
	}
	
	private void loadData(){
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		if(mWebServiceHelper == null){
			mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INSURANCE_CLAIM_LIST>() {

						@Override
						public void onSuccess(INSURANCE_CLAIM_LIST result) {
							LogHelper.i("tag", "result:"+result.data.size());
							AlertHelper.getInstance(InsuranceClaimActivity.this).hideLoading();					
							icm = result;					
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(InsuranceClaimActivity.this).hideLoading();
						}
			}, this);
		}
		mWebServiceHelper.getInsuranceClaim();
	}

}
