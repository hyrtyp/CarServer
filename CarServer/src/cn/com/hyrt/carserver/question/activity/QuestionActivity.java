package cn.com.hyrt.carserver.question.activity;

import java.io.File;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;

public class QuestionActivity extends BaseActivity {

	private RelativeLayout rl_teamNotifi;
	private RelativeLayout ll_teamNotifi;
	private Button mButton;

	private PhotoHelper mPhotoHelper;

	private int mobile_width = 0;
	private int mobile_height = 0;

	// private Uri mUri;

	private static final String IMGURL = "/CarServer/photo";
	public static final int FROM_CAMERA = 2;
	public static final int PHOTO_ZOOM = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mobile_width = metric.widthPixels; // 屏幕宽度（像素）
		mobile_height = metric.heightPixels; // 屏幕高度（像素）

		initView();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		Bundle extras = getIntent().getExtras();

		switch (requestCode) {
		case FROM_CAMERA:

			if (extras != null) {

				String output = extras.getString(MediaStore.EXTRA_OUTPUT);
				Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
			}

			break;

		case PHOTO_ZOOM:
			extras = intent.getExtras();
			if (extras != null) {

				String output = extras.getString("outputFormat");
				Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
			}

			break;

		default:
			break;
		}
	}

	private void initView() {

		rl_teamNotifi = (RelativeLayout) findViewById(R.id.rl_teamNotifi);
		ll_teamNotifi = (RelativeLayout) findViewById(R.id.rl_sysNotifi);
		mButton = (Button) findViewById(R.id.btn_commit);

		final Intent intent = new Intent();

		rl_teamNotifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				intent.setClass(QuestionActivity.this, PositionActivity.class);
				startActivity(intent);

			}
		});

		ll_teamNotifi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// intent.setClass(QuestionActivity.this,
				// UpphotoActivity.class);
				// startActivity(intent);

				mPhotoHelper = new PhotoHelper(QuestionActivity.this, getUri(),
						mobile_width, mobile_height);
				mPhotoHelper.getPhoto();

			}
		});

		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.setClass(QuestionActivity.this, CommitActivity.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 获取照片保存路径
	 * 
	 * @return
	 */
	public Uri getUri() {

		String path = Environment.getExternalStorageDirectory().toString()
				+ IMGURL;
		File mFile = new File(path);
		if (!mFile.exists()) {

			mFile.mkdirs();
		}
		File file = new File(mFile, System.currentTimeMillis() + ".jpg");
		Uri uri = Uri.fromFile(file);

		return uri;
	}

}
