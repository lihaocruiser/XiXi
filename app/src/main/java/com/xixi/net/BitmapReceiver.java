package com.xixi.net;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface BitmapReceiver {

    public void onFailure(String url);

    public void onSuccess(String url, Bitmap bitmap);

}
