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

    private int userId;

    Toolbar toolbar;
    EditText etComment;
    Button btnSend;
    CardView cardView;
    LoadListView loadListView;
    CircleCardViewHolder cardViewHolder;
    int id;
    CircleBean circleBean = new CircleBean();
    List<CircleCommentBean> commentBeanList = new ArrayList<>();
    CircleAdapter adapter = new CircleAdapter();

    private boolean loading;
    private int replyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);

        // init toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etComment = (EditText) findViewById(R.id.et_send);
        btnSend = (Button) findViewById(R.id.btn_send);

        loadListView = (LoadListView) findViewById(R.id.list_view);
        cardView = (CardView) getLayoutInflater().inflate(R.layout.cardview_circle_list, null);
        loadListView.addHeaderView(cardView);

        adapter = new CircleAdapter();
        loadListView.setAdapter(adapter);
        loadListView.setDividerHeight(0);
        loadListView.setOnLoadListener(new LoadListView.OnLoadListener() {
            @Override
            public void onLoad() {
                loadMore();
            }
        });

        circleBean = (CircleBean) getIntent().getSerializableExtra("CircleBean");
        id = circleBean.getId();

        cardViewHolder = new CircleCardViewHolder(cardView);
        cardViewHolder.bindBean(circleBean, userId);
        loadImage();

        loadMore();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO send comment
            }
        });
    }


    private void loadImage() {
        ImageDownloader imageDownloader = new ImageDownloader(loadListView);
        // set header pic
        String headUrl = circleBean.getPublisherHeadPic();
        if (imageDownloader.containsBitmap(headUrl)) {
            cardViewHolder.imHeader.setImageBitmap(imageDownloader.getBitmap(headUrl));
        } else {
            cardViewHolder.imHeader.setImageBitmap(null);
            int viewWidth = cardViewHolder.imHeader.getLayoutParams().width;
            int viewHeight = cardViewHolder.imHeader.getLayoutParams().height;
            imageDownloader.fetchImage(headUrl, viewWidth, viewHeight, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.SMALL);
        }
        // set moment pic
        String picUrl = circleBean.getPic();
        if (imageDownloader.containsBitmap(picUrl)) {
            cardViewHolder.imPic.setImageBitmap(imageDownloader.getBitmap(picUrl));
        } else {
            cardViewHolder.imPic.setImageBitmap(null);
            int viewWidth = cardViewHolder.imPic.getLayoutParams().width;
            int viewHeight = cardViewHolder.imPic.getLayoutParams().height;
            imageDownloader.fetchImage(picUrl, viewWidth, viewHeight, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.LARGE);
        }
    }


    private void loadMore() {
        if (loading) {
            return;
        }
        loading = true;
        new CircleJSONTask(id, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                loading = false;
                loadListView.onLoadComplete();
                // for test only
                for (int i = 0; i < 5; i++) {
                    CircleCommentBean bean = new CircleCommentBean();
                    bean.setSenderNickname("李浩");
                    bean.setReceiverNickname("胡哲");
                    bean.setComment("一二三四五，上山打老虎，老虎没打着，打着小松鼠。");
                    commentBeanList.add(bean);
                }
                for (int i = 0; i < 5; i++) {
                    CircleCommentBean bean = new CircleCommentBean();
                    bean.setSenderNickname("丁鑫");
                    bean.setComment("大炮");
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


    private class CircleAdapter extends BaseAdapter {

        private ImageDownloader imageDownloader = new ImageDownloader(loadListView);

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
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.lv_circle_item, null);
                holder = new ViewHolder();
                holder.tvComment = (TextView) convertView.findViewById(R.id.tv_comment);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bindBean(commentBeanList.get(position));

            holder.tvComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    replyId = commentBeanList.get(position).getUserId();
                    etComment.setHint("@" + commentBeanList.get(position).getSenderNickname());
                }
            });

            return convertView;
        }

        private class ViewHolder {

            ForegroundColorSpan span1 = new ForegroundColorSpan(getResources().getColor(R.color.blue_dark));
            ForegroundColorSpan span2 = new ForegroundColorSpan(getResources().getColor(R.color.blue_dark));
            ImageView imHeader;
            TextView tvComment;

            public void bindBean(CircleCommentBean bean) {

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

                // TODO load avatar
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
