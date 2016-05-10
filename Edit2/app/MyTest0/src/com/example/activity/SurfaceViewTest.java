package com.example.activity;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.widget.MyImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class SurfaceViewTest extends BaseActivity {
	
	private SurfaceView surfaceView;
	private SurfaceHolder holder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.surfaceview_test);
		initView();
	}

	public void initView() {
		surfaceView=(SurfaceView) findViewById(R.id.surface);
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
	}
	
	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}
	
	class Rotate3d extends Animation {
		
	    private float mCenterX;
	    private float mCenterY;
	     
	    private Camera mCamera;
		
		@Override
		public void initialize(int width, int height, int parentWidth, int parentHeight) {
			// TODO Auto-generated method stub
			super.initialize(width, height, parentWidth, parentHeight);
			mCenterX = width / 2;
	        mCenterY = height / 2;
	        mCamera = new Camera();
		}
		
	    @Override
	    protected void applyTransformation(float interpolatedTime, Transformation t) {
	        Matrix matrix = t.getMatrix();
	        mCamera.save();

	        // 设置camera动作为绕Y轴旋转
	        // 总共旋转360度，因此计算在每个补间时间点interpolatedTime的角度即为两着相乘
	        mCamera.rotateY(359 * interpolatedTime); 

	        // 根据camera动作产生一个matrix，赋给Transformation的matrix，以用来设置动画效果
	        mCamera.getMatrix(matrix);

	        mCamera.restore();
	        matrix.preTranslate(-mCenterX, -mCenterY);  
		    matrix.postTranslate(mCenterX, mCenterY);  
	    }
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "SurfaceViewTest";
	}

}
