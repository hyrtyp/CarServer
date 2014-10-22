package cn.com.hyrt.carserverseller.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_MERCHANT;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

public class InfoActivity extends BaseActivity{

	@ViewInject(id=R.id.iv_face) ImageLoaderView ivFace;
	@ViewInject(id=R.id.tv_phonenum) TextView tvPhoneNum;
	@ViewInject(id=R.id.layout_zzphoto) LinearLayout layoutZzPhoto;
	@ViewInject(id=R.id.iv_zzphoto) ImageLoaderView ivZzPhoto;
	@ViewInject(id=R.id.line_zzphoto) LinearLayout lineZzPhoto;
	@ViewInject(id=R.id.tv_fwclass) TextView tvFwClass;
	@ViewInject(id=R.id.layout_brand) LinearLayout layoutBrand;
	@ViewInject(id=R.id.tv_brand) TextView tvBrand;
	@ViewInject(id=R.id.line_brand) LinearLayout lineBrand;
	@ViewInject(id=R.id.tv_address) TextView tvAddress;
	@ViewInject(id=R.id.layout_contact) LinearLayout layoutContact;
	@ViewInject(id=R.id.tv_contact) TextView tvContact;
	@ViewInject(id=R.id.line_contact) LinearLayout lineContact;
	@ViewInject(id=R.id.layout_telnum) LinearLayout layoutTelNum;
	@ViewInject(id=R.id.tv_telnum) TextView tvTelNum;
	@ViewInject(id=R.id.line_telnum) LinearLayout lineTelNum;
	@ViewInject(id=R.id.tv_desc) TextView tvDesc;
	@ViewInject(id=R.id.btn_change,click="changeInfo") Button btnChange;
	
	private Define.INFO_MERCHANT mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		mData = (INFO_MERCHANT) intent.getSerializableExtra("vo");
		setContentView(R.layout.activity_info);
		initView();
	}
	
	private void initView(){
		Define.INFO_MERCHANT.CDATA merchantInfo = mData.data.get(0);
		ivFace.setImageUrl(merchantInfo.imagepath);
		tvPhoneNum.setText(merchantInfo.phonenum);
		if(merchantInfo.zzimagepath == null 
				|| "".equals(merchantInfo.zzimagepath.trim())){
			layoutZzPhoto.setVisibility(View.GONE);
			lineZzPhoto.setVisibility(View.GONE);
		}else{
			layoutZzPhoto.setVisibility(View.VISIBLE);
			lineZzPhoto.setVisibility(View.VISIBLE);
			ivZzPhoto.setImageUrl(merchantInfo.zzimagepath);
		}
		tvFwClass.setText(merchantInfo.serviceTypename);
		if(merchantInfo.brandname == null 
				|| "".equals(merchantInfo.brandname.trim())){
			layoutBrand.setVisibility(View.GONE);
			lineBrand.setVisibility(View.GONE);
		}else{
			layoutBrand.setVisibility(View.VISIBLE);
			lineBrand.setVisibility(View.VISIBLE);
			tvBrand.setText(merchantInfo.brandname);
		}
		tvAddress.setText(merchantInfo.areaname);
		if(merchantInfo.sjmanager == null
				|| "".equals(merchantInfo.sjmanager.trim())){
			layoutContact.setVisibility(View.GONE);
			lineContact.setVisibility(View.GONE);
		}else{
			layoutContact.setVisibility(View.VISIBLE);
			lineContact.setVisibility(View.VISIBLE);
			tvContact.setText(merchantInfo.sjmanager);
		}
		if(merchantInfo.sjtel == null
				|| "".equals(merchantInfo.sjtel.trim())){
			layoutTelNum.setVisibility(View.GONE);
			lineTelNum.setVisibility(View.GONE);
		}else{
			layoutTelNum.setVisibility(View.VISIBLE);
			lineTelNum.setVisibility(View.VISIBLE);
			tvTelNum.setText(merchantInfo.sjtel);
		}
		tvDesc.setText(merchantInfo.desc);
		
	}
	
	public void changeInfo(View view){
		Intent intent = new Intent();
		intent.setClass(this, MerchantInfoActivity.class);
		intent.putExtra("vo", mData);
		startActivity(intent);
	}
}
