package cn.com.hyrt.carserversurvey.product.fragment;

import java.util.ArrayList;
import java.util.List;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.adapter.AddPhotoGridAdapter;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoPopupHelper;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class ProductFragment extends Fragment{

	private View rootView;
	private GridView gvProductPhoto;
	
	private AddPhotoGridAdapter productAdapter;
	
	private List<Bitmap> productPhotos = new  ArrayList<Bitmap>();
	
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private String imgBuffer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_product, null);
		findView();
		setListener();
		loadData();
		return rootView;
	}
	
	private void loadData(){
		
	}
	
	private void setListener(){
		productAdapter.setCallback(new AddPhotoGridAdapter.PhotoGridCallback() {
			
			@Override
			public void onClick(int position) {
				if(position == -1){
					addPhoto();
				}else{
					PhotoPopupHelper.showPop(productPhotos.get(position), getActivity());
				}
			}

			@Override
			public void onLongClick(int position) {
				if(position == -1){
					addPhoto();
				}else{
					delPhoto(position);
				}
			}
		});
	}
	
	private void addPhoto(){
		if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 50);
		mPhotoHelper.getPhoto();
	}
	
	private void delPhoto(final int position){
		AlertDialog.Builder mDelPhotoDialog = new Builder(getActivity());
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
					productPhotos.remove(position);
					productAdapter.notifyDataSetChanged();
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		AlertHelper.getInstance(getActivity()).showCenterToast("onActivityResult:"+resultCode);
		if (resultCode == 0) {
			return;
		}
		
        if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                	productPhotos.add(bitmap);
                	productAdapter.notifyDataSetChanged();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 50);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 50);
        }
	}
	
	private void findView(){
		gvProductPhoto = (GridView) rootView.findViewById(R.id.gv_product_photo);
		
		if(productAdapter == null){
			productAdapter = new AddPhotoGridAdapter(productPhotos, getActivity());
		}
		gvProductPhoto.setAdapter(productAdapter);
	}
}
