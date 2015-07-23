package com.xixi.util.Image;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xixi.net.BitmapReceiver;
import com.xixi.net.image.ImageDownloadTask;
import com.xixi.util.WindowUtil;
import com.xixi.util.file.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Created by LiHao on 7/19/15.
 * manage async loading and caching of images in a ViewGroup
 */
public class ImageDownloader {

    private Set<String> taskSet = new HashSet<>();
    private Queue<String> imageQueue = new ArrayDeque<>();
    private Map<String, Bitmap> imageMap = new HashMap<>();

    private ViewGroup viewGroup;

    public ImageDownloader(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    public void setBitmap(String picUrl, ImageView imageView, ImageView.ScaleType scaleType, BitmapUtil.Size size) {
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
        String circlePath = FileUtil.getCirclePath();
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
            return null;
        }
    }


    // 获取到图片后更新内存
    private void updateCache(String url, Bitmap bitmap) {
        // 如果图片数量超过阈值且最早加载的图片不在屏幕中，则清除最早的一张
        if (imageQueue.size() > 50) {
            String earliest = imageQueue.peek();
            if (viewGroup.findViewWithTag(earliest) == null) {
                imageQueue.poll();
                imageMap.remove(url);
            }
        }
        imageQueue.offer(url);
        imageMap.put(url, bitmap);
    }

    // 获取到图片后更新UI
    private void updateImageView(String url, Bitmap bitmap, BitmapUtil.Size size) {
        ImageView imageView = (ImageView) viewGroup.findViewWithTag(url);
        if (imageView != null) {
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();
        }
    }

    /**
     * 头大，这函数没用过
     */
    private Bitmap resizeIfNeeded(Bitmap bitmap, ImageView imageView) {

        boolean xExceeds = (bitmap.getWidth() > imageView.getMaxWidth());
        boolean yExceeds = (bitmap.getHeight() > imageView.getMaxHeight());

        if (xExceeds || yExceeds) {
            int bitmapWidth = bitmap.getWidth();
            int viewWidth = Math.min(imageView.getMaxWidth(), WindowUtil.getWindowWidth());
            float scaleX = (float) viewWidth / (float) bitmapWidth;

            int bitmapHeight = bitmap.getHeight();
            int viewHeight = Math.min(imageView.getMaxHeight(), WindowUtil.getWindowHeight());
            float scaleY = (float) viewHeight / (float) bitmapHeight;

            float scale = Math.min(scaleX, scaleY);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            imageView.measure(bitmap.getWidth(), bitmap.getHeight());
        }
        return bitmap;
    }

}
