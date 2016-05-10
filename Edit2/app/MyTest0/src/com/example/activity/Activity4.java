package com.example.activity;


import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Activity4 extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity4);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity4, menu);
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
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "Activity4";
	}
}
