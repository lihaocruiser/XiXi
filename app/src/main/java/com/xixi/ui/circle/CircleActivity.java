package com.xixi.ui.circle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.xixi.R;
import com.xixi.bean.circle.CircleBean;
import com.xixi.bean.circle.CircleCommentBean;
import com.xixi.net.JSONReceiver;
import com.xixi.net.circle.CircleJSONTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CircleActivity extends AppCompatActivity {

    Toolbar toolbar;
    int id;
    CircleBean circleBean = new CircleBean();
    List<CircleCommentBean> commentBeanList = new ArrayList<>();
    CircleAdapter adapter = new CircleAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        // init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        circleBean = (CircleBean) getIntent().getSerializableExtra("CircleBean");
        id = circleBean.getId();

        // TODO initView

        new CircleJSONTask(id, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {

            }

            @Override
            public void onSuccess(JSONObject obj) {
                JSONArray array = obj.optJSONArray("list");
                commentBeanList = CircleCommentBean.getBeanList(array);
            }
        }).execute();
    }


    private class CircleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return commentBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return commentBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null; // TODO
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_circle, menu);
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
