package cn.com.hyrt.carserversurvey.info.adapter;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.REGRECODE;
import cn.com.hyrt.carserversurvey.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RegRecodeAdapter extends BaseAdapter {


	private Define.REGRECODE ic;
	private Context mContext;
	
	public RegRecodeAdapter(REGRECODE ic, Context mContext) {
		super();
		this.ic = ic;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return ic.data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return ic.data.get(arg0);
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

		
		Define.REGRECODE.CDATA icc = ic.data.get(position);
		ivFaceImg.setImageUrl(icc.recodeimagepath);
		regname.setText(icc.sjname);
		regadd.setText(icc.sjaddress);
		regdate.setText(icc.peopledate);
		return convertView;
	}

	
}
