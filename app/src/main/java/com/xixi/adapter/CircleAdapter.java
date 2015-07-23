package com.xixi.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.ui.circle.CircleActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by LiHao on 2015-7-20.
 */
public class CircleAdapter extends RecyclerView.Adapter<CircleCardViewHolder> {

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
    public CircleCardViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_circle_list, null);
        return new CircleCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final CircleCardViewHolder viewHolder, final int i) {
        CircleBean bean = beanList.get(i);
        viewHolder.bindBean(bean, userID);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CircleActivity.class);
                intent.putExtra("CircleBean", beanList.get(i));
                v.getContext().startActivity(intent);
            }
        });

        // set header pic
        String headUrl = bean.getPublisherHeadPic();
        imageDownloader.setBitmap(headUrl, viewHolder.imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

        // set moment pic
        String picUrl = bean.getPic();
        if (picUrl == null) {
            viewHolder.imPic.setVisibility(View.GONE);
        } else {
            viewHolder.imPic.setVisibility(View.VISIBLE);
            imageDownloader.setBitmap(picUrl, viewHolder.imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);
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

}
