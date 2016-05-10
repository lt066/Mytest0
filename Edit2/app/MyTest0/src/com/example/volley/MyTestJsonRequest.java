package com.example.volley;

import java.util.Map;

import org.json.JSONObject;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.widget.Toast;

public class MyTestJsonRequest extends MyTestRequestBase{

	public MyTestJsonRequest(int method, String url, JSONObject jsonObject, Listener<JSONObject> listener,
			final ErrorListener errorlistener, Map<String, String> params,final Activity ac) {
		super(method, method==Method.POST?url:Volley.createUrlForMethodGet(url, params), jsonObject, 
				listener, new ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						// TODO Auto-generated method stub
						if (error instanceof TimeoutError) {
							showToastMsg(ac, "连接超时，请保持网络畅通");
						} else if (error instanceof ServerError) {
							showToastMsg(ac, "连接失败，服务器可能正在休息~，请稍后再试");
						} else if (error instanceof NetworkError) {
							showToastMsg(ac, "连接失败，请确保网络处于打开状态");
						} else if (error instanceof ParseError) {
							showToastMsg(ac, "数据格式错误，请联系管理员");
						}
						error.printStackTrace();
						if (errorlistener != null) {
							errorlistener.onErrorResponse(error);
						}
					}
				}, params);
		// TODO Auto-generated constructor stub
	}
	private static void showToastMsg(Activity ac, String msg) {
		if (ac != null) {
			if (!ac.isFinishing()) {
				Toast.makeText(ac, msg, Toast.LENGTH_SHORT).show();
				// Toast.makeText(ac, msg, Toast.LENGTH_SHORT).show();
			}
		}
	}

}
