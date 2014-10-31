package cn.com.hyrt.carserverseller.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;

public class RegistActivity extends BaseActivity{
	
	@ViewInject(id=R.id.btn_back,click="goback") Button btnBack;
	@ViewInject(id=R.id.et_phonenum) EditText etPhoneNum;
	@ViewInject(id=R.id.btn_getcode,click="getCode") Button btnGetCode;
	@ViewInject(id=R.id.et_pwd) EditText etPwd;
	@ViewInject(id=R.id.et_repwd) EditText etRePwd;
	@ViewInject(id=R.id.et_code) EditText etCode;
	@ViewInject(id=R.id.btn_submit,click="onSubmit") Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
	}
	
	public void getCode(View view){
		LogHelper.i("tag", "getCode	");
	}
	
	public void onSubmit(View view){
		String phoneNnum = etPhoneNum.getText().toString();
		String pwd = etPwd.getText().toString();
		String rePwd = etRePwd.getText().toString();
//		String code = etCode.getText().toString();
		
		if(!StringHelper.isMobileNum(phoneNnum)){
			AlertHelper.getInstance(this).showCenterToast("请填入正确的手机号");
			return;
		}
		if(pwd == null || "".equals(pwd)){
			AlertHelper.getInstance(this).showCenterToast("请填入密码");
			return;
		}else if(rePwd == null || "".equals(rePwd)){
			AlertHelper.getInstance(this).showCenterToast("请填入确认密码");
			return;
		}else if(!rePwd.equals(pwd)){
			AlertHelper.getInstance(this).showCenterToast("确认密码与密码不符");
			return;
		}
		
		WebServiceHelper mRegistHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.SINGLE_ID>() {

					@Override
					public void onSuccess(Define.SINGLE_ID result) {
						AlertHelper.getInstance(RegistActivity.this).hideLoading();
						Define.INFO_LOGIN loginInfo = new Define.INFO_LOGIN();
						loginInfo.id = result.id;
						loginInfo.loginname = etPhoneNum.getText().toString();
						loginInfo.bcxx = "0";
						((CarServerApplication)getApplication())
						.setLoginInfo(loginInfo);
						Intent intent = new Intent();
						intent.setClass(RegistActivity.this, MerchantInfoActivity.class);
						startActivity(intent);
						finish();
						((LoginActivity)LoginActivity.getMeContext()).finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(RegistActivity.this).hideLoading();
						if(errorNo == 206){
							AlertHelper.getInstance(RegistActivity.this)
							.showCenterToast("用户名已存在！");
						}else{
							AlertHelper.getInstance(RegistActivity.this)
							.showCenterToast("注册失败！");
						}
					}
		}, this);
		AlertHelper.getInstance(RegistActivity.this).showLoading(null);
		mRegistHelper.regist(phoneNnum, pwd);
	}
	
	public void goback(View view){
		LogHelper.i("tag", "goback");
		finish();
	}
}
