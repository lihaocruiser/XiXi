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
import android.widget.Toast;

import com.xixi.R;
import com.xixi.adapter.CircleHeaderCardViewHolder;
import com.xixi.adapter.CommentAdapter;
import com.xixi.bean.CommentBean;
import com.xixi.bean.circle.CircleBean;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.circle.CircleJSONTask;
import com.xixi.ui.user.ProfileActivity;
import com.xixi.util.Image.ImageDownloader;
import com.xixi.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CircleActivity extends AppCompatActivity {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/";

    Toolbar toolbar;
    LoadListView listView;
    EditText etComment;
    Button btnSend;

    CommentAdapter adapter;
    ImageDownloader imageDownloader;

    CircleBean circleBean = new CircleBean();
    List<CommentBean> commentBeanList = new ArrayList<>();

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
        setContentView(R.layout.activity_circle);

        // init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get intent
        circleBean = (CircleBean) getIntent().getSerializableExtra("CircleBean");
        circleId = circleBean.getId();

        // find view
        etComment = (EditText) findViewById(R.id.et_send);
        btnSend = (Button) findViewById(R.id.btn_send);
        listView = (LoadListView) findViewById(R.id.list_view);

        // set adapter
        adapter = new CommentAdapter(listView);
        listView.setAdapter(adapter);
        imageDownloader = new ImageDownloader(listView);
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
        cardViewHolder.setData(circleBean, userId);

        // on click listener
        adapter.setOnAvatarClickListener(new CommentAdapter.OnAvatarClickListener() {
            @Override
            public void onAvatarClick(int userId) {
                Intent intent = new Intent(CircleActivity.this, ProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        adapter.setOnCommentClickListener(new CommentAdapter.OnCommentClickListener() {
            @Override
            public void onCommentClick(int receiverId, String receiverNickname) {
                CircleActivity.this.receiverId = receiverId;
                etComment.setHint("@" + receiverNickname);
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO send comment to receiverId
            }
        });

        // request data
        onLoadMore();
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
                    CommentBean bean = new CommentBean();
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
                commentBeanList = CommentBean.getBeanList(array);
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
