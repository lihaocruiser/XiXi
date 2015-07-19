package com.xixi.net.image;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created  on 2015-7-16.
 */
public class ImageUploadJSONTask extends JSONTask {

    public ImageUploadJSONTask(String localUrl, JSONReceiver receiver) {

        String url = RequestUrl.UPLOAD_IMAGE;

        RequestParams params = new RequestParams();
        File file = new File(localUrl);
        try {
            params.put("file", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        init(url, params, receiver);
    }

}
