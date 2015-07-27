package com.xixi.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-23.
 */
public class MagpieHeaderCardViewHolder extends RecyclerView.ViewHolder {

    ImageDownloader imageDownloader;

    public CardView cardView;

    public ImageView imHeader;
    public ImageView imPic;

    public TextView tvNickname;
    public TextView tvTitle;
    public TextView tvContent;

    public ImageView imLike;
    public ImageView imComment;

    public TextView tvLikeCount;
    public TextView tvCommentCount;

    public MagpieHeaderCardViewHolder(CardView v, ImageDownloader imageDownloader) {
        super(v);
        this.cardView = v;
        imHeader = (ImageView) v.findViewById(R.id.im_header);
        imPic = (ImageView) v.findViewById(R.id.im_pic);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
        imLike = (ImageView) v.findViewById(R.id.im_like);
        imComment = (ImageView) v.findViewById(R.id.im_comment);
        tvLikeCount = (TextView) v.findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) v.findViewById(R.id.tv_comment_count);
        this.imageDownloader = imageDownloader;
    }

    public void setData(MagpieBean bean, int userId) {

        tvNickname.setText(bean.getUserName());
        tvTitle.setText(bean.getTitle());
        tvContent.setText(bean.getContent());
        tvLikeCount.setText(bean.getLikeCount() + "");
        tvCommentCount.setText(bean.getCommentCount() + "");
        imageDownloader.setBitmap(bean.getUserHeaderUrl(), imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);
        imageDownloader.setBitmap(bean.getPicUrl(), imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);
    }

}
