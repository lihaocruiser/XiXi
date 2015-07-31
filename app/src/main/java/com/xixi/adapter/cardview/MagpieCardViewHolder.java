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

    public MagpieCardViewHolder(CardView v, ImageDownloader imageDownloader) {
        super(v, imageDownloader);
        imHeader = (CircleImageView) v.findViewById(R.id.im_header);
        tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
    }

    @Override
    public void setValue(final MagpieBean bean) {

        getRootView().setTag(bean);

        tvNickname.setText(bean.getUserName());
        tvTitle.setText(bean.getTitle());

        String headUrl = bean.getUserHeaderUrl();
        getImageDownloader().setBitmap(headUrl, imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

    }

    @Override
    public void recycle() {
        getImageDownloader().removeImageView(imHeader);
    }

    @Override
    public void onClick(View v) {
        MagpieBean bean = (MagpieBean) getRootView().getTag();
        Intent intent = new Intent(v.getContext(), MagpieActivity.class);
        intent.putExtra("id", bean.getId());
        v.getContext().startActivity(intent);
    }
}
