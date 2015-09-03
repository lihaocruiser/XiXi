package com.xixi.ui.main;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.xixi.R;
import com.xixi.adapter.cardview.CircleCardViewHolder;
import com.xixi.adapter.cardview.MagpieCardViewHolder;
import com.xixi.bean.ApplicationContext;
import com.xixi.ui.base.BaseActivity;
import com.xixi.ui.user.NotificationActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    PagerSlidingTabStrip tabs;

    private ViewPager viewPager;

    ApplicationContext ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ac = ApplicationContext.getInstance(MainActivity.this);

        // init pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(0);

        // init PagerSlidingTabStrip
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTextSize((int) (tabs.getTextSize() * 1.4));
        tabs.setShouldExpand(true);
        tabs.setViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_notify:
                intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
