package com.xixi.ui.circle;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xixi.R;
import com.xixi.adapter.CircleCardViewHolder;
import com.xixi.bean.circle.CircleBean;
import com.xixi.bean.circle.CircleCommentBean;
import com.xixi.net.JSONReceiver;
import com.xixi.net.circle.CircleJSONTask;
import com.xixi.util.Image.BitmapUtil;
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
    CardView cardView;
    LoadListView loadListView;
    EditText etComment;
    Button btnSend;
    CircleCardViewHolder cardViewHolder;

    CircleBean circleBean = new CircleBean();
    List<CircleCommentBean> commentBeanList = new ArrayList<>();
    CircleCommentAdapter adapter = new CircleCommentAdapter();

    ImageDownloader imageDownloader;

    private int circleId;
    private int userId;

    private boolean loading;
    private int receiverId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        // init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        circleBean = (CircleBean) getIntent().getSerializableExtra("CircleBean");
        circleId = circleBean.getId();

        etComment = (EditText) findViewById(R.id.et_send);
        btnSend = (Button) findViewById(R.id.btn_send);
        loadListView = (LoadListView) findViewById(R.id.list_view);


        // adapter
        adapter = new CircleCommentAdapter();
        loadListView.setAdapter(adapter);
        imageDownloader = new ImageDownloader(loadListView);
        loadListView.setDividerHeight(0);
        loadListView.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                onLoadMore();
            }
        });

        // add header view
        cardView = (CardView) getLayoutInflater().inflate(R.layout.cardview_circle_list, null);
        loadListView.addHeaderView(cardView);
        cardViewHolder = new CircleCardViewHolder(cardView);
        cardViewHolder.bindBean(circleBean, userId);
        String headUrl = circleBean.getPublisherHeadPic();
        imageDownloader.setBitmap(headUrl, cardViewHolder.imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.LARGE);
        String picUrl = circleBean.getPic();
        imageDownloader.setBitmap(picUrl, cardViewHolder.imPic, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.LARGE);

        onLoadMore();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO send comment
            }
        });
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
                loadListView.onLoadComplete();
                // for test only
                for (int i = 0; i < 10; i++) {
                    CircleCommentBean bean = new CircleCommentBean();
                    bean.setSenderNickname("李浩");
                    bean.setReceiverNickname("胡哲");
                    bean.setSenderHeadPic(base + i + ".jpg");
                    bean.setComment(base + i + ".jpg");
                    commentBeanList.add(bean);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                loading = false;
                loadListView.onLoadComplete();
                JSONArray array = obj.optJSONArray("list");
                commentBeanList = CircleCommentBean.getBeanList(array);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }


    private class CircleCommentAdapter extends BaseAdapter {

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
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            CircleCommentBean bean = commentBeanList.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.lv_circle_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bindBean(bean);
            return convertView;
        }

        private class ViewHolder {

            ForegroundColorSpan span1 = new ForegroundColorSpan(getResources().getColor(R.color.blue_dark));
            ForegroundColorSpan span2 = new ForegroundColorSpan(getResources().getColor(R.color.blue_dark));
            ImageView imHeader;
            TextView tvComment;

            public ViewHolder(View v) {
                imHeader = (ImageView) v.findViewById(R.id.im_header);
                tvComment = (TextView) v.findViewById(R.id.tv_comment);
            }

            public void bindBean(final CircleCommentBean bean) {

                // set text
                SpannableStringBuilder comment = new SpannableStringBuilder();
                String sender, receiver, content;

                sender = bean.getSenderNickname();
                comment.append(sender);
                comment.setSpan(span1, 0, comment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                receiver = bean.getReceiverNickname();
                if (receiver != null && !"".equals(receiver)) {
                    comment.append("@").append(receiver);
                    comment.setSpan(span2, comment.length() - receiver.length(), comment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                content = bean.getComment();
                comment.append("\n").append(content);
                tvComment.setText(comment);

                // set image
                imHeader.setTag(bean.getSenderHeadPic());
                imageDownloader.setBitmap(bean.getSenderHeadPic(), imHeader, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);

                // set OnClickListener
                tvComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        receiverId = bean.getUserId();
                        etComment.setHint("@" + bean.getSenderNickname());
                    }
                });

                imHeader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO start ProfileActivity
                    }
                });
            }
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
