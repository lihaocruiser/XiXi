package com.xixi.adapter;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xixi.R;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.ui.magpie.MagpieActivity;
import com.xixi.util.Image.ImageDownloader;

import java.util.ArrayList;

/**
 * Created by LiHao on 2015-7-23.
 */
public class MagpieAdapter extends RecyclerView.Adapter<MagpieCardViewHolder> {

    private int userId;

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
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public MagpieCardViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int i) {
        CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_magpie_list, null);
        return new MagpieCardViewHolder(v, imageDownloader);
    }

    @Override
    public void onBindViewHolder(final MagpieCardViewHolder viewHolder, final int i) {
        MagpieBean bean = beanList.get(i);
        viewHolder.setData(bean);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MagpieActivity.class);
                intent.putExtra("MagpieBean", beanList.get(i));
                v.getContext().startActivity(intent);
            }
        });

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
