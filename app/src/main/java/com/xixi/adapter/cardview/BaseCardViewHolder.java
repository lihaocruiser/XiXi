package com.xixi.adapter.cardview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-27.
 */
public abstract class BaseCardViewHolder<B> extends RecyclerView.ViewHolder implements View.OnClickListener {

    public CardView cardView;
    protected ImageDownloader imageDownloader;

    public BaseCardViewHolder(CardView cardView, ImageDownloader imageDownloader) {
        super(cardView);
        this.cardView = cardView;
        this.imageDownloader = imageDownloader;
        cardView.setOnClickListener(this);
    }

    public abstract void setValue(B bean);

}
