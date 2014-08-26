package cn.com.hyrt.carserversurvey.info.fragment;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.info.activity.EditPasswordActivity;
import cn.com.hyrt.carserversurvey.info.activity.InfoDetailActivity;
import cn.com.hyrt.carserversurvey.info.activity.LoginActivity;
import cn.com.hyrt.carserversurvey.info.activity.RegRecodeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class InfoFragment extends Fragment{
	
	private View rootView;
    private Button btn_editpassword;
    private Button btn_regrecode;
    private Button btn_loginout;
	private LinearLayout layout_info;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		findView();
		setListener();
		editListener();
		recodeListener();
		loginoutListener();
		return rootView;
	}
	
	private void findView(){
		btn_editpassword = (Button) rootView.findViewById(R.id.btn_editpassword);
		btn_regrecode = (Button) rootView.findViewById(R.id.btn_regrecode);
		btn_loginout = (Button) rootView.findViewById(R.id.btn_loginout);
		layout_info = (LinearLayout) rootView.findViewById(R.id.layout_info);
	}
	private void setListener(){
		layout_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), InfoDetailActivity.class);
				startActivity(intent);
				//startActivityForResult(intent, Define.RESULT_FROM_CHANGE_INFO);
			}
		});
	}
	//修改密码
	private void editListener(){
		btn_editpassword.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), EditPasswordActivity.class);
				startActivity(intent);
			}
		});
	}
	//注册记录
	private void recodeListener(){
		btn_regrecode.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), RegRecodeActivity.class);
				startActivity(intent);
			}
		});
	}
	//退出登录
	private void loginoutListener(){
		btn_loginout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
				//System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
				AlertHelper.getInstance(getActivity()).showCenterToast(R.string.logout_succuss);
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
	}

}
