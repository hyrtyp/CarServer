package cn.com.hyrt.carserver.question.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;

import org.kobjects.base64.Base64;

import android.R.array;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_POISTION;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_SAVE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.info.activity.ChangeInfoActivity;
import cn.com.hyrt.carserver.info.activity.QuestionDetailActivity;
import cn.com.hyrt.carserver.question.adapter.PositionAdapter;

/**
 * 车辆问答
 *
 */
public class QuestionActivity extends BaseActivity {
	private EditText contentText;
	private Button mButton;
	ImageLoaderView ivFaceImg;
	private PhotoHelper mPhotoHelper;
	private Dialog mSpotDialogOne;
	private int flag = 0, flag1 = 0;
	private String positionId = "";
	private TextView positionText;
	private TextView imageTxt;
	private ImageView teamimage;
	private ImageView sysimage;
	private ImageView camer;
	private RelativeLayout leftLayout;
	private RelativeLayout rightLayout;
//	private String imgBuffer;
	private Bitmap imgBitmap;
	private Uri questionUri;
	private Boolean isSuccess;
	@ViewInject(id=R.id.leftlay) RelativeLayout leftlay;
	@ViewInject(id=R.id.imglayout) RelativeLayout imglayout;
	
	private List<String> spotIdOne = new ArrayList<String>();
	private List<String> spotNameOne = new ArrayList<String>();
	private Map<String, List<String>> spotIdTwo = new HashMap<String, List<String>>();
	private Map<String, List<String>> spotNameTwo = new HashMap<String, List<String>>();

	private List<Define.QUESTION_POISTION.CDATA> data = new ArrayList<Define.QUESTION_POISTION.CDATA>();
	public static final int FROM_CAMERA = 2;
	public static final int PHOTO_ZOOM = 3;
	private ListView ls_correlation;
	private ArrayAdapter<String> correAdapter;
	private String beforeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		initView();

//		loadData();
	}

	public void updataImg() {
		if (questionUri == null) {
			questionUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(this, questionUri, 50);
		mPhotoHelper.getPhoto();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			return;
		}

		if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
			// 保存剪切好的图片
			LogHelper.i("tag", "data:" + data.getParcelableExtra("data")
					+ "---" + data.getData());

			if (data.getParcelableExtra("data") != null) {
				imgBitmap = data.getParcelableExtra("data");
				ivFaceImg.setVisibility(View.VISIBLE);
				imageTxt.setVisibility(View.GONE);
				camer.setVisibility(View.GONE);
				ivFaceImg.setImageBitmap(imgBitmap);
				flag1 = 1;
				sysimage.setBackgroundResource(R.drawable.position_close);
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//				imgBuffer = new String(Base64.encode(baos.toByteArray()));
			}

		} else if (requestCode == PhotoHelper.FROM_CAMERA) {
			if (mPhotoHelper == null) {
				if (questionUri == null) {
					questionUri = Uri.fromFile(FileHelper
							.createFile("question.jpg"));
				}
				mPhotoHelper = new PhotoHelper(QuestionActivity.this,
						questionUri, 50);
			}
			mPhotoHelper.startPhotoZoom(questionUri, 50);
		} else if (resultCode == Define.RESULT_FROM_ALTER_CAR) {
			AlertHelper.getInstance(this).showLoading(null);
		}
	}

	private void initView() {
		contentText = (EditText) findViewById(R.id.content);
		ivFaceImg = (ImageLoaderView) findViewById(R.id.iv_ic_img);
		mButton = (Button) findViewById(R.id.btn_commit);
		positionText = (TextView) findViewById(R.id.tuan);
		imageTxt = (TextView) findViewById(R.id.xi);
		teamimage = (ImageView) findViewById(R.id.teamimage);
		sysimage = (ImageView) findViewById(R.id.sysimage);
		camer = (ImageView) findViewById(R.id.camer);
		rightLayout = (RelativeLayout) findViewById(R.id.rl_teamNotifi2);
		leftLayout = (RelativeLayout) findViewById(R.id.rl_teamNotifi);
		final Intent intent = new Intent();
		imglayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (flag == 1) {
					positionId = "";
					positionText.setText(R.string.question_select_position);
					teamimage
							.setBackgroundResource(R.drawable.ic_question_arrow);
					flag = 0;
				} else {
					loadSpot();
				}
			}
		});
		rightLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag == 1) {
//					positionId = "";
//					positionText.setText(R.string.question_select_position);
//					teamimage
//							.setBackgroundResource(R.drawable.ic_question_arrow);
//					flag = 0;
				} else {
					loadSpot();
				}
			}
		});

		leftlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (flag1 == 1) {
					imgBitmap = null;
					ivFaceImg.setVisibility(View.GONE);
					imageTxt.setVisibility(View.VISIBLE);
					camer.setVisibility(View.VISIBLE);
					ivFaceImg.setImageBitmap(null);
					imageTxt.setText(R.string.question_up_photo);
					sysimage.setBackgroundResource(R.drawable.ic_question_arrow);
					flag1 = 0;
				}else {
					updataImg();
				}
			}
		});
		
		leftLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag1 == 1) {
//					imgBuffer = "";
//					ivFaceImg.setVisibility(View.GONE);
//					imageTxt.setVisibility(View.VISIBLE);
//					camer.setVisibility(View.VISIBLE);
//					ivFaceImg.setImageBitmap(null);
//					imageTxt.setText(R.string.question_up_photo);
//					sysimage.setBackgroundResource(R.drawable.ic_question_arrow);
//					flag1 = 0;
				} else {
					updataImg();
				}
			}
		});

		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				saveQuestionInfo();
				if (isSuccess) {

					intent.setClass(QuestionActivity.this, CommitActivity.class);
					startActivity(intent);
				}
			}
		});
		
		contentText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence text, int start,
			        int lengthBefore, int lengthAfter) {
				String content = contentText.getText().toString();
				if(content.length() > 200){
					AlertHelper.getInstance(QuestionActivity.this).showCenterToast(R.string.text_count_beyond);
					if(beforeText != null){
						contentText.setText(beforeText);
						contentText.setSelection(start);
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
	
	private void loadSpot(){
		if(spotIdOne.size() > 0){
			getPosition(null);
			return;
		}
		WebServiceHelper mSpotServiceHelper
		= new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				ClassifyJsonParser mClassifyJsonParser = new ClassifyJsonParser();
				mClassifyJsonParser.parse(result);
				List<Map<String, String>> oneList = mClassifyJsonParser.getOneList();
				List<List<Map<String, String>>> twoList
				= mClassifyJsonParser.getTwoList();
				
				
				for(int i=0,j=oneList.size(); i<j; i++){
					spotIdOne.add(oneList.get(i).get("id"));
					spotNameOne.add(oneList.get(i).get("name"));
					List<String> ids = new ArrayList<String>();
					List<String> names = new ArrayList<String>();
					List<Map<String, String>> cList = twoList.get(i);
					for(int a=0,b=cList.size(); a<b; a++){
						ids.add(cList.get(a).get("id"));
						names.add(cList.get(a).get("name"));
					}
					spotIdTwo.put(oneList.get(i).get("id"), ids);
					spotNameTwo.put(oneList.get(i).get("id"), names);
				}
				getPosition(null);
			}
		}, QuestionActivity.this);
		mSpotServiceHelper.getSpot();
	}

	/**
	 * 选择部位
	 */
	public void getPosition(final String spotId) {
		if(mSpotDialogOne == null){
			mSpotDialogOne = new Dialog(this, R.style.MyDialog);
			mSpotDialogOne.setContentView(R.layout.layout_question_position);
			mSpotDialogOne.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			ls_correlation = (ListView) mSpotDialogOne.findViewById(R.id.ls_correlation);
			
		}
		if(spotId != null){
			correAdapter = new ArrayAdapter<String>(
					QuestionActivity.this,
					R.layout.layout_question_position_item,
					R.id.tv_name, spotNameTwo.get(spotId));
			ls_correlation.setAdapter(correAdapter);
			((TextView)mSpotDialogOne.findViewById(R.id.text1)).setText(R.string.back);
		}else{
			correAdapter = new ArrayAdapter<String>(
					QuestionActivity.this,
					R.layout.layout_question_position_item,
					R.id.tv_name, spotNameOne);
			ls_correlation.setAdapter(correAdapter);
			((TextView)mSpotDialogOne.findViewById(R.id.text1)).setText(R.string.cancle);
		}
		
		View.OnClickListener mClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if(spotId != null){
					getPosition(null);
					return;
				}
				mSpotDialogOne.dismiss();
			}
		};
		mSpotDialogOne.findViewById(R.id.rl10).setOnClickListener(mClickListener);
		
		((ListView) ls_correlation)
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if(spotId == null){
							getPosition(spotIdOne.get(position));
							return;
						}
						positionId = spotIdTwo.get(spotId).get(position);
//						positionId = data.get(position).id;
//						positionText.setText(data.get(position).name);
						positionText.setText(spotNameTwo.get(spotId).get(position));
						teamimage
								.setBackgroundResource(R.drawable.position_close);
						flag = 1;
						mSpotDialogOne.dismiss();
					}
				});
		if(!mSpotDialogOne.isShowing()){
			mSpotDialogOne.show();
		}
	}

	/*private void loadData() {
		WebServiceHelper mCarInfoServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.QUESTION_POISTION>() {
					@SuppressWarnings("unused")
					@Override
					public void onSuccess(QUESTION_POISTION result) {
						AlertHelper.getInstance(QuestionActivity.this)
								.hideLoading();
						if (result == null) {
							AlertHelper.getInstance(QuestionActivity.this)
									.showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_ALTER_CAR);
							finish();
						} else {
							if (result == null && data.size() > 0) {
								AlertHelper.getInstance(QuestionActivity.this)
										.showCenterToast(R.string.load_done);
							}

							data.clear();
							data.addAll(result.data);

						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(QuestionActivity.this)
								.hideLoading();
						AlertHelper.getInstance(QuestionActivity.this)
								.showCenterToast(R.string.info_load_fail);
						setResult(Define.RESULT_FROM_ALTER_CAR);
						finish();
					}
				}, this);

		mCarInfoServiceHelper.getPoisition();
	}*/

	public void saveQuestionInfo() {
		if (CarServerApplication.loginInfo == null) {
			CarServerApplication.loginInfo = StorageHelper.getInstance(this)
					.getLoginInfo();
		}
		QUESTION_SAVE question = new QUESTION_SAVE();
		question.terminalid = CarServerApplication.loginInfo.id;
		question.classid = positionId;
		String content = contentText.getText().toString();
		question.content = content;

		if ("".equals(content.trim()) || "".equals(positionId)) {
			isSuccess = false;
			if ("".equals(content.trim())) {
				AlertHelper.getInstance(this).showCenterToast(
						R.string.question_sava_text);
				return;
			} else if ("".equals(positionId)) {
				AlertHelper.getInstance(this).showCenterToast(
						R.string.question_sava_position);
				return;
			}
		} else {

			isSuccess = true;
//			if (imgBuffer != null && !"".equals(imgBuffer)) {
//				question.image = imgBuffer;
//				question.imagename = "question.jpg";
//			}

			WebServiceHelper mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.RESULT_ID>() {

						@Override
						public void onSuccess(Define.RESULT_ID result) {
							LogHelper.i("tag", "result:" + result.message);
//							AlertHelper.getInstance(QuestionActivity.this)
//									.showCenterToast(
//											R.string.question_sava_success);
//							setResult(Define.RESULT_FROM_CHANGE_INFO);
//							finish();
							uploadImg(result.id);
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							LogHelper.i("tag", "onFailure:" + errorMsg);
							AlertHelper.getInstance(QuestionActivity.this)
									.showCenterToast(errorMsg);
							setResult(Define.RESULT_FROM_CHANGE_INFO);
							finish();
						}
					}, this);
			mWebServiceHelper.saveQuestionInfo(question);

		}

	}
	
	private void uploadImg(String uid){
		if(imgBitmap == null){
			AlertHelper.getInstance(QuestionActivity.this)
			.showCenterToast(
					R.string.question_sava_success);
			setResult(Define.RESULT_FROM_CHANGE_INFO);
			finish();
			return;
		}
		WebServiceHelper mUploadImgWebServiceHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						AlertHelper.getInstance(QuestionActivity.this)
						.showCenterToast(
								R.string.question_sava_success);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						LogHelper.i("tag", "onFailure:" + errorMsg);
						AlertHelper.getInstance(QuestionActivity.this)
						.showCenterToast(errorMsg);
						setResult(Define.RESULT_FROM_CHANGE_INFO);
						finish();
					}
		}, this);
		mUploadImgWebServiceHelper.saveImage(
				imgBitmap, "questionPhoto.jpeg",
				WebServiceHelper.IMAGE_TYPE_QUESTION,
				uid);

	}
}
