package cn.com.hyrt.carserver.emergency.activity;

import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.annotation.view.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.activity.MainActivity;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LocationHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.info.activity.ChangeInfoActivity;

public class SOSActivity extends BaseActivity{
	
	@ViewInject(id=R.id.et_content) EditText etContent;
	@ViewInject(id=R.id.iv_camera,click="camera") ImageView ivCamera;
	@ViewInject(id=R.id.iv_video,click="video") ImageView ivVideo;
	@ViewInject(id=R.id.iv_record,click="record") ImageView iv_record;
	@ViewInject(id=R.id.tv_record) TextView tv_record;
	@ViewInject(id=R.id.btn_save,click="commit") Button btnSave;
	
	private static final int VIDEO_CAPTURE = 101;
	
	private static final int STOP_RECORD = 201;
	
	private int recordTime = 10*1000;
	
	private Uri faceUri;
	
	private boolean isRecord = false;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case STOP_RECORD:
				isRecord = false;
				iv_record.setImageResource(R.drawable.ic_sos_record);
				iv_record.setImageResource(R.drawable.ic_sos_record);
				AlertHelper.getInstance(SOSActivity.this)
				.showCenterToast(R.string.send_success);
				finish();
				break;

			default:
				break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sos);
		setListener();
	}
	
	public void camera(View view){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("sos.jpg"));
		}
		PhotoHelper mPhotoHelper = new PhotoHelper(this, faceUri, 50);
		mPhotoHelper.getFromCamera();
	}
	
	public void video(View view){
//		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
//		startActivityForResult(intent, VIDEO_CAPTURE);

		Intent intent = new Intent();
		intent.setClass(this, RecordVideoActivity.class);
		startActivityForResult(intent, VIDEO_CAPTURE);
		
//		if(videoUri == null){
//			videoUri = Uri.fromFile(FileHelper.createFile("video.mp4"));
//		}
//		
//		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//		intent.putExtra(
//				android.provider.MediaStore.EXTRA_DURATION_LIMIT, 10);
//				startActivityForResult(intent, VIDEO_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
//		startActivityForResult(intent, VIDEO_CAPTURE);
	}
	
	public void record(View view){
		if(isRecord){
			isRecord = false;
			mHandler.removeMessages(STOP_RECORD);
			iv_record.setImageResource(R.drawable.ic_sos_record);
			iv_record.setImageResource(R.drawable.ic_sos_record);
			AlertHelper.getInstance(SOSActivity.this)
			.showCenterToast(R.string.send_success);
			finish();
		}else{
			Message msg = new Message();
			msg.what = STOP_RECORD;
			mHandler.sendMessageDelayed(msg, recordTime);
			iv_record.setImageResource(R.drawable.ic_recording);
			tv_record.setText(R.string.eme_recording);
			isRecord = true;
		}
	}
	
	public void commit(View view){
		String content = etContent.getText().toString();
		if(content == null || "".equals(content)){
			AlertHelper.getInstance(this).showCenterToast(R.string.eme_sos_content_nodata);
			return;
		}
		AlertHelper.getInstance(this).showCenterToast(R.string.send_success);
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
            if (data.getParcelableExtra("data") != null) {
            	AlertHelper.getInstance(this).showCenterToast(R.string.send_success);
        		finish();
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
//            if(mPhotoHelper == null){
//                if(faceUri == null){
//                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
//                }
//                mPhotoHelper = new PhotoHelper(ChangeInfoActivity.this, faceUri, 50);
//            }
//            mPhotoHelper.startPhotoZoom(faceUri, 50);
        	AlertHelper.getInstance(this).showCenterToast(R.string.send_success);
    		finish();
        }else if(VIDEO_CAPTURE == resultCode){
        	AlertHelper.getInstance(this).showCenterToast(R.string.send_success);
    		finish();
        }
	}
	
	private String beforeText;
	private void setListener(){
		etContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence text, int start,
					int lengthBefore, int lengthAfter) {
				String content = etContent.getText().toString();
				if (content.length() > 200) {
					AlertHelper.getInstance(SOSActivity.this)
							.showCenterToast(R.string.text_count_beyond);
					if (beforeText != null) {
						etContent.setText(beforeText);
						etContent.setSelection(start);
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
	
	@SuppressLint("NewApi")
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add("map").setIcon(R.drawable.ic_map)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		String title = item.getTitle().toString();
		if("map".equals(title)){
			LocationHelper.jumpBaiduMap(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
