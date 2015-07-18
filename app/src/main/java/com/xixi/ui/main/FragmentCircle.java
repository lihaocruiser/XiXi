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
import com.xixi.net.BitmapReceiver;
import com.xixi.net.circle.CircleListJSONTask;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageDownloadTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class FragmentCircle extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/u";

    int userID;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    List<CircleBean> beanList = new ArrayList<>();
    Collection<String> taskSet = new HashSet<>();
    Map<String, Bitmap> imageMap = new HashMap<>();

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
                List<CircleBean> refreshedBeanList = new ArrayList<>();
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
                List<CircleBean> refreshedBeanList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject o = (JSONObject) array.get(i);
                        CircleBean circleBean = new CircleBean(o);
                        refreshedBeanList.add(circleBean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                beanList = refreshedBeanList;
                adapter.notifyDataSetChanged();
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
            String url = beanList.get(i).getPublisherHeadPic();
            ((CardViewHolder) viewHolder).tvContent.setText(beanList.get(i).getPublisherHeadPic());
            ((CardViewHolder) viewHolder).imHeader.setTag(url);
            if (i == beanList.size() - 1 && !loading) {
                loadMore();
            }
            if (imageMap.containsKey(url)) {
                ((CardViewHolder) viewHolder).imHeader.setImageBitmap(imageMap.get(url));
            } else {
                fetchBitmap(url);
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
                for (int i = 0; i < array.length(); i++) {
                    try {
                        JSONObject o = (JSONObject) array.get(i);
                        CircleBean circleBean = new CircleBean(o);
                        beanList.add(circleBean);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        }).execute();
    }

    private void fetchBitmap(String url) {
        if (taskSet.contains(url)) {
            return;
        }
        new ImageDownloadTask(url, new BitmapReceiver() {
            @Override
            public void onFailure(String url) {
                taskSet.remove(url);
            }

            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                taskSet.remove(url);
                imageMap.put(url, bitmap);
                ImageView imageView = (ImageView) recyclerView.findViewWithTag(url);
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }).execute();
    }

}
