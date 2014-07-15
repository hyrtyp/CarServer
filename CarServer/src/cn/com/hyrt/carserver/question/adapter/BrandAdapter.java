package cn.com.hyrt.carserver.question.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.helper.LogHelper;
import cn.com.hyrt.carserver.base.helper.ScreenHelper;
import cn.com.hyrt.carserver.base.view.ImageLoaderView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class BrandAdapter extends BaseAdapter{

	private List<Map<String, String>> oneList;
	private List<List<Map<String, String>>> twoList;
	
	private List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
	
	private Context mContext;
	
	private int count;
	List<Integer> pinyinIndex = new ArrayList<Integer>();
	private AbsListView.LayoutParams carParams;
	private AbsListView.LayoutParams pinyinParams;
	private LinearLayout.LayoutParams imgParams;
	
	public BrandAdapter(List<Map<String, String>> oneList,
			List<List<Map<String, String>>> twoList, Context mContext) {
		super();
		this.oneList = oneList;
		this.twoList = twoList;
		this.mContext = mContext;
		this.count = 0;
		
		for(int i=0,j=oneList.size(); i<j; i++){
			List<Map<String, String>> mList = twoList.get(i);
			pinyinIndex.add(count);
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", oneList.get(i).get("id"));
			map.put("name", oneList.get(i).get("name"));
			datas.add(map);
			this.count++;
			for(int a=0,b=mList.size(); a<b; a++){
				Map<String, String> cmap = new HashMap<String, String>();
				LogHelper.i("tag", "mList:"+mList);
				cmap.put("id", mList.get(a).get("id"));
				cmap.put("name", mList.get(a).get("name"));
				cmap.put("attacpath", mList.get(a).get("attacpath"));
				datas.add(cmap);
				this.count++;
			}
			
		}
		LogHelper.i("tag", "datas:"+datas);
		LogHelper.i("tag", "count:"+this.count+" pinyin:"+pinyinIndex);
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public Object getItem(int arg0) {
		return datas.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parentView) {
		boolean isPinyin = true;
		if(convertView == null){
			if(pinyinIndex.contains(position)){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_brand_one_item, null);
				isPinyin = true;
			}else{
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_brand_two_item, null);
				isPinyin = false;
			}
		}else{
			LogHelper.i("tag", "convertView.getTag():"+convertView.getTag());
			isPinyin = (Boolean) convertView.getTag();
			if(pinyinIndex.contains(position) && !isPinyin){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_brand_one_item, null);
				isPinyin = true;
			}else if(!pinyinIndex.contains(position) && isPinyin){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_brand_two_item, null);
				isPinyin = false;
			}
		}
		
		Map<String, String> data = datas.get(position);
		
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		tvName.setText(data.get("name"));
		if(!isPinyin){
			ImageLoaderView ivImg = (ImageLoaderView) convertView.findViewById(R.id.iv_img);
			ivImg.setImageUrl(data.get("attacpath"));
			tvName.setTag(new String[]{data.get("id"), data.get("name")});
			ivImg.setVisibility(View.VISIBLE);
		}else{
			tvName.setTag(null);
		}
		
		
		
		convertView.setTag(isPinyin);
		return convertView;
	}
	

}
