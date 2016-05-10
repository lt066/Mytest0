package com.example.widget;

import com.example.mytest0.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class MyImageView extends ImageView{
	
	
	private Context context;
	private Bitmap showBitmap;
	private Matrix matrix;
	private Camera camera;
	private int deltaX,deltaY;
	private int centerX,centerY;


	public MyImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}
	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}

	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
	}



	@SuppressLint("NewApi")
	public MyImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
		this.context=context;
		init();
		Rotate3d rotate3d= new Rotate3d();
		rotate3d.setDuration(2000);
		startAnimation(rotate3d);
	}

	

	private void init() {
//		Drawable drawable= getBackground();
		if(getDrawable()!=null)
		{
			showBitmap = ((BitmapDrawable)getDrawable()).getBitmap();
		}
//		else {
//			showBitmap =BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);
//		}
        centerX = showBitmap.getWidth()/2 ;  
        centerY = showBitmap.getHeight()/2 ;  
        matrix = new Matrix();  
        camera = new Camera();  
	}
	
	  int lastMouseX ;  
	  int lastMouseY ;  
	  
	  @Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		  int x = (int) event.getX();      
	        int y = (int) event.getY();      
	        switch(event.getAction()) {      
	         case MotionEvent.ACTION_DOWN:   
	             lastMouseX = x ;  
	             lastMouseY = y ;  
	             break;      
	         case MotionEvent.ACTION_MOVE:  
	             int dx = x - lastMouseX ;  
	             int dy = y - lastMouseY ;  
	             deltaX += dx ;  
	             deltaY += dy ;  
	             break;      
	         }     
	         
	        invalidate();  
	        return true;  
	}
	  
	  @Override
	public void draw(Canvas canvas) {
//		  camera.save();  
//	        //绕X轴翻转   
////	      camera.rotateX(-deltaY);  
//	        //绕Y轴翻转   
//	      camera.rotateY(360);  
//	        //设置camera作用矩阵   
//	      camera.getMatrix(matrix);  
//	      camera.restore();  
//	        //设置翻转中心点   
	      matrix.preTranslate(-this.centerX, -this.centerY);  
	      matrix.postTranslate(this.centerX, this.centerY);  
	          
	      canvas.drawBitmap(showBitmap, matrix, null);  
	}
	  
	  class Rotate3d extends Animation {
		    @Override
		    protected void applyTransformation(float interpolatedTime, Transformation t) {
		        Matrix matrix = t.getMatrix();
		        Camera camera = new Camera();
		        camera.save();

		        // 设置camera动作为绕Y轴旋转
		        // 总共旋转180度，因此计算在每个补间时间点interpolatedTime的角度即为两着相乘
		        camera.rotateY(180 * interpolatedTime); 

		        // 根据camera动作产生一个matrix，赋给Transformation的matrix，以用来设置动画效果
		        camera.getMatrix(matrix);

		        camera.restore();
		    }
		}
	
}
