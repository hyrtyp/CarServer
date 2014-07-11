package cn.com.hyrt.carserver.info.fragment;

import java.util.Calendar;
import java.util.Locale;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_REPAIR_LIST.CDATA;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.info.activity.AlterCarActivity;
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

public class AlterRepairInfoFragment extends Fragment{

	private View rootView;
	private EditText et_mileage;
	private EditText et_wxtime;
	private EditText et_reason;
	private EditText et_company;
	private EditText et_item;
	private EditText et_repair;
	private EditText et_cost;
	private EditText et_situation;
	private EditText et_remarks;
	private Button btn_save;
	private DatePickerDialog mDatePickerDialog;
	
	private String carid;
	private Calendar mycalendar;
	private int year;
	private int month;
	private int day;
	private String wxTime;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_alert_repair_info, null);
		findView();
		return rootView;
	}
	
	private void findView(){
		et_mileage = (EditText) rootView.findViewById(R.id.et_mileage);
		et_wxtime = (EditText) rootView.findViewById(R.id.et_wxtime);
		et_reason = (EditText) rootView.findViewById(R.id.et_reason);
		et_company = (EditText) rootView.findViewById(R.id.et_company);
		et_item = (EditText) rootView.findViewById(R.id.et_item);
		et_repair = (EditText) rootView.findViewById(R.id.et_repair);
		et_cost = (EditText) rootView.findViewById(R.id.et_cost);
		et_situation = (EditText) rootView.findViewById(R.id.et_situation);
		et_remarks = (EditText) rootView.findViewById(R.id.et_remarks);
		btn_save = (Button) rootView.findViewById(R.id.btn_save);
		
		et_wxtime.setInputType(InputType.TYPE_NULL);
		
		et_wxtime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				LogHelper.i("tag", "arg1:"+arg1);
				if(arg1){
					showDatePickerDialog();
				}
			}
		});
		
		et_wxtime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog();
			}
		});
		
		btn_save.setOnClickListener(new View.OnClickListener() {
			

			@Override
			public void onClick(View arg0) {
				if(wxTime == null || "".equals(wxTime)){
					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_wxtime_nodata);
					return;
				}
				Define.INFO_REPAIR_LIST.CDATA repairInfo
				= new Define.INFO_REPAIR_LIST.CDATA();
				repairInfo.carid = carid;
				repairInfo.mileage = et_mileage.getText().toString();
				repairInfo.wxtime = wxTime;
				repairInfo.reason = et_reason.getText().toString();
				repairInfo.company = et_company.getText().toString();
				repairInfo.item = et_item.getText().toString();
				repairInfo.repair = et_repair.getText().toString();
				repairInfo.cost = et_cost.getText().toString();
				repairInfo.situation = et_situation.getText().toString();
				repairInfo.remarks = et_remarks.getText().toString();
				saveInfo(repairInfo);
			}
		});
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
							et_wxtime.setText(time);
							wxTime = time+" 00:00:00.000";
						
					}
				}, year, month, day);
		 }
		 mDatePickerDialog.setTitle(R.string.info_wxtime_label);
		 
		 mDatePickerDialog.show();
		 
		
	}
	
	private void saveInfo(Define.INFO_REPAIR_LIST.CDATA info){
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
		mWebServiceHelper.alterRepairInfo(info);
	}
	
	public void setCarid(String id){
		this.carid = id;
	}
}
