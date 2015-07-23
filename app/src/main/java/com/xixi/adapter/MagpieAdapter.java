package com.xixi.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.bean.MagpieBean;
import com.xixi.bean.circle.CircleBean;
import com.xixi.ui.circle.CircleActivity;
import com.xixi.ui.magpie.MagpieActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by LiHao on 2015-7-23.
 */
public class MagpieAdapter extends RecyclerView.Adapter<MagpieCardViewHolder> {


    private ArrayList<MagpieBean> beanList = new ArrayList<>();
    private ImageDownloader imageDownloader;
    private OnLoadMoreListener onLoadMoreListener;

    public MagpieAdapter(RecyclerView recyclerView) {
        super();
        imageDownloader = new ImageDownloader(recyclerView);
    }

    public void setBeanList(ArrayList<MagpieBean> beanList) {
        this.beanList = beanList;
    }

    public ArrayList<MagpieBean> getBeanList() {
        return beanList;
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public MagpieCardViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_magpie_list, null);
        return new MagpieCardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MagpieCardViewHolder viewHolder, final int i) {
        MagpieBean bean = beanList.get(i);
        viewHolder.bindBean(bean);
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MagpieActivity.class);
                intent.putExtra("MagpieBean", beanList.get(i));
                v.getContext().startActivity(intent);
            }
        });

        // set header pic
        String headUrl = bean.getUserHeaderUrl();
        imageDownloader.setBitmap(headUrl, viewHolder.imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

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
