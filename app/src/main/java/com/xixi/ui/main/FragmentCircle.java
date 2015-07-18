package com.xixi.ui.main;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.Circle.CircleBean;
import com.xixi.net.circle.CircleListJSONTask;
import com.xixi.net.JSONReceiver;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentCircle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    int userID;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    List<CircleBean> beanList = new ArrayList<>();
    Collection<String> taskSet = new HashSet<>();
    Map<String, Bitmap> images = new HashMap<>();

    int pageIndex = 0;
    int pageSize = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_circle, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);

        adapter = new CircleAdapter();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_deep);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new CircleListJSONTask(userID, 0, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                // test only
                List<CircleBean> refreshedBeanList = new ArrayList<>();
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setContent("load:" + i);
                    refreshedBeanList.add(circleBean);
                }
                beanList = refreshedBeanList;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(JSONObject obj) {

            }
        }).execute();
    }


    private class CircleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_circle, null);
            return new CardViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            ((CardViewHolder) viewHolder).tv.setText(beanList.get(i).getContent());
            // 实现分页加载
            if (i == beanList.size() - 1) {
                loadMore();
            }
        }

        @Override
        public int getItemCount() {
            return beanList.size();
        }

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public TextView tv;
        public CardViewHolder(CardView v) {
            super(v);
            this.tv = (TextView) v.findViewById(R.id.tv);
        }
    }

    private void loadMore() {
        new CircleListJSONTask(userID, pageIndex, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                // test only
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setContent("load:" + i);
                    beanList.add(circleBean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {

            }
        }).execute();
    }


}
