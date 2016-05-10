package com.example.volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import android.os.Build;
import android.util.Log;


public class MyTestRequestBase extends Request<JSONObject>{
	
	protected Listener<JSONObject> mListener;
	protected Map<String, String> params;

	public MyTestRequestBase(int method, String url, JSONObject jsonObject,Listener<JSONObject> listener,
			ErrorListener errorlistener,Map<String, String> params) {
		super(method, url,errorlistener);
		// TODO Auto-generated constructor stub
		setRetryPolicy(new DefaultRetryPolicy(60000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		mListener=listener;
		this.params=params;
	}
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
//		if (ShiKeeConstant.debug) {
//			for (Entry<String, String> entry : params.entrySet()) {
//				Log.d(tag, entry.getKey() + "：" + entry.getValue());
//			}
//		}
		return params;
	}
	
	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		// TODO Auto-generated method stub
		String jsonString = "";
		try {
			jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

			if (Build.VERSION.SDK_INT < 14) {
				if (jsonString != null && jsonString.startsWith("\ufeff")) {
					jsonString = jsonString.substring(1);
				}
			}
//			if (ShiKeeConstant.debug) {
//				Log.i(tag, "接口返回数据：" + jsonString);
//			}
			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {

			return Response.error(new ParseError(je));
		}
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		// TODO Auto-generated method stub
		mListener.onResponse(response);
	}

}
