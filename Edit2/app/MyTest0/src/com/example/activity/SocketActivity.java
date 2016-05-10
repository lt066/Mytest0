package com.example.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SocketActivity extends BaseActivity{
	private TextView accept_text;
	private Handler mHandler;
	public boolean isConne=true;
	
	private Thread mThread;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.socketactivity);
		initView();
		activityAnima();
		
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				while(true)
//				{
					initData();
//				}
			}
		};
		mHandler=new Handler(){
			public void handleMessage(android.os.Message msg) {
//				if(accept_text.getText().toString().isEmpty())
//				{
					accept_text.setText(msg.obj.toString()+"\n");
//				}
//				else {
//					accept_text.append(msg.obj.toString()+"\n");
//				}
			};
		};
		mThread=new Thread(runnable);
		mThread.start();
		
	}
	
	protected int activityCloseEnterAnimation;

	protected int activityCloseExitAnimation;
	
	private void activityAnima() {
		TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowAnimationStyle});

		int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);      

		activityStyle.recycle();

		activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] {android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});

		activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);

		activityCloseExitAnimation = activityStyle.getResourceId(1, 0);

		activityStyle.recycle();
	}
	
	private void initView()
	{
		accept_text=(TextView) findViewById(R.id.acceptText);
		findViewById(R.id.socketbutton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("data", "数据1");
				setResult(10, intent);
				finish();
			}
		});
		
	}
	
	private void initData()
	{
		try {
			Socket socket = new Socket("192.168.61.180", 30000);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(),"GBK"));
			String line = null;
			while ((line =br.readLine())!=null) {
				Message message = new Message();
				message.obj=line;
				mHandler.sendMessage(message);
				
			}
			
			br.close();
			socket.close();
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isConne=false;
		finishAndRemoveTask();
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "Socket页面";
	}

}
