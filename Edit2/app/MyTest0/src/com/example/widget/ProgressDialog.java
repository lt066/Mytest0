package com.example.widget;

import com.example.mytest0.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

public class ProgressDialog extends DialogFragment{

	private Context context;
	private Boolean isshow;
	public ProgressDialog(Context context,Boolean isshow) {
		// TODO Auto-generated constructor stub
		super();
		this.context=context;
		this.isshow=isshow;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Dialog dialog = new Dialog(context, R.style.MyProgressDialog);
		dialog.setContentView(R.layout.myprogress_dialog);
		dialog.setCanceledOnTouchOutside(isshow);
		return dialog;
	}
}
