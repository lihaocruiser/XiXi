package com.xixi.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-22.
 */
public class CircleHeaderCardViewHolder extends RecyclerView.ViewHolder {

    private ImageDownloader imageDownloader;

    public CardView cardView;
    public ImageView imHeader;
    public ImageView imPic;
    public TextView tvNickname;
    public TextView tvContent;
    public ImageView imLike;
    public ImageView imComment;
    public TextView tvLikeCount;
    public TextView tvCommentCount;

    public CircleHeaderCardViewHolder(CardView v, ImageDownloader imageDownloader) {
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
        this.imageDownloader = imageDownloader;
    }

    public void setData(CircleBean bean, int userID) {
        tvNickname.setText(bean.getPublisherNickname());
        tvContent.setText(bean.getContent());
        tvLikeCount.setText(bean.getLikeCount() + "");
        tvCommentCount.setText(bean.getCommentCount() + "");
        if (bean.getLikeIds().contains(userID)) {
            imLike.setImageResource(R.drawable.ic_circle_like);
        } else {
            imLike.setImageResource(R.drawable.ic_circle_unlike);
        }
        // set header pic
        String headUrl = bean.getPublisherHeadPic();
        imageDownloader.setBitmap(headUrl, imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

        // set moment pic
        String picUrl = bean.getPic();
        if (picUrl == null) {
            imPic.setVisibility(View.GONE);
        } else {
            imPic.setVisibility(View.VISIBLE);
            imageDownloader.setBitmap(picUrl, imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);
        }
    }

}
