package com.example.activity;


import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.object.JavaScriptInterface1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class Activity5 extends BaseActivity {
	private ProgressBar mProgressBar;
	private WebView mWebView;
	private String mURL = "http://192.168.61.180:8080/MytestServer/";
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mProgressBar.setVisibility(View.GONE);
		};
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity5);
		initview();
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	public void initview() {
		mProgressBar=(ProgressBar) findViewById(R.id.mProgressbar);
		mWebView=(WebView) findViewById(R.id.mWebview);
		mWebView.loadUrl(mURL);
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				
				
				if(url.contains("myscheme"))
				{
					Uri uri=Uri.parse(url);
					Intent intent = new Intent();
					intent.setData(uri);
					startActivityForResult(intent,9);
					return true;
				}
				
				
				mProgressBar.setVisibility(View.VISIBLE);
				
				
				
				if(!TextUtils.isEmpty(url)&& url.startsWith("http"))
				{
//					view.loadUrl(url);
//					view.addJavascriptInterface(new JavaScriptInterface1(), "robot");
					return super.shouldOverrideUrlLoading(view, url);
				}
				
				
				return true;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				mHandler.sendEmptyMessageDelayed(0, 1000);
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessageDelayed(0, 1000);
			}
			
		});
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		final JavaScriptInterface1 mJavaScriptInterface1 =new JavaScriptInterface1(getActivityContext());
		mWebView.addJavascriptInterface(mJavaScriptInterface1, "Android");
		
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if(newProgress==100)
				{
//					mHandler.sendEmptyMessageDelayed(0, 1000);
				}
				else
				{
					mProgressBar.setProgress(newProgress);
				}
//				super.onProgressChanged(view, newProgress);
			}
		});
		
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if(arg1==10)
		{
			if(arg2!=null)
			{
				showToastMsgShort("����javascript");
				mWebView.loadUrl("javascript:showTextFunction()");
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity5, menu);
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
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(mWebView.canGoBack())
			{
				mWebView.goBack();
				return true;
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		if(mHandler!=null)
		{
			mHandler.removeCallbacksAndMessages(null);
		}
		super.onDestroy();
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "ѧϰwebView";
	}
	
}
