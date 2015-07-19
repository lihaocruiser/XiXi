package com.xixi.net.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.xixi.net.BitmapReceiver;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.xixi.util.Image.BitmapUtil;

import org.apache.http.Header;

/**
 * download image online, return a Bitmap
 */
public class ImageDownloadTask {

    public int count = 0;

    private int viewWidth;
    private int viewHeight;
    private ImageView.ScaleType scaleType;

    private String url;
    private BitmapReceiver bitmapReceiver;

    private AsyncHttpResponseHandler asyncHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            Log.i(getClass().toString(), "onFailure");
            bitmapReceiver.onFailure(url);
            count--;
        }

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            Log.i(getClass().toString(), "onSuccess");
            if (arg2 == null) {
                bitmapReceiver.onFailure(url);
                return;
            }
            Bitmap bitmap;
            if (viewWidth == 0) {
                bitmap = BitmapFactory.decodeByteArray(arg2, 0, arg2.length);
            } else {
                bitmap = BitmapUtil.decodeByteArrayScaled(arg2, 0, arg2.length, viewWidth, viewHeight, scaleType);
            }
            bitmapReceiver.onSuccess(url, bitmap);
            count--;
        }

    };

    /**
     * return a Bitmap with its original size by default
     */
    public ImageDownloadTask(String url, BitmapReceiver bitmapReceiver) {
        this.url = url;
        this.bitmapReceiver = bitmapReceiver;
        count++;
    }

    /**
     * return a resized Bitmap that fits an ImageView with the given viewWidth, viewHeight and scaleType
     * aims at saving memory
     */
    public ImageDownloadTask(String url, int viewWidth, int viewHeight,
                             ImageView.ScaleType scaleType, BitmapReceiver bitmapReceiver) {
        this(url, bitmapReceiver);
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
        this.scaleType = scaleType;
    }

    public void execute() {
        new AsyncHttpClient().get(url, asyncHandler );
    }

}