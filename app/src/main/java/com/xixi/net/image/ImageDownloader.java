package com.xixi.net.image;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xixi.net.BitmapReceiver;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by LiHao on 7/19/15.
 */
public class ImageDownloader {

    public static void fetchBitmap(String url, int viewWidth, int viewHeight,
                                   ImageView.ScaleType scaleType,
                                   final Set<String> taskSet,
                                   final HashMap<String, Bitmap> imageMap,
                                   final RecyclerView recyclerView)  {

        if (taskSet.contains(url)) {
            return;
        }
        new ImageDownloadTask(url, viewWidth, viewHeight, scaleType, new BitmapReceiver() {
            @Override
            public void onFailure(String url) {
                taskSet.remove(url);
            }

            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                taskSet.remove(url);
                imageMap.put(url, bitmap);
                ImageView imageView = (ImageView) recyclerView.findViewWithTag(url);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }).execute();
    }

}
