package cn.com.hyrt.carserverseller.info.activity;


import com.lidroid.xutils.BitmapUtils;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ZZVERTIFICATION_DETITAL_LIST;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;

/**
 * 申请详细
 * @author zoe
 *
 */
public class VerificationDetitalActivity extends BaseActivity{
	
	@ViewInject(id=R.id.iv_photo11) ImageView ivphoto1;
	@ViewInject(id=R.id.iv_photo22) ImageView ivPhoto2;
//	@ViewInject(id=R.id.iv_photo2) ImageView ivPhoto2;
//	@ViewInject(id=R.id.iv_photo1) ImageView iv_photo1;
	@ViewInject(id=R.id.tv_zztypename) TextView tv_zztypename;
	@ViewInject(id=R.id.tv_zzlevelname) TextView tv_zzlevelname;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	private String typeid;
	private BitmapUtils bitmapUtils;
	private int position = 0;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_veritification_detidal);
		Intent intent = getIntent();
		typeid = intent.getStringExtra("typeid");
		bitmapUtils = new BitmapUtils(this);
		loadData();
		setListener();
	}
	
	private void loadData(){
		AlertHelper.getInstance(VerificationDetitalActivity.this).showLoading(null);
		new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.ZZVERTIFICATION_DETITAL_LIST>() {

					@Override
					public void onSuccess(ZZVERTIFICATION_DETITAL_LIST result) {
						AlertHelper.getInstance(VerificationDetitalActivity.this).dismissLoading();
						if (!"".equals(result.data.get(0).imagepath0)) {
							bitmapUtils.display(ivphoto1, result.data.get(0).imagepath0);
						}
						if (!"".equals(result.data.get(0).imagepath1)) {
							bitmapUtils.display(ivPhoto2, result.data.get(0).imagepath1);
						}
						tv_zztypename.setText(result.data.get(0).zztypename);
						tv_zzlevelname.setText(result.data.get(0).zzlevelname);
						if ("ysh".equals(result.data.get(0).status)) {
							btnSubmit.setVisibility(View.GONE);
						}else{
							btnSubmit.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(VerificationDetitalActivity.this).dismissLoading();
					}
				}, this).getMerchantQualificInfo(typeid);
	}
	
	private void setListener(){
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(VerificationDetitalActivity.this, VerificationActivity.class);
				startActivity(intent);
			}
		});
	}
}
