package com.xixi.util.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by LiHao on 2015/7/4.
 * Decode Bitmap with proper size, aims at avoiding OOM
 */
public class BitmapUtil {


    /**
     * decode bitmap from File
     * default ScaleType is CENTER_CROP
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     */
    public static Bitmap decodeFileScaled(String pathName, int viewWidth, int viewHeight) {
        return decodeFileScaled(pathName, viewWidth, viewHeight, ImageView.ScaleType.CENTER_CROP);
    }

    public static Bitmap decodeFileScaled(String pathName, int viewWidth, int viewHeight,
                                          ImageView.ScaleType scaleType) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = getInSampleSize(options.outWidth, options.outHeight, viewWidth, viewHeight, scaleType);
        Log.i("inSampleSize", options.inSampleSize + "");
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }


    /**
     * decode bitmap from ByteArray
     * default ScaleType is CENTER_CROP
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     */
    public static Bitmap decodeByteArrayScaled(byte[] data, int offset, int length, int viewWidth, int viewHeight) {
        return decodeByteArrayScaled(data, offset, length, viewWidth, viewHeight, ImageView.ScaleType.CENTER_CROP);
    }

    public static Bitmap decodeByteArrayScaled(byte[] data, int offset, int length, int viewWidth, int viewHeight,
                                               ImageView.ScaleType scaleType) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = getInSampleSize(options.outWidth, options.outHeight, viewWidth, viewHeight, scaleType);
        Log.i("inSampleSize", options.inSampleSize + "");
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }


    /**
     *
     * @param outWidth width of the original bitmap
     * @param outHeight height of the original bitmap
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     * @param scaleType scaleType
     * @return inSampleSize
     */
    private static int getInSampleSize(int outWidth, int outHeight, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {

        int inSampleX = outWidth / viewWidth;
        int inSampleY = outHeight / viewHeight;

        switch (scaleType) {
            case CENTER_CROP:
                return Math.min(inSampleX, inSampleY);

            case CENTER:
            case CENTER_INSIDE:
            case FIT_CENTER:
            case FIT_END:
            case FIT_START:
            case FIT_XY:
            case MATRIX:
            default:
                return Math.max(inSampleX, inSampleY);
        }

    }

}
