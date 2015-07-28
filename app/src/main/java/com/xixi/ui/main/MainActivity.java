package com.xixi.ui.main;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.xixi.R;
import com.xixi.bean.ApplicationContext;
import com.xixi.ui.magpie.NewMagpieActivity;
import com.xixi.ui.user.MessageActivity;
import com.xixi.util.WindowUtil;
import com.xixi.util.file.FileUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    PagerSlidingTabStrip tabs;

    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    ApplicationContext ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ac = ApplicationContext.getInstance(MainActivity.this);
        FileUtil.init(MainActivity.this);
        WindowUtil.init(MainActivity.this);

        // init layout_toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // init fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentMagpie());
        fragmentList.add(new FragmentCircle());
        fragmentList.add(new FragmentMe());

        // init pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setCurrentItem(0);

        // init PagerSlidingTabStrip
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setTextSize((int) (tabs.getTextSize() * 1.5));
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
            case R.id.action_new_magpie:
                intent = new Intent(MainActivity.this, NewMagpieActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_new_circle:
                //intent = new Intent(MainActivity.this, NewCircleActivity.class);
                //startActivity(intent);
                return true;
            case R.id.action_notify:
                intent = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
