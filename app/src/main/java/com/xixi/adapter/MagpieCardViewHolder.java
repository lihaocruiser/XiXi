package com.xixi.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;
import com.xixi.widget.CircleImageView;

/**
 * Created on 2015-7-23.
 */
public class MagpieCardViewHolder extends RecyclerView.ViewHolder {

    ImageDownloader imageDownloader;

    public CardView cardView;
    public CircleImageView imHeader;
    public TextView tvNickname;
    public TextView tvTitle;

    public MagpieCardViewHolder(CardView v, ImageDownloader imageDownloader) {
        super(v);
        this.cardView = v;
        imHeader = (CircleImageView) v.findViewById(R.id.im_header);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        this.imageDownloader = imageDownloader;
    }

    public void setData(MagpieBean bean) {
        imHeader.setTag(bean.getUserHeaderUrl());
        tvNickname.setText(bean.getUserName());
        tvTitle.setText(bean.getTitle());

        // set header pic
        String headUrl = bean.getUserHeaderUrl();
        imageDownloader.setBitmap(headUrl, imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);
    }

}
