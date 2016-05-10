package com.example.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.example.mytest0.BaseActivity;
import com.example.mytest0.R;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class FileUseTest extends BaseActivity implements OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fileuse_test);
		initView();
	}
	
	public void initView()
	{
		findViewById(R.id.create_file).setOnClickListener(this);
		findViewById(R.id.writing_file).setOnClickListener(this);
		findViewById(R.id.read_file).setOnClickListener(this);
		findViewById(R.id.delete_file).setOnClickListener(this);
		findViewById(R.id.delete_char).setOnClickListener(this);
	}

	@Override
	protected Context getActivityContext() {
		return this;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		String dir_parent=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"FileUseTest";
		String dir=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"FileUseTest"+File.separator+"a.txt";
		switch (id) {
		case R.id.create_file:
//			create_FilePath(dir_parent);
			create_File(dir);
			break;
		case R.id.writing_file:
			writing_file(dir, "写入第一次\n");
			break;
		case R.id.read_file:
			showToastMsgShort(read_file(dir));
			break;
		case R.id.delete_file:
			delete_file(dir);
			break;
		case R.id.delete_char:
			delete_char(dir, "一");
			break;

		default:
			break;
		}
	}
	
	public void create_FilePath(String dir)
	{
		File file = new File(dir);
		Log.d("测试", file.getPath());
		if(!file.exists())
		{		
			file.mkdirs();
			Log.d("测试", "进入创建");
			
			
		}
		
	}
	
	public void create_File(String dir)
	{
		File file = new File(dir);
		
		File file2= new File(file.getParent());
		Log.d("测试", file2.exists()+"");
		if(!file2.exists())
		{
			file2.mkdirs();
		}
		
		if(!file.exists() && file2.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				showToastMsgShort("创建文件："+dir+"失败！");
				Log.d("测试", e.toString());
			}
		}
		else {
			showToastMsgShort(dir+"文件已存在！或父路径文件夹不存在");
		}
	}
	
	public void writing_file(String dir,String text)
	{
		try {
			
			FileInputStream fileInputStream = new FileInputStream(dir);
			byte[] bs= new byte[fileInputStream.available()];
			fileInputStream.read(bs);
			String read_text=new String(bs);
			Log.d("测试", read_text.toString());
			fileInputStream.close();
			
			read_text=read_text+text;
			
			FileOutputStream fileOutputStream = new FileOutputStream(dir);
			fileOutputStream.write(read_text.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String read_file(String dir)
	{
		try {
			
			FileInputStream fileInputStream = new FileInputStream(dir);
			byte[] bs= new byte[fileInputStream.available()];
			fileInputStream.read(bs);
			String read_text=new String(bs);
			Log.d("测试", read_text.toString());
			fileInputStream.close();
			return read_text;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public void delete_char(String dir,String text)
	{
		try {
			FileInputStream fileInputStream = new FileInputStream(dir);
			byte[] bs= new byte[fileInputStream.available()];
			fileInputStream.read(bs);
			String read_text=new String(bs);
			Log.d("测试", read_text.toString());
			fileInputStream.close();
			
			if(read_text.contains(text))
			{
				read_text=read_text.replace(text, "二");
				Log.d("测试", "替换后："+read_text.toString());
			}
			
			FileOutputStream fileOutputStream = new FileOutputStream(dir);
			fileOutputStream.write(read_text.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void delete_file(String dir)
	{
		File file = new File(dir);
		if(file.exists())
		{
			file.delete();
		}
	}

	@Override
	protected String getActivityName() {
		// TODO Auto-generated method stub
		return "File功能";
	}

}
