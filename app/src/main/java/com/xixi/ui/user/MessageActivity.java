package com.xixi.ui.user;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.adapter.listview.BaseListAdapter;
import com.xixi.adapter.listview.MessageViewHolder;
import com.xixi.bean.ApplicationContext;
import com.xixi.bean.user.MessageBean;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.user.MessageJSONTask;
import com.xixi.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Toolbar toolbar;
    SwipeRefreshLayout swipeRefreshLayout;
    LoadListView listView;
    EditText etSend;
    Button btnSend;

    BaseListAdapter<MessageBean> adapter;

    int userId;
    int receiverId;
    String receiverNickname;
    int pageIndex = 0;
    int pageSize = 10;
    boolean loading;
    boolean noMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        receiverId = getIntent().getIntExtra("receiverId", 0);
        receiverNickname = getIntent().getStringExtra("receiverNickname");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (LoadListView) findViewById(R.id.load_list_view);
        etSend = (EditText) findViewById(R.id.et_send);
        btnSend = (Button) findViewById(R.id.btn_send);

        if (receiverId != 0) {
            etSend.setHint("@" + receiverNickname);
        }

        adapter = new BaseListAdapter<>(MessageViewHolder.class, R.layout.lv_message_item, null);
        listView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_deep);

        listView.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                onLoadMore();
            }
        });

        onRefresh();
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new MessageJSONTask(userId, 0, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                // for test only
                for (int i = 0; i < pageSize; i++) {
                    MessageBean bean = new MessageBean();
                    if (i % 3 == 0) {
                        bean.setSenderNickname("狸耗");
                    } else {
                        bean.setSenderNickname("");
                    }
                    bean.setReceiverNickname("鼎鑫");
                    bean.setContent("我是私信的内容");
                    adapter.getBeanList().add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                pageIndex++;
                JSONArray array = obj.optJSONArray("list");
                if (array == null || array.length() == 0) {
                    return;
                }
                adapter.setBeanList(MessageBean.getBeanList(array));
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }


    public void onLoadMore() {
        if (loading) {
            return;
        }
        loading = true;
        new MessageJSONTask(userId, pageIndex, pageSize, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                // for test only
                for (int i = 0; i < pageSize; i++) {
                    MessageBean bean = new MessageBean();
                    if (i % 3 == 0) {
                        bean.setSenderNickname("狸耗");
                    } else {
                        bean.setSenderNickname("");
                    }
                    bean.setReceiverNickname("鼎鑫");
                    bean.setContent("我是私信的内容");
                    adapter.getBeanList().add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                swipeRefreshLayout.setRefreshing(false);
                pageIndex++;
                loading = false;
                JSONArray array = obj.optJSONArray("list");
                if (array == null || array.length() == 0) {
                    noMore = true;
                    return;
                }
                MessageBean.appendBeanList(adapter.getBeanList(), array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
