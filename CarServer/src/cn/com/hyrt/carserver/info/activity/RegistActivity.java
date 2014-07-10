package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.activity.MainActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class RegistActivity extends FinalActivity{
	
	@ViewInject(id=R.id.et_username) EditText etUserName;
	@ViewInject(id=R.id.et_email) EditText etEmail;
	@ViewInject(id=R.id.et_pwd) EditText etPwd;
	@ViewInject(id=R.id.btn_regist,click="regist") Button btnLogin;
	@ViewInject(id=R.id.btn_loginback,click="back") Button backLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
//		showBackButton(false);
	}
	
	public void regist(View view){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO_LOGIN>() {

					@Override
					public void onSuccess(INFO_LOGIN result) {
						if(result != null){
							AlertHelper.getInstance(RegistActivity.this).showCenterToast(getString(R.string.login_loginsuccess));
							CarServerApplication.loginInfo = result;
							StorageHelper.getInstance(RegistActivity.this).saveLoginInfo(result);
							getUserInfo();
						}else{
							AlertHelper.getInstance(RegistActivity.this).showCenterToast(getString(R.string.login_loginfailure));
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(RegistActivity.this).showCenterToast(errorMsg);
					}
					
		}, this);
		mWebServiceHelper.login(etUserName.getText().toString(), etPwd.getText().toString());
	}
	
	private void getUserInfo(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO>() {

					@Override
					public void onSuccess(INFO result) {
						CarServerApplication.info = result;
						Intent intent = new Intent();
						intent.setClass(RegistActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
						
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						// TODO Auto-generated method stub
						
					}
		}, this);
		mWebServiceHelper.getUserInfo();
	}
	public void back(View view){
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
	}
}
