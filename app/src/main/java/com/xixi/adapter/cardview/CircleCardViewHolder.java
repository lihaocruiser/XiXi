package com.xixi.adapter.cardview;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.ui.circle.CircleActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-27.
 */
public class CircleCardViewHolder extends BaseCardViewHolder<CircleBean> {

    private int userId;

    public ImageView imHeader;
    public ImageView imPic;
    public TextView tvNickname;
    public TextView tvContent;
    public ImageView imLike;
    public ImageView imComment;
    public TextView tvLikeCount;
    public TextView tvCommentCount;

    public CircleCardViewHolder(CardView cardView, ImageDownloader imageDownloader) {
        super(cardView, imageDownloader);
        imHeader = (ImageView) cardView.findViewById(R.id.im_header);
        imPic = (ImageView) cardView.findViewById(R.id.im_pic);
        tvNickname = (TextView) cardView.findViewById(R.id.tv_nickname);
        tvContent = (TextView) cardView.findViewById(R.id.tv_content);
        imLike = (ImageView) cardView.findViewById(R.id.im_like);
        imComment = (ImageView) cardView.findViewById(R.id.im_comment);
        tvLikeCount = (TextView) cardView.findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) cardView.findViewById(R.id.tv_comment_count);
    }

    @Override
    public void setValue(CircleBean bean) {
        tvNickname.setText(bean.getPublisherNickname());
        tvContent.setText(bean.getContent());
        tvLikeCount.setText(bean.getLikeCount() + "");
        tvCommentCount.setText(bean.getCommentCount() + "");

        if (bean.getLikeIds().contains(userId)) {
            imLike.setImageResource(R.drawable.ic_circle_like);
        } else {
            imLike.setImageResource(R.drawable.ic_circle_unlike);
        }

        String headUrl = bean.getPublisherHeadPic();
        imageDownloader.setBitmap(headUrl, imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

        String picUrl = bean.getPic();
        if (picUrl == null) {
            imPic.setVisibility(View.GONE);
        } else {
            imPic.setVisibility(View.VISIBLE);
            imageDownloader.setBitmap(picUrl, imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);
        }
    }

    @Override
    public void onClick(View v) {
        CircleBean bean = (CircleBean) v.getTag();
        Intent intent = new Intent(v.getContext(), CircleActivity.class);
        intent.putExtra("id", bean.getId());
        v.getContext().startActivity(intent);
    }
}
