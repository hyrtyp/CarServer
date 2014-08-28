package cn.com.hyrt.carserversurvey.info.fragment;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.MainActivity;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.SAVE_INFO;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
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
import android.widget.TextView;

public class InfoFragment extends Fragment{
	
	private View rootView;
	private ImageLoaderView userphoto;
	private TextView usercode;
	private TextView regrecode;
	private TextView curlogin;
    private Button btn_editpassword;
    private Button btn_regrecode;
    private Button btn_loginout;
	private LinearLayout layout_info;
	public Object CarServerApplication  ;
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		findView();
		setListener();
		editListener();
		recodeListener();
		loginoutListener();
		loadData();
		return rootView;
	}
	
	private void findView(){
		btn_editpassword = (Button) rootView.findViewById(R.id.btn_editpassword);
		btn_regrecode = (Button) rootView.findViewById(R.id.btn_regrecode);
		btn_loginout = (Button) rootView.findViewById(R.id.btn_loginout);
		layout_info = (LinearLayout) rootView.findViewById(R.id.layout_info);
		userphoto = (ImageLoaderView) rootView.findViewById(R.id.iv_face_img);
		usercode = (TextView)rootView.findViewById(R.id.usercode);
		regrecode= (TextView)rootView.findViewById(R.id.regrecode);
		curlogin= (TextView)rootView.findViewById(R.id.curlogin);
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
				AlertHelper.getInstance(getActivity()).showCenterToast(R.string.logout_succuss);
				Intent intent = new Intent();
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
				((CarServerApplication)getActivity().getApplicationContext()).setLoginInfo(null);
				if(MainActivity.meContext != null){
					MainActivity.needFinish = true;
					MainActivity.meContext.finish();
				}
			}
		});
	}
	
	public void loadData(){
		String id = ((CarServerApplication)getActivity().getApplicationContext()).mLoginInfo.id;
		final String username = ((CarServerApplication)getActivity().getApplicationContext()).mLoginInfo.loginname;
		AlertHelper.getInstance(getActivity()).showLoading(null);
		
		WebServiceHelper mUserInfoWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.SAVE_INFO>() {

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(getActivity()).hideLoading();
			}

			@Override
			public void onSuccess(SAVE_INFO result) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				userphoto.setImageUrl(result.imagepath);
				usercode.setText(String.format(getString(R.string.usercode), username));
				regrecode.setText(String.format(getString(R.string.regrecode), result.mercount+"条"));
				curlogin.setText(String.format(getString(R.string.curlogin),result.lasttime));
			}
		}, getActivity());
		mUserInfoWebServiceHelper.getUserInfoImage(id);
	}

}
