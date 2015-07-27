package com.xixi.adapter.cardview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-27.
 */
public abstract class CardViewHolder<B> extends RecyclerView.ViewHolder {

    public CardView cardView;
    protected ImageDownloader imageDownloader;

    public CardViewHolder(CardView cardView, ImageDownloader imageDownloader) {
        super(cardView);
        this.cardView = cardView;
        this.imageDownloader = imageDownloader;
    }

    abstract public void setValue(B bean);

}
