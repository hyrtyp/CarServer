package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_CORRELATION;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.question.adapter.CorrelationAdapter;

/**
 * 相关问题
 * 
 * @author Administrator
 * 
 */
public class CorrelationActivity extends BaseActivity {

	@ViewInject(id = R.id.ptrv)
	PullToRefreshView ptrv;

	private String id;
	private String name;
	private String title;
	private Context context;

	ListView ls_correlation;
	private CorrelationAdapter correlationAdapter;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	private int pageNo = 1;
	private boolean isLoadMore = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_question_correlation);
		context = CorrelationActivity.this;

		Intent intent = getIntent();
		title = intent.getStringExtra("title");

		if (title == null) {

			setTitle(getResources().getString(R.string.question_correlation));
		} else {

			setTitle(title);
		}

		ls_correlation = (ListView) findViewById(R.id.ls_correlation);
		id = getIntent().getExtras().getString("id");
		name = getIntent().getExtras().getString("name");
		AlertHelper.getInstance(this).showLoading(null);
		setListener();
		loadData();
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
	}

	private void loadData() {
		WebServiceHelper mCarInfoServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.QUESTION_CORRELATION>() {
					@SuppressWarnings("unused")
					@Override
					public void onSuccess(QUESTION_CORRELATION result) {
						AlertHelper.getInstance(CorrelationActivity.this)
								.hideLoading();
						if (result == null) {
							AlertHelper.getInstance(CorrelationActivity.this)
									.showCenterToast(R.string.info_load_fail);
							setResult(Define.RESULT_FROM_ALTER_CAR);
							finish();
						} else {
							if (result == null && data.size() > 0) {
								AlertHelper.getInstance(
										CorrelationActivity.this)
										.showCenterToast(R.string.load_done);
							}

							if (!isLoadMore) {
								data.clear();
								ptrv.onHeaderRefreshComplete();
							} else {
								ptrv.onFooterRefreshComplete();
							}

							isLoadMore = false;

							data.clear();
							for (int i = 0, j = result.data.size(); i < j; i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("content", result.data.get(i).content);
								map.put("zyname", result.data.get(i).zyname);
								map.put("username", result.data.get(i).username);
								map.put("seekdate", result.data.get(i).seekdate);
								map.put("userid", result.data.get(i).userid);
								map.put("seektype", result.data.get(i).seektype);
								map.put("attacpathname",
										result.data.get(i).attacpathname);
								map.put("terminalid",
										result.data.get(i).terminalid);
								map.put("attacpath",
										result.data.get(i).attacpath);
								map.put("id", result.data.get(i).id);
								data.add(map);
							}

							if (correlationAdapter == null) {
								correlationAdapter = new CorrelationAdapter(
										context, data, name);
								ls_correlation.setAdapter(correlationAdapter);
								;
							} else {
								correlationAdapter.notifyDataSetChanged();
							}
						}
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(CorrelationActivity.this)
								.hideLoading();
						AlertHelper.getInstance(CorrelationActivity.this)
								.showCenterToast(R.string.info_load_fail);
						setResult(Define.RESULT_FROM_ALTER_CAR);
						ptrv.onFooterRefreshComplete();
						finish();
					}
				}, this);

		if (isLoadMore) {
			pageNo++;
		} else {
			pageNo = 1;
		}
		mCarInfoServiceHelper.getCorrelation(id, pageNo);
	}
}
