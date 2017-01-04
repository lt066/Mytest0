package com.example.animation;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Rotate3d extends Animation {
	
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
