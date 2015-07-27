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
import com.xixi.adapter.cardview.CircleCardViewHolder;
import com.xixi.bean.circle.CircleBean;
import com.xixi.net.circle.CircleListJSONTask;
import com.xixi.net.base.JSONReceiver;
import com.xixi.ui.circle.CircleActivity;
import com.xixi.util.Image.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCircle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/";

    int userID;

    ImageDownloader imageDownloader = new ImageDownloader();

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CardAdapter<CircleBean> adapter;

    int pageIndex = 0;
    int pageSize = 30;
    boolean loading = false;
    boolean noMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);

        adapter = new CardAdapter<>(CircleCardViewHolder.class, R.layout.cardview_circle_list, imageDownloader);
        adapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object bean) {
                Intent intent = new Intent(getActivity(), CircleActivity.class);
                intent.putExtra("id", ((CircleBean) bean).getId());
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
                swipeRefreshLayout.setRefreshing(false);
                // test only
                ArrayList<CircleBean> refreshedBeanList = new ArrayList<>();
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setPublisherHeadPic(base + i + ".jpg");
                    circleBean.setPic(base + "u" + i + ".jpg");
                    circleBean.setPublisherNickname("Nickname");
                    circleBean.setContent(base + i + "jpg");
                    refreshedBeanList.add(circleBean);
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
                adapter.setBeanList(CircleBean.getBeanList(array));
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public void loadMore() {
        loading = true;
        new CircleListJSONTask(userID, pageIndex, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                loading = false;
                // test only
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setPublisherHeadPic(base + i + ".jpg");
                    circleBean.setPic(base + "u" + i + ".jpg");
                    circleBean.setPublisherNickname("Nickname");
                    circleBean.setContent(base + i + "jpg");
                    adapter.getBeanList().add(circleBean);
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
                CircleBean.appendBeanList(adapter.getBeanList(), array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

}
