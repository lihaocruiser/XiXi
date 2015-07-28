package com.xixi.adapter.cardview;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.ui.magpie.MagpieActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;
import com.xixi.widget.CircleImageView;

/**
 * Created on 2015-7-27.
 */
public class MagpieCardViewHolder extends BaseCardViewHolder<MagpieBean> {

    public CircleImageView imHeader;
    public TextView tvNickname;
    public TextView tvTitle;

    public MagpieCardViewHolder(CardView cardView, ImageDownloader imageDownloader) {
        super(cardView, imageDownloader);
        imHeader = (CircleImageView) cardView.findViewById(R.id.im_header);
        tvNickname = (TextView) cardView.findViewById(R.id.tv_nickname);
        tvTitle = (TextView) cardView.findViewById(R.id.tv_title);
    }

    @Override
    public void setValue(final MagpieBean bean) {

        tvNickname.setText(bean.getUserName());
        tvTitle.setText(bean.getTitle());

        String headUrl = bean.getUserHeaderUrl();
        imageDownloader.setBitmap(headUrl, imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

    }

    @Override
    public void onClick(View v) {
        MagpieBean bean = (MagpieBean) v.getTag();
        Intent intent = new Intent(v.getContext(), MagpieActivity.class);
        intent.putExtra("id", bean.getId());
        v.getContext().startActivity(intent);
    }
}
