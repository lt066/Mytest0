package com.example.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.animation.Rotate3d;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.widget.MyAdapter;
import com.example.widget.MyImageView;
import com.jingchen.pulltorefreshs.PullToRefreshBase;
import com.jingchen.pulltorefreshs.PullToRefreshGridView;
import com.jingchen.pulltorefreshs.PullToRefreshBase.Mode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

@SuppressLint("HandlerLeak") 
public class SurfaceViewTest extends BaseActivity {
	
	private SurfaceView surfaceView;
	private SurfaceHolder holder;
	private PullToRefreshGridView mPullToRefresh;
	private List<String> items;
	private  MyAdapter adapter;
	private RelativeLayout imaRelativeLayout;
	private RelativeLayout gridRelativeLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surfaceview_test);
		initView();
		new Handler(){
			public void handleMessage(Message msg) {
				for (int i = 0; i < 30; i++)
				{
					items.add("这里是item " + i);
				}
				adapter.notifyDataSetChanged();
				imaRelativeLayout.setVisibility(View.GONE);
				imaRelativeLayout=null;
				gridRelativeLayout.setVisibility(View.VISIBLE);
			};
		}.sendEmptyMessageDelayed(0, 3000);
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public void initView() {
		surfaceView=(SurfaceView) findViewById(R.id.surface);
		imaRelativeLayout=(RelativeLayout) findViewById(R.id.imaRe);
		gridRelativeLayout=(RelativeLayout) findViewById(R.id.gridRe);
		holder=surfaceView.getHolder();
		holder.addCallback(new Callback() {
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				// TODO Auto-generated method stub
				Canvas canvas = holder.lockCanvas();
				Bitmap bitmap = BitmapFactory.decodeResource(SurfaceViewTest.this.getResources(), R.drawable.ic_launcher);
				canvas.drawBitmap(bitmap, 0, 0, null);
				holder.unlockCanvasAndPost(canvas);
				holder.lockCanvas(new Rect(0, 0, 0, 0));
				holder.unlockCanvasAndPost(canvas);
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_rotate);
		//设置匀速
		LinearInterpolator linearInterpolator=new LinearInterpolator();
		animation.setInterpolator(linearInterpolator);
		ImageView imageView = (ImageView) findViewById(R.id.ima);
		surfaceView.setAnimation(animation);
		
		Rotate3d rotate3d= new Rotate3d();
		rotate3d.setDuration(3000);
		rotate3d.setInterpolator(linearInterpolator);
		//设置循环动画
		rotate3d.setRepeatCount(-1);
//		rotate3d.setRepeatMode(Animation.INFINITE);
		imageView.startAnimation(rotate3d);
//		surfaceView.startAnimation(rotate3d);
		
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
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}
	

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "SurfaceViewTest";
	}

}
