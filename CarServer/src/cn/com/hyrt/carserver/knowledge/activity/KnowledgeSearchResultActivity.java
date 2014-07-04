package cn.com.hyrt.carserver.knowledge.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.Define;
import cn.com.hyrt.carserver.base.baseFunction.Define.QUESTION_SEARCH_RESULT;
import cn.com.hyrt.carserver.base.helper.AlertHelper;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class KnowledgeSearchResultActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_content)ListView lv_content;
	
	private String searchStr;
	
	private int pageNo = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		searchStr = intent.getStringExtra("str");
		AlertHelper.getInstance(this).showLoading(null);
		loadData();
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper = new WebServiceHelper(
				new WebServiceHelper.RequestCallback<Define.QUESTION_SEARCH_RESULT>() {

					@Override
					public void onSuccess(QUESTION_SEARCH_RESULT result) {
						AlertHelper.getInstance(KnowledgeSearchResultActivity.this).hideLoading();
						LogHelper.i("tag", "result:"+result.data.size());
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(KnowledgeSearchResultActivity.this).hideLoading();
						AlertHelper.getInstance(KnowledgeSearchResultActivity.this).showCenterToast(R.string.info_load_fail);
						
					}
		}, this);
		mWebServiceHelper.searchQuestion(searchStr, pageNo);
	}
}
