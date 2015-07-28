package com.xixi.adapter.listview;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.ReplyBean;
import com.xixi.ui.magpie.MagpieActivity;
import com.xixi.ui.user.ProfileActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-24.
 */
public class MagpieCommentViewHolder extends BaseListViewHolder<ReplyBean> {

    ReplyBean bean;
    ForegroundColorSpan span1;
    ForegroundColorSpan span2;
    ImageView imAvatar;
    TextView tvComment;

    public MagpieCommentViewHolder(View v, ImageDownloader imageDownloader) {
        super(v, imageDownloader);
        span1 = new ForegroundColorSpan(v.getContext().getResources().getColor(R.color.blue_dark));
        span2 = new ForegroundColorSpan(v.getContext().getResources().getColor(R.color.blue_dark));
        imAvatar = (ImageView) v.findViewById(R.id.im_avatar);
        tvComment = (TextView) v.findViewById(R.id.tv_comment);
    }

    @Override
    public void setValue(ReplyBean bean) {

        this.bean = bean;

        SpannableStringBuilder comment = new SpannableStringBuilder();
        String sender, receiver, content;

        sender = bean.getSenderNickname();
        comment.append(sender);
        comment.setSpan(span1, 0, comment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        receiver = bean.getReceiverNickname();
        if (receiver != null && !"".equals(receiver)) {
            comment.append("@").append(receiver);
            comment.setSpan(span2, comment.length() - receiver.length(), comment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        content = bean.getComment();
        comment.append("\n").append(content);
        tvComment.setText(comment);


        // set image

        imageDownloader.setBitmap(bean.getSenderAvatar(), imAvatar, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

        // OnClickListener

        imAvatar.setOnClickListener(this);
        tvComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.im_avatar:
                Intent intent = new Intent(v.getContext(), ProfileActivity.class);
                intent.putExtra("id", bean.getSenderId());
                v.getContext().startActivity(intent);
                break;
            case R.id.tv_comment:
                MagpieActivity.getInstance().replyMagpie(bean.getSenderId(), bean.getSenderNickname());
                break;
        }
    }
}
