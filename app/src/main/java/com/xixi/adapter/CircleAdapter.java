package com.xixi.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.util.Image.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by LiHao on 2015-7-20.
 */
public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.CardViewHolder> {

    private ArrayList<CircleBean> beanList = new ArrayList<>();
    private ImageDownloader imageDownloader;
    private OnLoadMoreListener onLoadMoreListener;

    public CircleAdapter(RecyclerView recyclerView) {
        super();
        imageDownloader = new ImageDownloader(recyclerView);
    }

    public void setBeanList(ArrayList<CircleBean> beanList) {
        this.beanList = beanList;
    }

    public ArrayList<CircleBean> getBeanList() {
        return beanList;
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_circle, null);
        CardViewHolder holder = new CardViewHolder(v);
        holder.imHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, int i) {
        String url = beanList.get(i).getPublisherHeadPic();
        viewHolder.tvContent.setText(beanList.get(i).getPublisherHeadPic());
        viewHolder.imHeader.setTag(url);
        if (i == beanList.size() - 1) {
            onLoadMoreListener.onLoadMore();
        }
        if (imageDownloader.containsBitmap(url)) {
            viewHolder.imHeader.setImageBitmap(imageDownloader.getBitmap(url));
        } else {
            int viewWidth = viewHolder.imHeader.getLayoutParams().width;
            int viewHeight = viewHolder.imHeader.getLayoutParams().height;
            imageDownloader.fetchImage(url, viewWidth, viewHeight, ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView imHeader;
        public ImageView imPic;
        public TextView tvNickname;
        public TextView tvContent;
        public ImageView imLike;
        public ImageView imComment;
        public TextView tvLikeCount;
        public TextView tvCommentCount;
        public CardViewHolder(CardView v) {
            super(v);
            imHeader = (ImageView) v.findViewById(R.id.im_header);
            imPic = (ImageView) v.findViewById(R.id.im_pic);
            tvNickname = (TextView) v.findViewById(R.id.tv_nickname);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            imLike = (ImageView) v.findViewById(R.id.im_like);
            imComment = (ImageView) v.findViewById(R.id.im_comment);
            tvLikeCount = (TextView) v.findViewById(R.id.tv_like_count);
            tvCommentCount = (TextView) v.findViewById(R.id.tv_comment_count);
        }
    }

}

