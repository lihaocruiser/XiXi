package com.xixi.net.image;

import android.graphics.Bitmap;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;
import com.xixi.util.Image.BitmapUtil;

/**
 * Created  on 2015-7-16.
 */
public class ImageUploadJSONTask extends JSONTask {

    // 图片最大长宽，单位pix
    private static final int maxImageSize = 1000;

    public ImageUploadJSONTask(String localUrl, JSONReceiver receiver) {

        String url = RequestUrl.UPLOAD_IMAGE;

        RequestParams params = new RequestParams();
        Bitmap bitmap = BitmapUtil.decodeFileWithMaxPix(localUrl, maxImageSize);
        params.put("bitmap", bitmap);

        init(url, params, receiver);
    }

}
