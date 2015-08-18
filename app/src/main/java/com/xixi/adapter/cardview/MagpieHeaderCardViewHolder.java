package com.xixi.adapter.cardview;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.ui.image.LocalImageShowActivity;
import com.xixi.ui.user.ProfileActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-23.
 */
public class MagpieHeaderCardViewHolder extends BaseCardViewHolder<MagpieBean> {

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
        super(v, imageDownloader);
        imHeader = (ImageView) v.findViewById(R.id.im_header);
        imPic = (ImageView) v.findViewById(R.id.im_pic);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
        imLike = (ImageView) v.findViewById(R.id.im_like);
        imComment = (ImageView) v.findViewById(R.id.im_comment);
        tvLikeCount = (TextView) v.findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) v.findViewById(R.id.tv_comment_count);
    }

    @Override
    public void setValue(MagpieBean bean) {

        getRootView().setTag(bean);

        tvNickname.setText(bean.getUserName());
        tvTitle.setText(bean.getTitle());
        tvContent.setText(bean.getContent());
        tvLikeCount.setText(bean.getLikeCount() + "");
        tvCommentCount.setText(bean.getCommentCount() + "");

        getImageDownloader().setBitmap(bean.getUserHeaderUrl(), imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

        String picUrl = bean.getPicUrl();
        if (picUrl == null) {
            imPic.setVisibility(View.GONE);
        } else {
            imPic.setVisibility(View.VISIBLE);
            getImageDownloader().setBitmap(picUrl, imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);
        }

        imHeader.setOnClickListener(this);
        imPic.setOnClickListener(this);
    }

    @Override
    public void recycle() {
        getImageDownloader().removeImageView(imHeader);
        getImageDownloader().removeImageView(imPic);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        MagpieBean bean = (MagpieBean) getRootView().getTag();
        Intent intent;
        switch (id) {
            case R.id.im_header:
                intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", bean.getId());
                v.getContext().startActivity(intent);
                break;
            case R.id.im_pic:
                intent = new Intent(v.getContext(), LocalImageShowActivity.class);
                intent.putExtra("imageUrl", bean.getPicUrl());
                v.getContext().startActivity(intent);
                break;
        }
    }
}
