package cn.com.hyrt.carserversurvey.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.activity.BaseActivity;

public class InfoDetailActivity extends BaseActivity{
	
	@ViewInject(id=R.id.btn_change_photo,click="changePhoto") Button btnChangeFace;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infodetail);
	}
	
	public void changePhoto(View view){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(this, faceUri, 50);
		mPhotoHelper.getPhoto();
	}
}
