package com.xixi.util.Image;

import com.xixi.net.base.JSONReceiver;
import com.xixi.net.image.ImageUploadJSONTask;
import com.xixi.util.SafeJSON;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * upload multiple images in one time sequentially
 */
public class ImageUploader {

    List<String> localUrls;
    List<String> receivedUrls;

    int imageCount;

    OnUploadFinishListener listener;

    private static ImageUploader imageUploader = new ImageUploader();

    private ImageUploader() {}

    public static ImageUploader getInstance() {
        return imageUploader;
    }

    public interface OnUploadFinishListener {
        public void onFailure();
        public void onSuccess(List<String> receivedUrls);
    }

    JSONReceiver receiver = new JSONReceiver() {
        @Override
        public void onFailure(JSONObject obj) {
            listener.onFailure();
        }
        @Override
        public void onSuccess(JSONObject obj) {
            int checked = SafeJSON.getInt(obj, "checked", 1);
            if (checked == 1) {
                receivedUrls = null;
                return;
            }
            String url = obj.optString("headPic");
            receivedUrls.add(url);
            imageCount++;
            if (imageCount < localUrls.size()) {
                uploadImage(imageCount);
            } else {
                listener.onSuccess(receivedUrls);
            }
        }
    };

    private void uploadImage(int imageCount) {
        new ImageUploadJSONTask(localUrls.get(imageCount), receiver).execute();
    }


    public void setOnUploadFinishListener(OnUploadFinishListener listener) {
        this.listener = listener;
    }


    /**
     * upload one picture
     */
    public void execute(String localUrl) {
        ArrayList<String> urls = new ArrayList<>();
        urls.add(localUrl);
        this.execute(urls);
    }


    /**
     * upload a bunch of picture
     */
    public void execute(List<String> localUrls) {
        imageCount = 0;
        this.localUrls = localUrls;
        receivedUrls = new ArrayList<>();
        uploadImage(imageCount);
    }

}
