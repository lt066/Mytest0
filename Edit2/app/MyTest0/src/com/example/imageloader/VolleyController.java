package com.example.imageloader;

/**
 * Created by lt413 on 2015/12/27.
 */
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyController {

    // 鍒涘缓涓�涓猅AG锛屾柟渚胯皟璇曟垨Log
    private static final String TAG = "VolleyController";

    // 鍒涘缓涓�涓叏灞�鐨勮姹傞槦鍒�
    private RequestQueue reqQueue;
    private ImageLoader imageLoader;

    // 鍒涘缓涓�涓猻tatic VolleyController瀵硅薄锛屼究浜庡叏灞�璁块棶
    private static VolleyController mInstance;

    private Context mContext;

    private VolleyController(Context context) {
        mContext=context;
    }

    /**
     * 浠ヤ笅涓洪渶瑕佹垜浠嚜宸卞皝瑁呯殑娣诲姞璇锋眰鍙栨秷璇锋眰绛夋柟娉�
     */

    // 鐢ㄤ簬杩斿洖涓�涓猇olleyController鍗曚緥
    public static VolleyController getInstance(Context context) {
        if (mInstance == null) {
            synchronized(VolleyController.class)
            {
                if (mInstance == null) {
                    mInstance = new VolleyController(context);
                }
            }
        }
        return mInstance;
    }

    // 鐢ㄤ簬杩斿洖鍏ㄥ眬RequestQueue瀵硅薄锛屽鏋滀负绌哄垯鍒涘缓瀹�
    public RequestQueue getRequestQueue() {
        if (reqQueue == null){
            synchronized(VolleyController.class)
            {
                if (reqQueue == null){
                    reqQueue = Volley.newRequestQueue(mContext);
                }
            }
        }
        return reqQueue;
    }


    public ImageLoader getImageLoader(){
        getRequestQueue();
        //濡傛灉imageLoader涓虹┖鍒欏垱寤哄畠锛岀浜屼釜鍙傛暟浠ｈ〃澶勭悊鍥惧儚缂撳瓨鐨勭被
        if(imageLoader==null){
            //LruCache
            //imageLoader=new ImageLoader(reqQueue, new LruBitmapCache());
            //LruCache  DiskLruCache
            imageLoader=new ImageLoader(reqQueue, new ImageCacheUtil(mContext));
        }
        return imageLoader;
    }


    /**
     * 灏哛equest瀵硅薄娣诲姞杩汻equestQueue锛岀敱浜嶳equest鏈�*StringRequest,JsonObjectResquest...
     * 绛夊绉嶇被鍨嬶紝鎵�浠ラ渶瑕佺敤鍒�*娉涘瀷銆傚悓鏃跺彲灏�*tag浣滀负鍙�夊弬鏁颁互渚挎爣绀哄嚭姣忎竴涓笉鍚岃姹�
     */

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // 濡傛灉tag涓虹┖鐨勮瘽锛屽氨鏄敤榛樿TAG
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    // 閫氳繃鍚凴equest瀵硅薄鐨凾ag灞炴�у彇娑堣姹�
    public void cancelPendingRequests(Object tag) {
        if (reqQueue != null) {
            reqQueue.cancelAll(tag);
        }
    }
}