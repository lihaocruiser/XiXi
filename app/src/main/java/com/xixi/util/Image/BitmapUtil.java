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

    public enum Size {
        SMALL,      //用于头像
        MIDDLE,     //用于列表中的图片
        LARGE,      //用于单张图片的页面
        FULL_SCREEN //用于全屏浏览
    }

    /**
     * decode bitmap from File
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     */
    public static Bitmap decodeFileScaled(String pathName, int viewWidth, int viewHeight,
                                          ImageView.ScaleType scaleType, Size size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = getInSampleSize(options.outWidth, options.outHeight, viewWidth, viewHeight, scaleType, size);
        Log.i("inSampleSize", options.inSampleSize + "");
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);
    }



    /**
     * decode bitmap from ByteArray
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     */
    public static Bitmap decodeByteArrayScaled(byte[] data, int offset, int length, int viewWidth, int viewHeight,
                                               ImageView.ScaleType scaleType, Size size) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options.inSampleSize = getInSampleSize(options.outWidth, options.outHeight, viewWidth, viewHeight, scaleType, size);
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
    public static Bitmap resize(Bitmap bitmap, int viewWidth, int viewHeight, ImageView.ScaleType scaleType, Size size) {
        float scale;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int inSampleSize = getInSampleSize(width, height, viewWidth, viewHeight, scaleType, size);
        scale = 1f / inSampleSize;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }



    /**
     *
     * @param outWidth width of the original bitmap
     * @param outHeight height of the original bitmap
     * @param viewWidth width of the view holding the bitmap
     * @param viewHeight height of the view holding the bitmap
     * @param size used to limit the max size of the out Bitmap, taking effect only when viewWidth == 0 or viewHeight == 0
     * @return inSampleSize
     */
    private static int getInSampleSize(int outWidth, int outHeight, int viewWidth, int viewHeight,
                                       ImageView.ScaleType scaleType, Size size) {

        if (viewWidth <= 0) {
            viewWidth = WindowUtil.getWindowWidth();
            switch (size) {
                case SMALL:
                    viewWidth /= 8;
                    break;
                case MIDDLE:
                    viewWidth = viewWidth * 2 / 5;
                    break;
                case LARGE:
                    viewWidth /= 2;
                    break;
                case FULL_SCREEN:
                    break;
            }
        } else if (viewWidth > WindowUtil.getWindowWidth()) {
            viewWidth = WindowUtil.getWindowWidth();
        }

        if (viewHeight <= 0) {
            viewHeight = WindowUtil.getWindowHeight();
            switch (size) {
                case SMALL:
                    viewWidth /= 8;
                    break;
                case MIDDLE:
                    viewHeight /= 4;
                    break;
                case LARGE:
                    viewHeight /= 2;
                    break;
                case FULL_SCREEN:
                    break;
            }
        } else if (viewHeight > WindowUtil.getWindowHeight()) {
            viewHeight = WindowUtil.getWindowHeight();
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
        return inSampleSize;
    }

    /**
     * 作用是使图片大小整齐
     */
    private Bitmap fitView(Bitmap bitmap, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {
        if (viewWidth > 0 && viewHeight > 0) {
            int outWidth = bitmap.getWidth();
            int outHeight = bitmap.getHeight();
            float scaleX = (float) viewWidth / (float) outWidth;
            float scaleY = (float) viewHeight / (float) outHeight;
            float scale = Math.min(scaleX, scaleY);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, outWidth, outHeight, matrix, true);
        }
        return bitmap;
    }

}
