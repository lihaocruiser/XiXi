package com.xixi.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;
import android.util.AttributeSet;
import android.R.color;

import com.xixi.R;

public class RefreshAndLoadListView extends ListView implements OnScrollListener {

    Context context;
    LayoutInflater inflater;
    OnRefreshAndLoadListener listener;
    FrameLayout.LayoutParams params;

    FrameLayout header;
    FrameLayout footer;
    TextView drag_header;
    TextView tv_footer;
    LinearLayout refresh_header;

    TranslateAnimation animation;

    boolean isRefreshing = false;
    boolean isLoading = false;
    boolean isLoadingOver = false;
    boolean pressFromTop = true;
    boolean isLastItemVisible;

    int pressPosition;
    int distanceDown;
    int firstVisibleItem;

    int listViewWidth;

    private final int HEADER_HEIGHT = 10;   // in pix
    private final int FOOTER_HEIGHT = 100;  // in pix
    private final int ANIM_DURATION = 300;  // in milliseconds
    private final int DRAG_THRESHOLD = 400; // in pix
    private final float ANIM_OFFSET = (1 + 0.5f) / (5 + 4*0.5f) / 2;

    public RefreshAndLoadListView(Context context) {
        super(context);
        init(context);
    }

    public RefreshAndLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RefreshAndLoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        header = (FrameLayout) inflater.inflate(R.layout.lv_refresh_and_load_header, null);
        footer = (FrameLayout) inflater.inflate(R.layout.lv_refresh_and_load_footer, null);
        drag_header = (TextView) header.findViewById(R.id.drag_header);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        refresh_header = (LinearLayout) header.findViewById(R.id.refresh_header);
        this.addHeaderView(header);
        this.addFooterView(footer);

        params = (FrameLayout.LayoutParams) drag_header.getLayoutParams();
        params.height = HEADER_HEIGHT;
        drag_header.setLayoutParams(params);
        params = (FrameLayout.LayoutParams) tv_footer.getLayoutParams();
        params.height = FOOTER_HEIGHT;
        tv_footer.setLayoutParams(params);
        params = (FrameLayout.LayoutParams) refresh_header.getLayoutParams();
        params.height = HEADER_HEIGHT;
        refresh_header.setLayoutParams(params);

        this.setOnScrollListener(this);

        animation =  new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -ANIM_OFFSET,
                Animation.RELATIVE_TO_PARENT, ANIM_OFFSET,
                Animation.RELATIVE_TO_PARENT, 0f,
                Animation.RELATIVE_TO_PARENT, 0f);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(ANIM_DURATION);
        animation.setFillAfter(false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pressFromTop = (header.getTop() == 0) && (firstVisibleItem == 0);
                pressPosition = (int) ev.getY();
                if (animation.hasStarted()) {
                    refresh_header.clearAnimation();
                    refresh_header.setVisibility(INVISIBLE);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (pressFromTop) {
                    setPressed(false);
                    for (int i = 0; i < ev.getHistorySize(); i++) {
                        distanceDown = (int) ev.getHistoricalY(i) - pressPosition;
                        if (!isRefreshing) {
                            onPreRefresh(distanceDown);
                            if (distanceDown > DRAG_THRESHOLD) {
                                onRefresh();
                                return true;
                            }
                        }
                    }
                    if (distanceDown > 0) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (pressFromTop) {
                    distanceDown = 0;
                    onPreRefresh(distanceDown);
                }
        }

        return super.onTouchEvent(ev);
    }

    private void onPreRefresh(int distanceDown) {
        if (isRefreshing) return;
        drag_header.setVisibility(VISIBLE);
        header.setBackgroundColor(color.white);
        refresh_header.setVisibility(INVISIBLE);
        listViewWidth = header.getMeasuredWidth();
        params = (FrameLayout.LayoutParams) drag_header.getLayoutParams();
        params.width = distanceDown * listViewWidth / DRAG_THRESHOLD;
        drag_header.setLayoutParams(params);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE && isLastItemVisible) {
            onLoad();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        isLastItemVisible = ((firstVisibleItem + visibleItemCount) == totalItemCount);
    }

    /**
     * 设置listener
     */
    public void setOnRefreshAndLoadListener(OnRefreshAndLoadListener listener) {
        this.listener = listener;
    }

    /**
     * 外部通知ListView数据更新的状态
     */
    public void onRefreshComplete() {
        isRefreshing = false;
        refresh_header.clearAnimation();
        refresh_header.setVisibility(INVISIBLE);
        Log.i("","onRefreshComplete");
    }

    public void onLoadComplete() {
        isLoading = false;
        Log.i("", "onLoadComplete");
    }

    public void onLoadOver() {
        isLoadingOver = true;
        Log.i("","onLoadOver");
        tv_footer.setText("No More Data.");
    }

    public void onError() {
        tv_footer.setText("Network Error.");
    }


    /**
     * ListView 通过 callback interface 发出更新数据的指令
     */
    private void onRefresh() {

        if (isRefreshing) return;
        isRefreshing = true;

        drag_header.setVisibility(INVISIBLE);
        refresh_header.setVisibility(VISIBLE);
        refresh_header.startAnimation(animation);

        Log.i("","onRefresh");
        if (listener != null) {
            listener.onRefresh();
        }
    }

    private void onLoad() {

        if (isLoading || isLoadingOver) return;
        isLoading = true;

        Log.i("","onLoad");
        if (listener != null) {
            listener.onLoad();
        }
    }

    /**
     * callback interface
     */
    public interface OnRefreshAndLoadListener {
        public void onRefresh();
        public void onLoad();
    }
}
