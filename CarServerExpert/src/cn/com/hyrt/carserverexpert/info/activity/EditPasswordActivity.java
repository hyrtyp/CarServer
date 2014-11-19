package cn.com.hyrt.carserverexpert.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import cn.com.hyrt.carserverexpert.base.application.CarServerApplication;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.SAVE_INFO;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.BaseActivity;
import cn.com.hyrt.carserverexpert.base.activity.MainActivity;

public class EditPasswordActivity  extends BaseActivity{
	
	@ViewInject(id=R.id.et_old_pwd) EditText etOldPwd;
	@ViewInject(id=R.id.et_new_pwd) EditText etNewPwd;
	@ViewInject(id=R.id.et_confirm_pwd) EditText etConfirmPwd;
	@ViewInject(id=R.id.button_commit,click="submit") Button btnSubmit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editpassword);
		
		setListener();
	}
	private String beforeText;
	private void setListener() {
		etOldPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etOldPwd.getText().toString();
				if (content.length() > 32) {
					AlertHelper.getInstance(EditPasswordActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etOldPwd.setText(beforeText);
						etOldPwd.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});

		etNewPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etNewPwd.getText().toString();
				if (content.length() > 32) {
					AlertHelper.getInstance(EditPasswordActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etNewPwd.setText(beforeText);
						etNewPwd.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});

		etConfirmPwd.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etConfirmPwd.getText().toString();
				if (content.length() > 32) {
					AlertHelper.getInstance(EditPasswordActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etConfirmPwd.setText(beforeText);
						etConfirmPwd.setSelection(start);
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				if (beforeText == null) {
					beforeText = text.toString();
				}

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});
	}
	public void submit(View view){
		
		/*SAVE_INFO info  = new SAVE_INFO();
		info.id = ((CarServerApplication)getApplicationContext()).getLoginInfo().id;
		
		String oldPwd = etOldPwd.getText().toString();
		String newPwd = etNewPwd.getText().toString();
		String confirmPwd = etConfirmPwd.getText().toString();
		
		if(!"".equals(oldPwd) || !"".equals(newPwd) || !"".equals(confirmPwd)){
			if("".equals(oldPwd)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_oldpwd_is_null);
				return;
			}else if("".equals(newPwd)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_newpwd_is_null);
				return;
			}else if("".equals(confirmPwd)){
				AlertHelper.getInstance(this).showCenterToast(R.string.info_confirmpwd_is_null);
				return;
			}else{
				if(!newPwd.equals(confirmPwd)){
					AlertHelper.getInstance(this).showCenterToast(R.string.info_confirmpwd_not_tally);
					return;
				}else{
					info.newpassword = newPwd;
					info.password = oldPwd;
				}
			}
		}
		
		WebServiceHelper mWebServiceHelper = 
				new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						LogHelper.i("tag", "result:"+result.message);
						AlertHelper.getInstance(EditPasswordActivity.this).showCenterToast(R.string.info_change_success);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(), LoginActivity.class);
						startActivity(intent);
						((CarServerApplication)getApplicationContext()).setLoginInfo(null);
						if(MainActivity.meContext != null){
							MainActivity.needFinish = true;
							MainActivity.meContext.finish();
						}
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						LogHelper.i("tag", "onFailure:"+errorMsg);
						AlertHelper.getInstance(EditPasswordActivity.this).showCenterToast(errorMsg);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						finish();
					}
		}, this);
		mWebServiceHelper.saveUserInfo(info);*/
		
	}

}
