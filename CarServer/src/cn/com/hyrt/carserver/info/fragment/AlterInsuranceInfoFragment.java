package cn.com.hyrt.carserver.info.fragment;

import java.util.Calendar;
import java.util.Locale;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
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
 * 新增&修改 保险信息
 * @author zoe
 *
 */
public class AlterInsuranceInfoFragment extends Fragment{

	private View rootView;
	private EditText et_bxtime;
	private EditText et_bxtype;
	private EditText et_bxcost;
	private EditText et_bxunit1;
	private EditText et_bxcompany;
	private EditText et_remarks;
	private Button btn_save;
	private DatePickerDialog mDatePickerDialog;
	
	private Calendar mycalendar;
	private int year;
	private int month;
	private int day;
	private String bxTime;

	private String carid;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_alert_insurance_info, null);
		findView();
		return rootView;
	}
	
	public void setCarid(String id){
		this.carid = id;
	}
	
	private void findView(){
		et_bxtime = (EditText) rootView.findViewById(R.id.et_bxtime);
		et_bxtype = (EditText) rootView.findViewById(R.id.et_bxtype);
		et_bxcost = (EditText) rootView.findViewById(R.id.et_bxcost);
		et_bxunit1 = (EditText) rootView.findViewById(R.id.et_bxunit1);
		et_bxcompany = (EditText) rootView.findViewById(R.id.et_bxcompany);
		et_remarks = (EditText) rootView.findViewById(R.id.et_remarks);
		
		et_bxtime.setInputType(InputType.TYPE_NULL);
		
		et_bxtime
		.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				LogHelper.i("tag", "arg1:"+arg1);
				if(arg1){
					showDatePickerDialog();
				}
			}
		});
		
		et_bxtime.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showDatePickerDialog();
			}
		});
		
		btn_save = (Button) rootView.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(bxTime == null || "".equals(bxTime)){
					AlertHelper.getInstance(getActivity()).showCenterToast(R.string.info_bxtime_nodata);
					return;
				}
				Define.INFO_INSURANCE_LIST.CDATA info
				= new Define.INFO_INSURANCE_LIST.CDATA();
				info.carid = carid;
				info.bxtime = bxTime;
				info.bxtype = et_bxtype.getText().toString();
				info.bxcost = et_bxcost.getText().toString();
				info.bxunit1 = et_bxunit1.getText().toString();
				info.bxcompany = et_bxcompany.getText().toString();
				info.remarks = et_remarks.getText().toString();
				saveInfo(info);
			}
		});
	}
	
	private void saveInfo(Define.INFO_INSURANCE_LIST.CDATA info){
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
		mWebServiceHelper.alterInsuranceInfo(info);
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
							et_bxtime.setText(time);
							bxTime = time+" 00:00:00.000";
						
					}
				}, year, month, day);
		 }
		 mDatePickerDialog.setTitle(R.string.info_bxtime_label);
		 
		 mDatePickerDialog.show();
		 
		
	}
}
