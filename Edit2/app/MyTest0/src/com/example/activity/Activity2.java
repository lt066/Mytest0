package com.example.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.example.imageloader.ImageCacheManager;
import com.example.model.Focus;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.mytest0.R.dimen;
import com.example.mytest0.R.drawable;
import com.example.mytest0.R.id;
import com.example.mytest0.R.layout;
import com.example.mytest0.R.menu;
import com.example.volley.MyTestJsonRequest;
import com.example.widget.LoopViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class Activity2 extends BaseActivity implements Listener<JSONObject>,ErrorListener, OnPageChangeListener{
	
	private LoopViewPager mViewPaper;
	private ViewGroup imgs;
	private ArrayList<Focus> list;
	private MyPaperAdaper myPaperAdaper;
	private ImageView[] imageViews;
	private EditText mEditText;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences.Editor editor ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity2);
		initView();
		getData();
	}
	
	private void initView()
	{
		mViewPaper=(LoopViewPager) findViewById(R.id.viewpaper);
		imgs=(ViewGroup) findViewById(R.id.imgs);
		mEditText=(EditText) findViewById(R.id.edit2);
		
		mSharedPreferences = getSharedPreferences("test_edit", Activity2.MODE_PRIVATE);
		if(mSharedPreferences.getString("mEditText", "")!=null && !mSharedPreferences.getString("mEditText", "").equals(""))
		{
			mEditText.setText(mSharedPreferences.getString("mEditText", ""));
		}
		editor = mSharedPreferences.edit();
		mEditText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				editor.putString("mEditText", s.toString());
				editor.commit(); 
			}
		});
		
	}
	
	private void getData()
	{
		showLoadView(false);
		String url = "http://appserver.sk.com/home/index";
		MyTestJsonRequest myTestJsonRequest = new MyTestJsonRequest(Method.GET, url, null, this, this, getParams(), this);
		mRequestQueue.add(myTestJsonRequest);
	}
	
	private Map<String, String> getParams(){
		Map<String, String> map  =new HashMap<String, String>();
		map.put("client_type", "1");
		map.put("version", "2.2");
		return map;
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mRequestQueue!=null){
			mRequestQueue.stop();
		}
	}
	
	@SuppressWarnings("deprecation")
	private void initPaper()
	{
		mViewPaper.setAdapter(myPaperAdaper);
		imageViews = new ImageView[list.size()];
		for(int i=0;i<imageViews.length;i++)
		{
			ImageView imageView = new ImageView(getActivityContext());
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen._14px),
					getResources().getDimensionPixelOffset(R.dimen._14px));
			lp.setMargins(1, 0, 8, 0);
			imageView.setLayoutParams(lp);
			imageView.setPadding(getResources().getDimensionPixelOffset(R.dimen._14px), 0, getResources().getDimensionPixelOffset(R.dimen._14px), 0);
			imageViews[i]=imageView;
			if(i==0)
			{
				imageView.setBackgroundResource(R.drawable.imas_selected2);
						
			}
			else {
				imageView.setBackgroundResource(R.drawable.imas_selected);
			}
			imgs.addView(imageView);
		}

		mViewPaper.setOnPageChangeListener(this);
		hideLoadView();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	class MyPaperAdaper extends PagerAdapter{

		private ArrayList<Focus> mList;
		public MyPaperAdaper(ArrayList<Focus> list) {
			// TODO Auto-generated constructor stub
			this.mList=list;
		}
		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position,
				Object object) {

			if (object != null) {
				container.removeView((View) object);
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub

			return arg0==arg1;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			ImageView imageView = new ImageView(getActivityContext());
			imageView.setScaleType(ScaleType.FIT_XY);
//			imageView.setImageDrawable(getResources().getDrawable(R.drawable.default_image));
			CacheImage(imageView, mList.get(position).getImg());
			container.addView(imageView);
			return imageView;
		}
		
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onResponse(JSONObject response) {
		// TODO Auto-generated method stub
		
		try {
			if(response.getString("code").equals("OK"))
			{
				if(response.optJSONObject("data")!=null)
				{
					Gson gson=new Gson();
					list=gson.fromJson(response.optJSONObject("data").getJSONArray("focus").toString(),
							new TypeToken<ArrayList<Focus>>(){}.getType());
					myPaperAdaper = new MyPaperAdaper(list);
					initPaper();
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CacheImage(ImageView view,String url){
	        Bitmap defaultImage=BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.default_image);
	        Bitmap errorImage= BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.error_image);
	        ImageCacheManager.loadImage(getActivityContext(), url, view, defaultImage, errorImage);
	}
	
	private int mPosition;
	private int state;
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		 Boolean isAutoPlay=false;
		 switch (arg0) {
		 	case 1:// 手势滑动，空闲中
		 		isAutoPlay = false;
		 		break;
		 	case 2:// 界面切换中
		 		isAutoPlay = true;
		 		break;
		 	case 0:// 滑动结束，即切换完毕或者加载完毕
		 // 当前为最后一张，此时从右向左滑，则切换到第一张
//		 		if (mViewPaper.getCurrentItem() == mViewPaper.getAdapter().getCount() - 1 && !isAutoPlay) {
//		 			new Handler(){
//		 				public void handleMessage(android.os.Message msg) {
//				 			mViewPaper.setCurrentItem(0);
//		 				};
//		 			}.sendEmptyMessageDelayed(0, 1000);
//
//		 		}
		 // 当前为第一张，此时从左向右滑，则切换到最后一张
//		 		else if (mViewPaper.getCurrentItem() == 0 && !isAutoPlay) {
//		 			new Handler(){
//		 				public void handleMessage(android.os.Message msg) {
//		 					mViewPaper.setCurrentItem(mViewPaper.getAdapter().getCount() - 1);
//		 				};
//		 			}.sendEmptyMessageDelayed(0, 1000);
//		 			
//		 		}
		 		break;
		 	}
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		showToastMsgShort(arg0+"");
		
	}

	@SuppressLint("NewApi")
	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		imageViews[arg0].setBackgroundResource(R.drawable.imas_selected2);
		for(int i=0;i<imageViews.length;i++)
		{
			if(i!=arg0)
			{
				imageViews[i].setBackgroundResource(R.drawable.imas_selected);
			}
		}
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "Activity2";
	}
	
}
