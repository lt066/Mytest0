package com.example.application;

import com.baidu.mapapi.SDKInitializer;

import cn.smssdk.SMSSDK;
import android.app.Application;

public class MyApplication extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		SMSSDK.initSDK(getApplicationContext(), "12b07afe3bc34", "fad656d99824a7b7c12d6dc1174f79ad");
		SDKInitializer.initialize(getApplicationContext());
	}

}
