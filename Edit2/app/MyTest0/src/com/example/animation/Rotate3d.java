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

        // ����camera����Ϊ��Y����ת
        // �ܹ���ת360�ȣ���˼�����ÿ������ʱ���interpolatedTime�ĽǶȼ�Ϊ�������
        mCamera.rotateY(359 * interpolatedTime); 

        // ����camera��������һ��matrix������Transformation��matrix�����������ö���Ч��
        mCamera.getMatrix(matrix);

        mCamera.restore();
        matrix.preTranslate(-mCenterX, -mCenterY);  
	    matrix.postTranslate(mCenterX, mCenterY);  
    }
}
