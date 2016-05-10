package com.example.activity;

import java.util.List;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.services.FirstService;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Activity7 extends BaseActivity implements OnClickListener{
	
	private ServiceConnection conn;
	private FirstService firstService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity7);
		initView();
		initData();
	
	}
	
	private void initView()
	{
		TextView action_name = (TextView) findViewById(R.id.action_name);
		action_name.setText(getIntent().getAction());
		TextView category_name = (TextView) findViewById(R.id.category_name);
		category_name.setText(getIntent().getCategories()+"");
		
		findViewById(R.id.openService).setOnClickListener(this);
		findViewById(R.id.bindService).setOnClickListener(this);
		findViewById(R.id.unBindService).setOnClickListener(this);
		findViewById(R.id.destrService).setOnClickListener(this);
		findViewById(R.id.sendBroad).setOnClickListener(this);
	}
	
	private void initData()
	{
		conn=new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				// TODO Auto-generated method stub
				firstService=((FirstService.MyBinder)service).getService();
			}
		};
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("com.example.mytest0.service.FirstService");
		intent.setPackage("com.example.mytest0");
		switch (v.getId()) {
		case R.id.openService:
			startService(intent);
			break;
			
		case R.id.bindService:
			bindService(intent, conn, Context.BIND_AUTO_CREATE);
			break;
			
		case R.id.unBindService:
			unbindService(conn);
			break;
			
		case R.id.destrService:
			stopService(intent);
			break;
			
		case R.id.sendBroad:
			Intent intentBroad=new Intent();
			intentBroad.setAction("com.example.mytest0.broadcastreceiver.MyReceiver");
			sendBroadcast(intentBroad);
			break;
			

		default:
			break;
		}
		
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "ѧϰservice";
	}
}
