package com.xixi.ui.magpie;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xixi.R;
import com.xixi.bean.MagpieBean;
import com.xixi.bean.MagpieCommentBean;
import com.xixi.net.BitmapReceiver;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageTask;
import com.xixi.net.magpie.MagpieCommentTask;
import com.xixi.net.magpie.MagpieTask;
import com.xixi.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MagpieActivity extends ActionBarActivity {

    int id;
    LinearLayout llHeader;
    LoadListView listView;
    MagpieCommentAdapter magpieCommentAdapter;
    LayoutInflater inflater;

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

    // Bottom
    EditText etContent;
    Button btnSend;

    List<MagpieCommentBean> beanList = new ArrayList<>();
    Collection<String> taskSet = new HashSet<>();
    Map<String, Bitmap> headMap = new HashMap<>();

    int pageIndex = 0;
    int pageSize = 10;
    boolean isLoading;
    boolean noMoreItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_magpie);
        inflater = getLayoutInflater();
        id = getIntent().getIntExtra("id", -1);

        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (Button) findViewById(R.id.btn_send);

        listView = (LoadListView) findViewById(R.id.magpie_listview);
        initHeaderView();
        listView.addHeaderView(llHeader);
        listView.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!noMoreItem && !isLoading) {
                    loadComment();
                }
            }
        });
        MagpieCommentAdapter magpieCommentAdapter = new MagpieCommentAdapter();
        listView.setAdapter(magpieCommentAdapter);

        loadMagpie();
        loadComment();
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
     *  load magpie content (header view)
     */
    private void loadMagpie() {
        if (id < 0) {
            return;
        }
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
        magpieBean.getUserSex();
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
     *  load comments content (list view)
     */
    private void loadComment() {

        if (isLoading) {
            return;
        } else {
            isLoading = true;
        }

        RequestParams params = new RequestParams();
        params.put("postID", id + "");
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);

        JSONReceiver receiver = new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                isLoading = false;
                Toast.makeText(MagpieActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                isLoading = false;
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
                        MagpieCommentBean bean = new MagpieCommentBean(item);
                        if (!beanList.contains(bean)) {
                            beanList.add(bean);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                magpieCommentAdapter.notifyDataSetChanged();
            }
        };

        new MagpieCommentTask(params, receiver).execute();
    }



    private class MagpieCommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return beanList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MagpieCommentBean bean = beanList.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lv_magpie_comment_item, null);
                holder = new ViewHolder();
                holder.createView(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.fillData(bean);
            return convertView;
        }

        private class ViewHolder {
            ImageView imUserHeader;
            ImageView imUserSex;
            TextView tvUserName;
            TextView tvTime;
            TextView tvFloor;
            TextView tvContent;
            TextView tvLikeCount;

            public void createView(View convertView) {
                imUserHeader = (ImageView) convertView.findViewById(R.id.im_header);
                imUserSex = (ImageView) convertView.findViewById(R.id.im_sex);
                tvUserName = (TextView) convertView.findViewById(R.id.tv_name);
                tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                tvFloor = (TextView) convertView.findViewById(R.id.tv_floor);
                tvContent = (TextView) convertView.findViewById(R.id.tv_content);
                tvLikeCount = (TextView) convertView.findViewById(R.id.tv_like_count);
            }

            public void fillData(MagpieCommentBean bean) {
                tvTime.setText(bean.getTime());
                tvUserName.setText(bean.getUserName());
                tvFloor.setText(bean.getFloor() + "");
                tvContent.setText(bean.getContent());
                tvLikeCount.setText(bean.getLikeCount() + "");
                // TODO set imSex according to userSex
                String sex = bean.getUserSex();

                final String headerUrl = bean.getUserHeaderUrl();
                if (headMap.containsKey(headerUrl)) {
                    imUserHeader.setImageBitmap(headMap.get(headerUrl));
                } else if (!taskSet.contains(headerUrl)) {
                    imUserHeader.setImageResource(R.drawable.ic_launcher);
                    taskSet.add(headerUrl);

                    new ImageTask(headerUrl, new BitmapReceiver() {
                        @Override
                        public void onFailure() {
                            taskSet.remove(headerUrl);
                        }

                        @Override
                        public void onSuccess(String url, Bitmap bitmap) {
                            taskSet.remove(headerUrl);
                            headMap.put(url, bitmap);
                            ImageView imUserHeader = (ImageView) listView.findViewWithTag(url);
                            if (imUserHeader != null) {
                                imUserHeader.setImageBitmap(bitmap);
                            }
                            magpieCommentAdapter.notifyDataSetChanged();
                        }
                    }).execute();
                }
            }
        }
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
