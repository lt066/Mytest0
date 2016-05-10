package com.jingchen.pulltorefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import com.jingchen.pulltorefresh.MyListener;
import com.jingchen.pulltorefresh.PullToRefreshLayout;
import com.jingchen.pulltorefresh.R;

public class RecyclerViewDemo extends Activity {
	private RecyclerView mRecyclerView;
	private RecyclerView.Adapter mAdapter;
	private RecyclerView.LayoutManager mLayoutManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// android.support.v4.util.Pools
		setContentView(R.layout.recycler_view_demo_layout);
		((PullToRefreshLayout) findViewById(R.id.refresh_view))
		.setOnRefreshListener(new MyListener());
		mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		mRecyclerView.setHasFixedSize(true);

		mLayoutManager = new LinearLayoutManager(this);
		mRecyclerView.setLayoutManager(mLayoutManager);
		

		// specify an adapter (see also next example)
		mAdapter = new MyAdapter(new String[] { "1", "2", "3", "4", "5", "6",
				"7", "8", "8", "8", "8", "8", "8", "8", "8", "8", "8", "8",
				"8", "8", "8", "8", "8" });
		mRecyclerView.setAdapter(mAdapter);
		mRecyclerView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
					int newState) {
				// TODO Auto-generated method stub
				super.onScrollStateChanged(recyclerView, newState);
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				// TODO Auto-generated method stub
				super.onScrolled(recyclerView, dx, dy);
				
			}
			
		});
	}

}
