package com.example.mytest0;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.helper.ToolBarHelper;
import com.example.widget.ProgressDialog;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

@SuppressLint("NewApi") public abstract class BaseActivity extends ActionBarActivity {

	private ProgressDialog mProgressDialog;
	public RequestQueue mRequestQueue;
	private Toolbar toolbar;
	private ToolBarHelper toolBarHelper;
	
	@SuppressLint("NewApi") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(this);
        }
	}
	
	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		toolBarHelper = new ToolBarHelper(this,layoutResID,getActivityName()) ;
        toolbar = toolBarHelper.getToolBar() ;
        setContentView(toolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        onCreateCustomToolBar(toolbar) ;
        
        if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setHomeButtonEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//			getSupportActionBar().setHomeAsUpIndicator(R.drawable.title_back_arrow);
		}
	}
	public void onCreateCustomToolBar(Toolbar toolbar){
        toolbar.setContentInsetsRelative(0,0);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if(id==android.R.id.home)
		{
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") protected void showLoadView(Boolean isShow)
	{
		mProgressDialog=new ProgressDialog(getActivityContext(),isShow);
		mProgressDialog.show(getFragmentManager(), null);
	}
	
	@SuppressLint("NewApi") protected void hideLoadView()
	{
		mProgressDialog.dismiss();
	}
	
	protected void showToastMsgShort(String msg)
	{
		Toast.makeText(getActivityContext(), msg, Toast.LENGTH_SHORT).show();
	}
	
	protected void showToastMsgLong(String msg)
	{
		Toast.makeText(getActivityContext(), msg, Toast.LENGTH_LONG).show();
	}
	
	protected abstract Context getActivityContext();
	protected abstract String getActivityName();
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mRequestQueue.stop();
	}
}
