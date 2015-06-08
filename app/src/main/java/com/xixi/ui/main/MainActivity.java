package com.xixi.ui.main;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageView;

import com.xixi.R;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<ImageView> imageViewList;

    ImageView ivMagpie;
    ImageView ivCircle;

    // drawable for bottom bar when unselected
    private final int[] navigationBar =
            {R.drawable.ic_magpie, R.drawable.ic_circle};

    // drawable for bottom bar when selected
    private final int[] navigationBarWhite =
            {R.drawable.ic_magpie_white, R.drawable.ic_circle_white};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init bottom bar
        ivMagpie = (ImageView) findViewById(R.id.iv_magpie);
        ivCircle = (ImageView) findViewById(R.id.iv_circle);
        imageViewList = new ArrayList<>();
        imageViewList.add(ivMagpie);
        imageViewList.add(ivCircle);
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setOnClickListener(new OnNavigationClickListener(i));
        }

        // init fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(new MagpieFragment());
        fragmentList.add(new CircleFragment());

        // init pager
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        viewPager.setOnPageChangeListener(new MainOnPageChangeListener());
        viewPager.setCurrentItem(0);
    }


    private class OnNavigationClickListener implements View.OnClickListener {

        private int position;

        public OnNavigationClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(position);
        }
    }


    /**
     * Change bottom navigation bar effect according to current fragment
     */
    private class MainOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < fragmentList.size(); i++) {
                if (position == i) {
                    imageViewList.get(i).setImageResource(navigationBar[i]);
                } else {
                    imageViewList.get(i).setImageResource(navigationBarWhite[i]);
                }
            }
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
