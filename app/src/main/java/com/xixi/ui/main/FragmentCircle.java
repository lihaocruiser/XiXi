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
                    circleBean.setPublisherHeadPic(base + i + ".jpg");
                    circleBean.setPic(base + "u" + i + ".jpg");
                    circleBean.setPublisherNickname("Nickname");
                    circleBean.setContent(base + i + ".jpg");
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
                for (int i = 0; i < pageSize; i++) {
                    CircleBean circleBean = new CircleBean();
                    circleBean.setPublisherHeadPic(base + i + ".jpg");
                    circleBean.setPic(base + "u" + i + ".jpg");
                    circleBean.setPublisherNickname("Nickname");
                    circleBean.setContent(base + i + ".jpg");
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
