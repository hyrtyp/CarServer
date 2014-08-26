package cn.com.hyrt.carserversurvey.info.activity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.MainActivity;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.INFO_LOGIN;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.StorageHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;

public class LoginActivity extends FinalActivity{
	
	@ViewInject(id=R.id.tv_username) TextView tvUsername;
	@ViewInject(id=R.id.tv_pwd) TextView tvPwd;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	
	private int focusIndex = -1;//焦点坐标 0：用户名；2：密码
	private boolean isFirstFocus = true;
	
	private int loginFailCount = 0;
	private long LimitTime = 1000*60*3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setListener();
	}
	
	public void submit(View view){
		long failTime = StorageHelper.getInstance(this).getLoginFailTime();
		if(failTime != -1 && System.currentTimeMillis()-failTime <= LimitTime){
			AlertHelper.getInstance(LoginActivity.this).showCenterToast(R.string.login_fail2);
			return;
		}else if(loginFailCount >= 3){
			StorageHelper.getInstance(this).saveLoginFailTime(System.currentTimeMillis());
			loginFailCount = 0;
			AlertHelper.getInstance(LoginActivity.this).showCenterToast(R.string.login_fail2);
			return;
		}
		String username = tvUsername.getText().toString();
		String pwd = tvPwd.getText().toString();
		
		if("".equals(username.trim())){
			AlertHelper.getInstance(this).showCenterToast(R.string.no_username);
			return;
		}else if("".equals(pwd.trim())){
			AlertHelper.getInstance(this).showCenterToast(R.string.no_pwd);
			return;
		}
		
		AlertHelper.getInstance(LoginActivity.this).showLoading(getString(R.string.login_msg));
		WebServiceHelper loginWebServiceHelper = new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.INFO_LOGIN>() {

			@Override
			public void onSuccess(INFO_LOGIN result) {
				AlertHelper.getInstance(LoginActivity.this).hideLoading();
				if(result != null){
					StorageHelper.getInstance(LoginActivity.this).saveLoginFailTime(-1);
					loginFailCount = 0;
					
					((CarServerApplication)getApplicationContext()).setLoginInfo(result);
					Intent intent = new Intent();
					intent.setClass(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					AlertHelper.getInstance(LoginActivity.this).showCenterToast(R.string.login_succuss);
				}else{
					loginFailCount++;
					AlertHelper.getInstance(LoginActivity.this).showCenterToast(R.string.login_fail);
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				loginFailCount++;
				AlertHelper.getInstance(LoginActivity.this).hideLoading();
				AlertHelper.getInstance(LoginActivity.this).showCenterToast(R.string.login_fail);
			}
		}, this);
		loginWebServiceHelper.login(username, pwd);
	}
	
	private void setListener(){
		tvUsername.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(focusIndex != 0){
					focusIndex = 0;
					tvUsername.setTextColor(getResources().getColor(R.color.login_blue));
					tvUsername.setBackgroundResource(R.drawable.bg_textview_white_focus);
					tvUsername.setHint("");
					
					tvPwd.setBackgroundResource(R.drawable.bg_textview_white);
					tvPwd.setTextColor(getResources().getColor(android.R.color.white));
					if("".equals(tvPwd.getText().toString().trim())){
						tvPwd.setHint(getString(R.string.pwd_hint));
					}else{
						tvPwd.setHint(tvPwd.getText());
					}
					
				}
			}
		});
		
		tvPwd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(focusIndex != 1){
					focusIndex = 1;
					tvPwd.setTextColor(getResources().getColor(R.color.login_blue));
					tvPwd.setBackgroundResource(R.drawable.bg_textview_white_focus);
					tvPwd.setHint("");
					
					tvUsername.setBackgroundResource(R.drawable.bg_textview_white);
					tvUsername.setTextColor(getResources().getColor(android.R.color.white));
					if("".equals(tvUsername.getText().toString().trim())){
						tvUsername.setHint(getString(R.string.pwd_hint));
					}else{
						tvUsername.setHint(tvUsername.getText());
					}
					
				}
			}
		});
		
		tvUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(isFirstFocus){
					isFirstFocus = false;
					return;
				}
				if(focusIndex != 0 && hasFocus){
					focusIndex = 0;
					tvUsername.setTextColor(getResources().getColor(R.color.login_blue));
					tvUsername.setBackgroundResource(R.drawable.bg_textview_white_focus);
					tvUsername.setHint("");
					
					tvPwd.setBackgroundResource(R.drawable.bg_textview_white);
					tvPwd.setTextColor(getResources().getColor(android.R.color.white));
					if("".equals(tvPwd.getText().toString().trim())){
						tvPwd.setHint(getString(R.string.pwd_hint));
					}else{
						tvPwd.setHint(tvPwd.getText());
					}
					
				}
			}
		});
		
		tvPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View arg0, boolean hasFocus) {
				if(isFirstFocus){
					isFirstFocus = false;
					return;
				}
				if(focusIndex != 1 && hasFocus){
					focusIndex = 1;
					tvPwd.setTextColor(getResources().getColor(R.color.login_blue));
					tvPwd.setBackgroundResource(R.drawable.bg_textview_white_focus);
					tvPwd.setHint("");
					
					tvUsername.setBackgroundResource(R.drawable.bg_textview_white);
					tvUsername.setTextColor(getResources().getColor(android.R.color.white));
					if("".equals(tvUsername.getText().toString().trim())){
						tvUsername.setHint(getString(R.string.username_hint));
					}else{
						tvUsername.setHint(tvUsername.getText());
					}
					
				}
			}
		});
	}
	
	@Override
	protected void onStop() {
		AlertHelper.getInstance(LoginActivity.this).hideLoading();
		super.onStop();
	}
}