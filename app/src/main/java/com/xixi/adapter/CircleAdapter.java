package com.xixi.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.ui.circle.CircleActivity;
import com.xixi.ui.magpie.NewMagpieActivity;
import com.xixi.util.Image.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by LiHao on 2015-7-20.
 */
public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.CardViewHolder> {

    private static int userID;

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
    public CardViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_circle, null);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewGroup.getContext(), CircleActivity.class);
                intent.putExtra("CircleBean", beanList.get(i));
                viewGroup.getContext().startActivity(intent);
            }
        });
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CardViewHolder viewHolder, int i) {
        CircleBean bean = beanList.get(i);
        viewHolder.bindBean(bean);
        // set header pic
        String headUrl = bean.getPublisherHeadPic();
        if (imageDownloader.containsBitmap(headUrl)) {
            viewHolder.imHeader.setImageBitmap(imageDownloader.getBitmap(headUrl));
        } else {
            viewHolder.imHeader.setImageBitmap(null);
            int viewWidth = viewHolder.imHeader.getLayoutParams().width;
            int viewHeight = viewHolder.imHeader.getLayoutParams().height;
            imageDownloader.fetchImage(headUrl, viewWidth, viewHeight, ImageView.ScaleType.CENTER_CROP);
        }
        // set moment pic
        String picUrl = bean.getPic();
        if (imageDownloader.containsBitmap(picUrl)) {
            viewHolder.imPic.setImageBitmap(imageDownloader.getBitmap(picUrl));
        } else {
            viewHolder.imPic.setImageBitmap(null);
            int viewWidth = viewHolder.imPic.getLayoutParams().width;
            int viewHeight = viewHolder.imPic.getLayoutParams().height;
            imageDownloader.fetchImage(picUrl, viewWidth, viewHeight, ImageView.ScaleType.CENTER_INSIDE);
        }
        // load more if scrolled to bottom
        if (i == beanList.size() - 1) {
            onLoadMoreListener.onLoadMore();
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
        public void bindBean(CircleBean bean) {
            imHeader.setTag(bean.getPublisherHeadPic());
            imPic.setTag(bean.getPic());
            tvNickname.setText(bean.getPublisherNickname());
            tvContent.setText(bean.getContent());
            tvLikeCount.setText(bean.getLikeCount() + "");
            tvCommentCount.setText(bean.getCommentCount() + "");
            if (bean.getLikeIds().contains(userID)) {
                imLike.setImageResource(R.drawable.ic_circle_like);
            } else {
                imLike.setImageResource(R.drawable.ic_circle_unlike);
            }
        }
    }

}

