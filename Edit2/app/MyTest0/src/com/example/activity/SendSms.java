package com.example.activity;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendSms extends BaseActivity{
	
	private EditText sendPhone;
	private EditText sendContent;
	private Button sendSmsButton;
	private SmsManager smsManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendsms);
		initView();
	}
	
	private void initView()
	{
		sendPhone=(EditText) findViewById(R.id.phone);
		sendContent=(EditText) findViewById(R.id.content);
		sendSmsButton=(Button) findViewById(R.id.sendSms);
		smsManager=SmsManager.getDefault();
		
		sendSmsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PendingIntent pi =  PendingIntent.getActivity(SendSms.this, 0, new Intent(), 0);
				smsManager.sendTextMessage(sendPhone.getText().toString(), null, sendContent.getText().toString(), pi, null);
			}
		});
	}
	
	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "·¢ËÍ¶ÌÐÅ";
	}

}
