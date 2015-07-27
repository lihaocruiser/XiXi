package com.xixi.adapter.cardview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xixi.util.Image.ImageDownloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created on 2015-7-27.
 */
public class CardAdapter<B> extends RecyclerView.Adapter<CardViewHolder<B>> {

    private int resId;
    private ArrayList<B> beanList;
    private ImageDownloader imageDownloader;
    private Class<? extends CardViewHolder<B>> clazz;

    private OnItemClickListener<B> onItemClickListener;
    private OnLoadMoreListener onLoadMoreListener;

    public CardAdapter(Class<? extends CardViewHolder<B>> clazz, int resId, ImageDownloader imageDownloader) {
        beanList = new ArrayList<>();
        this.clazz = clazz;
        this.resId = resId;
        this.imageDownloader = imageDownloader;
    }

    public void setBeanList(ArrayList<B> beanList) {
        this.beanList = beanList;
    }

    public ArrayList<B> getBeanList() {
        return beanList;
    }


    public void setOnItemClickListener(OnItemClickListener<B> listener) {
        this.onItemClickListener = listener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }


    public interface OnItemClickListener<B> {
        void onItemClick(B bean);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public CardViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(resId, null);
        CardViewHolder<B> holder = null;
        try {
            Class<?>[] paramTypes = {CardView.class, ImageDownloader.class};
            Constructor<? extends CardViewHolder<B>> constructor = clazz.getConstructor(paramTypes);
            Object[] params = {v, imageDownloader};
            holder = constructor.newInstance(params);
        } catch (NoSuchMethodException e) {
            Log.e("NoSuchMethodException", e.getMessage(), e);
        } catch (InstantiationException e) {
            Log.e("InstantiationException", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e("IllegalAccessException", e.getMessage(), e);
        } catch (InvocationTargetException e) {
            Log.e("InvocationTargetExcept", e.getMessage(), e);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder<B> holder, final int position) {
        final B bean = beanList.get(position);
        holder.setValue(bean);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(bean);
                }
            }
        });

        if (position == beanList.size() - 1) {
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMore();
            }
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }
}
