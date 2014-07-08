package cn.com.hyrt.carserver.question.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.activity.BaseActivity;
import cn.com.hyrt.carserver.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.WebServiceHelper;
import cn.com.hyrt.carserver.knowledge.activity.RelatedQuestionActivity;
import cn.com.hyrt.carserver.question.adapter.BrandAdapter;
import cn.com.hyrt.carserver.question.fragment.BrandFragment;

/**
 * 按品牌找
 * @author zoe
 *
 */
public class FindByBrandActivity extends BaseActivity{
	
	private List<Map<String, String>> oneList 
	= new ArrayList<Map<String,String>>();
	
	private List<List<Map<String, String>>> twoList 
	= new ArrayList<List<Map<String,String>>>();
	
	private List<List<List<Map<String, String>>>> threeList
	= new ArrayList<List<List<Map<String,String>>>>();
	
	public DrawerLayout drawerLayout;
	private LinearLayout leftDrawer;

	private Map<String, List<Map<String, String>>> modeMap 
	= new HashMap<String, List<Map<String,String>>>();
	
	@ViewInject(id=R.id.lv_content) ListView lvContent;

	private ArrayAdapter<String> mAdapter;

	private List<String> data = new ArrayList<String>();
	
	private String curBrandId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_by_brand);
		
		drawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
		leftDrawer = (LinearLayout) findViewById(R.id.left_drawer);
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		BrandFragment fragment = new BrandFragment();
		transaction.add(R.id.main_content, fragment);
		transaction.commit();
		setListener();
	}
	
	private void setListener(){
		lvContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				List<Map<String, String>> mList = modeMap.get(curBrandId);
				String id = mList.get(position).get("id");
				Intent intent = new Intent();
				intent.setClass(FindByBrandActivity.this, CorrelationActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
	}
	
	public void setData(
			List<Map<String, String>> oneList,
			List<List<Map<String, String>>> twoList,
			List<List<List<Map<String, String>>>> threeList){
		this.oneList.addAll(oneList);
		this.twoList.addAll(twoList);
		this.threeList.addAll(threeList);
		modeMap.clear();
		for(int i=0,j=threeList.size(); i<j; i++){
			List<List<Map<String, String>>> cList = threeList.get(i);
			for(int a=0,b=cList.size(); a<b; a++){
				List<Map<String, String>> ccList = cList.get(a);
				modeMap.put(twoList.get(i).get(a).get("id"), ccList);
			}
		}
	}
	
	public void open(String id){
		if (drawerLayout.isDrawerOpen(leftDrawer)) {
			drawerLayout.closeDrawer(leftDrawer);
		} else {
			curBrandId = id;
			List<Map<String, String>> mList = modeMap.get(id);
			data.clear();
			for(int i=0,j=mList.size(); i<j; i++){
				data.add(mList.get(i).get("name"));
			}
			if(mAdapter == null){
				mAdapter = new ArrayAdapter<String>(
						this, R.layout.layout_brand_two_item,
						R.id.tv_name, data);
				lvContent.setAdapter(mAdapter);
			}else{
				mAdapter.notifyDataSetChanged();
			}
			
			
			drawerLayout.openDrawer(leftDrawer);
		}
	}
}
