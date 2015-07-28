package com.xixi.util.Image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.xixi.net.base.BitmapReceiver;
import com.xixi.net.image.ImageDownloadTask;
import com.xixi.util.file.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created on 7/19/15.
 * manage async loading and caching of images
 */
public class ImageDownloader {

    private Set<String> taskSet = new HashSet<>();
    private Queue<String> imageQueue = new ArrayDeque<>();
    private Map<String, Bitmap> imageMap = new HashMap<>();
    private HashSet<ImageView> imSet = new HashSet<>();

    public void setBitmap(String picUrl, ImageView imageView, ImageView.ScaleType scaleType, BitmapUtil.Size size) {
        if (picUrl == null) {
            imageView.setImageBitmap(null);
            return;
        }

        if (!imSet.contains(imageView)) {
            imSet.add(imageView);
        }
        imageView.setTag(picUrl);

        if (containsBitmap(picUrl)) {
            imageView.setImageBitmap(getBitmap(picUrl));
        } else {
            imageView.setImageBitmap(null);
            int viewWidth = imageView.getLayoutParams().width;
            int viewHeight = imageView.getLayoutParams().height;
            fetchImage(picUrl, viewWidth, viewHeight, scaleType, size);
        }
    }

    // 判断图片是否在内存
    private boolean containsBitmap(String url) {
        return imageMap.containsKey(url);
    }

    // 从内存获取图片
    private Bitmap getBitmap(String url) {
        return imageMap.get(url);
    }

    // 如果文件存在则getFile()从文件获取，否则getOnline()从网络获取
    private void fetchImage(String url, int viewWidth, int viewHeight, ImageView.ScaleType scaleType, BitmapUtil.Size size) {
        String circlePath = FileUtil.getImageFolder();
        String imagePath = circlePath + File.separator + FileUtil.getFileName(url);
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            getFile(url, imagePath, viewWidth, viewHeight, scaleType, size);
        } else {
            getOnline(url, viewWidth, viewHeight, scaleType, size);
        }
    }



    // 发起AsyncTask从文件获取图片
    private void getFile(String url, String imagePath, int viewWidth, int viewHeight, ImageView.ScaleType scaleType, BitmapUtil.Size size) {
        new ReadFileTask(url, imagePath, viewWidth, viewHeight, scaleType, size).execute();
    }

    private class ReadFileTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private String imagePath;
        private int viewWidth;
        private int viewHeight;
        private ImageView.ScaleType scaleType;
        private BitmapUtil.Size size;

        public ReadFileTask(String url, String imagePath, int viewWidth, int viewHeight, ImageView.ScaleType scaleType, BitmapUtil.Size size) {
            this.url = url;
            this.imagePath = imagePath;
            this.viewWidth = viewWidth;
            this.viewHeight = viewHeight;
            this.scaleType = scaleType;
            this.size = size;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            return BitmapUtil.decodeFileScaled(imagePath, viewWidth, viewHeight, scaleType, size);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            updateCache(url, result);
            updateImageView(url, result, size);
        }
    }


    // 发起AsyncHttpRequest从网络获取图片
    private void getOnline(String url, final int viewWidth, final int viewHeight, final ImageView.ScaleType scaleType, final BitmapUtil.Size size)  {

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
                new WriteFileTask(url, bitmap).execute();
                Bitmap bitmapCompress = BitmapUtil.resize(bitmap, viewWidth, viewHeight, scaleType, size);
                updateCache(url, bitmapCompress);
                updateImageView(url, bitmapCompress, size);
                taskSet.remove(url);
            }
        }).execute();
    }

    private class WriteFileTask extends AsyncTask<Void, Void, Void> {

        private String url;
        private Bitmap bitmap;

        public WriteFileTask(String url, Bitmap bitmap) {
            this.url = url;
            this.bitmap = bitmap;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String imagePath = FileUtil.getImageFolder() + File.separator + FileUtil.getFileName(url);
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
            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.getMessage(), e);
            } catch (IOException e) {
                Log.e("IOException", e.getMessage(), e);
            }
            return null;
        }
    }


    // 获取到图片后更新内存
    private void updateCache(String url, Bitmap bitmap) {
        imageQueue.offer(url);
        imageMap.put(url, bitmap);
        // 如果图片数量超过阈值且最早加载的图片不在屏幕中，则清除最早的一张
        if (imageQueue.size() < 50) {
            return;
        }
        String earliest = imageQueue.peek();
        for (ImageView imageView : imSet) {
            if (url.equals(imageView.getTag())) {
                return;
            }
        }
        imageQueue.poll();
        imageMap.remove(earliest);
    }

    // 获取到图片后更新UI
    private void updateImageView(String url, Bitmap bitmap, BitmapUtil.Size size) {
        for (ImageView imageView : imSet) {
            if (url.equals(imageView.getTag())) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

}
