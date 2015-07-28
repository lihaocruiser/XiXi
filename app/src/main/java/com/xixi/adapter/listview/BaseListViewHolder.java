package com.xixi.adapter.listview;

import android.view.View;

import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-24.
 */
public abstract class BaseListViewHolder<B> implements View.OnClickListener {

    View view;
    ImageDownloader imageDownloader;

    public BaseListViewHolder(View view, ImageDownloader imageDownloader) {
        this.view = view;
        this.imageDownloader = imageDownloader;
    }

    public abstract void setValue(B bean);

}
