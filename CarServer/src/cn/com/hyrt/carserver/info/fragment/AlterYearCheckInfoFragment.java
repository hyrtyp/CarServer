package cn.com.hyrt.carserver.info.fragment;

import java.util.Calendar;
import java.util.Locale;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 新增&修改 年检信息
 * @author zoe
 *
 */
public class AlterYearCheckInfoFragment extends Fragment{

	private View rootView;
	private EditText et_njtime;
	private EditText et_njpeople;
	private EditText et_shouldtime;
	private EditText et_njefftime;
	private EditText et_njcost;
	private EditText et_remarks;
	private Button btn_save;
	private DatePickerDialog mDatePickerDialog;
	
	private Calendar mycalendar;
	private int year;
	private int month;
	private int day;
	private String njTime;
	private String shouldTime;
	private String njeffTime;

	private String carid;
	
	private int timeType;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_alert_yearcheck_info, null);
		findView();
		return rootView;
	}
	
	public void setCarid(String id){
		this.carid = id;
	}
	
	private void findView(){
		et_njtime = (EditText) rootView.findViewById(R.id.et_njtime);
		et_njpeople = (EditText) rootView.findViewById(R.id.et_njpeople);
		et_shouldtime = (EditText) rootView.findViewById(R.id.et_shouldtime);
		et_njefftime = (EditText) rootView.findViewById(R.id.et_njefftime);
		et_njcost = (EditText) rootView.findViewById(R.id.et_njefftime);
		et_remarks = (EditText) rootView.findViewById(R.id.et_remarks);
		
		et_njtime.setInputType(InputType.TYPE_NULL);
		et_shouldtime.setInputType(InputType.TYPE_NULL);
		et_njefftime.setInputType(InputType.TYPE_NULL);
		
		et_njtime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					showDatePickerDialog(0);
				}
			}
		});
		
		et_njtime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(0);
			}
		});
		
		et_shouldtime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					showDatePickerDialog(1);
				}
			}
		});
		
		et_shouldtime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(1);
			}
		});
		
		et_njefftime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if(arg1){
					showDatePickerDialog(2);
				}
			}
		});
		
		et_njefftime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog(2);
			}
		});
		
		btn_save = (Button) rootView.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(njTime == null || "".equals(njTime)){
					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_njtime_nodata);
					return;
				}
				Define.INFO_YEARCHECK_LIST.CDATA info
				= new Define.INFO_YEARCHECK_LIST.CDATA();
				info.carid = carid;
				info.njtime = njTime;
				info.njpeople = et_njpeople.getText().toString();
				info.shouldtime = shouldTime;
				info.njefftime = njeffTime;
				info.njcost = et_njcost.getText().toString();
				info.remarks = et_remarks.getText().toString();
				saveInfo(info);
			}
		});
	}
	
	private void saveInfo(Define.INFO_YEARCHECK_LIST.CDATA info){
		AlertHelper.getInstance(getActivity()).showLoading(null);
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_change_success);
						getActivity().finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(getActivity()).hideLoading();
						AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_change_fail);
					}
		}, getActivity());
		mWebServiceHelper.alterYearCheckInfo(info);
	}
	
	/**
	 * 
	 * @param type(0:年检时间 1：本车应年检时间 2：车检有效期)
	 */
	private void showDatePickerDialog(int type) {
		this.timeType = type;
		if (mDatePickerDialog == null) {
			mycalendar = Calendar.getInstance(Locale.CHINA);
			year = mycalendar.get(Calendar.YEAR);
			month = mycalendar.get(Calendar.MONTH);
			day = mycalendar.get(Calendar.DAY_OF_MONTH);

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
							switch (timeType) {
							case 0:
								et_njtime.setText(time);
								njTime = time + " 12:00:00.000";
								if(millis > System.currentTimeMillis()){
									AlertHelper.getInstance(getActivity()).showCenterToast(R.string.time_beyond);
									et_njtime.setText(StringHelper.getNowTime());
									njTime = StringHelper.getNowTime();
								}
								break;
							case 1:
								et_shouldtime.setText(time);
								shouldTime = time + " 00:00:00.000";
								break;
							case 2:
								et_njefftime.setText(time);
								njeffTime = time + " 00:00:00.000";
								break;
							default:
								break;
							}

						}
					}, year, month, day);
		}

		switch (type) {
		case 0:
			mDatePickerDialog.setTitle(R.string.info_njtime_label);
			break;
		case 1:
			mDatePickerDialog.setTitle(R.string.info_shouldtime_label);
			break;
		case 2:
			mDatePickerDialog.setTitle(R.string.info_njefftime_label);
			break;
		default:
			break;
		}

		mDatePickerDialog.show();

	}
}
