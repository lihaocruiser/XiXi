package com.xixi.widget;

import android.R.color;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xixi.R;

public class LoadListView extends ListView implements OnScrollListener {

    Context context;
    LayoutInflater inflater;
    OnLoadListener listener;
    FrameLayout.LayoutParams params;

    FrameLayout footer;
    TextView tv_footer;

    boolean isLoading = false;
    boolean isLoadingOver = false;
    boolean isLastItemVisible;

    int firstVisibleItem;

    private final int FOOTER_HEIGHT = 100;  // in pix

    public LoadListView(Context context) {
        super(context);
        init(context);
    }

    public LoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
    
    private void init(Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);

        footer = (FrameLayout) inflater.inflate(R.layout.lv_load_footer, null);
        tv_footer = (TextView) footer.findViewById(R.id.tv_footer);
        this.addFooterView(footer);

        setFooterDividersEnabled(false);

        params = (FrameLayout.LayoutParams) tv_footer.getLayoutParams();
        params.height = FOOTER_HEIGHT;
        tv_footer.setLayoutParams(params);

        this.setOnScrollListener(this);
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
    public void setOnLoadListener(OnLoadListener listener) {
        this.listener = listener;
    }

    /**
     * 外部通知ListView数据更新的状态
     */
    public void onLoadComplete() {
        isLoading = false;
    }

    public void onLoadOver() {
        isLoadingOver = true;
        tv_footer.setText("No More Data.");
    }

    public void onError() {
        tv_footer.setText("Unknown Error.");
    }


    /**
     * ListView 通过 callback interface 发出更新数据的指令
     */
    private void onLoad() {

        if (isLoading || isLoadingOver) return;
        isLoading = true;

        if (listener != null) {
            listener.onLoad();
        }
    }

    /**
     * callback interface
     */
    public interface OnLoadListener {
        public void onLoad();
    }
}
