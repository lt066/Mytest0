package com.example.activity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.widget.PoiOverlay;
import com.baidu.mapapi.search.core.CityInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiBoundSearchOption;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

public class Map_Activity  extends BaseActivity{
	private LocationClient mLocClient;
    public MyLocationListenner myListener;
    private LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private LocationClientOption option;
    // UI相关
    boolean isFirstLoc = true; // 是否首次定位
    
    /**
     * 传感器管理器
     */
    private SensorManager sm;
    // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);  
    // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))  
    
    private Sensor accelerometer; // 加速度传感器
	private Sensor magnetic; // 地磁场传感器

	private float[] accelerometerValues = new float[3];
	private float[] magneticFieldValues = new float[3];
	
	private PoiSearch mPoiSearch=null;
    
    private Handler handler = new Handler();
    private double latitude;
    private double longitude;
    private MyLocationData locData;
    private float direction=100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        
        initView();
        initData();
       
    }
    
    
    private void initView()
    {
    	mCurrentMode = LocationMode.NORMAL;
        
        sm=(SensorManager) getSystemService(SENSOR_SERVICE);
    	mPoiSearch=PoiSearch.newInstance();
    	
    	// 初始化加速度传感器
     	accelerometer = sm
     				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
     	// 初始化地磁场传感器
     	magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        // 地图初始化
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mLocClient = new LocationClient(this);
        option = new LocationClientOption();
        myListener = new MyLocationListenner();
    }
    
    private void initData()
    {
    	 // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                mCurrentMode, true, mCurrentMarker));
        // 定位初始化
        mLocClient.registerLocationListener(myListener);
        
        
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        		);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        		        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        		        int span=1000;
        		        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        		        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        		        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        		        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        		        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        		        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        		option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
        		        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        		option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocClient.setLocOption(option);
        mLocClient.start();
        findViewById(R.id.refresh).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				nearbySearch(10);
			}
		});
        
        mPoiSearch.setOnGetPoiSearchResultListener(poiListener);
        calculateOrientation();
    }
    
    private void calculateOrientation() {
		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, accelerometerValues,
				magneticFieldValues);
		SensorManager.getOrientation(R, values);
		values[0] = (float) Math.toDegrees(values[0]);
		// 修改定位图标方向
		direction = values[0];
		//  重新设置当前位置数据
		mBaiduMap.setMyLocationData(locData);
    }
    

    

    /**
     * 定位SDK监听函数
     */

    
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
        	
        	
        	
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(direction)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }
    
    Runnable changeDirectionRunnable = new Runnable() {
		
		@Override
		public void run() {
          sm.registerListener(sensorEventListener,
  				accelerometer, Sensor.TYPE_ACCELEROMETER);
  		  sm.registerListener(sensorEventListener, magnetic,
  				Sensor.TYPE_MAGNETIC_FIELD);
			
		}
	};
	
	final SensorEventListener sensorEventListener = new SensorEventListener() {
        // 用于传感器监听中，设置灵敏程度


			@Override
			public void onSensorChanged(SensorEvent event) {
				// 方向传感器
				
				if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					accelerometerValues = event.values;
				}
				if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
					magneticFieldValues = event.values;
				}
				calculateOrientation();
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub
				
			}

	};
	
	//poi搜索监听
	
	private Handler showText=new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			showToastMsgShort(msg.getData().getString("address"));
		};
	};
	
	OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener(){

		@Override
		public void onGetPoiDetailResult(PoiDetailResult result) {
			// TODO Auto-generated method stub
			if (result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(Map_Activity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
						.show();
			} else {
				Message message = new Message();
				Bundle bundle=new Bundle();
				bundle.putString("address", result.getName() + ": " + result.getAddress());
				message.setData(bundle);
				showText.sendMessage(message);
			}
		}

		@Override
		public void onGetPoiResult(PoiResult result) {
			String isnullString=result==null?"true":result.error.toString();
			Log.d("测试", isnullString);
			// TODO Auto-generated method stub
			if (result == null
					|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
				Toast.makeText(Map_Activity.this, "未找到结果", Toast.LENGTH_LONG)
				.show();
				return;
			}
			if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//				mBaiduMap.clear();
				PoiOverlay overlay = new MyPoiOverlay(mBaiduMap);
				mBaiduMap.setOnMarkerClickListener(overlay);
				overlay.setData(result);
				overlay.addToMap();
				overlay.zoomToSpan();
				return;
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {

				// 当输入关键字在本市没有找到，但在其他城市找到时，返回包含该关键字信息的城市列表
				String strInfo = "在";
				for (CityInfo cityInfo : result.getSuggestCityList()) {
					strInfo += cityInfo.city;
					strInfo += ",";
				}
				strInfo += "找到结果";
				Toast.makeText(Map_Activity.this, strInfo, Toast.LENGTH_LONG)
						.show();
			}
		}
		
	};
	
		
	
	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			PoiInfo poi = getPoiResult().getAllPoi().get(index);
			// if (poi.hasCaterDetails) {
				mPoiSearch.searchPoiDetail((new PoiDetailSearchOption())
						.poiUid(poi.uid));
			// }
			return true;
		}
	}
	
    /** 
     * 城市内搜索 
     */  
    private void citySearch(int page) {  
        // 设置检索参数  
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();  
        citySearchOption.city("南宁");// 城市  
        citySearchOption.keyword("银行");// 关键字  
        citySearchOption.pageCapacity(15);// 默认每页10条  
        citySearchOption.pageNum(page);// 分页编号  
        // 发起检索请求  
        mPoiSearch.searchInCity(citySearchOption);  
    }  
  
    /** 
     * 范围检索 
     */  
    private void boundSearch(int page) {  
        PoiBoundSearchOption boundSearchOption = new PoiBoundSearchOption();  
        LatLng southwest = new LatLng(latitude - 0.01, longitude - 0.012);// 西南  
        LatLng northeast = new LatLng(latitude + 0.01, longitude + 0.012);// 东北  
        LatLngBounds bounds = new LatLngBounds.Builder().include(southwest)  
                .include(northeast).build();// 得到一个地理范围对象  
        boundSearchOption.bound(bounds);// 设置poi检索范围  
        boundSearchOption.keyword("银行");// 检索关键字  
        boundSearchOption.pageNum(page);  
        mPoiSearch.searchInBound(boundSearchOption);// 发起poi范围检索请求  
    }  
  
    /** 
     * 附近检索 
     */  
    private void nearbySearch(int page) {  
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();  
        nearbySearchOption.location(new LatLng(latitude, longitude));  
        nearbySearchOption.keyword("银行");  
        nearbySearchOption.radius(5000);// 检索半径，单位是米  
        nearbySearchOption.pageNum(10);  
        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求  
    }  
			

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        handler.post(changeDirectionRunnable);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
    	mLocClient.unRegisterLocationListener(myListener);
        mLocClient.stop();
        // 关闭定位图层
        sm.unregisterListener(sensorEventListener);
        handler.removeCallbacks(changeDirectionRunnable);
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "百度地图";
	}

	
}
