package cn.com.hyrt.carserverexpert.history.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.CAR_YEAR;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_REPAIR_LIST;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.history.adapter.CarStatusYearAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class MaintenanceInfoFragment extends Fragment{

	private View rootView;
	private ListView lvYear;
	private GridView gvItem;
	
	private String carId;
	private CarStatusYearAdapter mAdapter;
	private List<Define.CAR_YEAR.CDATA> mData
	= new ArrayList<Define.CAR_YEAR.CDATA>();
	
	private List<String> mRepairItem = new ArrayList<String>();
	private ArrayAdapter<String> mArrayAdapter;
	
	
	public MaintenanceInfoFragment(String carId) {
		super();
		this.carId = carId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_car_status_detail, null);
		findView();
		loadData();
		setListener();
		return rootView;
	}
	
	private void loadData(){
		AlertHelper.getInstance(getActivity()).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.CAR_YEAR>() {

			@Override
			public void onSuccess(CAR_YEAR result) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				mData.clear();
				mData.addAll(result.data);
				if(mData.size() > 0){
					gvItem.setVisibility(View.VISIBLE);
				}else{
					gvItem.setVisibility(View.GONE);
				}
				getMaintenanceInfoList(result.data.get(0).time);
				if(mAdapter == null){
					mAdapter = new CarStatusYearAdapter(mData, getActivity());
					lvYear.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				gvItem.setVisibility(View.GONE);
				AlertHelper.getInstance(getActivity()).hideLoading();
			}
		}, getActivity()).getMaintenanceYear(carId);
	}
	
	private void getMaintenanceInfoList(String year){
		AlertHelper.getInstance(getActivity()).showLoading(null);
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.INFO_MAINTENANCE_LIST>() {

			@Override
			public void onSuccess(Define.INFO_MAINTENANCE_LIST result) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				mRepairItem.clear();
				for(int i=0,j=result.data.size(); i<j; i++){
					mRepairItem.add(result.data.get(i).byitem);
				}
				
				if(mArrayAdapter == null){
					mArrayAdapter = new ArrayAdapter<String>(
							getActivity(),
							R.layout.layout_text_item,
							R.id.tv_content,
							mRepairItem);
					gvItem.setAdapter(mArrayAdapter);
				}else{
					mArrayAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				mRepairItem.clear();
				mRepairItem.add("暂无数据");
				if(mArrayAdapter == null){
					mArrayAdapter = new ArrayAdapter<String>(
							getActivity(),
							R.layout.layout_text_item,
							R.id.tv_content,
							mRepairItem);
					gvItem.setAdapter(mArrayAdapter);
				}else{
					mArrayAdapter.notifyDataSetChanged();
				}
				
			}
		}, getActivity()).getMaintenanceInfoList(carId, year, 1);
	}
	
	private void setListener(){
		lvYear.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position,
					long arg3) {
				LogHelper.i("tag", "position:"+position);
				int curIndex = mAdapter.curIndex;
				if(curIndex == position){
					return;
				}
				TextView curBtnYear = (TextView) parentView.getChildAt(curIndex)
						.findViewById(R.id.btn_year);
				curBtnYear.setBackgroundResource(R.drawable.bg_car_status_year_normal);
				curBtnYear.setTextColor(0xff7f7f7f);
				
				TextView nextBtnYear = (TextView) view.findViewById(R.id.btn_year);
				nextBtnYear.setBackgroundResource(R.drawable.bg_car_status_year_focus);
				nextBtnYear.setTextColor(0xff289fff);
				mAdapter.curIndex = position;
				
				getMaintenanceInfoList(mData.get(position).time);
			}
		});
	}
	
	private void findView(){
		lvYear = (ListView) rootView.findViewById(R.id.lv_year);
		gvItem = (GridView) rootView.findViewById(R.id.gv_item);
	}
}
