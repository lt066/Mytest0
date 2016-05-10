package com.example.widget;


import com.example.mytest0.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint({ "DrawAllocation", "Recycle" })
public class ImageTextView extends ImageView{
	
	private String mText;
    private int mBorderThickness = 0;  
    private int defaultColor = 0xFFFFFFFF;  
    // ���ֻ������һ����ֵ����ֻ��һ��Բ�α߿�  
    private int mBorderOutsideColor = 0;  
    private int mBorderInsideColor = 0;  
    // �ؼ�Ĭ�ϳ�����  
    private int defaultWidth = 0;  
    private int defaultHeight = 0;  
	
	public ImageTextView(Context context) {
		super(context);
	}

	public ImageTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageTextView);
		int textId = typedArray.getResourceId(R.styleable.ImageTextView_Text, 0);
        mBorderThickness = typedArray.getDimensionPixelSize(R.styleable.ImageTextView_border_thickness, 0);  
        mBorderOutsideColor = typedArray.getColor(R.styleable.ImageTextView_border_outside_color,defaultColor);  
        mBorderInsideColor = typedArray.getColor(R.styleable.ImageTextView_border_inside_color, defaultColor);  
		mText=textId > 0 ? typedArray.getResources().getText(textId).toString() : "";
	}
	
	
	@SuppressLint({ "DrawAllocation", "NewApi" })
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		 if (drawable == null) {  
             return;  
         }  
         if (getWidth() == 0 || getHeight() == 0) {  
             return;  
         }  
         this.measure(0, 0);  
 		Bitmap b= ((BitmapDrawable)drawable).getBitmap();
 		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);   
         if (defaultWidth == 0) {  
             defaultWidth = getWidth();  
         }  
         if (defaultHeight == 0) {  
             defaultHeight = getHeight();  
         }  
         int radius = 0;  
         if (mBorderInsideColor != defaultColor && mBorderOutsideColor != defaultColor) {// ���廭�����߿򣬷ֱ�Ϊ��Բ�߿����Բ�߿�  
             radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - 2 * mBorderThickness;  
             // ����Բ  
             drawCircleBorder(canvas, radius + mBorderThickness / 2,mBorderInsideColor);  
             // ����Բ  
             drawCircleBorder(canvas, radius + mBorderThickness + mBorderThickness / 2, mBorderOutsideColor);  
         } else if (mBorderInsideColor != defaultColor && mBorderOutsideColor == defaultColor) {// ���廭һ���߿�  
             radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;  
             drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderInsideColor);  
         } else if (mBorderInsideColor == defaultColor && mBorderOutsideColor != defaultColor) {// ���廭һ���߿�  
             radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2 - mBorderThickness;  
             drawCircleBorder(canvas, radius + mBorderThickness / 2, mBorderOutsideColor);  
         } else {// û�б߿�  
             radius = (defaultWidth < defaultHeight ? defaultWidth : defaultHeight) / 2;  
         }  

		Paint mPaint = new Paint();
		mPaint.setColor(Color.RED);
		Bitmap rouB = getCroppedRoundBitmap(bitmap, radius);
		canvas.drawBitmap(rouB, defaultWidth/2- radius, defaultHeight/2- radius,null);
		mPaint.setTextSize(20);
		canvas.drawText(mText, defaultWidth/2, defaultHeight/2, mPaint);
		
	}
	 /**  
     * ��ȡ�ü����Բ��ͼƬ  
     * @param radius�뾶  
     */  
    public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
    	 Bitmap scaledSrcBmp;  
         int diameter = radius * 2;  
         // Ϊ�˷�ֹ��߲���ȣ����Բ��ͼƬ���Σ���˽�ȡ�������д����м�λ������������ͼƬ  
         int bmpWidth = bmp.getWidth();  
         int bmpHeight = bmp.getHeight();  
         int squareWidth = 0, squareHeight = 0;  
         int x = 0, y = 0;  
         Bitmap squareBitmap;  
         if (bmpHeight > bmpWidth) {// �ߴ��ڿ�  
             squareWidth = squareHeight = bmpWidth;  
             x = 0;  
             y = (bmpHeight - bmpWidth) / 2;  
             // ��ȡ������ͼƬ  
             squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth, squareHeight);  
         } else if (bmpHeight < bmpWidth) {// ����ڸ�  
             squareWidth = squareHeight = bmpHeight;  
             x = (bmpWidth - bmpHeight) / 2;  
             y = 0;  
             squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,squareHeight);  
         } else {  
             squareBitmap = bmp;  
         }  
         if (squareBitmap.getWidth() != diameter || squareBitmap.getHeight() != diameter) {  
             scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,diameter, true);  
         } else {  
             scaledSrcBmp = squareBitmap;  
         }   
         Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),  
                 scaledSrcBmp.getHeight(),   
                 Config.ARGB_8888);  
         Canvas canvas = new Canvas(output);  
   
         Paint paint = new Paint();  
         Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),scaledSrcBmp.getHeight());  
   
         paint.setAntiAlias(true);  
         paint.setFilterBitmap(true);  
         paint.setDither(true);  
         canvas.drawARGB(0, 0, 0, 0);  
         canvas.drawCircle(scaledSrcBmp.getWidth() / 2,  
                 scaledSrcBmp.getHeight() / 2,   
                 scaledSrcBmp.getWidth() / 2,  
                 paint);  
         paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  
         canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);  
         bmp = null;  
         squareBitmap = null;  
         scaledSrcBmp = null;  
         return output;  
    	
    }
    
    /**  
     * ��Ե��Բ  
     */  
    private void drawCircleBorder(Canvas canvas, int radius, int color) {  
        Paint paint = new Paint();  
        /* ȥ��� */  
        paint.setAntiAlias(true);  
        paint.setFilterBitmap(true);  
        paint.setDither(true);  
        paint.setColor(color);  
        /* ����paint�ġ�style��ΪSTROKE������ */  
        paint.setStyle(Paint.Style.STROKE);  
        /* ����paint������� */  
        paint.setStrokeWidth(mBorderThickness);  
        canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);  
    }  


}
