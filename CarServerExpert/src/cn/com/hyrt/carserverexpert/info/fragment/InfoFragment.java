package cn.com.hyrt.carserverexpert.info.fragment;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.activity.MainActivity;
import cn.com.hyrt.carserverexpert.base.application.CarServerApplication;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.info.activity.ChangePwdActivity;
import cn.com.hyrt.carserverexpert.info.activity.InfoDetailActivity;
import cn.com.hyrt.carserverexpert.info.activity.LoginActivity;
import cn.com.hyrt.carserverexpert.info.activity.MyIntegrationActivity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoFragment extends Fragment implements OnClickListener{
	
	private View rootView;
	private LinearLayout layoutInfo;
	private LinearLayout layoutIntegration;
	private LinearLayout layoutChangePwd;
	private LinearLayout layoutVersion;
	private Button btnLogOut;
	private TextView tvVersion;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		findView();
		setListener();
		try {
			String versionName = getActivity().getPackageManager()
			.getPackageInfo(getActivity().getPackageName(), 0)
			.versionName;
			tvVersion.setText("版本信息 v"+versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return rootView;
	}
	
	@Override
	public void onClick(View view) {
		int viewId = view.getId();
		Intent intent = new Intent();
		if(viewId == layoutInfo.getId()){
			Define.INFO_LOGIN loginInfo = ((CarServerApplication)getActivity()
					.getApplication()).getLoginInfo();
			LogHelper.i("tag", "loginInfo.status:"+loginInfo.status);
			if("wsh".equals(loginInfo.status)){
				AlertHelper.getInstance(getActivity()).showCenterToast("资料审核中，无法编辑");
				return;
			}
			intent.setClass(getActivity(), InfoDetailActivity.class);
		}else if(viewId == layoutIntegration.getId()){
			intent.setClass(getActivity(), MyIntegrationActivity.class);
		}else if(viewId == layoutChangePwd.getId()){
			intent.setClass(getActivity(), ChangePwdActivity.class);
		}else if(viewId == layoutVersion.getId()){
			return;
		}else if(viewId == btnLogOut.getId()){
			((CarServerApplication)getActivity().getApplication()).setLoginInfo(null);
			((MainActivity)getActivity()).mandatoryFinish();
			intent.setClass(getActivity(), LoginActivity.class);
		}
		startActivity(intent);
	}
	
	public void loadData(){
		
	}
	
	private void setListener(){
		layoutInfo.setOnClickListener(this);
		layoutIntegration.setOnClickListener(this);
		layoutChangePwd.setOnClickListener(this);
		layoutVersion.setOnClickListener(this);
		btnLogOut.setOnClickListener(this);
	}
	
	private void findView(){
		layoutInfo = (LinearLayout) rootView.findViewById(R.id.layout_info);
		layoutIntegration = (LinearLayout) rootView.findViewById(R.id.layout_integration);
		layoutChangePwd = (LinearLayout) rootView.findViewById(R.id.layout_changepwd);
		layoutVersion = (LinearLayout) rootView.findViewById(R.id.layout_version);
		btnLogOut = (Button) rootView.findViewById(R.id.btn_log_out);
		tvVersion = (TextView) rootView.findViewById(R.id.tv_version);
	}
}
