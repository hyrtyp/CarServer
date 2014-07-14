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

/**
 * 新增&修改 保养信息
 * @author zoe
 *
 */
public class AlterMaintenanceInfoFragment extends Fragment{

	private View rootView;
	private EditText et_bytime;
	private EditText et_bypeople;
	private EditText et_bymileage;
	private EditText et_byitem;
	private EditText et_byunit;
	private EditText et_bycost;
	private EditText et_nextbymileage;
	private EditText et_remarks;
	private Button btn_save;
	private DatePickerDialog mDatePickerDialog;
	
	private Calendar mycalendar;
	private int year;
	private int month;
	private int day;
	private String byTime;

	private String carid;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_alert_maintenace_info, null);
		findView();
		return rootView;
	}
	
	public void setCarid(String id){
		this.carid = id;
	}
	
	private void findView(){
		et_bytime = (EditText) rootView.findViewById(R.id.et_bytime);
		et_bypeople = (EditText) rootView.findViewById(R.id.et_bypeople);
		et_bymileage = (EditText) rootView.findViewById(R.id.et_bymileage);
		et_byitem = (EditText) rootView.findViewById(R.id.et_byitem);
		et_byunit = (EditText) rootView.findViewById(R.id.et_byunit);
		et_bycost = (EditText) rootView.findViewById(R.id.et_bycost);
		et_nextbymileage = (EditText) rootView.findViewById(R.id.et_nextbymileage);
		et_remarks = (EditText) rootView.findViewById(R.id.et_remarks);
		
		et_bytime.setInputType(InputType.TYPE_NULL);
		
		et_bytime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				LogHelper.i("tag", "arg1:"+arg1);
				if(arg1){
					showDatePickerDialog();
				}
			}
		});
		
		et_bytime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog();
			}
		});
		
		btn_save = (Button) rootView.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(byTime == null || "".equals(byTime)){
					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_bytime_nodata);
					return;
				}
				Define.INFO_MAINTENANCE_LIST.CDATA info
				= new Define.INFO_MAINTENANCE_LIST.CDATA();
				info.carid = carid;
				info.bytime = byTime;
				info.bypeople = et_bypeople.getText().toString();
				info.bymileage = et_bymileage.getText().toString();
				info.byitem = et_byitem.getText().toString();
				info.byunit = et_byunit.getText().toString();
				info.bycost = et_bycost.getText().toString();
				info.nextbymileage = et_nextbymileage.getText().toString();
				info.remarks = et_remarks.getText().toString();
				saveInfo(info);
			}
		});
	}
	
	private void saveInfo(Define.INFO_MAINTENANCE_LIST.CDATA info){
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
		mWebServiceHelper.alterMaintenanceInfo(info);
	}
	
	private void showDatePickerDialog(){
		 if(mDatePickerDialog == null){
			 mycalendar = Calendar.getInstance(Locale.CHINA);
			 year = mycalendar.get(Calendar.YEAR);
		     month = mycalendar.get(Calendar.MONTH);
		     day = mycalendar.get(Calendar.DAY_OF_MONTH);
		     
		     mDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					

					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
							String time = String.format("%s-%s-%s", year,
									monthOfYear + 1 >= 10 ? monthOfYear + 1 : "0" + (monthOfYear + 1),
									dayOfMonth >= 10 ? dayOfMonth : "0" + (dayOfMonth));
							et_bytime.setText(time);
							byTime = time+" 12:00:00.000";
							long millis = StringHelper.string2Millis(time);
							if(millis > System.currentTimeMillis()){
								AlertHelper.getInstance(getActivity()).showCenterToast(R.string.time_beyond);
								et_bytime.setText(StringHelper.getNowTime());
								byTime = StringHelper.getNowTime();
							}
							
						
					}
				}, year, month, day);
		 }
		 mDatePickerDialog.setTitle(R.string.info_bytime_label);
		 
		 mDatePickerDialog.show();
		 
		
	}
}
