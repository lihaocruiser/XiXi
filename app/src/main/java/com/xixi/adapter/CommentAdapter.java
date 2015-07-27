package com.xixi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.xixi.R;
import com.xixi.bean.circle.ReplyBean;
import com.xixi.util.Image.ImageDownloader;

import java.util.ArrayList;

/**
 * Created on 2015-7-24.
 */
public class CommentAdapter extends BaseAdapter {

    private int userId;
    private ArrayList<ReplyBean> beanList = new ArrayList<>();
    private ImageDownloader imageDownloader;

    private OnAvatarClickListener avatarClickListener;
    private OnCommentClickListener commentClickListener;

    public CommentAdapter() {
        imageDownloader = new ImageDownloader();
    }

    public CommentAdapter(ImageDownloader imageDownloader) {
        this.imageDownloader = imageDownloader;
    }

    public interface OnAvatarClickListener {
        void onAvatarClick(int userId);
    }

    public interface OnCommentClickListener {
        void onCommentClick(int receiverId, String receiverNickname);
    }

    public ArrayList<ReplyBean> getBeanList() {
        return beanList;
    }

    public void setBeanList(ArrayList<ReplyBean> beanList) {
        this.beanList = beanList;
    }

    public void setOnAvatarClickListener(OnAvatarClickListener listener) {
        avatarClickListener = listener;
    }

    public void setOnCommentClickListener(OnCommentClickListener listener) {
        commentClickListener = listener;
    }

    @Override
    public int getCount() {
        return beanList.size();
    }

    @Override
    public Object getItem(int i) {
        return beanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ReplyBean replyBean = beanList.get(i);
        CommentViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.lv_comment_item, null);
            holder = new CommentViewHolder(view, imageDownloader);
            view.setTag(holder);
        } else {
            holder = (CommentViewHolder) view.getTag();
        }

        holder.setData(beanList.get(i));

        holder.imHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = replyBean.getSenderId();
                if (avatarClickListener != null) {
                    avatarClickListener.onAvatarClick(userId);
                }
            }
        });

        holder.tvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int receiverId = replyBean.getSenderId();
                String  receiverNickname = replyBean.getSenderNickname();
                if (commentClickListener != null) {
                    commentClickListener.onCommentClick(receiverId, receiverNickname);
                }
            }
        });

        return view;
    }
}
