package cn.com.hyrt.carserverseller.product.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.soundcloud.android.crop.Crop;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import cn.com.hyrt.carserverseller.R;
import cn.com.hyrt.carserverseller.base.activity.MainActivity;
import cn.com.hyrt.carserverseller.base.baseFunction.ClassifyJsonParser;
import cn.com.hyrt.carserverseller.base.baseFunction.Define;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.BASE;
import cn.com.hyrt.carserverseller.base.baseFunction.Define.SINGLE_ID;
import cn.com.hyrt.carserverseller.base.helper.AlertHelper;
import cn.com.hyrt.carserverseller.base.helper.BaseWebServiceHelper;
import cn.com.hyrt.carserverseller.base.helper.FileHelper;
import cn.com.hyrt.carserverseller.base.helper.LogHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoHelper;
import cn.com.hyrt.carserverseller.base.helper.PhotoPopupHelper;
import cn.com.hyrt.carserverseller.base.helper.StringHelper;
import cn.com.hyrt.carserverseller.base.helper.WebServiceHelper;
import cn.com.hyrt.carserverseller.base.view.ImageLoaderView;
import cn.com.hyrt.carserverseller.product.activity.ProductDetailActivity2;

public class ProductFragment extends Fragment{

	private View rootView;
	
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
	private ImageLoaderView ivProductPhoto;
	private TextView tvProductPhoto;
	private Bitmap productBitmap;
	private String productImgUrl;
	private String istype="sp";
	
	private RelativeLayout rl_setvicetype;
	private TextView tv_setvicetype;
	private List<String> spotIdOne = new ArrayList<String>();
	private List<String> spotNameOne = new ArrayList<String>();
	private Map<String, List<String>> spotIdTwo = new HashMap<String, List<String>>();
	private Map<String, List<String>> spotNameTwo = new HashMap<String, List<String>>();
	private Dialog mSpotDialogOne;
	private ListView ls_correlation;
	private ArrayAdapter<String> correAdapter;
	private String positionId = "";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_product, null);
		findView();
//		Intent intent = getIntent();
//		isAdd = intent.getBooleanExtra("isAdd", true);
//		shId = intent.getStringExtra("shId");
//		productInfo = (INFO_PRODUCT) intent.getSerializableExtra("vo");
//		if(isAdd){
//			setTitle("上架商品");
//			btnSubmit.setText("提交查看");
//		}else{
//			setTitle("编辑商品");
//			btnSubmit.setText("提交编辑");
//			loadData();
//		}
		setListener();
		return rootView;
	}
	
	private void setListener(){
		//RadioGroup checked 监听
		isproductservice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String issp = String.valueOf(isproductservice.getCheckedRadioButtonId()); 
				if(issp.equals(String.valueOf(rb_product.getId()))){
					istype="sp";
				}
				if (issp.equals(String.valueOf(rb_service.getId()))) {
					istype="fw";
				}
				LogHelper.i("tag", istype+"------");
			}
		});
		
		//TODO  商品分类
		rl_setvicetype.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadSpot();
			}
		});
		
		ivProductPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(productBitmap == null && productImgUrl == null){
					addPhoto();
				}else{
					if(productBitmap != null){
						PhotoPopupHelper.showPop(productBitmap, getActivity());
					}else{
						PhotoPopupHelper.showPop(productImgUrl, getActivity());
					}
				}
			}
		});
		ivProductPhoto.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if(productBitmap == null && productImgUrl == null){
					addPhoto();
				}else{
					delPhoto();
				}
				return true;
			}
		});
		
//		productAdapter.setCallback(new AddPhotoGridAdapter.PhotoGridCallback() {
//			
//			@Override
//			public void onClick(int position) {
//				if(position == -1){
//					addPhoto();
//				}else{
//					PhotoPopupHelper.showPop(productPhotos.get(position), getActivity());
//				}
//			}
//
//			@Override
//			public void onLongClick(int position) {
//				if(position == -1){
//					addPhoto();
//				}else{
//					delPhoto(position);
//				}
//			}
//		});

		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				submit();
			}
		});
	}
	
	private void addPhoto(){
			if (FileHelper.sdCardExist()) {
				if(faceUri == null){
					faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
				}
				mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
				mPhotoHelper.getPhoto();
			}else{
				AlertHelper.getInstance(getActivity()).showCenterToast("sd卡不存在");
			}
		
		/*if(faceUri == null){
			faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
		}
		mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
		mPhotoHelper.getPhoto();*/
	}
	
	/*private void delPhoto(final int position){
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
	}*/
	
	private void delPhoto(){
		AlertDialog.Builder mDelPhotoDialog = new Builder(getActivity());
		mDelPhotoDialog.setTitle("删除");
		mDelPhotoDialog.setMessage("是否删除？");
		mDelPhotoDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
					tvProductPhoto.setVisibility(View.VISIBLE);
					productBitmap = null;
					productImgUrl = null;
					ivProductPhoto.setImageResource(R.drawable.ic_photo_add);
			}
		});
		mDelPhotoDialog.setNegativeButton("取消", null);
		mDelPhotoDialog.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			return;
		}
		
        /*if (requestCode == PhotoHelper.PHOTO_ZOOM && data != null) {
            //保存剪切好的图片
        	LogHelper.i("tag", "data:"+data.getParcelableExtra("data")+"---"+data.getData());
        	
            if (data.getParcelableExtra("data") != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
//                	productPhotos.add(bitmap);
//                	productAdapter.notifyDataSetChanged();
                tvProductPhoto.setVisibility(View.GONE);
            	productBitmap = bitmap;
            	productImgUrl = null;
            	ivProductPhoto.setImageBitmap(bitmap);
            	
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//                imgBuffer = new String(Base64.encode(baos.toByteArray()));
//                StorageHelper.getInstance(getActivity()).saveTest(imgBuffer);
            }

        }else if (requestCode == PhotoHelper.FROM_CAMERA) {
            if(mPhotoHelper == null){
                if(faceUri == null){
                    faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
                }
                mPhotoHelper = new PhotoHelper(getActivity(), faceUri, 400);
            }
            mPhotoHelper.startPhotoZoom(faceUri, 400);
        }*/
		
		if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
			beginCrop(data.getData());
		} else if (requestCode == Crop.REQUEST_CROP) {
			handleCrop(resultCode, data);
		}else if (requestCode == PhotoHelper.FROM_CAMERA) {
			beginCrop(faceUri);
        } else if (resultCode == 101) {
			((MainActivity) getActivity()).changeActionBar(2);
			((MainActivity) getActivity()).mTabHost.setCurrentTab(2);
        }
	}
	
	private void beginCrop(Uri source) {
		if (FileHelper.sdCardExist()) {
			if(faceUri == null){
	            faceUri = Uri.fromFile(FileHelper.createFile1("face.jpg"));
	        }
	        new Crop(source).output(faceUri).asSquare().start(getActivity());
		}else{
			AlertHelper.getInstance(getActivity()).showCenterToast("sd卡不存在");
		}
		/*if(faceUri == null){
            faceUri = Uri.fromFile(FileHelper.createFile("face.jpg"));
        }
        new Crop(source).output(faceUri).asSquare().start(getActivity());*/
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {
//            resultView.setImageURI(Crop.getOutput(result));
        	Bitmap bitmap;
			try {
				bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(result));
				tvProductPhoto.setVisibility(View.GONE);
				productBitmap = bitmap;
				productImgUrl = null;
				ivProductPhoto.setImageBitmap(bitmap);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else if (resultCode == Crop.RESULT_ERROR) {
            AlertHelper.getInstance(getActivity()).showCenterToast(Crop.getError(result).getMessage());
        }
    }
	
	private void submit(){
		
//		if(true){
//			Intent intent = new Intent();
//			intent.setClass(getActivity(), ProductDetailActivity.class);
//			startActivity(intent);
//			return;
//		}
		
		String productname = et_proName.getText().toString();
		String curpirce = et_proPrice.getText().toString();
		String discountprice = et_proDisPrice.getText().toString();
		String productdec = et_proDec.getText().toString();
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
		
//		StringBuffer pdPhoto = new StringBuffer("");
//		StringBuffer pdPhotoName = new StringBuffer("");
//		for(int i=0,j=productPhotos.size(); i<j; i++){
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			Bitmap mBitmap = productPhotos.get(i);
//			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			if(i == j-1){
//				pdPhoto.append(new String(Base64.encode(baos.toByteArray())));
//				pdPhotoName.append("sjphoto"+(i+1)+".jpeg");
//			}else{
//				pdPhoto.append(new String(Base64.encode(baos.toByteArray()))+";");
//				pdPhotoName.append("sjphoto"+(i+1)+".jpeg;");
//			}
//		}
		
//		String pdPhoto = null;
//		String pdPhotoName = null;
//		if(productBitmap != null){
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			productBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			pdPhoto = new String(Base64.encode(baos.toByteArray()));
//			pdPhotoName = "sjPhoto.jpeg";
//		}
		
		Define.INFO_PRODUCT mProductInfo = new Define.INFO_PRODUCT();
		
		if(Integer.parseInt(curpirce) > Integer.parseInt(discountprice)){
			AlertHelper.getInstance(getActivity()).showCenterToast("售出价格不能超出折扣前价");
			return;
		}
		mProductInfo.spname=productname;
		mProductInfo.price=curpirce;
		mProductInfo.discount=discountprice;
		mProductInfo.sptitle=productdec;
		mProductInfo.type=istype;
		mProductInfo.setvicetype = positionId;
//		mProductInfo.image=pdPhoto;
//		mProductInfo.imagename=pdPhotoName;

		//调用保存商品接口saveMerchantComm
		AlertHelper.getInstance(getActivity()).showLoading(null);
		WebServiceHelper SaveProductHelper = new WebServiceHelper(
				new BaseWebServiceHelper.RequestCallback<Define.SINGLE_ID>() {

			@Override
			public void onFailure(int errorNo, String errorMsg) {
				LogHelper.i("tag", "errorMsg:"+errorMsg);
				AlertHelper.getInstance(getActivity()).showCenterToast("上传失败");
				AlertHelper.getInstance(getActivity()).hideLoading();
			}

			@Override
			public void onSuccess(final SINGLE_ID result) {
				if(productBitmap == null){
//					getActivity().finish();
					AlertHelper.getInstance(getActivity()).hideLoading();
					AlertHelper.getInstance(getActivity()).showCenterToast("上架成功");
					Intent intent = new Intent();
					intent.setClass(getActivity(), ProductDetailActivity2.class);
					intent.putExtra("id", (String) result.id);
//					startActivityForResult(intent, Define.RESULT_FROM_ALTER_CAR);
					cleanData();
					startActivityForResult(intent, 101);
					return;
				}
				WebServiceHelper mUploadImage = new WebServiceHelper(new BaseWebServiceHelper.RequestCallback<Define.BASE>() {
				
							@Override
							public void onSuccess(BASE result2) {
								AlertHelper.getInstance(getActivity()).hideLoading();
								AlertHelper.getInstance(getActivity()).showCenterToast("上架成功");
									Intent intent = new Intent();
									intent.setClass(getActivity(), ProductDetailActivity2.class);
									intent.putExtra("id", (String) result.id);
//									startActivityForResult(intent, Define.RESULT_FROM_ALTER_CAR);
									cleanData();
									startActivityForResult(intent, 101);
//								getActivity().finish();
							}
				
							@Override
							public void onFailure(int errorNo, String errorMsg) {
								LogHelper.i("tag", "errorMsg:"+errorMsg);
								AlertHelper.getInstance(getActivity()).showCenterToast("上传失败");
								AlertHelper.getInstance(getActivity()).hideLoading();
							}
						}, getActivity());
						String mId = result.id;
						mUploadImage.saveImage(
								productBitmap, "productPhoto.jpeg",
								WebServiceHelper.IMAGE_TYPE_SJSP, mId);
				
				
			}
		}, getActivity());
		SaveProductHelper.saveProductInfo(mProductInfo);
		
	}
	
	private void cleanData(){
		et_proName.setText("");
		et_proPrice.setText("");
		et_proDisPrice.setText("");
		et_proDec.setText("");
		ivProductPhoto.setImageResource(R.drawable.ic_photo_add);
		productBitmap = null;
		productImgUrl = null;
		tvProductPhoto.setVisibility(View.VISIBLE);
	}
	
	private void findView(){
//		gvProductPhoto = (GridView) findViewById(R.id.gv_product_photo);
		et_proName = (TextView) rootView.findViewById(R.id.et_proName);
		et_proPrice = (TextView) rootView.findViewById(R.id.et_proPrice);
		et_proDisPrice = (TextView) rootView.findViewById(R.id.et_proDisPrice);
		et_proDec = (TextView) rootView.findViewById(R.id.et_proDec); 
		isproductservice = (RadioGroup)rootView.findViewById(R.id.rg_issp);
		rb_product=(RadioButton)rootView.findViewById(R.id.rb_product);
		rb_service=(RadioButton)rootView.findViewById(R.id.rb_service);
		
		btnSubmit =(Button)rootView.findViewById(R.id.btn_prodectsubmit);
		ivProductPhoto = (ImageLoaderView) rootView.findViewById(R.id.iv_product_photo);
		tvProductPhoto = (TextView) rootView.findViewById(R.id.tv_product_photo);
		
		tv_setvicetype = (TextView) rootView.findViewById(R.id.tv_setvicetype);
		rl_setvicetype = (RelativeLayout) rootView.findViewById(R.id.rl_setvicetype);
	}
	
	private void loadSpot(){
		//防止数据重复
		spotIdOne.clear();
		spotNameOne.clear();
		spotIdTwo.clear();
		spotNameTwo.clear();
		
		WebServiceHelper mSpotServiceHelper = new WebServiceHelper(
				new WebServiceHelper.OnSuccessListener() {
			@Override
			public void onSuccess(String result) {
				ClassifyJsonParser mClassifyJsonParser = new ClassifyJsonParser();
				mClassifyJsonParser.parse(result); 
				List<Map<String, String>> oneList = mClassifyJsonParser.getOneList();
				List<List<Map<String, String>>> twoList
				= mClassifyJsonParser.getTwoList();
				
				
				for(int i=0,j=oneList.size(); i<j; i++){
					spotIdOne.add(oneList.get(i).get("id"));
					spotNameOne.add(oneList.get(i).get("name"));
					List<String> ids = new ArrayList<String>();
					List<String> names = new ArrayList<String>();
					List<Map<String, String>> cList = twoList.get(i);
					for(int a=0,b=cList.size(); a<b; a++){
						ids.add(cList.get(a).get("id"));
						names.add(cList.get(a).get("name"));
					}
					spotIdTwo.put(oneList.get(i).get("id"), ids);
					spotNameTwo.put(oneList.get(i).get("id"), names);
				}
				getPosition(null);
			}
		}, getActivity());
		mSpotServiceHelper.getFwSpClassList(istype);
	}
	
	/**
	 * 选择部位
	 */
	public void getPosition(final String spotId) {
		if(mSpotDialogOne == null){
			mSpotDialogOne = new Dialog(getActivity(), R.style.MyDialog1);
			mSpotDialogOne.setContentView(R.layout.layout_question_position);
			mSpotDialogOne.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			ls_correlation = (ListView) mSpotDialogOne.findViewById(R.id.ls_correlation);
			
		}
		if(spotId != null){
			correAdapter = new ArrayAdapter<String>(
					getActivity(),
					R.layout.layout_question_position_item,
					R.id.tv_name, spotNameTwo.get(spotId));
			ls_correlation.setAdapter(correAdapter);
			((TextView)mSpotDialogOne.findViewById(R.id.text1)).setText("返回");
		}else{
			correAdapter = new ArrayAdapter<String>(
					getActivity(),
					R.layout.layout_question_position_item,
					R.id.tv_name, spotNameOne);
			ls_correlation.setAdapter(correAdapter);
			((TextView)mSpotDialogOne.findViewById(R.id.text1)).setText(R.string.cancle);
		}
		
		View.OnClickListener mClickListener = new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				if(spotId != null){
					getPosition(null);
					return;
				}
				mSpotDialogOne.dismiss();
			}
		};
		mSpotDialogOne.findViewById(R.id.rl10).setOnClickListener(mClickListener);
		
		((ListView) ls_correlation).setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if(spotId == null){
							getPosition(spotIdOne.get(position));
							return;
						}
						//TODO 取得选择分类ID
						positionId = spotIdTwo.get(spotId).get(position); 
						tv_setvicetype.setText(spotNameTwo.get(spotId).get(position));
						mSpotDialogOne.dismiss();
					}
				});
		if(!mSpotDialogOne.isShowing()){
			mSpotDialogOne.show();
		}
	}
}
