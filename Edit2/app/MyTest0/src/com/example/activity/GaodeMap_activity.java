package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMapClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptor;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

public class GaodeMap_activity extends BaseActivity implements LocationSource,
	AMapLocationListener,OnPoiSearchListener, OnClickListener, OnMapClickListener, OnMarkerClickListener, InfoWindowAdapter{
	private MapView mMapView = null;
	private AMap aMap;
	//声明AMapLocationClient类对象
	private AMapLocationClient mLocationClient = null;
	//声明定位回调监听器
	private OnLocationChangedListener mListener;
	//声明mLocationOption对象
	private AMapLocationClientOption mLocationOption = null;
	
	//定位点坐标
	private LatLonPoint lp;
	
	private RelativeLayout mPoiDetail;
	private TextView mPoiName, mPoiAddress;
	private EditText mSearchText;
	private Marker locationMarker; // 选择的点
	private Marker detailMarker;
	private Marker mlastMarker;
	private String keyWord;
	private int currentPage;
	private Query query;
	private myPoiOverlay poiOverlay;// poi图层
	private PoiSearch poiSearch;// POI搜索
	private boolean iszoomToSpan=true;//是否关键词第一次搜索移动
	private PoiResult poiResult; // poi返回的结果
	private List<PoiItem> poiItems;// poi数据
	private boolean isLocation=true;//是否第一次定位
	
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
	private float direction=100;
	private Handler handler = new Handler();

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gaode_map);
		  //获取地图控件引用
	    mMapView = (MapView) findViewById(R.id.map);
	    //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，实现地图生命周期管理
	    mMapView.onCreate(savedInstanceState);
	    init();
	}
	/**
	 * 初始化AMap对象
	 */
	private void init() {
		if (aMap == null) {
			aMap = mMapView.getMap();
			aMap.setOnMapClickListener(this);
			aMap.setOnMarkerClickListener(this);
			aMap.setInfoWindowAdapter(this);
			sm=(SensorManager) getSystemService(SENSOR_SERVICE);
	    	
	    	// 初始化加速度传感器
	     	accelerometer = sm
	     				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	     	// 初始化地磁场传感器
	     	magnetic = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
			TextView searchButton = (TextView) findViewById(R.id.btn_search);
			searchButton.setOnClickListener(this);
			setup();
			setUpMap();
		}
	}
	
	private void setup() {
		mPoiDetail = (RelativeLayout) findViewById(R.id.poi_detail);
		mPoiDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(PoiSearchActivity.this,
//						SearchDetailActivity.class);
//				intent.putExtra("poiitem", mPoi);
//				startActivity(intent);
				
			}
		});
		mPoiName = (TextView) findViewById(R.id.poi_name);
		mPoiAddress = (TextView) findViewById(R.id.poi_address);
		mSearchText = (EditText)findViewById(R.id.input_edittext);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 仅定位时使用--自定义系统定位小蓝点
//		MyLocationStyle myLocationStyle = new MyLocationStyle();
//		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
//				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
//		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
//		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
//		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
//		aMap.setMyLocationStyle(myLocationStyle);
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	   // aMap.setMyLocationType()
	}
	
	private void calculateOrientation() {
		float[] values = new float[3];
		float[] R = new float[9];
		SensorManager.getRotationMatrix(R, null, accelerometerValues,
				magneticFieldValues);
		SensorManager.getOrientation(R, values);
		values[0] = (float) Math.toDegrees(values[0]);
		// 修改定位图标方向
		direction = -values[0];
		//  重新设置当前位置数据
		locationMarker.setRotateAngle(direction);
    }

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
		
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
		deactivate();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				lp= new LatLonPoint(mLocationClient.getLastKnownLocation().getLatitude(), mLocationClient
						.getLastKnownLocation().getLongitude());
				
				if(isLocation)
				{
					//不需要定位的箭头可用此生成小蓝点
					locationMarker = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 0.5f)
					.icon(BitmapDescriptorFactory
						.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.location_marker)))
						.position(new LatLng(lp.getLatitude(), lp.getLongitude())));
					handler.post(changeDirectionRunnable);
					isLocation=false;
				}
				
				doSearchQuery();
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode()+ ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr",errText);
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mLocationClient == null) {
			mLocationClient = new AMapLocationClient(this);
			mLocationOption = new AMapLocationClientOption();
			//设置定位监听
			mLocationClient.setLocationListener(this);
			//设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			//设置定位参数
			mLocationClient.setLocationOption(mLocationOption);
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用onDestroy()方法
			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
			mLocationClient.startLocation();
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mLocationClient != null) {
			mLocationClient.stopLocation();
			mLocationClient.onDestroy();
		}
		mLocationClient = null;
	}
	
	
	/**
	 * 开始进行poi搜索
	 */
	
	
	protected void doSearchQuery() {
		keyWord =mSearchText.getText().toString().trim();
		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", "南宁市");
		query.setPageSize(20);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		if (lp != null && !keyWord.isEmpty()) {
			poiSearch = new PoiSearch(this, query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 5000, true));//
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}
	
	@Override
	public void onPoiItemSearched(PoiItem arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	

	
	@Override
	public void onPoiSearched(PoiResult result, int rcode) {
		if (rcode == 1000) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {

						//并还原点击marker样式
						if (mlastMarker != null) {
							resetlastmarker();
						}				
						//清理之前搜索结果的marker
						if (poiOverlay !=null) {
							poiOverlay.removeFromMap();
						}
//						aMap.clear();
						poiOverlay = new myPoiOverlay(aMap, poiItems);
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
						
						//不需要定位的箭头可用此生成小蓝点
//						aMap.addMarker(new MarkerOptions()
//						.anchor(0.5f, 0.5f)
//						.icon(BitmapDescriptorFactory
//								.fromBitmap(BitmapFactory.decodeResource(
//										getResources(), R.drawable.location_marker)))
//						.position(new LatLng(lp.getLatitude(), lp.getLongitude())));
						
//						aMap.addCircle(new CircleOptions()
//						.center(new LatLng(lp.getLatitude(),
//								lp.getLongitude())).radius(5000)
//						.strokeColor(Color.BLUE)
//						.fillColor(Color.argb(50, 1, 1, 1))
//						.strokeWidth(2));
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						showToastMsgShort("没有找到");
					}
				}
			} else {
				showToastMsgShort("没有找到");
			}
		}
		
	}
	
	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		showToastMsgShort(infomation);

	}
	
	private void whetherToShowDetailInfo(boolean isToShow) {
		if (isToShow) {
			mPoiDetail.setVisibility(View.VISIBLE);
		} else {
			mPoiDetail.setVisibility(View.GONE);
		}
	}
	
	// 将之前被点击的marker置为原来的状态
		private void resetlastmarker() {
			int index = poiOverlay.getPoiIndex(mlastMarker);
			if (index < 10) {
				mlastMarker.setIcon(BitmapDescriptorFactory
						.fromBitmap(BitmapFactory.decodeResource(
								getResources(),
								markers[index])));
			}else {
				mlastMarker.setIcon(BitmapDescriptorFactory.fromBitmap(
				BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight)));
			}
			mlastMarker = null;
			
		}
	
	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}
	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "高德地图";
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_search:
			keyWord = mSearchText.getText().toString().trim();
			if ("".equals(keyWord)) {
				showToastMsgShort("请输入搜索关键字");
				return;
			} else {
				iszoomToSpan=true;
				//清除POI信息显示
				whetherToShowDetailInfo(false);
				doSearchQuery();
			}
			break;
		default:
			break;
		}
		
	}
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (marker.getObject() != null) {
			whetherToShowDetailInfo(true);
			try {
				PoiItem mCurrentPoi = (PoiItem) marker.getObject();
				if (mlastMarker == null) {
					mlastMarker = marker;
				} else {
					// 将之前被点击的marker置为原来的状态
					resetlastmarker();
					mlastMarker = marker;
				}
				detailMarker = marker;
				detailMarker.setIcon(BitmapDescriptorFactory
									.fromBitmap(BitmapFactory.decodeResource(
											getResources(),
											R.drawable.poi_marker_pressed)));
				setPoiItemDisplayContent(mCurrentPoi);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else {
			whetherToShowDetailInfo(false);
			resetlastmarker();
		}
		return true;
	}
	
	private void setPoiItemDisplayContent(final PoiItem mCurrentPoi) {
		mPoiName.setText(mCurrentPoi.getTitle());
		mPoiAddress.setText(mCurrentPoi.getSnippet());
	}
	
	
	@Override
	public void onMapClick(LatLng arg0) {
		whetherToShowDetailInfo(false);
		if (mlastMarker != null) {
			resetlastmarker();
		}
		
	}	
	
	
	private int[] markers = {R.drawable.poi_marker_1,
			R.drawable.poi_marker_2,
			R.drawable.poi_marker_3,
			R.drawable.poi_marker_4,
			R.drawable.poi_marker_5,
			R.drawable.poi_marker_6,
			R.drawable.poi_marker_7,
			R.drawable.poi_marker_8,
			R.drawable.poi_marker_9,
			R.drawable.poi_marker_10
			};
	private class myPoiOverlay {
	
	private AMap mamap;
	private List<PoiItem> mPois;
    private ArrayList<Marker> mPoiMarks = new ArrayList<Marker>();
	public myPoiOverlay(AMap amap ,List<PoiItem> pois) {
		mamap = amap;
        mPois = pois;
	}

    /**
     * 添加Marker到地图中。
     * @since V2.1.0
     */
    public void addToMap() {
        for (int i = 0; i < mPois.size(); i++) {
            Marker marker = mamap.addMarker(getMarkerOptions(i));
            PoiItem item = mPois.get(i);
			marker.setObject(item);
            mPoiMarks.add(marker);
        }
    }

    /**
     * 去掉PoiOverlay上所有的Marker。
     *
     * @since V2.1.0
     */
    public void removeFromMap() {
        for (Marker mark : mPoiMarks) {
            mark.remove();
        }
    }

    /**
     * 移动镜头到当前的视角。
     * @since V2.1.0
     */
    public void zoomToSpan() {
        if (mPois != null && mPois.size() > 0 && iszoomToSpan) {
            if (mamap == null)
                return;
            LatLngBounds bounds = getLatLngBounds();
            mamap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            iszoomToSpan=false;
        }
    }

    private LatLngBounds getLatLngBounds() {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < mPois.size(); i++) {
            b.include(new LatLng(mPois.get(i).getLatLonPoint().getLatitude(),
                    mPois.get(i).getLatLonPoint().getLongitude()));
        }
        return b.build();
    }

    private MarkerOptions getMarkerOptions(int index) {
        return new MarkerOptions()
                .position(
                        new LatLng(mPois.get(index).getLatLonPoint()
                                .getLatitude(), mPois.get(index)
                                .getLatLonPoint().getLongitude()))
                .title(getTitle(index)).snippet(getSnippet(index))
                .icon(getBitmapDescriptor(index));
    }

    protected String getTitle(int index) {
        return mPois.get(index).getTitle();
    }

    protected String getSnippet(int index) {
        return mPois.get(index).getSnippet();
    }

    /**
     * 从marker中得到poi在list的位置。
     *
     * @param marker 一个标记的对象。
     * @return 返回该marker对应的poi在list的位置。
     * @since V2.1.0
     */
    public int getPoiIndex(Marker marker) {
        for (int i = 0; i < mPoiMarks.size(); i++) {
            if (mPoiMarks.get(i).equals(marker)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 返回第index的poi的信息。
     * @param index 第几个poi。
     * @return poi的信息。poi对象详见搜索服务模块的基础核心包（com.amap.api.services.core）中的类 <strong><a href="../../../../../../Search/com/amap/api/services/core/PoiItem.html" title="com.amap.api.services.core中的类">PoiItem</a></strong>。
     * @since V2.1.0
     */
    public PoiItem getPoiItem(int index) {
        if (index < 0 || index >= mPois.size()) {
            return null;
        }
        return mPois.get(index);
    }

	protected BitmapDescriptor getBitmapDescriptor(int arg0) {		
		if (arg0 < 10) {
			BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
					BitmapFactory.decodeResource(getResources(), markers[arg0]));
			return icon;
		}else {
			BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
					BitmapFactory.decodeResource(getResources(), R.drawable.marker_other_highlight));
			return icon;
		}	
	}


	}
	
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
	
	Runnable changeDirectionRunnable = new Runnable() {
		
		@Override
		public void run() {
          sm.registerListener(sensorEventListener,
  				accelerometer, Sensor.TYPE_ACCELEROMETER);
  		  sm.registerListener(sensorEventListener, magnetic,
  				Sensor.TYPE_MAGNETIC_FIELD);
			
		}
	};
	

}	
