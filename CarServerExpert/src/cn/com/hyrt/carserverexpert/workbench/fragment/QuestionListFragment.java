package cn.com.hyrt.carserverexpert.workbench.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_QUESTION_LIST;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.LogHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.PullToRefreshView;
import cn.com.hyrt.carserverexpert.info.activity.MyIntegrationActivity;
import cn.com.hyrt.carserverexpert.workbench.activity.QuestionDetailActivity;
import cn.com.hyrt.carserverexpert.workbench.adapter.QuestionAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class QuestionListFragment extends Fragment{
	
	private View rootView;
	private PullToRefreshView ptrv;
	private ListView listview;
	private TextView tvNoData;
	private List<Define.INFO_QUESTION_LIST.CDATA> mData 
	= new ArrayList<Define.INFO_QUESTION_LIST.CDATA>();
	private QuestionAdapter mAdapter;
	private int page = 1;
	private LinearLayout layoutSearch;
	
	private String type;
	
	private EditText etSearch;
	private ImageView ivSearch;
	private String searchText;
	
	public QuestionListFragment(String type) {
		super();
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_question_list, null);
		searchText = "";
		findView();
		AlertHelper.getInstance(getActivity()).showLoading(null);
		loadData(false);
		setListener();
		return rootView;
	}
	
	public void loadData(final boolean isMore){
		if(isMore){
			page++;
		}else{
			page = 1;
		}
		new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.INFO_QUESTION_LIST>() {

			@Override
			public void onSuccess(INFO_QUESTION_LIST result) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				ptrv.onHeaderRefreshComplete();
				ptrv.onFooterRefreshComplete();
				//TODO
				tvNoData.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
				if (!isMore) {
					mData.clear();
				}
				mData.addAll(result.data);
				if(mAdapter == null){
					mAdapter = new QuestionAdapter(mData, getActivity(), type);
					listview.setAdapter(mAdapter);
				}else{
					mAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				ptrv.onHeaderRefreshComplete();
				ptrv.onFooterRefreshComplete();
				if(mData.size() > 0){
					if(isMore){
						if (204==errorNo) {
							AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部...");
						}else{
							AlertHelper.getInstance(getActivity()).showCenterToast("获取数据失败...");
						}
					}else{
						AlertHelper.getInstance(getActivity()).showCenterToast("获取数据失败...");
					}
				}else{
					tvNoData.setVisibility(View.VISIBLE);
					listview.setVisibility(View.GONE);
				}
			}
		}, getActivity()).getQuestionInfoList(type, page, searchText);
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				AlertHelper.getInstance(getActivity()).showLoading("正在刷新");
				loadData(false);
			}
		});
		
		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
			
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				AlertHelper.getInstance(getActivity()).showLoading(null);
				loadData(true);
			}
		});
	
		ivSearch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				searchText = etSearch.getText().toString();
				loadData(false);
			}
		});
	}
	
	/**
	 * 切换搜索框显示和隐藏
	 */
	public void toggleSearch(){
		if(layoutSearch.getVisibility() == View.VISIBLE){
			layoutSearch.setVisibility(View.GONE);
		}else{
			layoutSearch.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		loadData(false);
	}
	
	private void findView(){
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		listview = (ListView) rootView.findViewById(R.id.listview);
		tvNoData = (TextView) rootView.findViewById(R.id.tv_nodata);
		layoutSearch = (LinearLayout) rootView.findViewById(R.id.layout_search);
		etSearch = (EditText) rootView.findViewById(R.id.et_search);
		ivSearch = (ImageView) rootView.findViewById(R.id.iv_search);
	}
}
