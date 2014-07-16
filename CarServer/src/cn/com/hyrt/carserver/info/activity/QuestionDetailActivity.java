package cn.com.hyrt.carserver.info.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.kobjects.base64.Base64;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.FullListView;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView.OnHeaderRefreshListener;
import cn.com.hyrt.carserver.info.adapter.QuestionDetailAdapter;
import cn.com.hyrt.carserver.question.activity.CommentExpertActivity;

public class QuestionDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_replys)
	public FullListView lv_replys;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.tv_prompt_one) TextView tvPromptOne;
	@ViewInject(id=R.id.tv_prompt_two) TextView tvPromptTwo;
	@ViewInject(id=R.id.iv_photo,click="uploadPhoto") ImageLoaderView ivPhoto;
	@ViewInject(id=R.id.et_content) EditText etContent;
	@ViewInject(id=R.id.btn_reply,click="reply") Button btnReply;
	@ViewInject(id=R.id.layout_reply) LinearLayout layoutReply;
	@ViewInject(id=R.id.tv_prompt_three) TextView tvPromptThree;
	
	private List<Define.REPLY_DETAIL.CDATA> datas 
	= new ArrayList<Define.REPLY_DETAIL.CDATA>();
	
	private int type;
	private String replyId;
	private QuestionDetailAdapter mQuestionAdapter;
	
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private String imgBuffer;
	private String imageName = "photo.jpg";
	
	public static final int TYPE_NEW = 0;
	public static final int TYPE_HISTORY = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_detail);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", QuestionActivity.TYPE_NEW);
		replyId = intent.getStringExtra("replyId");
		ptrv.disableScroolUp();
		setListener();
		AlertHelper.getInstance(this).showLoading(null);
		
		if(type == QuestionActivity.TYPE_HISTORY){
			tvPromptOne.setVisibility(View.GONE);
			tvPromptTwo.setVisibility(View.GONE);
			layoutReply.setVisibility(View.GONE);
			tvPromptThree.setVisibility(View.VISIBLE);
		}else{
			tvPromptThree.setVisibility(View.GONE);
			tvPromptOne.setVisibility(View.VISIBLE);
			tvPromptTwo.setVisibility(View.VISIBLE);
			layoutReply.setVisibility(View.VISIBLE);
			tvPromptOne.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent();
					intent.setClass(QuestionDetailActivity.this, CommentExpertActivity.class);
					intent.putExtra("replyId", replyId);
					startActivityForResult(intent, 101);
				}
			});
		}
		loadData();
	}
	
	private void loadData(){
//		if(type == QuestionActivity.TYPE_HISTORY){
//			mWebServiceHelper.getReplyDetail(replyId, WebServiceHelper.REPLY_DETAIL_HISTORY);
//		}else{
			mWebServiceHelper.getReplyDetail(replyId, WebServiceHelper.REPLY_DETAIL_HISTORY);
//		}
		
	}
	
	private WebServiceHelper mWebServiceHelper = new WebServiceHelper(
			new WebServiceHelper.RequestCallback<Define.REPLY_DETAIL>() {

				@Override
				public void onSuccess(Define.REPLY_DETAIL result) {
					AlertHelper.getInstance(QuestionDetailActivity.this).hideLoading();
					LogHelper.i("tag", "result:"+result.data.size());
					setData(result);
					ptrv.onHeaderRefreshComplete();
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					AlertHelper.getInstance(QuestionDetailActivity.this).hideLoading();
					ptrv.onHeaderRefreshComplete();
				}
	}, this);
	private WebServiceHelper mReplyWebServiceHelper;
	
	private void setData(Define.REPLY_DETAIL result){
		datas.clear();
		datas.addAll(result.data);
		if(mQuestionAdapter == null){
			mQuestionAdapter = new QuestionDetailAdapter(datas, this);
			lv_replys.setAdapter(mQuestionAdapter);
		}else{
			mQuestionAdapter.notifyDataSetChanged();
		}
//		lv_replys.smoothScrollToPosition(lv_replys.getCount() - 1);
	}
	
	
	private String beforeText;
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData();
			}
		});
		
		etContent.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start,
			        int lengthBefore, int lengthAfter) {
				String content = etContent.getText().toString();
				if(content.length() > 200){
					AlertHelper.getInstance(QuestionDetailActivity.this)
					.showCenterToast(R.string.text_count_beyond);
					if(beforeText != null){
						etContent.setText(beforeText);
						etContent.setSelection(start);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence text, int start,
			        int lengthBefore, int lengthAfter) {
				if(beforeText == null){
					beforeText = text.toString();
				}
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				beforeText = null;
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                ivPhoto.setImageBitmap(bitmap);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                imgBuffer = new String(Base64.encode(baos.toByteArray()));
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile(imageName));
                }
                mPhotoHelper = new PhotoHelper(QuestionDetailActivity.this, faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }else if(resultCode == 101){
        	setResult(0);
        	finish();
        }
	}
	
	//上传照片
	public void uploadPhoto(View view){
		if(imgBuffer != null && !"".equals(imgBuffer)){
			imgBuffer = null;
			ivPhoto.setImageResource(R.drawable.ic_question_camera);
			return;
		}
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile(imageName));
		}
		if(mPhotoHelper == null){
			mPhotoHelper = new PhotoHelper(this, faceUri, 50);
		}
		
		mPhotoHelper.getPhoto();
	}
	
	//回复
	public void reply(View view){
		if(replyId == null){
			return;
		}else if("".equals(etContent.getText().toString().trim()) && imgBuffer == null){
			AlertHelper.getInstance(this).showCenterToast(R.string.reply_hint);
			return;
		}
		AlertHelper.getInstance(this).showLoading(null);
		Define.QUESTION_SAVE questionSave = new Define.QUESTION_SAVE();
		questionSave.consultationid = replyId;
		if(imgBuffer != null){
			questionSave.image = imgBuffer;
			questionSave.imagename = imageName;
		}
		questionSave.content = etContent.getText().toString();
		
		
		if(mReplyWebServiceHelper == null){
			mReplyWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.BASE>() {

						@Override
						public void onSuccess(BASE result) {
							AlertHelper.getInstance(QuestionDetailActivity.this)
							.hideLoading();
							ivPhoto.setImageResource(R.drawable.ic_question_camera);
							etContent.setText("");
							imgBuffer = null;
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
							imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0); 
							loadData();
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							AlertHelper.getInstance(QuestionDetailActivity.this)
							.hideLoading();
							AlertHelper.getInstance(QuestionDetailActivity.this)
							.showCenterToast(R.string.question_sava_fail);
						}
			}, this);
		}
		
		mReplyWebServiceHelper.replyQuestion(questionSave);
	}
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		PhotoPopupHelper.hidePop();
	}
	
}
