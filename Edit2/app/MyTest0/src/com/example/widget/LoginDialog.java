package com.example.widget;

import com.example.interfaces.Login_interface;
import com.example.mytest0.R;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
@SuppressLint("NewApi") 
public class LoginDialog extends DialogFragment implements android.view.View.OnClickListener{
	private Login_interface login_interface;
	private Activity parentActivity;
	
	public LoginDialog(Activity a) {
		// TODO Auto-generated constructor stub
		parentActivity=a;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getDialog().getWindow();
//		WindowManager.LayoutParams lp = window.getAttributes();
//		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
//		window.setAttributes(lp);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.login_dialog, ((ViewGroup) window.findViewById(android.R.id.content)),false);
		view.findViewById(R.id.button_login).setOnClickListener(this);
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		window.setLayout(-1, -2);
		return view;
	}

	/*
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.login_dialog, null);
		builder.setView(view)
			   .setPositiveButton("��¼", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					login_interface.runLogin();
				}
			})
				.setNegativeButton("ȡ��", null);
		return builder.create();
	}
	*/
	
	public void setLoginInterface(Login_interface li)
	{
		login_interface=li;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.button_login)
		{
			login_interface.runLogin();
		}
	}
	
}
