package com.xixi.ui.magpie;

import android.graphics.Bitmap;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xixi.R;
import com.xixi.bean.MagpieBean;
import com.xixi.net.BitmapReceiver;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageTask;
import com.xixi.net.magpie.MagpieTask;
import com.xixi.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.zip.Inflater;

public class MagpieActivity extends ActionBarActivity {

    int id;
    LinearLayout llHeader;
    LoadListView listView;

    // HeaderView
    ImageView imUserHeader;
    ImageView imMagpiePic;
    ImageView imSex;
    TextView tvTitle;
    TextView tvUserName;
    TextView tvTime;
    TextView tvLikeCount;
    TextView tvCommentCount;
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_magpie);
        listView = (LoadListView) findViewById(R.id.magpie_listview);
        initHeaderView();
        listView.addHeaderView(llHeader);

        //listView.setAdapter(MagpieCommentAdapter);
        id = getIntent().getIntExtra("id", -1);

        LoadMagpie();
        LoadComment();
    }

    private void initHeaderView() {
        llHeader = (LinearLayout) LayoutInflater.from(MagpieActivity.this).inflate(R.layout.lv_magpie_header, null);
        imUserHeader = (ImageView) findViewById(R.id.im_header);
        imMagpiePic = (ImageView) findViewById(R.id.im_pic);
        imSex = (ImageView) findViewById(R.id.im_sex);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvUserName = (TextView) findViewById(R.id.tv_name);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvLikeCount = (TextView) findViewById(R.id.tv_like_count);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
        tvContent = (TextView) findViewById(R.id.tv_content);
    }

    /**
     *
     */
    private void LoadMagpie() {
        RequestParams params = new RequestParams();
        params.put("id", id);
        JSONReceiver receiver = new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                Toast.makeText(MagpieActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                if (obj == null) return;
                MagpieBean magpieBean = new MagpieBean(obj);
                FillHeaderView(magpieBean);
                LoadHeadPic(magpieBean.getUserHeaderUrl());
                LoadMagpiePic(magpieBean.getPicUrl());
            }
        };
        new MagpieTask(params, receiver).execute();
    }

    private void FillHeaderView(MagpieBean magpieBean) {
        tvTitle.setText(magpieBean.getTitle());
        tvUserName.setText(magpieBean.getUserName());
        tvTime.setText(magpieBean.getTime());
        tvLikeCount.setText(magpieBean.getLikeCount() + "");
        tvContent.setText(magpieBean.getContent());
        // TODO set imSex according to userSex
    }

    private void LoadHeadPic(String headerUrl) {
        if (headerUrl == null) {
            return;
        }
        BitmapReceiver receiver = new BitmapReceiver() {
            @Override
            public void onFailure() {
                imUserHeader.setImageResource(R.drawable.ic_launcher);
            }
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                imUserHeader.setImageBitmap(bitmap);
            }
        };
        new ImageTask(headerUrl, receiver).execute();
    }

    private void LoadMagpiePic(String picUrl) {
        if (picUrl == null) {
            return;
        }
        BitmapReceiver receiver = new BitmapReceiver() {
            @Override
            public void onFailure() {
                imMagpiePic.setImageResource(R.drawable.ic_launcher);
            }
            @Override
            public void onSuccess(String url, Bitmap bitmap) {
                imMagpiePic.setImageBitmap(bitmap);
            }
        };
        new ImageTask(picUrl, receiver).execute();
    }


    /**
     *
     */
    private void LoadComment() {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_magpie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
