package com.example.widget;

import java.util.List;

import com.example.mytest0.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	List<String> items;
	Context context;

	public MyAdapter(Context context, List<String> items) {
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ViewHolder")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view =null;
		if(position<=1){
			view = LayoutInflater.from(context).inflate(R.layout.frist_item, null);
		}else {
			view = LayoutInflater.from(context).inflate(R.layout.main_section_item, null);
		}
//		TextView tv = (TextView) view.findViewById(R.id.tv);
//		tv.setText(items.get(position));
		return view;
	}

}
