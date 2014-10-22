package cn.com.hyrt.carserverseller.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;

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
		
		
	}
	
	public void goback(View view){
		LogHelper.i("tag", "goback");
		finish();
	}
}
