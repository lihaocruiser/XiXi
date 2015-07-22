package com.xixi.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;

/**
 * Created by LiHao on 2015-7-22.
 */
public class CircleCardViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public ImageView imHeader;
    public ImageView imPic;
    public TextView tvNickname;
    public TextView tvContent;
    public ImageView imLike;
    public ImageView imComment;
    public TextView tvLikeCount;
    public TextView tvCommentCount;

    public CircleCardViewHolder(CardView v) {
        super(v);
        this.cardView = v;
        imHeader = (ImageView) v.findViewById(R.id.im_header);
        imPic = (ImageView) v.findViewById(R.id.im_pic);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
        imLike = (ImageView) v.findViewById(R.id.im_like);
        imComment = (ImageView) v.findViewById(R.id.im_comment);
        tvLikeCount = (TextView) v.findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) v.findViewById(R.id.tv_comment_count);
    }

    public void bindBean(CircleBean bean, int userID) {
        imHeader.setTag(bean.getPublisherHeadPic());
        imPic.setTag(bean.getPic());
        tvNickname.setText(bean.getPublisherNickname());
        tvContent.setText(bean.getContent());
        tvLikeCount.setText(bean.getLikeCount() + "");
        tvCommentCount.setText(bean.getCommentCount() + "");
        if (bean.getLikeIds().contains(userID)) {
            imLike.setImageResource(R.drawable.ic_circle_like);
        } else {
            imLike.setImageResource(R.drawable.ic_circle_unlike);
        }
    }

}
