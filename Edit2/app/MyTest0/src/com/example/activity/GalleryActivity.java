package com.example.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.widget.MyHorizontalScrollView;
import com.example.widget.MyHorizontalScrollView.CurrentImageChangeListener;
import com.example.widget.MyHorizontalScrollView.OnItemClickListener;

public class GalleryActivity extends BaseActivity{
	
	 private List<Integer> mDatas = new ArrayList<Integer>(
			 Arrays.asList(  
						R.drawable.img1,
						R.drawable.img2,
						R.drawable.img3,
						R.drawable.img4,
						R.drawable.img5,
						R.drawable.img6)); 

	 private MyHorizontalScrollView mHorizontalScrollView;  
	 private HorizontalScrollViewAdapter mAdapter;  
	 private ImageView mImg;
	
	 @Override  
	    protected void onCreate(Bundle savedInstanceState)  
	    {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_gallery);  
	  
	        mImg = (ImageView) findViewById(R.id.switcher);  
	  
	        mHorizontalScrollView = (MyHorizontalScrollView) findViewById(R.id.id_horizontalScrollView);  
	        mAdapter = new HorizontalScrollViewAdapter(this, mDatas);  
	        //添加滚动回调  
	        mHorizontalScrollView  
	                .setCurrentImageChangeListener(new CurrentImageChangeListener()  
	                {  
	                    @Override  
	                    public void onCurrentImgChanged(int position,  
	                            View viewIndicator)  
	                    {  
	                        mImg.setImageResource(mDatas.get(position));  
	                        viewIndicator.setBackgroundColor(Color  
	                                .parseColor("#AA024DA4"));  
	                    }  
	                });  
	        //添加点击回调  
	        mHorizontalScrollView.setOnItemClickListener(new OnItemClickListener()  
	        {  

				@Override
				public void onClick(View view, int position) {
					// TODO Auto-generated method stub
					mImg.setImageResource(mDatas.get(position));  
	                view.setBackgroundColor(Color.parseColor("#AA024DA4"));  
				}  
	        });  
	        //设置适配器  
	        mHorizontalScrollView.initDatas(mAdapter);  
	    }  
	  

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "使用Gallery";
	}
	  
	public class HorizontalScrollViewAdapter  
	{  
	  
	    private Context mContext;  
	    private LayoutInflater mInflater;  
	    private List<Integer> mDatas;  
	  
	    public HorizontalScrollViewAdapter(Context context, List<Integer> mDatas)  
	    {  
	        this.mContext = context;  
	        mInflater = LayoutInflater.from(context);  
	        this.mDatas = mDatas;  
	    }  
	  
	    public int getCount()  
	    {  
	        return mDatas.size();  
	    }  
	  
	    public Object getItem(int position)  
	    {  
	        return mDatas.get(position);  
	    }  
	  
	    public long getItemId(int position)  
	    {  
	        return position;  
	    }  
	  
	    public View getView(int position, View convertView, ViewGroup parent)  
	    {  
	        ViewHolder viewHolder = null;  
	        if (convertView == null)  
	        {  
	            viewHolder = new ViewHolder();  
	            convertView = mInflater.inflate(  
	                    R.layout.activity_index_gallery_item, parent, false);  
	            viewHolder.mImg = (ImageView) convertView  
	                    .findViewById(R.id.id_index_gallery_item_image);  
	            viewHolder.mText = (TextView) convertView  
	                    .findViewById(R.id.id_index_gallery_item_text);  
	  
	            convertView.setTag(viewHolder);  
	        } else  
	        {  
	            viewHolder = (ViewHolder) convertView.getTag();  
	        }  
	        viewHolder.mImg.setImageResource(mDatas.get(position));  
	        viewHolder.mText.setText("some info ");  
	  
	        return convertView;  
	    }  
	  
	    private class ViewHolder  
	    {  
	        ImageView mImg;  
	        TextView mText;  
	    }  
	  
	}  

}
