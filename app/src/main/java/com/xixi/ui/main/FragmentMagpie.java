package com.xixi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xixi.R;
import com.xixi.adapter.cardview.CardAdapter;
import com.xixi.adapter.cardview.MagpieCardViewHolder;
import com.xixi.bean.magpie.MagpieBean;
import com.xixi.net.magpie.MagpieListJSONTask;
import com.xixi.net.base.JSONReceiver;
import com.xixi.ui.magpie.MagpieActivity;
import com.xixi.util.Image.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentMagpie extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/";

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CardAdapter<MagpieBean> adapter;

    ImageDownloader imageDownloader = new ImageDownloader();

    int pageIndex = 0;
    int pageSize = 30;
    boolean loading = false;
    boolean noMore = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);

//        adapter = new MagpieAdapter();
        adapter = new CardAdapter<>(MagpieCardViewHolder.class, R.layout.cardview_magpie_list, imageDownloader);
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object bean) {
                Intent intent = new Intent(getActivity(), MagpieActivity.class);
                intent.putExtra("id", ((MagpieBean) bean).getId());
                startActivity(intent);
            }
        });
        adapter.setOnLoadMoreListener(new CardAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!loading) {
                    loadMore();
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_deep);

        return rootView;
    }

    @Override
    public  void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new MagpieListJSONTask(0, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                // test only
                ArrayList<MagpieBean> refreshedBeanList = new ArrayList<>();
                for (int i = 0; i < pageSize; i++) {
                    MagpieBean magpieBean = new MagpieBean();
                    magpieBean.setUserName("李浩");
                    magpieBean.setPicUrl(base + "u" + i + ".jpg");
                    magpieBean.setTitle(base + i + ".jpg");
                    magpieBean.setUserHeaderUrl(base + i + ".jpg");
                    refreshedBeanList.add(magpieBean);
                }
                adapter.setBeanList(refreshedBeanList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                JSONArray array = obj.optJSONArray("list");
                if (array == null || array.length() == 0) {
                    return;
                }
                adapter.setBeanList(MagpieBean.getBeanList(array));
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public void loadMore() {
        loading = true;
        new MagpieListJSONTask(pageIndex, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                loading = false;
                for (int i = 0; i < pageSize; i++) {
                    MagpieBean magpieBean = new MagpieBean();
                    magpieBean.setUserName("李浩");
                    magpieBean.setTitle(base + i + ".jpg");
                    magpieBean.setUserHeaderUrl(base + i + ".jpg");
                    adapter.getBeanList().add(magpieBean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                loading = false;
                JSONArray array = obj.optJSONArray("list");
                if (array == null || array.length() == 0) {
                    noMore = true;
                    return;
                }
                MagpieBean.appendBeanList(adapter.getBeanList(), array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

}
