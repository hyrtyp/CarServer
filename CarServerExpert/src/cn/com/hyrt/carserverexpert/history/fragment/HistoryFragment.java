package cn.com.hyrt.carserverexpert.history.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserverexpert.R;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define;
import cn.com.hyrt.carserverexpert.base.baseFunction.Define.INFO_QUESTION_LIST;
import cn.com.hyrt.carserverexpert.base.helper.AlertHelper;
import cn.com.hyrt.carserverexpert.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverexpert.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverexpert.base.view.PullToRefreshView;
import cn.com.hyrt.carserverexpert.workbench.adapter.QuestionAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryFragment extends Fragment{

	private View rootView;
	private PullToRefreshView ptrv;
	private ListView listview;
	private TextView tvNoData;
	private List<Define.INFO_QUESTION_LIST.CDATA> mData 
	= new ArrayList<Define.INFO_QUESTION_LIST.CDATA>();
	private QuestionAdapter mAdapter;
	private int page = 1;
	private String searchText = "";
	private EditText etSearch;
	private ImageView ivSearch;
	private LinearLayout layoutSearch;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_question_list, null);
		findView();
		mAdapter = null;
		loadData(false);
		setListener();
		return rootView;
	}
	
	@Override
	public void onResume() {
		etSearch.setText("");
		searchText = "";
		loadData(false);
		super.onResume();
	}
	
	public void loadData(final boolean isMore){
		AlertHelper.getInstance(getActivity()).showLoading(null);
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
				if(mData.size() > 0){
					if(isMore){
						if(result.data.size() <= 0){
							AlertHelper.getInstance(getActivity())
							.showCenterToast("已经加载全部");
						}
					}else{
						mData.clear();
						if(result.data.size() <= 0){
							AlertHelper.getInstance(getActivity())
							.showCenterToast("加载失败");
							listview.setVisibility(View.GONE);
							return;
						}
					}
				}else{
					if(result.data.size() <= 0){
						AlertHelper.getInstance(getActivity())
						.showCenterToast("加载失败");
						listview.setVisibility(View.GONE);
						return;
					}
				}
				listview.setVisibility(View.VISIBLE);
				mData.addAll(result.data);
				if(mAdapter == null){
					mAdapter = new QuestionAdapter(mData, getActivity(), WebServiceHelper.QUESTION_TYPE_HIS);
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
				if(isMore){
					if(mData.size() <= 0){
						AlertHelper.getInstance(getActivity())
						.showCenterToast("已经加载全部");
					}
				}else{
					mData.clear();
					if(mData.size() <= 0){
						AlertHelper.getInstance(getActivity())
						.showCenterToast("加载失败");
						tvNoData.setVisibility(View.VISIBLE);
						listview.setVisibility(View.GONE);
						return;
					}
				}
			}
		}, getActivity()).getQuestionInfoList(WebServiceHelper.QUESTION_TYPE_HIS, page, searchText);
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData(false);
			}
		});
		
		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {
			
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
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
	
	private void findView(){
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		listview = (ListView) rootView.findViewById(R.id.listview);
		tvNoData = (TextView) rootView.findViewById(R.id.tv_nodata);
		layoutSearch = (LinearLayout) rootView.findViewById(R.id.layout_search);
		etSearch = (EditText) rootView.findViewById(R.id.et_search);
		ivSearch = (ImageView) rootView.findViewById(R.id.iv_search);
	}
}
