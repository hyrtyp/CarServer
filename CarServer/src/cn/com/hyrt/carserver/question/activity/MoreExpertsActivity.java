package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.ZJUSERMAIN_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.question.adapter.MoreExpertsAdapter;

public class MoreExpertsActivity extends BaseActivity {
	private ListView mMoreexperts ;
	private TextView tvNoData ;
	private PullToRefreshView ptrv;
	private LinearLayout expert_search_zhuanye ;
	private LinearLayout expert_search_brands ;
	private TextView tv_expert_search_brands_result ;
	private TextView tv_expert_search_zhuanye_result ;
	@ViewInject(id=R.id.leftlay) RelativeLayout leftlay;
	@ViewInject(id=R.id.imglayout) RelativeLayout imglayout;
	private Context mContext;
	private int pageNo = 1;
	private boolean isLoadMore = false;
	private MoreExpertsAdapter adapter ;
	private List<Define.ZJUSERMAIN_LIST.CDATA> mExpertList = new ArrayList<Define.ZJUSERMAIN_LIST.CDATA>();
	private Dialog mSpotDialogOne;
	private int flag = 0, flag1 = 0;
	private List<String> spotIdOne = new ArrayList<String>();
	private List<String> spotNameOne = new ArrayList<String>();
	private Map<String, List<String>> spotIdTwo = new HashMap<String, List<String>>();
	private Map<String, List<String>> spotNameTwo = new HashMap<String, List<String>>();
	public static final int FROM_CAMERA = 2;
	public static final int PHOTO_ZOOM = 3;
	private ListView ls_correlation;
	private ArrayAdapter<String> correAdapter;
	private String mBrandsPositionId = "";
	private String mZhuanPositionId = "";
	private boolean isBrandId = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_experts);
		mContext = this ;
		initView();
		isLoadMore = false;
		loadData();
		setListener();
	}
	
	private void initView() {
		tv_expert_search_brands_result = (TextView) findViewById(R.id.tv_expert_search_brands_result);
		tv_expert_search_zhuanye_result = (TextView) findViewById(R.id.tv_expert_search_zhuanye_result);
		expert_search_brands = (LinearLayout) findViewById(R.id.expert_search_brands);
		expert_search_zhuanye = (LinearLayout) findViewById(R.id.expert_search_zhuanye);
		mMoreexperts = (ListView) findViewById(R.id.fulllistview_moreexperts);
		ptrv = (PullToRefreshView) findViewById(R.id.ptrv);
		tvNoData = (TextView) findViewById(R.id.tv_noData);
	}
	
	
	private void setListener() {
		ptrv.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				isLoadMore = false;
				loadData();
			}
		});

		ptrv.setOnFooterRefreshListener(new PullToRefreshView.OnFooterRefreshListener() {

			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				isLoadMore = true;
				loadData();
			}
		});
		
		
		expert_search_brands.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				spotIdOne.clear();
				spotNameOne.clear();
				spotIdTwo.clear();
				spotNameTwo.clear();
				isBrandId = true ;
				loadSpot("brands");
			}
		});
		
		expert_search_zhuanye.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				spotIdOne.clear();
				spotNameOne.clear();
				spotIdTwo.clear();
				spotNameTwo.clear();
				isBrandId = false ;
				loadSpot("zhuanye");
			}
		});
		
		
	}
	
	private void loadData() {
		AlertHelper.getInstance(this).showLoading(getString(R.string.loading_msg));
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(new WebServiceHelper.RequestCallback<Define.ZJUSERMAIN_LIST>() {

				@Override
				public void onSuccess(ZJUSERMAIN_LIST result) {
					AlertHelper.getInstance(MoreExpertsActivity.this).hideLoading();
					ptrv.onFooterRefreshComplete();
					ptrv.onHeaderRefreshComplete();
					if(result.data.size() <= 0){
						if(mExpertList.size() > 0){
							AlertHelper.getInstance(MoreExpertsActivity.this).showCenterToast(R.string.load_done);
						}else{
							mExpertList.clear();
							tvNoData.setVisibility(View.VISIBLE);
							mMoreexperts.setVisibility(View.GONE);
						}
					}else{
						tvNoData.setVisibility(View.GONE);
						mMoreexperts.setVisibility(View.VISIBLE);
					}
					if (!isLoadMore) {
						mExpertList.clear();
					}
					mExpertList.addAll(result.data);
					
					if (adapter == null) {
						adapter = new MoreExpertsAdapter(mContext ,mExpertList);
						mMoreexperts.setAdapter(adapter);
					}else{
						adapter.notifyDataSetChanged();
					}
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					ptrv.onFooterRefreshComplete();
					ptrv.onHeaderRefreshComplete();
					AlertHelper.getInstance(MoreExpertsActivity.this).hideLoading();
					AlertHelper.getInstance(MoreExpertsActivity.this).showCenterToast(R.string.load_done);
				}
			}, MoreExpertsActivity.this);
		if (isLoadMore) {
			pageNo++;
		} else {
			pageNo = 1;
		}
		mWebServiceHelper.getZJUserList(pageNo,mBrandsPositionId,mZhuanPositionId);
	}
	
	
	private void loadSpot(String select){
		if(spotIdOne.size() > 0){
			getPosition(null);
			return;
		}
		WebServiceHelper mSpotServiceHelper = new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				spotIdOne.clear();
				spotNameOne.clear();
				spotIdTwo.clear();
				spotNameTwo.clear();
				ClassifyJsonParser mClassifyJsonParser = new ClassifyJsonParser();
				mClassifyJsonParser.parse(result); 
				List<Map<String, String>> oneList = mClassifyJsonParser.getOneList();
				List<List<Map<String, String>>> twoList = mClassifyJsonParser.getTwoList();
				
				
				for(int i=0,j=oneList.size(); i<j; i++){
					spotIdOne.add(oneList.get(i).get("id"));
					spotNameOne.add(oneList.get(i).get("name"));
					List<String> ids = new ArrayList<String>();
					List<String> names = new ArrayList<String>();
					List<Map<String, String>> cList = twoList.get(i);
					for(int a=0,b=cList.size(); a<b; a++){
						ids.add(cList.get(a).get("id"));
						names.add(cList.get(a).get("name"));
					}
					spotIdTwo.put(oneList.get(i).get("id"), ids);
					spotNameTwo.put(oneList.get(i).get("id"), names);
				}
				getPosition(null);
			}
		}, MoreExpertsActivity.this);
		
		if ("brands".equals(select)) {
			mSpotServiceHelper.getBrands();
		} 
		if ("zhuanye".equals(select)) {
			mSpotServiceHelper.getSpot();
		}
	}

	/**
	 * 选择部位
	 */
	public void getPosition(final String spotId) {
		if(mSpotDialogOne == null){
			mSpotDialogOne = new Dialog(this, R.style.MyDialog);
			mSpotDialogOne.setContentView(R.layout.layout_question_position);
			mSpotDialogOne.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			ls_correlation = (ListView) mSpotDialogOne.findViewById(R.id.ls_correlation);
			
		}
		if(spotId != null){
			correAdapter = new ArrayAdapter<String>(
					MoreExpertsActivity.this,
					R.layout.layout_question_position_item,
					R.id.tv_name, spotNameTwo.get(spotId));
			ls_correlation.setAdapter(correAdapter);
			((TextView)mSpotDialogOne.findViewById(R.id.text1)).setText(R.string.back);
		}else{
			correAdapter = new ArrayAdapter<String>(
					MoreExpertsActivity.this,
					R.layout.layout_question_position_item,
					R.id.tv_name, spotNameOne);
			ls_correlation.setAdapter(correAdapter);
			((TextView)mSpotDialogOne.findViewById(R.id.text1)).setText(R.string.cancle);
		}
		
		View.OnClickListener mClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if(spotId != null){
					getPosition(null);
					return;
				}
				mSpotDialogOne.dismiss();
			}
		};
		mSpotDialogOne.findViewById(R.id.rl10).setOnClickListener(mClickListener);
		
		((ListView) ls_correlation)
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if(spotId == null){
							getPosition(spotIdOne.get(position));
							return;
						}
						if (isBrandId) {
							mBrandsPositionId = spotIdTwo.get(spotId).get(position);
							tv_expert_search_brands_result.setText(spotNameTwo.get(spotId).get(position));
						}else{
							mBrandsPositionId = spotIdTwo.get(spotId).get(position);
							tv_expert_search_zhuanye_result.setText(spotNameTwo.get(spotId).get(position));
						}
						
						if ("".equals(mBrandsPositionId) || "".equals(mBrandsPositionId)) {
							isLoadMore = false;
							loadData();
						}else if (!"".equals(mBrandsPositionId) && !"".equals(mBrandsPositionId)) {
							isLoadMore = false;
							loadData();
						}
						
						flag = 1;
						mSpotDialogOne.dismiss();
					}
				});
		if(!mSpotDialogOne.isShowing()){
			mSpotDialogOne.show();
		}
	}
}
