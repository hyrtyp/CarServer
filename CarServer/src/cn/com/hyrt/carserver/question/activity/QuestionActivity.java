package cn.com.hyrt.carserver.question.activity;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.kobjects.base64.Base64;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.application.CarServerApplication;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_POISTION;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_SAVE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.FileHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.PhotoHelper;
import cn.com.hyrt.carserver.base.helper.StorageHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import cn.com.hyrt.carserver.question.adapter.PositionAdapter;

public class QuestionActivity extends BaseActivity {
	private EditText contentText;
	private Button mButton;
	ImageLoaderView ivFaceImg;
	private PhotoHelper mPhotoHelper;
	private Dialog mDialog;
	private int flag = 0, flag1 = 0;
	private String positionId = "";
	private TextView positionText;
	private TextView imageTxt;
	private ImageView teamimage;
	private ImageView sysimage;
	private ImageView camer;
	private RelativeLayout leftLayout;
	private RelativeLayout rightLayout;
	private String imgBuffer;
	private Uri questionUri;
	private Boolean isSuccess;

	private List<Define.QUESTION_POISTION.CDATA> data = new ArrayList<Define.QUESTION_POISTION.CDATA>();
	public static final int FROM_CAMERA = 2;
	public static final int PHOTO_ZOOM = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		initView();

		loadData();
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
				Bitmap bitmap = data.getParcelableExtra("data");
				ivFaceImg.setVisibility(View.VISIBLE);
				imageTxt.setVisibility(View.GONE);
				camer.setVisibility(View.GONE);
				ivFaceImg.setImageBitmap(bitmap);
				flag1 = 1;
				sysimage.setBackgroundResource(R.drawable.position_close);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				imgBuffer = new String(Base64.encode(baos.toByteArray()));
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
		rightLayout = (RelativeLayout) findViewById(R.id.imglayout);
		leftLayout = (RelativeLayout) findViewById(R.id.leftlay);
		final Intent intent = new Intent();
		rightLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag == 1) {
					positionId = "";
					positionText.setText(R.string.question_select_position);
					teamimage
							.setBackgroundResource(R.drawable.ic_question_arrow);
					flag = 0;
				} else {
					getPosition();
				}
			}
		});

		leftLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag1 == 1) {
					imgBuffer = "";
					ivFaceImg.setVisibility(View.GONE);
					imageTxt.setVisibility(View.VISIBLE);
					camer.setVisibility(View.VISIBLE);
					ivFaceImg.setImageBitmap(null);
					imageTxt.setText(R.string.question_up_photo);
					sysimage.setBackgroundResource(R.drawable.ic_question_arrow);
					flag1 = 0;
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
	}

	/**
	 * 选择部位
	 */
	public void getPosition() {
		mDialog = new Dialog(this, R.style.MyDialog);
		mDialog.setContentView(R.layout.layout_question_position);
		mDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);

		final View ls_correlation = mDialog.findViewById(R.id.ls_correlation);

		((ListView) ls_correlation).setAdapter(new PositionAdapter(
				QuestionActivity.this, data));

		final View layout_cancle = mDialog.findViewById(R.id.rl10);

		View.OnClickListener mClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				int id = view.getId();
				if (id == layout_cancle.getId()) {

					mDialog.dismiss();
				}
			}
		};

		layout_cancle.setOnClickListener(mClickListener);

		((ListView) ls_correlation)
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						positionId = data.get(position).id;
						positionText.setText(data.get(position).name);
						teamimage
								.setBackgroundResource(R.drawable.position_close);
						flag = 1;
						mDialog.dismiss();
					}
				});

		mDialog.show();
	}

	private void loadData() {
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
							System.out.println("load data==========" + data);

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
	}

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

		if ("".equals(content) || "".equals(positionId)) {
			isSuccess = false;
			if ("".equals(content)) {
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
			if (imgBuffer != null && !"".equals(imgBuffer)) {
				question.image = imgBuffer;
				question.imagename = "question.jpg";
			}

			WebServiceHelper mWebServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.BASE>() {

						@Override
						public void onSuccess(BASE result) {
							LogHelper.i("tag", "result:" + result.message);
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
			mWebServiceHelper.saveQuestionInfo(question);

		}

	}
}
