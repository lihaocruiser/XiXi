package com.xixi.ui.main;

import com.xixi.bean.magpie.MagpieBean;
import com.xixi.net.magpie.MagpieListJSONTask;
import com.xixi.net.base.JSONReceiver;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentMagpie extends FragmentMain<MagpieBean> {

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
