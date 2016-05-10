package com.example.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.example.model.Message_detail;
import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;
import com.example.volley.MyTestJsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Activity1 extends BaseActivity {
	
	private TextView text1;
	private Message_detail message_detail=null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity1);
		initView();
		getdata();
	}
	
	private void initView()
	{
		text1=(TextView) findViewById(R.id.text1);
	}
	
	private void  getdata()
	{
		showLoadView(false);
		String url = "http://appserver.sk.com/message/message_detail";
//		String url = "http://appserver.sk3.com/trys/try_list";
		MyTestJsonRequest myTestJsonRequest = new MyTestJsonRequest(Method.POST, url, null,
				new Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						// TODO Auto-generated method stub
						hideLoadView();
						Gson gson =new Gson();
						try {
							message_detail=gson.fromJson(response.getString("data"), Message_detail.class);
							text1.setText(Html.fromHtml(message_detail.getContent().toString()));
							text1.setMovementMethod(LinkMovementMethod.getInstance());
						} catch (JsonSyntaxException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				hideLoadView();
				showToastMsgShort("³ö´í");
			}
		},getParams(),this);
		
		mRequestQueue.add(myTestJsonRequest);
	}
	
	private Map<String, String> getParams()
	{
		Map<String, String> map =new HashMap<String, String>();
		map.put("uid", "2092407339");
		map.put("sign", "ee1f512fbfea60d3f72cb7fdb1f83fa9");
		map.put("client_type", "1");
		map.put("id", "306523");
		map.put("version", "2.2");
		return map;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity1, menu);
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
			finish();
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
		return "Activity1";
	}
}
