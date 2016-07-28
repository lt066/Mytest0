package com.example.mytest0;


import android.util.Log;

import com.example.activity.Activity1;
import com.example.activity.Activity2;
import com.example.activity.Activity3;
import com.example.activity.Activity4;
import com.example.activity.Activity5;
import com.example.activity.Activity6;
import com.example.activity.DictResolverActivity;
import com.example.activity.FileUseTest;
import com.example.activity.GalleryActivity;
import com.example.activity.GaodeMap_activity;
import com.example.activity.Map_Activity;
import com.example.activity.MyRefreshActivity;
import com.example.activity.SMSCode_Activity;
import com.example.activity.SendSms;
import com.example.activity.SocketActivity;
import com.example.activity.SurfaceViewTest;
import com.example.interfaces.Login_interface;
import com.example.widget.LoginDialog;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements OnClickListener, Login_interface {
	
	private final static String ACTIVITY7_ACTION="com.example.intent.action.activity7";
	private TextView text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(getSupportActionBar()!=null)
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
			
		}
		initView();
	}
	
	private void initView()
	{
		findViewById(R.id.button1).setOnClickListener(this);
		findViewById(R.id.button2).setOnClickListener(this);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
		findViewById(R.id.button5).setOnClickListener(this);
		findViewById(R.id.button6).setOnClickListener(this);
		findViewById(R.id.button7).setOnClickListener(this);
		findViewById(R.id.button8).setOnClickListener(this);
		findViewById(R.id.button9).setOnClickListener(this);
		findViewById(R.id.button10).setOnClickListener(this);
		findViewById(R.id.button11).setOnClickListener(this);
		findViewById(R.id.button12).setOnClickListener(this);
		findViewById(R.id.button13).setOnClickListener(this);
		findViewById(R.id.button14).setOnClickListener(this);
		findViewById(R.id.button15).setOnClickListener(this);
		findViewById(R.id.button16).setOnClickListener(this);
		findViewById(R.id.button17).setOnClickListener(this);
		findViewById(R.id.button18).setOnClickListener(this);
		text=(TextView) findViewById(R.id.text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@SuppressLint("NewApi") @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id =v.getId();
		switch (id) {
		case R.id.button1:
			startActivity(new Intent(MainActivity.this,Activity2.class));
			break;
		case R.id.button2:
			startActivity(new Intent(MainActivity.this,Activity3.class));
			break;
		case R.id.button3:
			startActivity(new Intent(MainActivity.this,Activity4.class));
			break;
		case R.id.button4:
			startActivity(new Intent(MainActivity.this,Activity1.class));
			break;
		case R.id.button5:
			startActivity(new Intent(MainActivity.this,Activity5.class));
			break;
		case R.id.button6:
			startActivity(new Intent(MainActivity.this,Activity6.class));
			break;
		case R.id.button7:
			Intent intent = new Intent();
			//一般使用action，category
			intent.setAction(MainActivity.ACTIVITY7_ACTION);
			intent.addCategory("com.example.intent.category.activity7");
			
			//打开系统浏览器
//			intent.setAction(Intent.ACTION_VIEW);
//			Uri uri = Uri.parse("http://www.baidu.com");
//			intent.setData(uri);
			
			//打开某一联系人
//			intent.setAction(Intent.ACTION_EDIT);
//			Uri uri = Uri.parse("content://com.android.contacts/contacts/15");
//			intent.setData(uri);
			
			//打开拨号界面
//			intent.setAction(Intent.ACTION_DIAL);
//			Uri uri = Uri.parse("tel:10086");
//			intent.setData(uri);
			
//			intent.setAction(Intent.ACTION_GET_CONTENT);
//			intent.setType("vnd.android.cursor.item/phone");
			startActivity(intent);
			break;
		case R.id.button8:
			startActivity(new Intent(MainActivity.this,SurfaceViewTest.class));
			break;
		case R.id.button9:
			startActivity(new Intent(MainActivity.this,FileUseTest.class));
			break;
		case R.id.button10:
			startActivity(new Intent(MainActivity.this,DictResolverActivity.class));
			break;
		case R.id.button11:
			startActivity(new Intent(MainActivity.this,SendSms.class));
			break;
		case R.id.button12:
			startActivity(new Intent(MainActivity.this,SocketActivity.class));
			break;
		case R.id.button13:
			LoginDialog loginDialog=new LoginDialog(this);
			loginDialog.show(getFragmentManager(), "登录窗口");
			loginDialog.setLoginInterface(this);
			break;
		case R.id.button14:
			startActivity(new Intent(MainActivity.this,SMSCode_Activity.class));
			break;
		case R.id.button15:
			startActivity(new Intent(MainActivity.this,Map_Activity.class));
			break;
		case R.id.button16:
			startActivity(new Intent(MainActivity.this,GaodeMap_activity.class));
			break;
		case R.id.button17:
			startActivity(new Intent(MainActivity.this,GalleryActivity.class));
			break;
		case R.id.button18:
			startActivity(new Intent(MainActivity.this,MyRefreshActivity.class));
			break;
		default:
			break;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("测试", "onActivityResult");
		switch (requestCode) {
		case 0:
//			if(resultCode==Activity.RESULT_OK)
//			{
				Log.d("测试", "onActivityResult1");
				Uri contactsData=data.getData();
				CursorLoader cursorLoader = new CursorLoader(this,contactsData,null,null,null,null);
				Cursor cursor = cursorLoader.loadInBackground();
				if(cursor.moveToFirst())
				{
					Log.d("测试", "onActivityResult2");
					String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
					text.setText("name:"+name);
				}
//			}
			
			break;

		default:
			break;
		}
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "主界面";
	}

	@Override
	public void runLogin() {
		// TODO Auto-generated method stub
		showToastMsgShort("登录");
	}
}
