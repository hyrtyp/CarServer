package cn.com.hyrt.carserversurvey.info.adapter;

import java.util.List;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.REGRECODE;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.REGRECODE.CDATA;
import cn.com.hyrt.carserversurvey.base.helper.StringHelper;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegRecodeAdapter extends BaseAdapter {


	private List<Define.REGRECODE.CDATA> iccs;
	private Context mContext;
	
	public RegRecodeAdapter(List<CDATA> iccs, Context mContext) {
		super();
		this.iccs = iccs;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return iccs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return iccs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup arg2) {
		if(convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_regrecodeadapter, null);
		}
		final ImageLoaderView ivFaceImg = (ImageLoaderView) convertView.findViewById(R.id.iv_ic_img);
		final TextView regname = (TextView) convertView.findViewById(R.id.tv_recode_name);
		final TextView  regadd= (TextView) convertView.findViewById(R.id.tv_recode_address);
		final TextView regdate =(TextView)convertView.findViewById(R.id.tv_recode_regdata); 

		
		Define.REGRECODE.CDATA icc = iccs.get(position);
		ivFaceImg.setImageUrl(icc.imagepath);
		regname.setText(icc.sjname);
		regadd.setText("地址："+icc.sjaddress);
		String date = StringHelper.formatDate(icc.peopledate);
		regdate.setText("注册时间："+date);
		return convertView;
	}

	
}
