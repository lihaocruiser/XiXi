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
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.Circle.CircleBean;
import com.xixi.net.circle.CircleListJSONTask;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageDownloader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class FragmentCircle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/u";

    int userID;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    ArrayList<CircleBean> beanList = new ArrayList<>();

    int pageIndex = 0;
    int pageSize = 8;
    boolean loading = false;
    boolean noMore = false;

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
                swipeRefreshLayout.setRefreshing(false);
                // test only
                ArrayList<CircleBean> refreshedBeanList = new ArrayList<>();
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setPublisherHeadPic(base + i + ".jpg");
                    refreshedBeanList.add(circleBean);
                }
                beanList = refreshedBeanList;
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                JSONArray array = obj.optJSONArray("list");
                if (array == null || array.length() == 0) {
                    return;
                }
                beanList = CircleBean.getBeanList(array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

    private void loadMore() {
        loading = true;
        new CircleListJSONTask(userID, pageIndex, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                loading = false;
                // test only
                for (int i = 0; i < pageSize; i++) {
                    int j = i + 8;
                    CircleBean circleBean = new CircleBean();
                    circleBean.setPublisherHeadPic(base + j + ".jpg");
                    beanList.add(circleBean);
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
                CircleBean.appendBeanList(beanList, array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }


    private class CircleAdapter extends RecyclerView.Adapter<CardViewHolder> {

        HashSet<String> taskSet = new HashSet<>();
        HashMap<String, Bitmap> imageMap = new HashMap<>();

        ImageDownloader imageDownloader = new ImageDownloader(getActivity(), taskSet, imageMap, recyclerView);

        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            CardView v = (CardView) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_circle, null);
            return new CardViewHolder(v);
        }

        @Override
        public void onBindViewHolder(CardViewHolder viewHolder, int i) {
            String url = beanList.get(i).getPublisherHeadPic();
            viewHolder.tvContent.setText(beanList.get(i).getPublisherHeadPic());
            viewHolder.imHeader.setTag(url);
            if (i == beanList.size() - 1 && !loading) {
                loadMore();
            }
            if (imageMap.containsKey(url)) {
                viewHolder.imHeader.setImageBitmap(imageMap.get(url));
            } else {
                int viewWidth = viewHolder.imHeader.getLayoutParams().width;
                int viewHeight = viewHolder.imHeader.getLayoutParams().height;
                imageDownloader.getImage(url, viewWidth, viewHeight, ImageView.ScaleType.CENTER_CROP);
            }
        }

        @Override
        public int getItemCount() {
            return beanList.size();
        }

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView imHeader;
        public TextView tvContent;
        public CardViewHolder(CardView v) {
            super(v);
            tvContent = (TextView) v.findViewById(R.id.tv_content);
            imHeader = (ImageView) v.findViewById(R.id.im_header);
        }
    }

}
