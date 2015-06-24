package com.xixi.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.bean.MagpieTitleBean;
import com.xixi.net.image.ImageTask;
import com.xixi.net.magpie.MagpieListTask;
import com.xixi.ui.magpie.MagpieActivity;
import com.xixi.net.BitmapReceiver;
import com.xixi.net.JSONReceiver;
import com.xixi.util.StringUtil;
import com.xixi.widget.RefreshAndLoadListView;

import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class FragmentMagpie extends Fragment implements AdapterView.OnItemClickListener {

    RefreshAndLoadListView listView;
    MagpieAdapter magpieAdapter;
    LayoutInflater inflater;
    RefreshAndLoadListView.OnRefreshAndLoadListener listener;

    // beans in cache
    List<MagpieTitleBean> beanList = new ArrayList<MagpieTitleBean>();
    // tasks under execution
    Collection<String> taskSet = new HashSet<String>();
    // pictures in cache
    Map<String, Bitmap> headMap = new HashMap<String, Bitmap>();

    int pageIndex = 0;
    int pageSize = 6;
    boolean isRefreshing = false;
    boolean noMoreItem = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_magpie, container, false);
        listView = (RefreshAndLoadListView) rootView.findViewById(R.id.magpie_listview);
        return rootView;
    }

    @Override
    public  void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inflater = LayoutInflater.from(getActivity());

        magpieAdapter = new MagpieAdapter();
        listView.setAdapter(magpieAdapter);

        listener = new RefreshAndLoadListView.OnRefreshAndLoadListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing)
                    refreshData(pageSize);
            }

            @Override
            public void onLoad() {
                if (!isRefreshing)
                    loadData(pageIndex + 1, pageSize);
            }
        };
        listView.setOnRefreshAndLoadListener(listener);

        refreshData(pageSize);
    }

    // refresh
    private void refreshData(int pageSize) {
        isRefreshing = true;
        RequestParams params = new RequestParams();
        params.put("pageIndex", 0);
        params.put("pageSize", pageSize);
        new MagpieListTask(params, refreshReceiver).execute();
    }

    // load more
    private void loadData(int pageIndex, int pageSize) {
        isRefreshing = true;
        RequestParams params = new RequestParams();
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);
        new MagpieListTask(params, loadReceiver).execute();
    }

    private JSONReceiver refreshReceiver = new JSONReceiver() {
        @Override
        public void onFailure(JSONObject obj) {
            isRefreshing = false;
            listView.onRefreshComplete();
            listView.onError();
        }
        @Override
        public void onSuccess(JSONObject obj) {
            isRefreshing = false;
            listView.onRefreshComplete();
            if (obj == null) return;
            JSONArray array = obj.optJSONArray("list");
            if (array == null) return;
            pageIndex = 0;
            beanList.clear();
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = (JSONObject) array.get(i);
                    MagpieTitleBean bean = new MagpieTitleBean(item);
                    beanList.add(bean);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            magpieAdapter.notifyDataSetChanged();
        }
    };

    private JSONReceiver loadReceiver = new JSONReceiver() {
        @Override
        public void onFailure(JSONObject obj) {
            isRefreshing = false;
            listView.onLoadComplete();
            listView.onError();
        }
        @Override
        public void onSuccess(JSONObject obj) {
            isRefreshing = false;
            if (obj == null) {return;}
            JSONArray array = obj.optJSONArray("list");
            if (array == null || array.length() == 0) {
                noMoreItem = true;
                listView.onLoadOver();
                return;
            }
            pageIndex++;
            try {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = (JSONObject) array.get(i);
                    MagpieTitleBean bean = new MagpieTitleBean(item);
                    if (!beanList.contains(bean)) {
                        beanList.add(bean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            magpieAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), MagpieActivity.class);
        intent.putExtra("id", beanList.get(position).id);
        startActivity(intent);
    }


    private class MagpieAdapter extends BaseAdapter {

        public int getCount() {
            return beanList.size();
        }

        @Override
        public MagpieTitleBean getItem(int position) {
            if (position < beanList.size()) {
                return beanList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lv_magpie_title_item, null);
                holder = new ViewHolder();
                holder.imHead = (ImageView) convertView.findViewById(R.id.head);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final String url = beanList.get(position).url;
            final String title = beanList.get(position).title;
            holder.tvTitle.setText(title);
            holder.imHead.setTag(url);
            if (headMap.containsKey(url)) {
                holder.imHead.setImageBitmap(headMap.get(url));
            } else {
                holder.imHead.setImageResource(R.drawable.ic_launcher);
                if (!StringUtil.isInCollection(url, taskSet) & !url.equals("")) {
                    BitmapReceiver bitmapReceiver = new BitmapReceiver() {
                        @Override
                        public void onFailure() {
                            taskSet.remove(url);
                        }
                        @Override
                        public void onSuccess(String url, Bitmap bitmap) {
                            headMap.put(url, bitmap);
                            ImageView imageView = (ImageView) listView.findViewWithTag(url);
                            if (imageView != null) {
                                imageView.setImageBitmap(bitmap);
                            }
                            magpieAdapter.notifyDataSetChanged();
                        }
                    };
                    taskSet.add(url);
                    // TODO cache new tasks if task number is larger than a const
                    new ImageTask(url, bitmapReceiver).execute();
                }
            }
            return convertView;
        }

        private class ViewHolder {
            public ImageView imHead;
            public TextView tvTitle;
        }

    }

}
