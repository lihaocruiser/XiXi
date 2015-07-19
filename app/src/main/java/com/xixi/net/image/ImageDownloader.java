package com.xixi.net.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.xixi.net.BitmapReceiver;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by LiHao on 7/19/15.
 * manage async loading of images in RecyclerView
 */
public class ImageDownloader {

    Set<String> taskSet;
    HashMap<String, Bitmap> imageMap;
    RecyclerView recyclerView;

    public ImageDownloader(Context context, Set<String> taskSet, HashMap<String, Bitmap> imageMap,
                           RecyclerView recyclerView) {
        this.taskSet = taskSet;
        this.imageMap = imageMap;
        this.recyclerView = recyclerView;
        FileUtil.init(context);
    }

    public void getImage(String url, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {
        Bitmap bitmap = getFile(url, viewWidth ,viewHeight, scaleType);
        if (bitmap != null) {
            imageMap.put(url, bitmap);
            updateImageView(url, bitmap);
        } else {
            getOnline(url, viewWidth, viewHeight, scaleType);
        }
    }

    public Bitmap getFile(String url, int viewWidth, int viewHeight, ImageView.ScaleType scaleType) {
        Bitmap bitmap = null;
        String circlePath = FileUtil.getCirclePath();
        String imagePath = circlePath + File.separator + FileUtil.getFileName(url);
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            bitmap = BitmapUtil.decodeFileScaled(imagePath, viewWidth, viewHeight, scaleType);
        }
        return bitmap;
    }

    public void getOnline(String url, final int viewWidth, final int viewHeight, final ImageView.ScaleType scaleType)  {

        if (taskSet.contains(url)) {
            return;
        }
        new ImageDownloadTask(url, new BitmapReceiver() {
            @Override
            public void onFailure(String url) {
                taskSet.remove(url);
            }

            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                // 先保存原图到SD卡，然后压缩显示到屏幕
                saveImage(url, bitmap);
                bitmap = BitmapUtil.resize(bitmap, viewWidth, viewHeight, scaleType);
                taskSet.remove(url);
                imageMap.put(url, bitmap);
                updateImageView(url, bitmap);
            }
        }).execute();
    }

    private void updateImageView(String url, Bitmap bitmap) {
        ImageView imageView = (ImageView) recyclerView.findViewWithTag(url);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private void saveImage(String url, Bitmap bitmap) {
        String circlePath = FileUtil.getCirclePath();
        String imagePath = circlePath + File.separator + FileUtil.getFileName(url);
        File imageFile = new File(imagePath);
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(imageFile);
            if (url.contains(".png") || url.contains(".PNG")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, outputStream);
            }
            else{
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            }
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
