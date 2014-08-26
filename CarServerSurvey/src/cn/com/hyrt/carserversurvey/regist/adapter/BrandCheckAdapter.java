package cn.com.hyrt.carserversurvey.regist.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BrandCheckAdapter extends BaseAdapter{
	private List<Map<String, String>> oneList;
	private List<List<Map<String, String>>> twoList;
	
	private List<Map<String, String>> datas = new ArrayList<Map<String,String>>();
	
	private Context mContext;
	
	private int count;
	List<Integer> pinyinIndex = new ArrayList<Integer>();
	private AbsListView.LayoutParams carParams;
	private AbsListView.LayoutParams pinyinParams;
	private LinearLayout.LayoutParams imgParams;
	
	private boolean ignoreCheckChange = false;
	
	private List<String> checkedId = new ArrayList<String>();
	private List<String> checkedName = new ArrayList<String>();
	
	public BrandCheckAdapter(List<Map<String, String>> oneList,
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
		return count+1;
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
	public View getView(final int position, View convertView, final ViewGroup parentView) {
		boolean isPinyin = true;
		if(convertView == null){
			if(pinyinIndex.contains(position-1)){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_checkbrand_one_item, null);
				isPinyin = true;
			}else{
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_checkbrand_two_item, null);
				isPinyin = false;
			}
		}else{
			LogHelper.i("tag", "convertView.getTag():"+convertView.getTag());
			isPinyin = (Boolean) convertView.getTag();
			if(pinyinIndex.contains(position-1) && !isPinyin){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_checkbrand_one_item, null);
				isPinyin = true;
			}else if((!pinyinIndex.contains(position-1) || position == 0) && isPinyin){
				convertView = LayoutInflater.from(mContext)
						.inflate(R.layout.layout_checkbrand_two_item, null);
				isPinyin = false;
			}
		}
		
		Map<String, String> data = null;
		if(position != 0){
			data = datas.get(position-1);
		}
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
		CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
		if(position == 0){
//			tvName.setTag(new String[]{data.get("id"), data.get("name")});
			tvName.setText("全部");
			if(checkedId.contains("-1")){
				checkBox.setChecked(true);
			}
			String[] mTag = new String[]{"-1", "-1"};
			checkBox.setTag(mTag);
		}else{
			tvName.setText(data.get("name"));
			if(!isPinyin){
//				ImageLoaderView ivImg = (ImageLoaderView) convertView.findViewById(R.id.iv_img);
//				ivImg.setImageUrl(data.get("attacpath"));
				tvName.setTag(new String[]{data.get("id"), data.get("name")});
				if(checkedId.contains(data.get("id"))){
					checkBox.setChecked(true);
				}
				checkBox.setTag(new String[]{data.get("id"), data.get("name")});
//				ivImg.setVisibility(View.VISIBLE);
			}else{
				tvName.setTag(null);
			}
		}
		
		final String id = data != null ? data.get("id") : "-1";
		if(!isPinyin){
			checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(position == 0 && !ignoreCheckChange){
						for(int i=0,j=parentView.getChildCount(); i<j; i++){
							CheckBox mCheckBox = (CheckBox) parentView.getChildAt(i).findViewById(R.id.checkbox);
							if(mCheckBox != null){
								mCheckBox.setChecked(isChecked);
//								if(isChecked){
//									String[] mTag = (String[]) mCheckBox.getTag();
//									checkedId.add(mTag[0]);
//									checkedName.add(mTag[1]);
//								}else{
//									checkedId.clear();
//									checkedName.clear();
//								}
							}
						}
					}else{
						CheckBox mCheckBox = (CheckBox) parentView.getChildAt(0).findViewById(R.id.checkbox);
						String[] mTag = (String[]) parentView.getChildAt(position).findViewById(R.id.checkbox).getTag();
						if(!isChecked){
							ignoreCheckChange = true;
							mCheckBox.setChecked(false);
							checkedId.remove(mTag[0]);
							checkedName.remove(mTag[1]);
						}else{
							checkedId.add(mTag[0]);
							checkedName.add(mTag[1]);
						}
					}
					ignoreCheckChange = false;
				}
			});
		}
		
		
		convertView.setTag(isPinyin);
		return convertView;
	}
}
