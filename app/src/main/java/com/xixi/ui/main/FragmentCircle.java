package com.xixi.ui.main;

import com.xixi.bean.circle.CircleBean;
import com.xixi.net.circle.CircleListJSONTask;
import com.xixi.net.base.JSONReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentCircle extends FragmentMain<CircleBean> {

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new CircleListJSONTask(userId, 0, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                // test only
                ArrayList<CircleBean> refreshedBeanList = new ArrayList<>();
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setId(i);
                    circleBean.setPublisherHeadPic(base + "header (" + i + ").png");
                    circleBean.setPic(base + "pic (" + i + ").jpg");
                    circleBean.setPublisherNickname("Username" + i);
                    circleBean.setContent("Content" + i);
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
        new CircleListJSONTask(userId, pageIndex, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                loading = false;
                // test only
                for (int i = pageSize; i < 2 * pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setId(i);
                    circleBean.setPublisherHeadPic(base + "header (" + i + ").png");
                    circleBean.setPic(base + "pic (" + i + ").jpg");
                    circleBean.setPublisherNickname("Username" + i);
                    circleBean.setContent("Content" + i);
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
