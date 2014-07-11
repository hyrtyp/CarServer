package cn.com.hyrt.carserver.base.helper;

import android.app.Service;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

/**
 * 定位助手
 * @author zoe
 *
 */
public class LocationHelper {
	
	private LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListener mMyLocationListener;
	public Vibrator mVibrator;
	private LocationCallback mCallback;
	
	private LocationMode locMode = LocationMode.Hight_Accuracy;//定位模式
	private String tempcoor = "gcj02";//坐标系
	private int scanSpan = 1000;//扫描间隔
	
	private static LocationHelper mLocationHelper;
	private Context mContext;

	private LocationHelper(Context context){
		this.mContext = context;
		mCallback = null;
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(context);
		mVibrator =(Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
	}
	
	public static LocationHelper getInstance(Context context){
		if(mLocationHelper == null){
			mLocationHelper = new LocationHelper(context);
		}
		return mLocationHelper;
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(locMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		option.setScanSpan(scanSpan);//设置发起定位请求的间隔时间
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}
	
	/**
	 * 启动定位
	 */
	public void start(){
		InitLocation();
		mLocationClient.start();
	}
	
	/**
	 * 停止定位
	 */
	public void stop(){
		mLocationClient.stop();
	}
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer sb = new StringBuffer(256);
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			if (location.getLocType() == BDLocation.TypeGpsLocation){
				sb.append("\nspeed : ");
				sb.append(location.getSpeed());
				sb.append("\nsatellite : ");
				sb.append(location.getSatelliteNumber());
				sb.append("\ndirection : ");
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				sb.append(location.getDirection());
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
				sb.append("\naddr : ");
				sb.append(location.getAddrStr());
				//运营商信息
				sb.append("\noperationers : ");
				sb.append(location.getOperators());
			}
			logMsg(sb.toString());
			
//			Log.i("BaiduLocationApiDem", sb.toString());
			LogHelper.i("tag", "lat:"+location.getLatitude()
					+" lon:"+location.getLongitude()
					+" error code:"+location.getLocType()+" city:"+location.getCity());
			if(mCallback != null && location.getLocType() == BDLocation.TypeNetWorkLocation){
				StorageHelper.getInstance(mContext).saveLocation(location.getLongitude()+"", location.getLatitude()+"", location.getCity());
				mCallback.onLocation(location.getLongitude(), location.getLatitude(), location.getCity());
				stop();
			}
		}
		

	}
	
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsg(String str) {
//		try {
//			if (mLocationResult != null)
//				mLocationResult.setText(str);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	
	public void setLocationCallback(LocationCallback callback){
		this.mCallback = callback;
	}
	
	public static interface LocationCallback{
		/**
		 * 
		 * @param lon 经度
		 * @param lat 纬度
		 */
		public void onLocation(double lon, double lat, String city);
	}
	
}
