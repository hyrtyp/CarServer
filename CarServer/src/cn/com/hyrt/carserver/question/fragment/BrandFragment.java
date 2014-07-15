package cn.com.hyrt.carserver.question.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.question.activity.FindByBrandActivity;
import cn.com.hyrt.carserver.question.adapter.BrandAdapter;

public class BrandFragment extends Fragment{
	
	
	private List<Map<String, String>> oneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> twoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private BrandAdapter mAdapter;

	private ListView lvBrand;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmet_brand, null);
		lvBrand = (ListView) view.findViewById(R.id.lv_brand);
		setListener();
		loadData();
		return view;
	}
	
	private void setListener(){
		lvBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				String[] data = (String[]) view.findViewById(R.id.tv_name).getTag();
				String id = data[0];
				if(id != null){
					((FindByBrandActivity)getActivity()).open(id, data[1]);
				}
			}
		});
	}
	
	private void loadData(){
	WebServiceHelper mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
		
		@Override
		public void onSuccess(String result) {
			oneList.clear();
			twoList.clear();

			ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
			classifyJsonParser.parse(result);
			oneList.addAll(classifyJsonParser.getOneList());
			 LogHelper.i("tag", "oneList:"+oneList);
			twoList.addAll(classifyJsonParser.getTwoList());
			 LogHelper.i("tag", "twoList:"+twoList);
			 
			 ((FindByBrandActivity)getActivity()).setData(oneList, twoList, classifyJsonParser.getThreeList());
			 
			 if(mAdapter == null){
				 mAdapter = new BrandAdapter(
						 oneList, twoList, getActivity());
				 lvBrand.setAdapter(mAdapter);
			 }else{
				 mAdapter.notifyDataSetChanged();
			 }
		}
	}, getActivity());
	mWebServiceHelper.getBrands();
}
	
}
