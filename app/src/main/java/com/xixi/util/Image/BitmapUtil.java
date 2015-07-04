package com.xixi.util.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by LiHao on 2015/7/4.
 * Decode Bitmap with proper size, aims at avoid OOM
 */
public class BitmapUtil {

    /**
     * default ScaleType is CENTER_CROP
     * @param viewWidth width of the view where you want to show the bitmap
     * @param viewHeight height of the view where you want to show the bitmap
     */
    public static Bitmap decodeScaledBitmap(String pathName, int viewWidth, int viewHeight) {
        return decodeScaledBitmap(pathName, viewWidth, viewHeight, ImageView.ScaleType.CENTER_INSIDE);
    }

    public static Bitmap decodeScaledBitmap(String pathName, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);

        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;

        int inSampleX = bitmapWidth / viewWidth;
        int inSampleY = bitmapHeight / viewHeight;

        switch (scaleType) {
            case CENTER_CROP:
                options.inSampleSize = Math.min(inSampleX, inSampleY);
                break;

            case CENTER:
            case CENTER_INSIDE:
            case FIT_CENTER:
            case FIT_END:
            case FIT_START:
            case FIT_XY:
            case MATRIX:
            default:
                options.inSampleSize = Math.max(inSampleX, inSampleY);
        }

        Log.i("inSampleSize", options.inSampleSize + "");
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }

}
