package com.example.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;


import cn.smssdk.SMSSDK;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

public class SMSCode_Activity extends BaseActivity implements OnClickListener{
	
	private EditText phone_Text;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smscode_activity);
		findViewById(R.id.sendSmsCode).setOnClickListener(this);
		phone_Text=(EditText) findViewById(R.id.phone);
	}

	@Override
	protected Context getActivityContext() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "发送短信验证码";
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.sendSmsCode)
		{
			if(checkPhone(phone_Text.getText().toString()))
			{
				SMSSDK.getVerificationCode("86", phone_Text.getText().toString());
			}
		}
	}
	
	private boolean checkPhone(String phone)
	{
		
		Pattern pattern=Pattern.compile("^1\\d{10}$");
		Matcher matcher=pattern.matcher(phone);
		if(matcher.matches())
		{
			return true;
		}
		else {
			showToastMsgShort("手机号格式不正确");
			return false;
		}
	}

}
