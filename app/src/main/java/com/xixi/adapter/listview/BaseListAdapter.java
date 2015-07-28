package com.xixi.adapter.listview;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xixi.util.Image.ImageDownloader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created on 2015-7-24.
 */
public class BaseListAdapter<B> extends BaseAdapter {

    private int resId;
    private ArrayList<B> beanList;
    private ImageDownloader imageDownloader;
    private Class<? extends BaseListViewHolder<B>> clazz;

    public BaseListAdapter(Class<? extends BaseListViewHolder<B>> clazz, int resId, ImageDownloader imageDownloader) {
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        final B replyBean = beanList.get(position);
        BaseListViewHolder<B> holder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(resId, null);
            try {
                Class<?> paramTypes[] = { View.class, ImageDownloader.class };
                Object params[] = { view, imageDownloader };
                Constructor<? extends BaseListViewHolder<B>> constructor = clazz.getConstructor(paramTypes);
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
            view.setTag(holder);
        } else {
            holder = (BaseListViewHolder<B>) view.getTag();
        }

        B bean = beanList.get(position);
        holder.setValue(bean);

        return view;
    }
}
