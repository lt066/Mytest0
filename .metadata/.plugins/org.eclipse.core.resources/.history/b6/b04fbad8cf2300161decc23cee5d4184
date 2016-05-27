package com.example.activity;

import android.support.v7.app.ActionBarActivity;

import java.util.ArrayList;
import java.util.List;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.mytest0.R.id;
import com.example.mytest0.R.layout;
import com.example.mytest0.R.menu;
import com.example.widget.MyAdapter;
import com.example.widget.PullToRefreshLayout;
import com.example.widget.PullToRefreshLayout.OnRefreshListener;
import com.jingchen.pulltorefreshs.PullToRefreshBase;
import com.jingchen.pulltorefreshs.PullToRefreshBase.Mode;
import com.jingchen.pulltorefreshs.PullToRefreshGridView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.GridView;

public class Activity3 extends BaseActivity implements OnTouchListener {
	
	private PullToRefreshGridView mPullToRefresh;
	private List<String> items;
	private  MyAdapter adapter;
	private boolean isautoRefresh=true;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity3);
		mPullToRefresh = (PullToRefreshGridView)findViewById(R.id.refresh_view);
		mPullToRefresh.setMode(Mode.BOTH);
		mPullToRefresh.setOnRefreshListener(new com.jingchen.pulltorefreshs.PullToRefreshBase.OnRefreshListener2<GridView>() {


			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
						if(items!=null && items.size()>0)
						{
							items.clear();

						}
						new Handler() {
							@Override
							public void handleMessage(Message msg) {
								// 千万别忘了告诉控件刷新完毕了哦！
								for (int i = 0; i < 30; i++)
								{
									items.add("这里是item " + i);
								}
								adapter.notifyDataSetChanged();
								mPullToRefresh.onRefreshComplete();
							}
						}.sendEmptyMessage(0);
						
						if(mPullToRefresh!=null)
						{
							mPullToRefresh.removeAutoRefresh_run();
						}
			}

			@SuppressLint("HandlerLeak")
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				// TODO Auto-generated method stub
				int nums=items.size()-1;
				for (int i = nums; i < (nums+10); i++)
				{
					items.add("这里是item " + i);
				}
				adapter.notifyDataSetChanged();

				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// 千万别忘了告诉控件加载完毕了哦！
						mPullToRefresh.onRefreshComplete();
					}
				}.sendEmptyMessageDelayed(0, 3000);
			}
		});	
		
		items = new ArrayList<String>();
//		for (int i = 0; i < 30; i++)
//		{
//			items.add("这里是item " + i);
//		}
		adapter = new MyAdapter(this, items);
		mPullToRefresh.setAdapter(adapter);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		if(mPullToRefresh!=null && isautoRefresh)
		{
			isautoRefresh=false;
			mPullToRefresh.autoRefresh();
		}
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity3, menu);
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
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mPullToRefresh!=null)
		{
			mPullToRefresh.removeAutoRefresh_run();
		}
	}
	
	

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
//		showToastMsgShort(""+event.getX());
		return false;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "Activity3";
	}
}
