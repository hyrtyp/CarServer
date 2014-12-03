package cn.com.hyrt.carserver.info.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.SEEK_REPLY_LIST;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.StringHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.base.view.PullToRefreshView;
import cn.com.hyrt.carserver.base.view.PullToRefreshView.OnFooterRefreshListener;
import cn.com.hyrt.carserver.base.view.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 最新咨询&提问历史
 * @author zoe
 *
 */
public class QuestionActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_question) ListView lvQuestion;
	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.tv_noData) TextView tvNoData;
	
	public static final int TYPE_NEW = 0;
	public static final int TYPE_HISTORY = 1;
	
	private int loadStatus;
	
	private int STATUS_REFRESH = 2;
	private int STATUS_LOAD_MORE = 3;
	
	private int type;
	private int page = 1;
	private List<Map<String, Object>> datas = new ArrayList<Map<String,Object>>();
	private SimpleAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		Intent intent = getIntent();
		type = intent.getIntExtra("type", TYPE_NEW);
		if(type == TYPE_HISTORY){
			setTitle(getString(R.string.info_question_history));
		}else{
			setTitle(getString(R.string.info_new_question));
		}
		
		setListener();
		
		AlertHelper.getInstance(this).showLoading(null);
		loadData(false);
		
	}
	
	private void loadData(final boolean isLoadMore){
		if(isLoadMore){
			page++;
			loadStatus = STATUS_LOAD_MORE;
		}else{
			page = 1;
			loadStatus = STATUS_REFRESH;
		}
		
		if(type == TYPE_HISTORY){
			mWebServiceHelper.getSeekReplyList(page, WebServiceHelper.SEEK_REPLY_HISTORY);
		}else{
			mWebServiceHelper.getSeekReplyList(page, WebServiceHelper.SEEK_REPLY_QUESTION);
		}
		
	}
	
	private WebServiceHelper mWebServiceHelper = new WebServiceHelper(
			new WebServiceHelper.RequestCallback<Define.SEEK_REPLY_LIST>() {
				@Override
				public void onSuccess(SEEK_REPLY_LIST result) {
					setData(result);
				}

				@Override
				public void onFailure(int errorNo, String errorMsg) {
					ptrv.onHeaderRefreshComplete();
					ptrv.onFooterRefreshComplete();
					if(loadStatus == STATUS_LOAD_MORE && datas.size() > 0){
						AlertHelper.getInstance(QuestionActivity.this).showCenterToast(R.string.load_done);
					}else{
						datas.clear();
						tvNoData.setVisibility(View.VISIBLE);
//						lvQuestion.setVisibility(View.INVISIBLE);
					}
					LogHelper.i("tag", "msg:"+errorMsg);
					AlertHelper.getInstance(QuestionActivity.this).hideLoading();
					loadStatus = 0;
				}
	}, this);
	
	private void setData(SEEK_REPLY_LIST result){
		if(loadStatus == STATUS_REFRESH){
			datas.clear();
		}
		ptrv.onHeaderRefreshComplete();
		ptrv.onFooterRefreshComplete();
		AlertHelper.getInstance(QuestionActivity.this).hideLoading();
		LogHelper.i("tag", "result:"+result.data.size());
		if(result.data.size() <= 0){
			if(loadStatus == STATUS_LOAD_MORE && datas.size() > 0){
				AlertHelper.getInstance(QuestionActivity.this).showCenterToast(R.string.load_done);
			}else{
				datas.clear();
				tvNoData.setVisibility(View.VISIBLE);
//				lvQuestion.setVisibility(View.INVISIBLE);
			}
		}else{
			tvNoData.setVisibility(View.GONE);
			lvQuestion.setVisibility(View.VISIBLE);
		}
		
		for(int i=0,j=result.data.size(); i<j; i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("classname", result.data.get(i).classname);
			map.put("content", result.data.get(i).content);
			map.put("seekdate", StringHelper.getFriendlydate(result.data.get(i).seekdate));
			map.put("consultationid", result.data.get(i).consultationid);
			String status = result.data.get(i).status;
			if("gb".equals(status) || "fb".equals(status)){
				map.put("status", getString(R.string.info_status_ygb));
			}else if("wjj".equals(status)){
				map.put("status", getString(R.string.info_status_wjj));
			}else{
				map.put("status", getString(R.string.info_status_pdz));
			}
			datas.add(map);
		}
		
		if(mAdapter == null){
			String[] from = new String[]{
					"classname", "content",
					"seekdate", "status"};
			int[] to = new int[]{
					R.id.tv_classname,
					R.id.tv_content,
					R.id.tv_seekdate,
					R.id.tv_status
			};
			mAdapter = new SimpleAdapter(
					QuestionActivity.this,
					datas, R.layout.layout_question_item,
					from, to);
			lvQuestion.setAdapter(mAdapter);
		}else{
			mAdapter.notifyDataSetChanged();
		}
		loadStatus = 0;
	}
	
	private void setListener(){
		ptrv.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(PullToRefreshView view) {
				loadData(false);
			}
		});
		
		ptrv.setOnFooterRefreshListener(new OnFooterRefreshListener() {
			
			@Override
			public void onFooterRefresh(PullToRefreshView view) {
				loadData(true);
			}
		});
		
		lvQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				LogHelper.i("tag", "lvQueston:"+datas.get(position).get("consultationid"));
				Intent intent = new Intent();
				intent.setClass(QuestionActivity.this, QuestionDetailActivity.class);
				intent.putExtra("type", type);
				String replyId = (String) datas.get(position).get("consultationid");
				intent.putExtra("replyId", replyId);
				startActivityForResult(intent, 0);
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int arg0, int resultCode, Intent arg2) {
		super.onActivityResult(arg0, resultCode, arg2);
		if(resultCode == 0){
			AlertHelper.getInstance(QuestionActivity.this).showLoading(null);
			loadData(false);
		}
	}
}
