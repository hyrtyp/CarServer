package cn.com.hyrt.carserverseller.info.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.INFO_AUDIT_LIST;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.PullToRefreshView;
import cn.com.hyrt.carserverseller.info.fragment.AuditListFragment;
import cn.com.hyrt.carserverseller.preferential.fragment.PreferentialListFragment;
import cn.com.hyrt.carserverseller.shop.fragment.ProductListFragment;

/**
 * 审核列表
 * @author zoe
 *
 */
public class AuditListActivity extends BaseActivity{

	@ViewInject(id=R.id.ptrv) PullToRefreshView ptrv;
	@ViewInject(id=R.id.listview) ListView listview;
	@ViewInject(id=R.id.tv_nodata) TextView tvNoData;
	@ViewInject(id=R.id.btn_product) Button btnProduct;
	@ViewInject(id=R.id.btn_service) Button btnService;
	@ViewInject(id=R.id.btn_preferential) Button btnPreferential;
	
	private AuditListFragment mSpAuditListFragment;
	private AuditListFragment mFwAuditListFragment;
	private AuditListFragment mYhAuditListFragment;
	
	private int tabPosition = 0;
	private int curPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audit_list);
		
		mSpAuditListFragment = new AuditListFragment(WebServiceHelper.PRODUCT_TYPE_SP);
		getSupportFragmentManager()
		.beginTransaction()
		.add(R.id.layout_content, mSpAuditListFragment)
		.commit();
		setListener();
	}
	
	private void setListener(){
		btnProduct.setOnClickListener(mTabOnClickListener);
		btnService.setOnClickListener(mTabOnClickListener);
		btnPreferential.setOnClickListener(mTabOnClickListener);
	}
	
	private View.OnClickListener mTabOnClickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View view) {
			focusManage(false, tabPosition);
			int id = view.getId();
			if(id == btnProduct.getId()){
				tabPosition = 0;
				focusManage(true, tabPosition);
			}else if(id == btnService.getId()){
				tabPosition = 1;
				focusManage(true, tabPosition);
			}else if(id == btnPreferential.getId()){
				tabPosition = 2;
				focusManage(true, tabPosition);
			}
		}
	};
	
	private void focusManage(boolean hasFocus, int position){
		if(hasFocus){
			changeFragment(position);
			switch (position) {
			case 0:
				btnProduct.setBackgroundResource(R.drawable.bg_tab_left_focus);
				btnProduct.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 1:
				btnService.setBackgroundResource(R.drawable.bg_tab_center_focus);
				btnService.setTextColor(getResources().getColor(android.R.color.white));
				break;
			case 2:
				btnPreferential.setBackgroundResource(R.drawable.bg_tab_right_focus);
				btnPreferential.setTextColor(getResources().getColor(android.R.color.white));
				break;
			}
		}else{
			switch (position) {
			case 0:
				btnProduct.setBackgroundResource(R.drawable.bg_tab_left);
				btnProduct.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 1:
				btnService.setBackgroundResource(R.drawable.bg_tab_center);
				btnService.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			case 2:
				btnPreferential.setBackgroundResource(R.drawable.bg_tab_right);
				btnPreferential.setTextColor(getResources().getColor(R.color.login_blue));
				break;
			}
		}
		
	}
	
	private void changeFragment(int position){
		if(position == curPosition){
			return;
		}
		switch (position) {
		case 0:
			if(mSpAuditListFragment == null){
				mSpAuditListFragment = new AuditListFragment(WebServiceHelper.PRODUCT_TYPE_SP);
				getSupportFragmentManager().beginTransaction()
				.add(R.id.layout_content, mSpAuditListFragment).commit();
			}else{
				getSupportFragmentManager().beginTransaction().show(mSpAuditListFragment).commit();
			}
			break;
		case 1:
			if(mFwAuditListFragment == null){
				mFwAuditListFragment = new AuditListFragment(WebServiceHelper.PRODUCT_TYPE_FW);
				getSupportFragmentManager().beginTransaction()
				.add(R.id.layout_content, mFwAuditListFragment).commit();
			}else{
				getSupportFragmentManager().beginTransaction().show(mFwAuditListFragment).commit();
			}
			
			break;
		case 2:
			if(mYhAuditListFragment == null){
				mYhAuditListFragment = new AuditListFragment(WebServiceHelper.PRODUCT_TYPE_YH);
				getSupportFragmentManager().beginTransaction()
				.add(R.id.layout_content, mYhAuditListFragment).commit();
			}else{
				getSupportFragmentManager().beginTransaction().show(mYhAuditListFragment).commit();
			}
			break;
		default:
			break;
		}
		
		switch (curPosition) {
		case 0:
			if(mSpAuditListFragment != null){
				getSupportFragmentManager().beginTransaction().hide(mSpAuditListFragment).commit();
			}
			break;
		case 1:
			if(mFwAuditListFragment != null){
				getSupportFragmentManager().beginTransaction().hide(mFwAuditListFragment).commit();
			}
			break;
		case 2:
			if(mYhAuditListFragment != null){
				getSupportFragmentManager().beginTransaction().hide(mYhAuditListFragment).commit();
			}
			break;
		default:
			break;
		}
		
		curPosition = position;
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1 == 101){
			if(mSpAuditListFragment != null){
				mSpAuditListFragment.loadData(false);
			}
			if(mFwAuditListFragment != null){
				mFwAuditListFragment.loadData(false);
			}
			if(mYhAuditListFragment != null){
				mYhAuditListFragment.loadData(false);
			}
		}
	}
	
}
