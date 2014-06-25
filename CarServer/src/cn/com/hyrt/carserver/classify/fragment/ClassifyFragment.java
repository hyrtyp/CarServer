package cn.com.hyrt.carserver.classify.fragment;

import cn.com.hyrt.carserver.R;
import cn.com.hyrt.carserver.base.adapter.PortalGridAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ClassifyFragment extends Fragment{
	
	private View rootView;
	private GridView gvMyInfo;
	private GridView gvFound;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_classify, null);
		findView();
		int[] imgArray = new int[]{R.drawable.classify_wxby,R.drawable.classify_pjgz, R.drawable.classify_nwzx, 
				R.drawable.classify_dzpj, R.drawable.classify_caryy, R.drawable.classify_loclser};
		int[] textSourceArray = new int[]{R.string.classify_wxby, R.string.classify_pjgz,R.string.classify_nwzx, 
				R.string.classify_dzpj,R.string.classify_caryy,R.string.classify_loclser};
		PortalGridAdapter mAdapter = new PortalGridAdapter(imgArray, textSourceArray, getActivity());
		gvMyInfo.setAdapter(mAdapter);
		
		int[] imgArrayf = new int[]{R.drawable.classify_zfw,R.drawable.classify_zsj};
		int[] textSourceArrayf = new int[]{R.string.classify_zfw, R.string.classify_zsj};
		PortalGridAdapter mfAdapter = new PortalGridAdapter(imgArrayf, textSourceArrayf, getActivity());
		gvFound.setAdapter(mfAdapter);
		return rootView;
	}
	
	private void findView(){
		gvMyInfo = (GridView) rootView.findViewById(R.id.gvkjrk);
		gvFound =(GridView) rootView.findViewById(R.id.gvServer);
	}
}
