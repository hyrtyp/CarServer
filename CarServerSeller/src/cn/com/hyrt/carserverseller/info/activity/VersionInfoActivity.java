package cn.com.hyrt.carserverseller.info.activity;

import java.io.File;
import java.util.List;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.VS_INFO_LIST;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.VS_INFO_LIST.VS_INFO;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.RoundProgressBar;

public class VersionInfoActivity extends BaseActivity{
	@ViewInject(id=R.id.bt_updateversion,click="update") Button btnUpdate;
	@ViewInject(id=R.id.roundProgressBar) RoundProgressBar roundProgressBar;
	@ViewInject(id=R.id.ll_loading) LinearLayout ll_loading;
	@ViewInject(id=R.id.ll_upload) LinearLayout ll_upload;
	private List<VS_INFO_LIST.VS_INFO> mVsInfos;
	private VS_INFO_LIST.VS_INFO mVsInfo;
	private String apkurl = "http://192.168.13.124:8080/CarServerSeller.apk";
	private String versionClassi;
	private int maxLength = 0;
	private int progress = 0;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				ll_loading.setVisibility(View.VISIBLE);
				ll_upload.setVisibility(View.GONE);
				//roundProgressBar.setMax(maxLength);
				roundProgressBar.setProgress(progress);
				break;

			case 1:
				ll_loading.setVisibility(View.GONE);
				ll_upload.setVisibility(View.VISIBLE);
				AlertHelper.getInstance(getApplicationContext()).showCenterToast("下载完成");
				break;
			}
		};
	};
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_info);
		Intent intent = getIntent();
		versionClassi = intent.getStringExtra("vc");
	}
	
	public void update(View view){/*
		WebServiceHelper VersionInfoListHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.VS_INFO_LIST>() {
					@Override
					public void onSuccess(VS_INFO_LIST result) {
						mVsInfos.clear();
						mVsInfos.addAll(result.vsdata);
						for (VS_INFO mVsIf : mVsInfos) {
							mVsInfo = mVsIf;
							if (mVsInfo.versionClassi != null && !"".equals(mVsInfo.versionClassi) && mVsInfo.versionClassi == versionClassi) {
								if (mVsInfo.versionNum != null && mVsInfo.versionNum == getVersion()) {
									updateVersion(mVsInfo.versionPkName, mVsInfo.apkurl);
								}else{
									AlertHelper.getInstance(getApplicationContext()).showCenterToast("已是最新版本");
								}
							}else{
								AlertHelper.getInstance(getApplicationContext()).showCenterToast("不存在商家的版本");
							}
						}
					}
					@Override
					public void onFailure(int errorNo, String errorMsg) {
						switch (errorNo) {
						case 202:
							AlertHelper.getInstance(VersionInfoActivity.this).hideLoading();
							AlertHelper.getInstance(VersionInfoActivity.this).showCenterToast("获取版本信息失败");
							break;

						default:
							break;
						}
					}
				}, this);
		VersionInfoListHelper.getVersionInfoList();*/
		updateVersion();
	}
	//public void updateVersion(String packName,final String apkurl){
	public void updateVersion(){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("新版本");
		builder.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("立刻升级", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (FileHelper.sdCardExist()) {
					// 下载APK 覆盖安装
					FinalHttp finalHttp = new FinalHttp();
					finalHttp.download(apkurl, Environment.getExternalStorageDirectory()
							+ "/CarServerSeller.apk", 
							new AjaxCallBack<File>() {
						@Override
						public void onFailure(Throwable t, int errorNo,
								String strMsg) {
							Toast.makeText(getApplicationContext(),
									"下载失败", 1).show();
							t.printStackTrace();
							super.onFailure(t, errorNo, strMsg);
						}

						@Override
						public void onLoading(long count, long current) {
							Message msg = Message.obtain();
							progress = (int) (current * 100 / count);
							msg.what = 0;
							handler.sendMessage(msg);
							super.onLoading(count, current);
						}

						@Override
						public void onSuccess(File t) {
							Message msg1 = Message.obtain();
							msg1.what = 1;
							handler.sendMessage(msg1);
							installApk(t);
							super.onSuccess(t);
						}

						// 安装应用
						private void installApk(File t) {
							Intent intent = new Intent();
							intent.setAction("android.intent.action.VIEW");
							intent.addCategory("android.intent.category.DEFAULT");
							intent.setDataAndType(Uri.fromFile(t),
									"application/vnd.android.package-archive");
							startActivity(intent);
						}
					});
				}else{
					AlertHelper.getInstance(getApplicationContext()).showCenterToast("sd卡不存在");
				}
			}
		});
		builder.setPositiveButton("下次再说", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();

	}
	/*
	 * 获取版本信息
	 */
	private String getVersion() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo packInfo = pm.getPackageInfo(getPackageName(), 0);
			return packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
}
