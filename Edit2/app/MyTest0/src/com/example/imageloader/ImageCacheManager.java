package com.example.imageloader;

/**
 * Created by lt413 on 2015/12/27.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;

/**
 * 鍥剧墖缂撳瓨绠＄悊绫� 鑾峰彇ImageLoader瀵硅薄
 * @author Javen
 *
 */
public class ImageCacheManager {
    private static String TAG = ImageCacheManager.class.getSimpleName();

    /**
     * 鑾峰彇ImageListener
     *
     * @param view
     * @param defaultImage
     * @param errorImage
     * @return
     */
    public static ImageListener getImageListener(final ImageView view, final Bitmap defaultImage, final Bitmap errorImage) {

        return new ImageListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // 鍥炶皟澶辫触
                if (errorImage != null) {
                    view.setImageBitmap(errorImage);
                }
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
                // 鍥炶皟鎴愬姛
                if (response.getBitmap() != null) {
                    view.setImageBitmap(response.getBitmap());
                } else if (defaultImage != null) {
                    view.setImageBitmap(defaultImage);
                }
            }
        };

    }

    /**
     * 鎻愪緵缁欏閮ㄨ皟鐢ㄦ柟娉�
     *
     * @param url
     * @param view
     * @param defaultImage
     * @param errorImage
     */
    public static void loadImage(Context context,String url, ImageView view, Bitmap defaultImage, Bitmap errorImage) {
        VolleyController.getInstance(context).getImageLoader().get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), 0, 0);
    }

    /**
     * 鎻愪緵缁欏閮ㄨ皟鐢ㄦ柟娉�
     *
     * @param url
     * @param view
     * @param defaultImage
     * @param errorImage
     */
    public static void loadImage(Context context,String url, ImageView view, Bitmap defaultImage, Bitmap errorImage, int maxWidth, int maxHeight) {
        VolleyController.getInstance(context).getImageLoader().get(url, ImageCacheManager.getImageListener(view, defaultImage, errorImage), maxWidth, maxHeight);
    }
}
