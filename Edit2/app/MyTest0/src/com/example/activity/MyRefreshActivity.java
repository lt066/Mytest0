package com.example.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.animation.Rotate3d;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

public class MyRefreshActivity extends BaseActivity{
	
	private float moveX=0;
	private float moveY=0;
	private float downX=0;
	private float downY=0;
	private RelativeLayout headLayout;
	private LayoutParams mainLayoutParams;
	private int initTomargin=0;
	private ImageView imageView;
	private TextView textView;
	Rotate3d rotate3d;
	float head_heigh=0;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_myrefresh);
		headLayout=(RelativeLayout) findViewById(R.id.refresh_layout);
		mainLayoutParams=(LayoutParams) headLayout.getLayoutParams();
		imageView =(ImageView) findViewById(R.id.refresh_img);
		textView=(TextView) findViewById(R.id.refresh_text);
		initTomargin=mainLayoutParams.topMargin;
		head_heigh=mainLayoutParams.height;
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downX=event.getX();
			downY=event.getY();
			break;
			
		case MotionEvent.ACTION_MOVE:
			moveX=event.getX();
			moveY=event.getY();
			float abX=moveX-downX;
			float abY=moveY-downY;
			mainLayoutParams.topMargin=(int) abY+initTomargin;
			headLayout.setLayoutParams(mainLayoutParams);
			if(mainLayoutParams.topMargin>=(head_heigh+initTomargin))
			{
				textView.setText("松开刷新...");
			}
			else {
				textView.setText("下拉刷新...");
			}
//			headLayout.setPadding(0, (int) abY, 0, 0);
			
			break;
			
		case MotionEvent.ACTION_UP:
			final Animation cancelAnimation =AnimationUtils.loadAnimation(getActivityContext(), R.anim.exit_view);
			if(mainLayoutParams.topMargin>=(head_heigh+initTomargin))
			{
				mainLayoutParams.topMargin=0;
				headLayout.setLayoutParams(mainLayoutParams);
				rotate3d=new Rotate3d();
				LinearInterpolator linearInterpolator=new LinearInterpolator();
				rotate3d.setDuration(3000);
				rotate3d.setInterpolator(linearInterpolator);
				//设置循环动画
				rotate3d.setRepeatCount(-1);
				
				imageView.startAnimation(rotate3d);
				
				new Handler()
				{
					public void handleMessage(android.os.Message msg) {
						rotate3d.cancel();
						textView.setText("刷新完成...");
						headLayout.startAnimation(cancelAnimation);
						mainLayoutParams.topMargin=initTomargin;
						headLayout.setLayoutParams(mainLayoutParams);
						cancelAnimation.cancel();
					};
				}.sendEmptyMessageDelayed(0, 2000);
			}
			else {
				
				headLayout.startAnimation(cancelAnimation);
				mainLayoutParams.topMargin=initTomargin;
				headLayout.setLayoutParams(mainLayoutParams);
				cancelAnimation.cancel();
			}
		

//			headLayout.setPadding(0, 0, 0, 0);
			break;
		default:
			break;
		}
		return true;
	}
	

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "自定义刷新";
	}

}
