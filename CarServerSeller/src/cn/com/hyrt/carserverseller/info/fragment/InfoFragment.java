package cn.com.hyrt.carserverseller.info.fragment;

import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.MainActivity;
import cn.com.hyrt.carserverseller.base.application.CarServerApplication;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_MERCHANT;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;
import cn.com.hyrt.carserverseller.info.activity.AboutActivity;
import cn.com.hyrt.carserverseller.info.activity.AuditListActivity;
import cn.com.hyrt.carserverseller.info.activity.ChangePwdActivity;
import cn.com.hyrt.carserverseller.info.activity.ChangeShopActivity;
import cn.com.hyrt.carserverseller.info.activity.InfoActivity;
import cn.com.hyrt.carserverseller.info.activity.LoginActivity;
import cn.com.hyrt.carserverseller.info.activity.VerificationActivity;
import cn.com.hyrt.carserverseller.info.activity.VersionInfoActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoFragment extends Fragment implements View.OnClickListener{
	
	private View rootView;
	private LinearLayout layoutVersion;
	private RelativeLayout layoutFace;
	private LinearLayout layoutAlterPwd;
	private LinearLayout layoutVerification;
	private LinearLayout layoutAbout;
	private LinearLayout layoutAuditList;
	private ImageLoaderView ivFace;
	private TextView tvPhonenum;
	private Button btnLogout;
	
	private INFO_MERCHANT mData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_info, null);
		findView();
		setListener();
		loadData();
		return rootView;
	}
	
	private void loadData(){
		WebServiceHelper mGetInfoHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_MERCHANT>() {

					@Override
					public void onSuccess(INFO_MERCHANT result) {
						mData = result;
						ivFace.setImageUrl(result.data.get(0).imagepath);
						tvPhonenum.setText(result.data.get(0).phonenum);
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						
					}
				}, getActivity());
		mGetInfoHelper.getMerchantInfo();
	}
	
	private void setListener(){
		layoutVersion.setOnClickListener(this);
		layoutFace.setOnClickListener(this);
		layoutAlterPwd.setOnClickListener(this);
		layoutVerification.setOnClickListener(this);
		layoutAbout.setOnClickListener(this);
		btnLogout.setOnClickListener(this);
		layoutAuditList.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		int viewId = view.getId();
		if(viewId == layoutVersion.getId()){
			Intent intent = new Intent();
			intent.setClass(getActivity(), VersionInfoActivity.class);
			startActivity(intent);
		}else if(viewId == layoutFace.getId()){
			if(mData == null){
				AlertHelper.getInstance(getActivity())
				.showCenterToast("商户信息获取失败");
				return;
			}
			Intent intent = new Intent();
			intent.setClass(getActivity(), InfoActivity.class);
			intent.putExtra("vo", mData);
			startActivity(intent);
		}else if(viewId == layoutAlterPwd.getId()){
			if(mData == null){
				AlertHelper.getInstance(getActivity())
				.showCenterToast("商户信息获取失败");
				return;
			}
			Intent intent = new Intent();
			intent.setClass(getActivity(), ChangePwdActivity.class);
			startActivity(intent);
		}else if(viewId == layoutVerification.getId()){
			if(mData == null){
				AlertHelper.getInstance(getActivity())
				.showCenterToast("商户信息获取失败");
				return;
			}
			Intent intent = new Intent();
			intent.setClass(getActivity(), VerificationActivity.class);
			startActivity(intent);
		}else if(viewId == layoutAbout.getId()){
			Intent intent = new Intent();
			intent.setClass(getActivity(), AboutActivity.class);
			startActivity(intent);
		}else if(viewId == btnLogout.getId()){
			((CarServerApplication)getActivity().getApplication())
			.setLoginInfo(null);
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			startActivity(intent);
			((MainActivity)getActivity()).mandatoryFinish();
		}else if(viewId == layoutAuditList.getId()){
			if(mData == null){
				AlertHelper.getInstance(getActivity())
				.showCenterToast("商户信息获取失败");
				return;
			}
			Intent intent = new Intent();
			intent.setClass(getActivity(), AuditListActivity.class);
			startActivity(intent);
		}
	}
	
	public void findView(){
		layoutVersion = (LinearLayout) rootView.findViewById(R.id.layout_version);
		layoutFace = (RelativeLayout) rootView.findViewById(R.id.layout_face);
		layoutAlterPwd = (LinearLayout) rootView.findViewById(R.id.layout_alter_pwd);
		layoutVerification = (LinearLayout) rootView.findViewById(R.id.layout_verification);
		layoutAbout = (LinearLayout) rootView.findViewById(R.id.layout_about);
		ivFace = (ImageLoaderView) rootView.findViewById(R.id.iv_face);
		tvPhonenum = (TextView) rootView.findViewById(R.id.tv_phonenum);
		btnLogout = (Button) rootView.findViewById(R.id.btn_logout);
		layoutAuditList = (LinearLayout) rootView.findViewById(R.id.layout_verification_list);
	}

}
