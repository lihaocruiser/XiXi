package com.xixi.ui.main;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.ui.magpie.NewMagpieActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;


public class MainActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<ImageView> imageViewList;

    ImageView ivMagpie;
    ImageView ivDiscover;
    ImageView ivMe;

    MenuItem menuAdd;

    // drawable for bottom bar when unselected
    private final int[] navigationBar =
            {R.drawable.ic_main_magpie, R.drawable.ic_main_discover, R.drawable.ic_main_me};

    // drawable for bottom bar when selected
    private final int[] navigationBarWhite =
            {R.drawable.ic_main_magpie_white, R.drawable.ic_main_discover_white, R.drawable.ic_main_me_white};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init bottom bar
        ivMagpie = (ImageView) findViewById(R.id.iv_magpie);
        ivDiscover = (ImageView) findViewById(R.id.iv_discover);
        ivMe = (ImageView) findViewById(R.id.iv_me);
        imageViewList = new ArrayList<>();
        imageViewList.add(ivMagpie);
        imageViewList.add(ivDiscover);
        imageViewList.add(ivMe);
        for (int i = 0; i < imageViewList.size(); i++) {
            imageViewList.get(i).setOnClickListener(new OnNavigationClickListener(i));
        }

        // init fragment
        fragmentList = new ArrayList<>();
        fragmentList.add(new FragmentMagpie());
        fragmentList.add(new FragmentDiscover());
        fragmentList.add(new FragmentMe());

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
     * Change bottom navigation bar and option menu effect according to current fragment
     */
    private class MainOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageScrollStateChanged(int state) {}

        @Override
        public void onPageSelected(int position) {
            if (position == 0) {
                menuAdd.setVisible(true);
            } else {
                menuAdd.setVisible(false);
            }
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menuAdd = menu.findItem(R.id.action_new_magpie);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_new_magpie) {
            Intent intent = new Intent(MainActivity.this, NewMagpieActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
