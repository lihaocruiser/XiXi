package com.xixi.ui.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xixi.R;
import com.xixi.adapter.cardview.CircleHeaderCardViewHolder;
import com.xixi.adapter.listview.CircleReplyViewHolder;
import com.xixi.adapter.listview.BaseListAdapter;
import com.xixi.bean.circle.ReplyBean;
import com.xixi.bean.circle.CircleBean;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.circle.CircleJSONTask;
import com.xixi.util.Image.ImageDownloader;
import com.xixi.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CircleActivity extends AppCompatActivity {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/";

    private static CircleActivity instance;

    public static CircleActivity getInstance() {
        return instance;
    }

    Toolbar toolbar;
    LoadListView listView;
    EditText etComment;
    Button btnSend;

    BaseListAdapter<ReplyBean> adapter;
    ImageDownloader imageDownloader;

    CircleBean circleBean = new CircleBean();
    List<ReplyBean> replyBeanList = new ArrayList<>();

    private int circleId;
    private int userId;

    int pageIndex = 0;
    int pageSize = 10;
    boolean loading;
    boolean noMoreItem;
    int receiverId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_list_view);
        instance = this;

        // init layout_toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get intent
        circleId = getIntent().getIntExtra("id", 0);

        // find view
        etComment = (EditText) findViewById(R.id.et_send);
        btnSend = (Button) findViewById(R.id.btn_send);
        listView = (LoadListView) findViewById(R.id.list_view);

        // set adapter
        imageDownloader = new ImageDownloader();
        adapter = new BaseListAdapter<>(CircleReplyViewHolder.class, R.layout.lv_circle_reply_item, imageDownloader);
        listView.setAdapter(adapter);
        listView.setDividerHeight(0);
        listView.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                onLoadMore();
            }
        });

        // add header view
        CardView cardView = (CardView) getLayoutInflater().inflate(R.layout.cardview_circle_header, null);
        listView.addHeaderView(cardView);
        CircleHeaderCardViewHolder cardViewHolder = new CircleHeaderCardViewHolder(cardView, imageDownloader);
        cardViewHolder.setData(circleBean);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO send comment to receiverId
            }
        });

        // request data
        onLoadMore();
    }

    public void replyCircle(int receiverId, String receiverNickname) {
        this.receiverId = receiverId;
        etComment.setHint("@" + receiverNickname);
    }


    private void onLoadMore() {
        if (loading) {
            return;
        }
        loading = true;
        new CircleJSONTask(circleId, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                loading = false;
                listView.onLoadComplete();
                // for test only
                for (int i = 0; i < 10; i++) {
                    ReplyBean bean = new ReplyBean();
                    bean.setSenderNickname("Sender");
                    bean.setSenderAvatar(base + i + ".jpg");
                    bean.setComment(base + i + ".jpg");
                    adapter.getBeanList().add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                loading = false;
                listView.onLoadComplete();
                JSONArray array = obj.optJSONArray("list");
                replyBeanList = ReplyBean.getBeanList(array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_new_magpie:
                Intent intent = new Intent();   // TODO
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
