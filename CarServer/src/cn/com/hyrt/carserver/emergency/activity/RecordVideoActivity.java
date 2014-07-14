package cn.com.hyrt.carserver.emergency.activity;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;

public class RecordVideoActivity extends FinalActivity implements SurfaceHolder.Callback{
	
	@ViewInject(id=R.id.sv_video) SurfaceView surfaceView; 
	@ViewInject(id=R.id.btn_stop,click="stop") Button btn_stop;
	
	private MediaRecorder mediarecorder;// 录制视频的类  
	private SurfaceHolder surfaceHolder;
	
	private boolean recording = false;
	
	Timer mTimer;
	
	private int recordTime = 10*1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_video);
		init();
	}
	
	private void init() {  
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this); // holder加入回调接口  
        // setType必须设置，要不出错.  
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);  
    }
	
	public void start(){
		if(recording){
			return;
		}
		
		AlertHelper.getInstance(this).showCenterToast(R.string.eme_record_video_start);
		
		mTimer = new Timer();
		mTimer.schedule(mTimerTask, recordTime);
		
		recording = true;
		mediarecorder = new MediaRecorder();// 创建mediarecorder对象  
        // 设置录制视频源为Camera(相机)  
        mediarecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);  
        // 设置录制完成后视频的封装格式THREE_GPP为3gp.MPEG_4为mp4  
        mediarecorder  
                .setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  
        // 设置录制的视频编码h263 h264  
        mediarecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);  
        // 设置视频录制的分辨率。必须放在设置编码和格式的后面，否则报错  
        mediarecorder.setVideoSize(176, 144);  
        // 设置录制的视频帧率。必须放在设置编码和格式的后面，否则报错  
        mediarecorder.setVideoFrameRate(20);  
        mediarecorder.setPreviewDisplay(surfaceHolder.getSurface());  
        // 设置视频文件输出的路径  
        
        File mFile = FileHelper.createFile("video.mp4");
        mediarecorder.setOutputFile(mFile.getAbsolutePath());  
        try {  
            // 准备录制  
            mediarecorder.prepare();  
            // 开始录制  
            mediarecorder.start();  
        } catch (IllegalStateException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
	}
	
	public void stop(View view){
		if(!recording){
			return;
		}
		recording = false;
		if (mediarecorder != null) {  
            // 停止录制  
            mediarecorder.stop();  
            // 释放资源  
            mediarecorder.release();  
            mediarecorder = null;  
        }
		setResult(101);
		finish();
	}
	
	private TimerTask mTimerTask = new TimerTask() {
		
		@Override
		public void run() {
			stop(null);
		}
	};
	
	
	@Override
	public void finish() {
		if(recording){
			recording = false;
			if (mediarecorder != null) {  
	            // 停止录制  
	            mediarecorder.stop();  
	            // 释放资源  
	            mediarecorder.release();  
	            mediarecorder = null;  
	        }
		}
		
		super.finish();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,  
            int height) {
		surfaceHolder = holder;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		surfaceHolder = holder;
		start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		surfaceView = null;  
	        surfaceHolder = null;  
	        mediarecorder = null;  
	}  
	
	@Override
	protected void onStop() {
		stop(null);
		super.onStop();
	}
	
}
