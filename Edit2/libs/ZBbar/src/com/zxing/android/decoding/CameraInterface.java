package com.zxing.android.decoding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import com.google.zxing.Result;
import com.zxing.android.camera.CameraManager;
import com.zxing.android.view.ViewfinderView;

public interface CameraInterface {

	public Activity getActivity();
	
	public ViewfinderView getViewfinderView();  
	
	public CameraManager getCameraManager();

	 public Handler getHandler();
	 
	 public void handleDecode(Result obj, Bitmap barcode);

	public void startActivity(Intent intent);
}
