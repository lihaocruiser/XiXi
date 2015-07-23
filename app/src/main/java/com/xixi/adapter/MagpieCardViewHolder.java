package com.xixi.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.MagpieBean;
import com.xixi.bean.circle.CircleBean;
import com.xixi.widget.CircleImageView;

/**
 * Created by LiHao on 2015-7-23.
 */
public class MagpieCardViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public CircleImageView imHeader;
    public TextView tvNickname;
    public TextView tvTitle;

    public MagpieCardViewHolder(CardView v) {
        super(v);
        this.cardView = v;
        imHeader = (CircleImageView) v.findViewById(R.id.im_header);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
    }

    public void bindBean(MagpieBean bean) {
        imHeader.setTag(bean.getUserHeaderUrl());
        tvNickname.setText(bean.getUserName());
        tvTitle.setText(bean.getTitle());
    }

}
