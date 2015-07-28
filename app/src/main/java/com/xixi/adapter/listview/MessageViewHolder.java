package com.xixi.adapter.listview;

import android.view.View;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.ApplicationContext;
import com.xixi.bean.user.MessageBean;
import com.xixi.util.Image.ImageDownloader;

/**
 * Created on 2015-7-28.
 */
public class MessageViewHolder extends BaseListViewHolder<MessageBean> {

    TextView tvTitle;
    TextView tvContent;

    public MessageViewHolder(View v, ImageDownloader imageDownloader) {
        super(v, imageDownloader);
        tvTitle = (TextView) v.findViewById(R.id.tv_title);
        tvContent = (TextView) v.findViewById(R.id.tv_content);
    }

    @Override
    public void setValue(MessageBean bean) {
        String sender = bean.getSenderNickname();
        String receiver = bean.getReceiverNickname();
        if (sender != null & ApplicationContext.getInstance().getNickname().equals(bean.getSenderNickname())) {
            tvTitle.setText("To:" + receiver);
        } else {
            tvTitle.setText("From:" + sender);
        }
        tvContent.setText(bean.getContent());
    }

    @Override
    public void onClick(View v) {

    }
}
