package com.example.object;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * webview中调用native方法
 * @author lt
 * @date 2016-05-06
 * 
 */

public class JavaScriptInterface1 {
	private Context context;
	
	public JavaScriptInterface1(Context c) {
		// TODO Auto-generated constructor stub
		context=c;
	}
	
	//------android 代码示例------
	//android 4.2 之后版本提供给js调用的函数必须带有注释语句@JavascriptInterface
	@JavascriptInterface
    public void callFromJSBasicDataType(int x,float y,char c,boolean result){
           String str ="-"+(x+1)+"-"+(y+1)+"-"+c+"-"+result;
           System.out.print(str);
           Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
           Log.d("测试",str);
       }

    @JavascriptInterface
    public void callAndroidMethod(){
          Log.d("测试","回调函数");
       }
}
