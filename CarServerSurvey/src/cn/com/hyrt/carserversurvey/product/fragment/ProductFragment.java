package cn.com.hyrt.carserversurvey.product.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.kobjects.base64.Base64;

import cn.com.hyrt.carserversurvey.R;
import cn.com.hyrt.carserversurvey.base.adapter.AddPhotoGridAdapter;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define;
import cn.com.hyrt.carserversurvey.base.baseFunction.Define.INFO_PRODUCT;
import cn.com.hyrt.carserversurvey.base.helper.AlertHelper;
import cn.com.hyrt.carserversurvey.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserversurvey.base.helper.FileHelper;
import cn.com.hyrt.carserversurvey.base.helper.LogHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoHelper;
import cn.com.hyrt.carserversurvey.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserversurvey.base.helper.StringHelper;
import cn.com.hyrt.carserversurvey.base.helper.WebServiceHelper;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * 上架商品
 * @author zoe
 *
 */
public class ProductFragment extends Fragment{

	private View rootView;
	private GridView gvProductPhoto;
	
	private AddPhotoGridAdapter productAdapter;
	
	private List<Bitmap> productPhotos = new  ArrayList<Bitmap>();
	
	private Uri faceUri;
	private PhotoHelper mPhotoHelper;
	private String imgBuffer;
	private Button btnSubmit;
	private TextView et_proName;
	private TextView et_proPrice;
	private TextView et_proDisPrice;
	private TextView et_proDec;
	private RadioGroup isproductservice;
	private RadioButton rb_product;
	private RadioButton rb_service;
	
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

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				submit();
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
	
	private void submit(){

		String productname = et_proName.getText().toString();
		String curpirce = et_proPrice.getText().toString();
		String discountprice = et_proDisPrice.getText().toString();
		String productdec = et_proDec.getText().toString();
		String istype="";
		if("".equals(productname) || productname == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "商品名称"));
			return;
		}
		if("".equals(curpirce) || curpirce == null){
			AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "售出价格"));
			return;
		}else{
			if(!StringHelper.isNumeric(curpirce)){
				AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "售出价格必须为数字"));
				return;
			}
		}
		if("".equals(discountprice) || discountprice == null){
			if(!StringHelper.isNumeric(curpirce)){
				AlertHelper.getInstance(getActivity()).showCenterToast(String.format(getString(R.string.text_not_null), "折扣价格必须为数字"));
				return;
			}
		}
		String issp = String.valueOf(isproductservice.getCheckedRadioButtonId()); 

		if(issp.equals(String.valueOf(rb_product.getId()))){
			istype="sp";
		}else{
			istype="fw";
		}
		
		StringBuffer pdPhoto = new StringBuffer("");
		StringBuffer pdPhotoName = new StringBuffer("");
		for(int i=0,j=productPhotos.size(); i<j; i++){
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Bitmap mBitmap = productPhotos.get(i);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			if(i == j-1){
				pdPhoto.append(new String(Base64.encode(baos.toByteArray())));
				pdPhotoName.append("sjphoto"+(i+1)+".jpeg");
			}else{
				pdPhoto.append(new String(Base64.encode(baos.toByteArray()))+";");
				pdPhotoName.append("sjphoto"+(i+1)+".jpeg;");
			}
		}
		
		Define.INFO_PRODUCT productInfo = new Define.INFO_PRODUCT();
		productInfo.spname=productname;
		productInfo.price=curpirce;
		productInfo.discount=discountprice;
		productInfo.sptitle=productdec;
		productInfo.type=istype;
		productInfo.serviceid = "1";	
		productInfo.imagepath0=pdPhoto.toString();
		productInfo.imagename0=pdPhotoName.toString();

		//调用保存商品接口saveMerchantComm
		AlertHelper.getInstance(getActivity()).showLoading(null);
		WebServiceHelper SaveProductHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.INFO_PRODUCT>() {

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				AlertHelper.getInstance(getActivity()).hideLoading();
			}

			@Override
			public void onSuccess(INFO_PRODUCT result) {
				AlertHelper.getInstance(getActivity()).hideLoading();
				
			}
		}, getActivity());
		SaveProductHelper.saveProductInfo(productInfo);
		
	}
	
	private void findView(){
		gvProductPhoto = (GridView) rootView.findViewById(R.id.gv_product_photo);
		et_proName = (TextView) rootView.findViewById(R.id.et_proName);
		et_proPrice = (TextView) rootView.findViewById(R.id.et_proPrice);
		et_proDisPrice = (TextView) rootView.findViewById(R.id.et_proDisPrice);
		et_proDec = (TextView) rootView.findViewById(R.id.et_proDec); 
		isproductservice = (RadioGroup)rootView.findViewById(R.id.rg_issp);
		rb_product=(RadioButton)rootView.findViewById(R.id.rb_product);
		rb_service=(RadioButton)rootView.findViewById(R.id.rb_service);
		
		btnSubmit =(Button)rootView.findViewById(R.id.btn_prodectsubmit);
		if(productAdapter == null){
			productAdapter = new AddPhotoGridAdapter(productPhotos, getActivity());
		}
		gvProductPhoto.setAdapter(productAdapter);
	}
}
