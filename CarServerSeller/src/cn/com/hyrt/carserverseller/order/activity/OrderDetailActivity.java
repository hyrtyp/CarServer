package cn.com.hyrt.carserverseller.order.activity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.BaseActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.ORDER_LIST.CDATA;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;

public class OrderDetailActivity extends BaseActivity{

	@ViewInject(id=R.id.tv_title) TextView tvTitle;
	@ViewInject(id=R.id.tv_customer) TextView tvCustomer;
	@ViewInject(id=R.id.tv_createtime) TextView tvCreateTime;
	@ViewInject(id=R.id.tv_accepttime) TextView tvAcceptTime;
	@ViewInject(id=R.id.tv_marktime) TextView tvMarkTime;
	@ViewInject(id=R.id.tv_arrivetime) TextView tvArriveTime;
	@ViewInject(id=R.id.tv_remark) TextView tvRemark;
	@ViewInject(id=R.id.tv_content) TextView tv_content;//订单详情
	@ViewInject(id=R.id.layout_control) LinearLayout layoutControl;
	@ViewInject(id=R.id.btn_refusal,click="refusal") Button btnRefusal;
	@ViewInject(id=R.id.btn_accept,click="accept") Button btnAccept;
	@ViewInject(id=R.id.tv_status) TextView tvStatus;
	@ViewInject(id=R.id.btn_submit,click="submit") Button btnSubmit;
	private Define.ORDER_LIST.CDATA orderInfo;
	private String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		orderInfo = (CDATA) intent.getSerializableExtra("vo");
		type = intent.getStringExtra("type");
		setContentView(R.layout.activity_order_detail);
		
		if(WebServiceHelper.ORDER_TYPE_NEW.equals(type)){
			tvAcceptTime.setVisibility(View.GONE);
			tvArriveTime.setVisibility(View.GONE);
			layoutControl.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.GONE);
		}else if(WebServiceHelper.ORDER_TYPE_AND.equals(type)){
			tvArriveTime.setVisibility(View.GONE);
			tvStatus.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.VISIBLE);
		}else if(WebServiceHelper.ORDER_TYPE_HIS.equals(type)){
			tvStatus.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.GONE);
		}
		
		initView();
	}
	
	private void initView(){
		tvTitle.setText(orderInfo.spname);
		tvCustomer.setText(orderInfo.phone);
		tv_content.setText(orderInfo.content);
		tvCreateTime.setText(StringHelper.formatDate(orderInfo.createtime));
		tvMarkTime.setText(StringHelper.formatDate(orderInfo.makedate));
		if(WebServiceHelper.ORDER_TYPE_AND.equals(type)){
			tvAcceptTime.setText(StringHelper.formatDate(orderInfo.accepttime));
			tvStatus.setText(Html.fromHtml("距离到店时间还有：<font color='#ffc105'>"
			+StringHelper.timeAfterDay(orderInfo.makedate)+"</font>"));
		}else if(WebServiceHelper.ORDER_TYPE_HIS.equals(type)){
			tvAcceptTime.setText(StringHelper.formatDate(orderInfo.accepttime));
			tvArriveTime.setText(StringHelper.formatDate(orderInfo.arrivetime));
			String status = orderInfo.status;
			LogHelper.i("tag", "status:"+status);
			if("ywc".equals(status)){
				tvStatus.setText("已完成");
			}else if("qx".equals(status)){
				tvStatus.setText("已取消");
			}else{
				if(orderInfo.arrivetime != null 
						&& !"".equals(orderInfo.arrivetime) 
						&& StringHelper.beyondTime(orderInfo.arrivetime)){
					tvStatus.setText("已过期");
				}
			}
		}
	}
	
	/**
	 * 拒绝
	 * @param view
	 */
	public void refusal(View view){
		saveOrderStatus(false);
	}
	
	/**
	 * 接受
	 * @param view
	 */
	public void accept(View view){
		saveOrderStatus(true);
	}
	
	public void submit(View view){
		AlertHelper.getInstance(this).showLoading(null);
		WebServiceHelper mSaveStatusHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						AlertHelper.getInstance(OrderDetailActivity.this).hideLoading();
						setResult(101);
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(OrderDetailActivity.this).hideLoading();
						AlertHelper.getInstance(OrderDetailActivity.this).showCenterToast("请求失败");
					}
		}, this);
		mSaveStatusHelper.saveOrderStatus(orderInfo.id, "ywc");
	}
	
	private void saveOrderStatus(boolean isAccept){
		AlertHelper.getInstance(this).showLoading(null);
		WebServiceHelper mSaveStatusHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.BASE>() {

					@Override
					public void onSuccess(BASE result) {
						AlertHelper.getInstance(OrderDetailActivity.this).hideLoading();
						setResult(101);
						finish();
					}

					@Override
					public void onFailure(int errorNo, String errorMsg) {
						AlertHelper.getInstance(OrderDetailActivity.this).hideLoading();
						AlertHelper.getInstance(OrderDetailActivity.this).showCenterToast("请求失败");
					}
		}, this);
		String status = isAccept ? WebServiceHelper.ORDER_STATUS_ACCEPT
				: WebServiceHelper.ORDER_STATUS_REFUSAL;
		mSaveStatusHelper.saveOrderStatus(orderInfo.id, status);
	}
}
