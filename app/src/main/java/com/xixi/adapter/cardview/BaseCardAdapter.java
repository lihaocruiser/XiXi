package com.xixi.adapter.cardview;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.xixi.util.Image.ImageDownloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created on 2015-7-27.
 */
public class BaseCardAdapter<B> extends RecyclerView.Adapter<BaseCardViewHolder<B>> {

    private int resId;
    private ArrayList<B> beanList;
    private ImageDownloader imageDownloader;
    private Class<? extends BaseCardViewHolder<B>> clazz;
    private OnLoadMoreListener onLoadMoreListener;

    public BaseCardAdapter(Class<? extends BaseCardViewHolder<B>> clazz, int resId, ImageDownloader imageDownloader) {
        beanList = new ArrayList<>();
        this.clazz = clazz;
        this.resId = resId;
        this.imageDownloader = imageDownloader;
    }


    public ArrayList<B> getBeanList() {
        return beanList;
    }

    public void setBeanList(ArrayList<B> beanList) {
        this.beanList = beanList;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener) {
        this.onLoadMoreListener = listener;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    @Override
    public BaseCardViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(resId, null);
        BaseCardViewHolder<B> holder = null;
        try {
            Class<?>[] paramTypes = {CardView.class, ImageDownloader.class};
            Constructor<? extends BaseCardViewHolder<B>> constructor = clazz.getConstructor(paramTypes);
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
    public void onBindViewHolder(BaseCardViewHolder<B> holder, final int position) {

        B bean = beanList.get(position);

        // 为CardViewHolder填充内容
        holder.setValue(bean);

        // 使CardView持有bean的引用，以便在CardViewHolder中设置OnClickListener
        holder.getRootView().setTag(bean);

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
