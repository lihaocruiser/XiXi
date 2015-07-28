package com.xixi.adapter.listview;

import android.view.View;

import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-24.
 */
public abstract class BaseListViewHolder<B> implements View.OnClickListener {

    View rootView;
    ImageDownloader imageDownloader;

    public BaseListViewHolder(View rootView, ImageDownloader imageDownloader) {
        this.rootView = rootView;
        this.imageDownloader = imageDownloader;
    }

    public abstract void setValue(B bean);

}
