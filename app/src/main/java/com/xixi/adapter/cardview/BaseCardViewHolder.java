package com.xixi.adapter.cardview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-27.
 */
public abstract class BaseCardViewHolder<B> extends RecyclerView.ViewHolder implements View.OnClickListener {

    private CardView cardView;
    private ImageDownloader imageDownloader;

    public BaseCardViewHolder(CardView cardView, ImageDownloader imageDownloader) {
        super(cardView);
        this.cardView = cardView;
        this.imageDownloader = imageDownloader;
        cardView.setOnClickListener(this);
    }

    public CardView getRootView() {
        return cardView;
    }

    protected ImageDownloader getImageDownloader() {
        return imageDownloader;
    }

    public abstract void setValue(B bean);

    public abstract void recycle();

}
