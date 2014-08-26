package cn.com.hyrt.carserversurvey.info.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import cn.com.hyrt.carserversurvey.R;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

public class EditPasswordActivity  extends FinalActivity{
	
	@ViewInject(id=R.id.tv_username) TextView tvUsername;
	@ViewInject(id=R.id.tv_pwd) TextView tvPwd;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	
	private int focusIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

}
