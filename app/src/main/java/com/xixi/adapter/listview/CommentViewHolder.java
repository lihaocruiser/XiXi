package com.xixi.adapter.listview;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.ReplyBean;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-24.
 */
public class CommentViewHolder {

    ForegroundColorSpan span1;
    ForegroundColorSpan span2;
    ImageView imHeader;
    TextView tvComment;
    ImageDownloader imageDownloader;

    public CommentViewHolder(View v, ImageDownloader imageDownloader) {
        span1 = new ForegroundColorSpan(v.getContext().getResources().getColor(R.color.blue_dark));
        span2 = new ForegroundColorSpan(v.getContext().getResources().getColor(R.color.blue_dark));
        imHeader = (ImageView) v.findViewById(R.id.im_header);
        tvComment = (TextView) v.findViewById(R.id.tv_comment);
        this.imageDownloader = imageDownloader;
    }

    public void setData(final ReplyBean bean) {

        // set text

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

        imageDownloader.setBitmap(bean.getSenderAvatar(), imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);
    }
}
