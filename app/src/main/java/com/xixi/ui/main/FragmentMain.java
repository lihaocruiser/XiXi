package com.xixi.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.melnykov.fab.FloatingActionButton;
import com.xixi.R;
import com.xixi.adapter.cardview.BaseCardAdapter;
import com.xixi.adapter.cardview.BaseCardViewHolder;
import com.xixi.ui.circle.NewCircleActivity;
import com.xixi.ui.magpie.NewMagpieActivity;
import com.xixi.util.Image.ImageDownloader;

public abstract class FragmentMain<B> extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/";

    private Class<? extends BaseCardViewHolder<B>> clazz;
    private int resId;
    protected int userId;

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;
    protected FloatingActionButton fab;
    protected PopupMenu popupMenu;
    protected BaseCardAdapter<B> adapter;

    ImageDownloader imageDownloader = new ImageDownloader();

    int pageIndex = 0;
    int pageSize = 30;
    boolean loading = false;
    boolean noMore = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        clazz = (Class<? extends  BaseCardViewHolder<B>>) getArguments().getSerializable("clazz");
        resId = getArguments().getInt("resId");

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle_view);
        fab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        adapter = new BaseCardAdapter<>(clazz, resId, imageDownloader);
        adapter.setOnLoadMoreListener(new BaseCardAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!loading) {
                    loadMore();
                }
            }
        });
        adapter.setOnLeaveTopListener(new BaseCardAdapter.OnLeaveTopListener() {
            @Override
            public void onLeaveTop() {
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onRestoreTop() {
                fab.setVisibility(View.VISIBLE);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_deep);

        popupMenu = new PopupMenu(getActivity(), fab);
        popupMenu.getMenuInflater().inflate(R.menu.menu_main_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                Intent intent;
                switch (id) {
                    case R.id.action_new_magpie:
                        intent = new Intent(getActivity(), NewMagpieActivity.class);
                        getActivity().startActivity(intent);
                        return true;
                    case R.id.action_new_circle:
                        intent = new Intent(getActivity(), NewCircleActivity.class);
                        getActivity().startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        return rootView;
    }

    @Override
    public  void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onRefresh();
    }

    public abstract void loadMore();

}
