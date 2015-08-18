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
import com.xixi.adapter.cardview.CircleCardViewHolder;
import com.xixi.adapter.cardview.MagpieCardViewHolder;
import com.xixi.bean.ApplicationContext;
import com.xixi.ui.base.BaseActivity;
import com.xixi.ui.user.NotificationActivity;
import com.xixi.util.WindowUtil;
import com.xixi.util.file.FileUtil;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    PagerSlidingTabStrip tabs;

    private ViewPager viewPager;
    private List<Fragment> fragmentList;

    ApplicationContext ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        ac = ApplicationContext.getInstance(MainActivity.this);
        FileUtil.init(MainActivity.this);
        WindowUtil.init(MainActivity.this);

        // init fragment
        Bundle circleBundle = new Bundle();
        circleBundle.putInt("resId", R.layout.cardview_circle_list);
        circleBundle.putSerializable("clazz", CircleCardViewHolder.class);
        FragmentCircle fragmentCircle = new FragmentCircle();
        fragmentCircle.setArguments(circleBundle);

        Bundle magpieBundle = new Bundle();
        magpieBundle.putInt("resId", R.layout.cardview_magpie_list);
        magpieBundle.putSerializable("clazz", MagpieCardViewHolder.class);
        FragmentMagpie fragmentMagpie = new FragmentMagpie();
        fragmentMagpie.setArguments(magpieBundle);

        fragmentList = new ArrayList<>();
        fragmentList.add(fragmentMagpie);
        fragmentList.add(fragmentCircle);
        fragmentList.add(new FragmentMe());

        // init pager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
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
