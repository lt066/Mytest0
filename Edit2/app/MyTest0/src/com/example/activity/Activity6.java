package com.example.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.mytest0.R.id;
import com.example.mytest0.R.layout;
import com.example.widget.PullToRefreshLayout;
import com.example.widget.PullToRefreshLayout.OnRefreshListener;
import com.example.widget.RecycleGridIDivider;
import com.example.widget.RecyclerDivider;
import com.example.widget.RefreshLayout;
import com.example.widget.RefreshLayout.OnLoadListener;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class Activity6 extends BaseActivity implements OnLoadListener, android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener{
	
	private RefreshLayout swipeRefreshLayout;
	private RecyclerView mRecyclerView;
	private List<String> list;
	private boolean isRefresh;
	private MyAdapter myAdapter;
	private boolean isLoad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity6);
		list=new ArrayList<String>();
		
		for(int i=0;i<16;i++)
		{
			list.add(""+i);
		}
		
		
		swipeRefreshLayout = (RefreshLayout) findViewById(R.id.swipe_refresh_widget);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setOnLoadListener(this);
		
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
		mRecyclerView.addItemDecoration(new RecycleGridIDivider(this));
		
		myAdapter=new MyAdapter(this,list);
		
		mRecyclerView.setAdapter(myAdapter);
		mRecyclerView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
//				if(recyclerView.getChildAt(mRecyclerView.getChildCount()-3).isShown() && !isRefresh)
//				{
//					showToastMsgShort("≤‚");
//					isRefresh=true;
////					onRefreshing();
//				}
//				else if(!recyclerView.getChildAt(mRecyclerView.getChildCount()-3).isShown())
//				{
//					isRefresh=false;
//				}
				
			}
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == 0) {
//					if(mRecyclerView.getChildAt(mRecyclerView.getChildCount()-1).isShown() && !isLoad)
//					{
//						isLoad=true;
////						onRefreshing();
////						new Handler(){
////							public void handleMessage(android.os.Message msg) {
////								myAdapter.notifyDataSetChanged();
////								isLoad=false;
////								this.removeCallbacksAndMessages(null);
////							}
////						}.sendEmptyMessageDelayed(0, 3000);
//						swipeRefreshLayout.setLoading(true);
//						
//					}
				}
			}
			
		});
		
	}
	
	private void onRefreshing()
	{
		for(int i=list.size()-1;i<list.size()+15;i++)
		{
			list.add(""+i);
		}
		
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}
	
	class MyAdapter extends RecyclerView.Adapter<ViewHolder>
	{

		private List<String> list;
		private Context mContext;
		private static final int TYPE_ITEM = 0;  
		private static final int TYPE_FOOTER = 1; 
		
		public MyAdapter(Context context,List<String> list) {

			this.list=list;
			mContext=context;
		}
		
		public void setList(List<String> list) {
			this.list = list;
		}
		
		@Override
		public int getItemCount() {
			// TODO Auto-generated method stub
			return list!=null?list.size()+1:0;
		}
		
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if (position + 1 == getItemCount()) {  
				   return TYPE_FOOTER;  
			} else {  
				   return TYPE_ITEM;  
			}  
		}

		@Override
		public void onBindViewHolder(ViewHolder arg0, int arg1) {
			// TODO Auto-generated method stub
			if(arg0.getItemViewType()==TYPE_ITEM)
			{
				((MyViewHolder)arg0).textView.setText("≤‚ ‘"+list.get(arg1));
			}
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == TYPE_ITEM) {  
				View view = LayoutInflater.from(mContext).inflate(  
					     R.layout.recycler_item, null);  
				return new MyViewHolder(view);
			}
			else if (arg1 == TYPE_FOOTER) {  
				   View view = LayoutInflater.from(mContext).inflate(  
				     R.layout.refresh_footer, null);  
				   view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,  
				     LayoutParams.WRAP_CONTENT));  
				   return new FooterViewHolder(view);  
			}  
				  
			return null;  
		}
		
		class MyViewHolder extends ViewHolder
		{
			TextView textView;
			public MyViewHolder(View itemView) {
				super(itemView);
				textView = (TextView) itemView.findViewById(R.id.recycler_view_item);
			}		
		}
		class FooterViewHolder extends ViewHolder {  
			  
			  public FooterViewHolder(View view) {  
			   super(view);  
		}  
	}
}

	@Override
	public void onRefresh() {
		mRecyclerView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				list.clear();
				for(int i=0;i<26;i++)
				{
					list.add(""+i);
				}
				myAdapter.notifyDataSetChanged();
				swipeRefreshLayout.setRefreshing(false);
			}
		}, 1000);
		
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		showToastMsgShort("…œ‘ÿ");
		mRecyclerView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onRefreshing();
				myAdapter.notifyDataSetChanged();
				isLoad=false;
				swipeRefreshLayout.setRefreshing(false);
				
			}
		},1000);
		
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "À¢–¬“≥√Ê";
	}
}
