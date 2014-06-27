package cn.com.hyrt.carserver.info.activity;

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
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class LoginActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_username) EditText etUserName;
	@ViewInject(id=R.id.et_pwd) EditText etPwd;
	@ViewInject(id=R.id.btn_login,click="login") Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		showBackButton(false);
	}
	
	public void login(View view){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO_LOGIN>() {

					@Override
					public void onSuccess(INFO_LOGIN result) {
						if(result != null){
							AlertHelper.getInstance(LoginActivity.this).showCenterToast("登录成功");
							StorageHelper.getInstance(LoginActivity.this).saveLoginInfo(result);
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						}else{
							AlertHelper.getInstance(LoginActivity.this).showCenterToast("登录失败");
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(LoginActivity.this).showCenterToast("登录失败");
					}
					
		}, this);
		mWebServiceHelper.login(etUserName.getText().toString(), etPwd.getText().toString());
	}
}
