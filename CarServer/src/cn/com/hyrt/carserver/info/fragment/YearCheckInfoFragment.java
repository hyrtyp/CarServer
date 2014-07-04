package cn.com.hyrt.carserver.info.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_MAINTENANCE_LIST;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_REPAIR_LIST;
import cn.com.hyrt.carserver.base.baseFunction.Define.INFO_YEAR;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.info.activity.CarConditionDetailActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 保养信息
 * @author zoe
 *
 */
public class YearCheckInfoFragment extends Fragment{

	private View rootView;
	private ListView lv_year;
	private ListView lv_content;
	private List<String> years = new ArrayList<String>();
	private List<String> contents = new ArrayList<String>();
	private MyAdapter mAdapter;
	private String carid;
	private boolean inited = false;
	private int curIndex = 0;
	private WebServiceHelper mContentServiceHelper;
	private int page;
	private ArrayList<String> titles;
	private Define.INFO_YEARCHECK_LIST datas;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_repair_info, null);
		findView();
		setListener();
		if(carid != null){
			loadData();
		}
		inited = true;
		return rootView;
	}
	
	public void setCarid(String id){
		this.carid = id;
		if(inited){
			loadData();
		}
	}
	
	private void loadData(){
		WebServiceHelper mYearServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.INFO_YEAR>() {

					@Override
					public void onSuccess(INFO_YEAR result) {
						setData(result);
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						// TODO Auto-generated method stub
						
					}
		}, getActivity());
		mYearServiceHelper.getConditionYear(carid, 0);
	}
	
	private void loadContent(String year){
		if(mContentServiceHelper == null){
			mContentServiceHelper = new WebServiceHelper(
					new WebServiceHelper.RequestCallback<Define.INFO_YEARCHECK_LIST>() {

						private ArrayAdapter<String> mContentAdapter;

						@Override
						public void onSuccess(Define.INFO_YEARCHECK_LIST result) {
							contents.clear();
							datas = result;
							for(int i=0,j=result.data.size(); i<j; i++){
								contents.add(result.data.get(i).id);
							}
							if(mContentAdapter == null){
								mContentAdapter = new ArrayAdapter<String>(
										getActivity(),
										R.layout.layout_car_condition_year_item,
										R.id.tv_year, contents);
								lv_content.setAdapter(mContentAdapter);
							}else{
								mContentAdapter.notifyDataSetChanged();
							}
							
							
						}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							contents.clear();
							if(mContentAdapter != null){
								mContentAdapter.notifyDataSetChanged();
							}
							
						}
			}, getActivity());
		}
		mContentServiceHelper.getYearCheckInfo(carid, year, page);
		
	}
	
	private void setData(Define.INFO_YEAR result){
		years.clear();
		for(int i=0,j=result.data.size(); i<j; i++){
			years.add(result.data.get(i).time);
		}
		if(mAdapter == null){
			mAdapter = new MyAdapter(
					getActivity(),
					R.layout.layout_car_condition_year_item,
					R.id.tv_year, years);
			lv_year.setAdapter(mAdapter);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		loadContent(years.get(0));
	}
	
	private class MyAdapter extends ArrayAdapter<String>{

		public MyAdapter(Context context, int resource, int textViewResourceId,
				List<String> objects) {
			super(context, resource, textViewResourceId, objects);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view =  super.getView(position, convertView, parent);
			TextView tv = (TextView) view.findViewById(R.id.tv_year);
			if(position == curIndex){
				view.setBackgroundColor(0xff07c5e9);
				tv.setTextColor(0xffffffff);
			}else{
				view.setBackgroundColor(0x00000000);
				tv.setTextColor(0xff07c5e9);
			}
			return view;
		}
		
	}
	
	private void setListener(){
		lv_year.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position,
					long arg3) {
					View acurTv = parentView.getChildAt(curIndex);
					acurTv.setBackgroundColor(0x00000000);
					TextView curTv = (TextView) acurTv.findViewById(R.id.tv_year);
					curTv.setTextColor(0xff07c5e9);
				
				loadContent(years.get(position));
				view.setBackgroundColor(0xff07c5e9);
				TextView tv = (TextView) view.findViewById(R.id.tv_year);
				tv.setTextColor(0xffffffff);
				curIndex = position;
			}
		});
		
		lv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView, View view, int position,
					long arg3) {
				jump(position);
			}
		});
	}
	
	private void jump(int position){
		if(titles == null){
			titles = new ArrayList<String>();
//			titles.add("年检时间");
//			titles.add("送检人");
//			titles.add("本车应检日期");
//			titles.add("年检有效期");
//			titles.add("年检费用");
			
			String[] array = getResources()
					.getStringArray(R.array.info_yearcheck_labels);
			for (String str : array) {
				titles.add(str);
			}
		}
		
		ArrayList<String> contents = new ArrayList<String>();
		contents.add(datas.data.get(position).njtime);
		contents.add(datas.data.get(position).njpeople);
		contents.add(datas.data.get(position).shouldtime);
		contents.add(datas.data.get(position).njefftime);
		contents.add(datas.data.get(position).njcost);
		
		Intent intent = new Intent();
		intent.setClass(getActivity(), CarConditionDetailActivity.class);
		intent.putExtra("title", "年检信息");
		intent.putStringArrayListExtra("titles", titles);
		intent.putStringArrayListExtra("contents", contents);
		startActivity(intent);
	}
	
	private void findView(){
		lv_year = (ListView) rootView.findViewById(R.id.lv_year);
		lv_content = (ListView) rootView.findViewById(R.id.lv_content);
	}
}
