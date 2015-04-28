package cn.com.hyrt.carserversurvey.info.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.application.CarServerApplication;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.REGRECODE;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
import cn.com.hyrt.carserversurvey.base.view.FullListView;
import cn.com.hyrt.carserversurvey.base.view.PullToRefreshView;
import cn.com.hyrt.carserversurvey.info.activity.MerchantInfoActivity;
import cn.com.hyrt.carserversurvey.info.activity.RegRecodeActivity;
import cn.com.hyrt.carserversurvey.info.adapter.RegRecodeAdapter;
import cn.com.hyrt.carserversurvey.product.activity.ProductActivity;
import cn.com.hyrt.carserversurvey.regist.activity.RegistMerchantInfoActivity;
import cn.com.hyrt.carserversurvey.shop.activity.ShopActivity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RegistRecordFragment extends Fragment {

	private View rootView;
	private FullListView lvclaim;
	private PullToRefreshView ptrv;
	private TextView tvNoData;

	private List<Define.REGRECODE.CDATA> recode = new ArrayList<Define.REGRECODE.CDATA>();
	private RegRecodeAdapter recodeAdapter;
	private int pageNo = 1;
	private boolean isLoadMore = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_regrecode, null);
		findView();
		isLoadMore = false;
		recodeAdapter = null;
		loadData();
		setListener();
		return rootView;
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

		lvclaim.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			private Dialog mSelectInfoDialog;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				LogHelper.i("tag", "lvclaim:" + position);
				// Intent intent = new Intent();
				// intent.setClass(getActivity(), MerchantInfoActivity.class);
				// intent.putExtra("id", recode.data.get(position).id);
				// startActivity(intent);

				mSelectInfoDialog = new Dialog(getActivity(), R.style.MyDialog);
				mSelectInfoDialog.setContentView(R.layout.layout_select_info);
				mSelectInfoDialog.getWindow().setLayout(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);

				final Button btnProductIndex = (Button) mSelectInfoDialog
						.findViewById(R.id.btn_product_index);
				final Button btnSjProduct = (Button) mSelectInfoDialog
						.findViewById(R.id.btn_sj_product);
				final Button btnRegRecord = (Button) mSelectInfoDialog
						.findViewById(R.id.btn_reg_record);
				final Button btnCancle = (Button) mSelectInfoDialog
						.findViewById(R.id.btn_cancle);
				final View bg = mSelectInfoDialog.findViewById(R.id.bg);
				final LinearLayout layout_select = (LinearLayout) mSelectInfoDialog
						.findViewById(R.id.layout_select);

				View.OnClickListener mClickListener = new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						int id = view.getId();
						boolean needDismiss = true;

						if (id == btnProductIndex.getId()) {
							Intent intent = new Intent();
							intent.setClass(getActivity(), ShopActivity.class);
							intent.putExtra("shId",
									recode.get(position).id);
							startActivity(intent);
						} else if (id == btnSjProduct.getId()) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									ProductActivity.class);
							intent.putExtra("shId",
									recode.get(position).id);
							startActivity(intent);
						} else if (id == btnRegRecord.getId()) {
							Intent intent = new Intent();
							intent.setClass(getActivity(),
									MerchantInfoActivity.class);
							intent.putExtra("id", recode.get(position).id);
							startActivity(intent);
						} else if (id == layout_select.getId()) {
							needDismiss = false;
						}
						if (needDismiss) {
							mSelectInfoDialog.dismiss();
							mSelectInfoDialog = null;
						}
					}
				};
				btnProductIndex.setOnClickListener(mClickListener);
				btnSjProduct.setOnClickListener(mClickListener);
				btnRegRecord.setOnClickListener(mClickListener);
				btnCancle.setOnClickListener(mClickListener);
				bg.setOnClickListener(mClickListener);
				layout_select.setOnClickListener(mClickListener);

				mSelectInfoDialog.show();
			}

		});
	}

	public void loadData() {
		AlertHelper.getInstance(getActivity()).showLoading(getString(R.string.loading_msg));
		String id = ((CarServerApplication) getActivity().getApplicationContext()).getLoginInfo().id;
		WebServiceHelper mwebserviceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.REGRECODE>() {

					@Override
					public void onSuccess(REGRECODE result) {
						LogHelper.i("tag", "result:" + result.data.size());
						if (result == null || result.data.size() <= 0) {
							if (recode.size()>0) {
								if (isLoadMore) {
									AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部...");
								} else {
									AlertHelper.getInstance(getActivity()).showCenterToast("刷新失败...");
								}
							}else{
								tvNoData.setVisibility(View.VISIBLE);
								lvclaim.setVisibility(View.GONE);
							}
						}
							
						if (!isLoadMore) {
							recode.clear();
						}
						recode.addAll(result.data);
						
						if(recodeAdapter == null){
							recodeAdapter = new RegRecodeAdapter(recode,getActivity());
							lvclaim.setAdapter(recodeAdapter);
						}else{
							recodeAdapter.notifyDataSetChanged();
						}
						ptrv.onHeaderRefreshComplete();
						ptrv.onFooterRefreshComplete();
						AlertHelper.getInstance(getActivity()).hideLoading();
					}

						@Override
						public void onFailure(int errorNo, String errorMsg) {
							if (recode.size()>0) {
								if (isLoadMore) {
									AlertHelper.getInstance(getActivity()).showCenterToast("已加载全部...");
								} else {
									AlertHelper.getInstance(getActivity()).showCenterToast("刷新失败...");
								}
							}else{
								tvNoData.setVisibility(View.VISIBLE);
//								lvclaim.setVisibility(View.GONE);
							}
							ptrv.onHeaderRefreshComplete();
							ptrv.onFooterRefreshComplete();
							AlertHelper.getInstance(getActivity()).hideLoading();
						}
					}, getActivity());

		if (isLoadMore) {
			pageNo++;
		} else {
			pageNo = 1;
		}
		mwebserviceHelper.getRegRecode(id, pageNo);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if(resultCode == Define.RESULT_FROM_ALTER_CAR){
		// loadData();
		// }
	}

	private void findView() {
		lvclaim = (FullListView) rootView.findViewById(R.id.lv_insuranceclaim);
		ptrv = (PullToRefreshView) rootView.findViewById(R.id.ptrv);
		tvNoData = (TextView) rootView.findViewById(R.id.tv_noData);
	}
}
