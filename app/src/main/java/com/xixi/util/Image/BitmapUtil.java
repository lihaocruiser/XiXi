package com.xixi.util.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;

import com.xixi.util.WindowUtil;

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
     * resize a bitmap
     * @param bitmap the Bitmap to be resized
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     */
    public static Bitmap resize(Bitmap bitmap, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {
        float scale;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int inSampleSize = getInSampleSize(width, height, viewWidth, viewHeight, scaleType);
        scale = 1f / inSampleSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }



    /**
     * 限制了图片的最大尺寸，宽不超过屏幕的1/2，高不超过屏幕的1/3
     * @param outWidth width of the original bitmap
     * @param outHeight height of the original bitmap
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     * @return inSampleSize
     */
    private static int getInSampleSize(int outWidth, int outHeight, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {

        if (viewWidth <= 0 || viewWidth > WindowUtil.getWindowWidth()) {
            viewWidth = WindowUtil.getWindowWidth() / 2;
        }

        if (viewHeight <= 0 || viewHeight > WindowUtil.getWindowHeight()) {
            viewHeight = WindowUtil.getWindowHeight() / 3;
        }

        int inSampleX = outWidth / viewWidth;
        int inSampleY = outHeight / viewHeight;

        int inSampleSize;

        // 使用max则压缩程度大，使用min则压缩程度小
        switch (scaleType) {
            case CENTER_CROP:
            case CENTER:
            case FIT_XY:
            case MATRIX:
                inSampleSize = Math.min(inSampleX, inSampleY);
                break;

            case CENTER_INSIDE:
            case FIT_START:
            case FIT_END:
            case FIT_CENTER:
                inSampleSize = Math.max(inSampleX, inSampleY);
                break;

            default:
                inSampleSize = Math.max(inSampleX, inSampleY);
        }
        // inSampleSize在使用时会被变为2的幂次，所以这里乘以2
        return inSampleSize * 2;
    }

}
