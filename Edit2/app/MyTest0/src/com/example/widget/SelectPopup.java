package com.example.widget;

import java.util.ArrayList;
import java.util.List;
import com.example.mytest0.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;

public class SelectPopup extends PopupWindow{
	private View view;
	private List<String> list;
	private Context context;
	
	public SelectPopup(Context context) {
		super(context);
		this.context=context;
		setTouchable(true);
		setOutsideTouchable(true);
		view=LayoutInflater.from(context).inflate(R.layout.selectpop_layout, null);
		initView();
		this.setContentView(view);
		 //设置宽度  
        setWidth(LayoutParams.MATCH_PARENT);  
        //设置高度  
        setHeight(LayoutParams.WRAP_CONTENT); 
		
	}
	
	private void initView()
	{
		Spinner spinner0 = (Spinner) view.findViewById(R.id.spiner0);
		Spinner spinner1 = (Spinner) view.findViewById(R.id.spiner1);
		Spinner spinner2 = (Spinner) view.findViewById(R.id.spiner2);
		Spinner spinner3 = (Spinner) view.findViewById(R.id.spiner3);
		
		list = new ArrayList<String>();
		list.add("北京");
		list.add("上海");
		list.add("广州");
		list.add("深圳");
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, list);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		spinner0.setAdapter(adapter);
		spinner1.setAdapter(adapter);
		spinner2.setAdapter(adapter);
		spinner3.setAdapter(adapter);
	}

}
