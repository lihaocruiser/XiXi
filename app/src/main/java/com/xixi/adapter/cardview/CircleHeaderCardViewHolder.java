package com.xixi.adapter.cardview;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.ui.image.LocalImageShowActivity;
import com.xixi.ui.user.ProfileActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-22.
 */
public class CircleHeaderCardViewHolder extends BaseCardViewHolder<CircleBean> {


    private int userId;

    public ImageView imHeader;
    public ImageView imPic;
    public TextView tvNickname;
    public TextView tvContent;
    public ImageView imLike;
    public ImageView imComment;
    public TextView tvLikeCount;
    public TextView tvCommentCount;

    public CircleHeaderCardViewHolder(CardView v, ImageDownloader imageDownloader) {
        super(v, imageDownloader);
        imHeader = (ImageView) v.findViewById(R.id.im_header);
        imPic = (ImageView) v.findViewById(R.id.im_pic);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
        imLike = (ImageView) v.findViewById(R.id.im_like);
        imComment = (ImageView) v.findViewById(R.id.im_comment);
        tvLikeCount = (TextView) v.findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) v.findViewById(R.id.tv_comment_count);
    }

    @Override
    public void setValue(CircleBean bean) {

        getRootView().setTag(bean);

        tvNickname.setText(bean.getPublisherNickname());
        tvContent.setText(bean.getContent());
        tvLikeCount.setText(bean.getLikeCount() + "");
        tvCommentCount.setText(bean.getCommentCount() + "");

        if (bean.getLikeIds().contains(userId)) {
            imLike.setImageResource(R.drawable.ic_circle_like);
        } else {
            imLike.setImageResource(R.drawable.ic_circle_unlike);
        }

        getImageDownloader().setBitmap(bean.getPublisherHeadPic(), imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

        // set moment pic
        String picUrl = bean.getPic();
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
        CircleBean bean = (CircleBean) getRootView().getTag();
        Intent intent;
        switch (id) {
            case R.id.im_header:
                intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", bean.getId());
                v.getContext().startActivity(intent);
                break;
            case R.id.im_pic:
                intent = new Intent(v.getContext(), LocalImageShowActivity.class);
                intent.putExtra("imageUrl", bean.getPic());
                v.getContext().startActivity(intent);
                break;
        }
    }
}
