package cn.com.hyrt.carserverexpert.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.activity.MainActivity;
import cn.com.hyrt.carserverexpert.base.application.CarServerApplication;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.SAVE_INFO_RESULT;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;

public class RegistActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_phonenum) EditText etPhoneNum;
	@ViewInject(id=R.id.et_pwd) EditText etPwd;
	@ViewInject(id=R.id.et_repwd) EditText etRePwd;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
	}
	
	public void submit(View view){
		Define.SAVE_INFO info = new Define.SAVE_INFO();
		String phoneNum = etPhoneNum.getText().toString();
		String pwd = etPwd.getText().toString();
		String rePwd = etRePwd.getText().toString();
		
		if(phoneNum == null || "".equals(phoneNum.trim())){
			AlertHelper.getInstance(this).showCenterToast("手机号不能为空");
			return;
		}else if(pwd == null || "".equals(pwd.trim())){
			AlertHelper.getInstance(this).showCenterToast("密码不能为空");
			return;
		}else if(rePwd == null || "".equals(rePwd.trim())){
			AlertHelper.getInstance(this).showCenterToast("确认密码不能为空");
			return;
		}else if(!pwd.equals(rePwd)){
			AlertHelper.getInstance(this).showCenterToast("密码与确认密码不符");
			return;
		}
		info.loginname = phoneNum;
		info.password = pwd;
		AlertHelper.getInstance(this).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.SAVE_INFO_RESULT>() {

			@Override
			public void onSuccess(SAVE_INFO_RESULT result) {
				AlertHelper.getInstance(RegistActivity.this).hideLoading();
				Define.INFO_LOGIN loginInfo = new Define.INFO_LOGIN();
				loginInfo.id = result.id;
				loginInfo.loginname = result.loginname;
				loginInfo.name = result.name;
				Intent intent = new Intent();
				intent.setClass(RegistActivity.this, MainActivity.class);
				startActivity(intent);
				((CarServerApplication)getApplicationContext()).setLoginInfo(loginInfo);
				((LoginActivity)LoginActivity.getMeContext()).finish();
				finish();
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(RegistActivity.this).hideLoading();
				AlertHelper.getInstance(RegistActivity.this).showCenterToast(errorMsg);
			}
		}, this).saveUserInfo(info);
	}
}
