package cn.com.hyrt.carserverseller.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.activity.MainActivity;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;

public class ChangePwdActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_oldpwd) EditText etOldPwd;
	@ViewInject(id=R.id.et_newpwd) EditText etNewPwd;
	@ViewInject(id=R.id.et_rewpwd) EditText etRewPwd;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
	}
	
	public void submit(View view){
		String oldPwd = etOldPwd.getText().toString();
		String newPwd = etNewPwd.getText().toString();
		String rePwd = etRewPwd.getText().toString();
		if("".equals(oldPwd)){
			AlertHelper.getInstance(this).showCenterToast("请输入原密码");
			return;
		}
		if("".equals(newPwd)){
			AlertHelper.getInstance(this).showCenterToast("请输入新密码");
			return;
		}
		if("".equals(rePwd)){
			AlertHelper.getInstance(this).showCenterToast("请输入确认密码");
			return;
		}
		
		LogHelper.i("tag", "newPwd:"+newPwd+" rePwd:"+rePwd);
		if(newPwd.equals(rePwd)){
			changePwdHelper.changePwd(oldPwd, newPwd);
		}else{
			AlertHelper.getInstance(this)
			.showCenterToast("新密码与确认密码不符");
		}
	}
	
	private WebServiceHelper changePwdHelper = new WebServiceHelper(
			new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

				@Override
				public void onSuccess(BASE result) {
					AlertHelper.getInstance(ChangePwdActivity.this).showCenterToast("修改成功");
					finish();
					((MainActivity)MainActivity.getMe()).mandatoryFinish();
					((CarServerApplication)getApplication()).setLoginInfo(null);
					Intent intent = new Intent();
					intent.setClass(ChangePwdActivity.this, LoginActivity.class);
					startActivity(intent);
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					if(errorNo == 202){
						AlertHelper.getInstance(ChangePwdActivity.this).showCenterToast("密码错误");
						return;
					}
					AlertHelper.getInstance(ChangePwdActivity.this).showCenterToast("修改失败");
				}
	}, this);
}
