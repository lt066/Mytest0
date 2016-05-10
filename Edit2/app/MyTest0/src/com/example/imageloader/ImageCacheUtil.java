package com.example.imageloader;

/**
 * Created by lt413 on 2015/12/27.
 */

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;


import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 鍥剧墖缂撳瓨甯姪绫�
 * 鍖呭惈鍐呭瓨缂撳瓨LruCache鍜岀鐩樼紦瀛楧iskLruCache
 * @author Javen
 */
public class ImageCacheUtil implements ImageLoader.ImageCache {

    private String TAG=ImageCacheUtil.this.getClass().getSimpleName();

    //缂撳瓨绫�
    private static LruCache<String, Bitmap> mLruCache;

    private static DiskLruCache mDiskLruCache;
    //纾佺洏缂撳瓨澶у皬
    private static final int DISKMAXSIZE = 10 * 1024 * 1024;

    public ImageCacheUtil(Context context) {
        // 鑾峰彇搴旂敤鍙崰鍐呭瓨鐨�1/8浣滀负缂撳瓨
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        // 瀹炰緥鍖朙ruCaceh瀵硅薄
        mLruCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        try {
            // 鑾峰彇DiskLruCahce瀵硅薄

//            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(MyApplication.newInstance(), "xxxxx"), getAppVersion(MyApplication.newInstance()), 1, DISKMAXSIZE);
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context.getApplicationContext()), getAppVersion(context), 1, DISKMAXSIZE);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 浠庣紦瀛橈紙鍐呭瓨缂撳瓨锛岀鐩樼紦瀛橈級涓幏鍙朆itmap
     */
    @Override
    public Bitmap getBitmap(String url) {
        if (mLruCache.get(url) != null) {
            // 浠嶭ruCache缂撳瓨涓彇
            Log.i(TAG, "浠嶭ruCahce鑾峰彇");

            return mLruCache.get(url);
        } else {
            String key = MD5Utils.md5(url);
            try {
                if (mDiskLruCache.get(key) != null) {
                    // 浠嶥iskLruCahce鍙�
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    Bitmap bitmap = null;
                    if (snapshot != null) {
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        // 瀛樺叆LruCache缂撳瓨
                        mLruCache.put(url, bitmap);
                        Log.i(TAG,"浠嶥iskLruCahce鑾峰彇");
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 瀛樺叆缂撳瓨锛堝唴瀛樼紦瀛橈紝纾佺洏缂撳瓨锛�
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        // 瀛樺叆LruCache缂撳瓨
        mLruCache.put(url, bitmap);
        // 鍒ゆ柇鏄惁瀛樺湪DiskLruCache缂撳瓨锛岃嫢娌℃湁瀛樺叆
        String key = MD5Utils.md5(url);
        try {
            if (mDiskLruCache.get(key) == null) {
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if (editor != null) {
                    OutputStream outputStream = editor.newOutputStream(0);
                    if (bitmap.compress(CompressFormat.JPEG, 100, outputStream)) {
                        editor.commit();
                    } else {
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 璇ユ柟娉曚細鍒ゆ柇褰撳墠sd鍗℃槸鍚﹀瓨鍦紝鐒跺悗閫夋嫨缂撳瓨鍦板潃
     *
     * @param context
     * @return
     */
    public static File getDiskCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath);
    }

    /**
     * 鑾峰彇搴旂敤鐗堟湰鍙�
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}