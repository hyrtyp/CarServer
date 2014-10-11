package cn.com.hyrt.carserverseller.preferential.fragment;

import java.util.Calendar;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;

public class PreferentialFragment extends Fragment{

	private View rootView;
	private Spinner spPreferentialType;
	private RadioButton rbWelfare;
	private RadioButton rbActivity;
	private ImageLoaderView ivPropagandaPhoto;
	private TextView tvPropagandaPhoto;
	private EditText etActivityName;
	private EditText etStartTime;
	private EditText etEndTime;
	private EditText etTelNum;
	private EditText etAddress;
	private EditText etDesc;
	
	private Bitmap productBitmap;
	private String productImgUrl;
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	
	private int timeType;
	private DatePickerDialog mDatePickerDialog;
	private Calendar mycalendar;
	private int startYear;
	private int startMonth;
	private int startDay;
	private int endYear;
	private int endMonth;
	private int endDay;
	private String startTime;
	private String endTime;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_preferential, null);
		findView();
		loadData();
		setListener();
		if(productBitmap != null){
			ivPropagandaPhoto.setImageBitmap(productBitmap);
			tvPropagandaPhoto.setVisibility(View.GONE);
		}
		return rootView;
	}
	
	private void loadData(){
		ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(
				getActivity(),
				R.layout.layout_spinner_item,
				getResources().getStringArray(R.array.test_array));
		spPreferentialType.setAdapter(mAdapter);
	}
	
	private void setListener() {
		ivPropagandaPhoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (productBitmap == null && productImgUrl == null) {
					addPhoto();
				} else {
					if (productBitmap != null) {
						PhotoPopupHelper.showPop(productBitmap, getActivity());
					} else {
						PhotoPopupHelper.showPop(productImgUrl, getActivity());
					}
				}
			}
		});
		ivPropagandaPhoto.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				if (productBitmap == null && productImgUrl == null) {
					addPhoto();
				} else {
					delPhoto();
				}
				return true;
			}
		});
		
		etStartTime.setInputType(InputType.TYPE_NULL);
		etEndTime.setInputType(InputType.TYPE_NULL);
		
		etStartTime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					showDatePickerDialog(0);
				}
			}
		});
		
		etStartTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(0);
			}
		});
		
		etEndTime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					showDatePickerDialog(1);
				}
			}
		});
		
		etEndTime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(1);
			}
		});
	}
	
	/**
	 * 
	 * @param type(0:开始时间 1：结束时间 2：车检有效期)
	 */
	private void showDatePickerDialog(int type) {
		this.timeType = type;
		if(mDatePickerDialog != null){
			mDatePickerDialog = null;
		}
//		if (mDatePickerDialog == null) {
			mycalendar = Calendar.getInstance(Locale.CHINA);
			int year = 0;
			int month = 0;
			int day = 0;
			if(type == 0 && startDay != 0){
				year = startYear;
				month = startMonth;
				day = startDay;
			}else if(type == 1 && endDay != 0){
				year = endYear;
				month = endMonth;
				day = endDay;
			}
			if(year == 0){
				year = mycalendar.get(Calendar.YEAR);
				month = mycalendar.get(Calendar.MONTH);
				day = mycalendar.get(Calendar.DAY_OF_MONTH);
			}

			mDatePickerDialog = new DatePickerDialog(getActivity(),
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							String time = String.format("%s-%s-%s", year,
									monthOfYear + 1 >= 10 ? monthOfYear + 1
											: "0" + (monthOfYear + 1),
									dayOfMonth >= 10 ? dayOfMonth : "0"
											+ (dayOfMonth));
							long millis = StringHelper.string2Millis(time);
							if(timeType == 0){
								startYear = year;
								startMonth = monthOfYear;
								startDay = dayOfMonth;
							}else if(timeType == 1){
								endYear = year;
								endMonth = monthOfYear;
								endDay = dayOfMonth;
							}
							switch (timeType) {
							case 0:
								etStartTime.setText(time);
								startTime = time + " 12:00:00";
								/*if(millis > System.currentTimeMillis()){
									AlertHelper.getInstance(getActivity()).showCenterToast(R.string.time_beyond);
									etStartTime.setText(StringHelper.getNowTime());
									startTime = StringHelper.getNowTime();
								}*/
								break;
							case 1:
								etEndTime.setText(time);
								endTime = time + " 12:00:00";
								if(millis < System.currentTimeMillis()){
									AlertHelper.getInstance(getActivity()).showCenterToast("截止时间不能比当前时间早");
									etEndTime.setText(StringHelper.getNowTime());
									endTime = StringHelper.getNowTime();
								}
								break;
							default:
								break;
							}

						}
					}, year, month, day);
//		}

		switch (type) {
		case 0:
			mDatePickerDialog.setTitle(R.string.start_time_label);
			break;
		case 1:
			mDatePickerDialog.setTitle(R.string.end_time_label);
			break;
		default:
			break;
		}
		mDatePickerDialog.show();

	}
	
	private void addPhoto(){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
		mPhotoHelper.getPhoto();
	}
	
	private void delPhoto(){
		AlertDialog.Builder mDelPhotoDialog = new Builder(getActivity());
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
					tvPropagandaPhoto.setVisibility(View.VISIBLE);
					productBitmap = null;
					productImgUrl = null;
					ivPropagandaPhoto.setImageResource(R.drawable.ic_photo_add);
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			return;
		}
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
//                	productPhotos.add(bitmap);
//                	productAdapter.notifyDataSetChanged();
                tvPropagandaPhoto.setVisibility(View.GONE);
            	productBitmap = bitmap;
            	productImgUrl = null;
            	ivPropagandaPhoto.setImageBitmap(bitmap);
            	
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
//                StorageHelper.getInstance(getActivity()).saveTest(imgBuffer);
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 400);
        }
	}
	
	private void findView(){
		spPreferentialType = (Spinner) rootView.findViewById(R.id.sp_preferential_type);
		rbWelfare = (RadioButton) rootView.findViewById(R.id.rb_welfare);
		rbActivity = (RadioButton) rootView.findViewById(R.id.rb_activity);
		ivPropagandaPhoto = (ImageLoaderView) rootView.findViewById(R.id.iv_propaganda_photo);
		tvPropagandaPhoto = (TextView) rootView.findViewById(R.id.tv_propaganda_photo);
		etActivityName = (EditText) rootView.findViewById(R.id.et_activityname);
		etStartTime = (EditText) rootView.findViewById(R.id.et_starttime);
		etEndTime = (EditText) rootView.findViewById(R.id.et_endtime);
		etTelNum = (EditText) rootView.findViewById(R.id.et_telnum);
		etAddress = (EditText) rootView.findViewById(R.id.et_address);
		etDesc = (EditText) rootView.findViewById(R.id.et_desc);
	}
}
