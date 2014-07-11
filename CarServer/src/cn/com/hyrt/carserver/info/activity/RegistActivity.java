package cn.com.hyrt.carserver.info.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.activity.MainActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_SAVE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class RegistActivity extends FinalActivity{
	
	@ViewInject(id=R.id.login_userphone) EditText userName;
	@ViewInject(id=R.id.login_pwd) EditText uPwd;
	@ViewInject(id=R.id.btn_regist,click="regist") Button btnLogin;
	@ViewInject(id=R.id.btn_loginback,click="back") Button backLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_regist);
	}
	
	public void regist(View view){
		
		INFO_SAVE info  = new INFO_SAVE();
		
		String user = userName.getText().toString();
		String password = uPwd.getText().toString();
		
		if("".equals(user)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_username_is_null);
				return;
		}else{
			StringHelper isphone = new StringHelper();
		    if(!isphone.isMobileNum(user)){
		    	AlertHelper.getInstance(this).showCenterToast(R.string.regist_phone);
		    	return;
		    }else{
		    	info.loginname = user;
		    }
			
		} 
		if("".equals(password)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_confirmpwd_is_null);
				return;
		}else{
			info.password = password;
			
		}
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						if(result != null){
							AlertHelper.getInstance(RegistActivity.this).showCenterToast(getString(R.string.login_registsuccess));
						}else{
							AlertHelper.getInstance(RegistActivity.this).showCenterToast(getString(R.string.login_registfailure));
						}
					}
					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(RegistActivity.this).showCenterToast(errorMsg);
					}
					
		}, this);
		mWebServiceHelper.saveUserInfo(info);
	}
	public void back(View view){
		Intent intent = new Intent();
		intent.setClass(this, LoginActivity.class);
		startActivity(intent);
	}
}
