package cn.com.hyrt.carserver.knowledge.activity;

import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.widget.ListView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;

public class RelatedQuestionActivity extends BaseActivity{

	@ViewInject(id=R.id.lv_left) ListView lvLeft;
	@ViewInject(id=R.id.lv_right) ListView lvRight;
	private List<String> leftText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_related_question);
		loadData();
	}
	
	private void loadData(){
		WebServiceHelper mWebServiceHelper 
		= new WebServiceHelper(new WebServiceHelper.OnSuccessListener() {
			
			@Override
			public void onSuccess(String result) {
				ClassifyJsonParser classifyJsonParser = new ClassifyJsonParser();
				classifyJsonParser.parse(result);
				//一级分类
				 List<Map<String, String>> oneList =classifyJsonParser.getOneList();
				 LogHelper.i("tag", "oneList:"+oneList);
				
				//二级分类
				 List<List<Map<String, String>>> twoList = classifyJsonParser.getTwoList();
				 LogHelper.i("tag", "twoList:"+twoList);
				//三级分类
				 List<List<List<Map<String, String>>>> threeList = classifyJsonParser.getThreeList();
				 LogHelper.i("tag", "threeList:"+threeList);
			}
		}, this);
		mWebServiceHelper.getMaintainCheck();
	}
}
