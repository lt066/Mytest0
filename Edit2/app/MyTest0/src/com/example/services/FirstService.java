package com.example.services;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.mytest0.R;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class FirstService extends Service{
	private TextView textView;
	private NotificationManager notificationManager;
	private Notification notification;
	private Builder builder;
	private boolean isCancel;
	private File apkFile;
	
	public void setTextView(TextView textView) {
		this.textView = textView;
	}
	
	@SuppressLint("NewApi")
	public void initNotifition()
	{
		notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		builder = new Notification.Builder(getApplicationContext());
		RemoteViews view = new RemoteViews(getPackageName(), R.layout.notification_view);
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent)
			   .setSmallIcon(R.drawable.ic_launcher)
			   .setWhen(System.currentTimeMillis())
//			   .setContentTitle("下载新版本")
//			   .setContentText("测试")
			   .setContent(view)
			   .setOngoing(false)
			   .setAutoCancel(true);
		notification = builder.build();
//		notification.contentView=view;
//		notification.contentIntent=pendingIntent;
	}
	
	class downLoadTask extends AsyncTask<Void, Void, Void> {

        @SuppressLint("NewApi")
		@Override
        protected Void doInBackground(Void... params) {
        	while(!isCancel)

        	{
        		long time = 0;
				if(System.currentTimeMillis()-500>time)
				{
					time=System.currentTimeMillis();
					int percent = (int) (((float)currentLen/(float)maxLen)*100);
					 // 更改文字
		        		notification.contentView.setTextViewText(R.id.noti_tv, "联盟app更新下载进度"+percent
		                   + "%");
		           // 更改进度条
		        		notification.contentView.setProgressBar(R.id.noti_pd, maxLen,
		                   currentLen, false);
		           // 发送消息
		        		notificationManager.notify(0, notification);
				}
				if(currentLen==maxLen)
				{
//					notificationManager.cancel(0);
					builder.setDefaults(Notification.DEFAULT_ALL);
					notification=builder.build();
					notification.contentView.setTextViewText(R.id.noti_tv, "联盟app更新下载进度"+100
			                   + "%");
			           // 更改进度条
			        		notification.contentView.setProgressBar(R.id.noti_pd, maxLen,
			                   currentLen, false);
			           // 发送消息
			        		notificationManager.notify(0, notification);
					isCancel=true;
					stopSelf();
				}
        		
        	}
        	

            return null;
        }
	}
	
	public class MyBinder extends Binder
	{
		public FirstService getService()
		{
			return FirstService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("测试", "onBind");
//		new downLoadTask().execute();
		return new MyBinder();
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		Log.d("测试", "onCreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.d("测试", "onStartCommand");
		if(textView!=null)
		{
			textView.setText("onStartCommand");
		}
		apkFile = new File(getApplicationContext().getExternalCacheDir().getAbsolutePath()+File.separator+"Shikee.apk");
		initNotifition();
		new Thread(runnable).start();
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		isCancel=true;
		Log.d("测试", "onDestroy");
		super.onDestroy();

	}
	
	private int maxLen=0;
	private int currentLen=0;
	
	
	Runnable runnable =new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			downFile();
		}
	};
	
	private void downFile()
	{
		String path="http://appserver.shikee.com/apk/ShiKeeApp.apk";
		createFilePath();		
		URL url;
		try {
			
			url = new URL(path);
			HttpURLConnection httpURLConnection=(HttpURLConnection) url.openConnection();
			httpURLConnection.setConnectTimeout(20000);
			maxLen=httpURLConnection.getContentLength();
			Log.d("测试", "步骤1"+maxLen);
			if(apkFile.exists())
			{
				if(maxLen==apkFile.length())
				{
					apkFile=null;
					httpURLConnection.disconnect();
					return;
				}
			}
			
			InputStream in= httpURLConnection.getInputStream();
			FileOutputStream fo=new FileOutputStream(apkFile);
			BufferedInputStream br=new BufferedInputStream(in);
			byte[] bs = new byte[1024]; 
			int len=0;
			downLoadTask dLoadTask=new downLoadTask();
			dLoadTask.execute();
			while ((len=br.read(bs))!=-1 && !isCancel) {
				fo.write(bs, 0, len);
				currentLen+=len;
				
			}
			
			fo.close();
			br.close();
			in.close();
			httpURLConnection.disconnect();
			dLoadTask.cancel(true);
			apkFile=null;
			if(isCancel)
			{				
				return;
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private void createFilePath()
	{
		String filePathst=getApplicationContext().getExternalCacheDir().getAbsolutePath()+File.separator;
		File filePath=new File(filePathst);
		if(!filePath.exists())
		{
			filePath.mkdirs();
		}
	}
	
	private void sendMessage(int arg1)
	{
		Message message=new Message();
		message.arg1=arg1;
		handler.sendMessage(message);
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 0:
				int percent = (int) (((float)currentLen/(float)maxLen)*100);
				 // 更改文字
                notification.contentView.setTextViewText(R.id.noti_tv, "联盟app更新下载进度"+percent
                        + "%");
                // 更改进度条
                notification.contentView.setProgressBar(R.id.noti_pd, maxLen,
                        currentLen, false);
                // 发送消息
                notificationManager.notify(0, notification);
				break;
					
			case 1:
				notificationManager.cancel(0);
				isCancel=true;
				stopSelf();
			default:
				break;
			}
		};
	};

	private void startInstall(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
		notificationManager.cancel(0);
		stopSelf();
	}
}
