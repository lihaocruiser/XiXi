package com.xixi.ui.magpie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.xixi.R;
import com.xixi.ui.Image.ImageBucketFragment;
import com.xixi.ui.Image.ImageGridFragment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class NewMagpieActivity extends ActionBarActivity {

    NewMagpieFragment newMagpieFragment;
    ImageBucketFragment imageBucketFragment;
    ImageGridFragment imageGridFragment;
    Fragment currentFragment;

    MenuItem menuSend;
    MenuItem menuFinish;

    HashMap<Long, String> checkedMap;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_magpie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newMagpieFragment = new NewMagpieFragment();
        imageBucketFragment = new ImageBucketFragment();
        imageGridFragment = new ImageGridFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, newMagpieFragment)
                   .add(R.id.fragment_container, imageBucketFragment)
                   .add(R.id.fragment_container, imageGridFragment)
                   .show(newMagpieFragment)
                   .commit();
        currentFragment = newMagpieFragment;

        newMagpieFragment.setOnAddPhotoListener(new NewMagpieFragment.OnAddPhotoListener() {
            @Override
            public void onAddPhoto() {
                switchFragment(newMagpieFragment, imageBucketFragment);
            }
        });

        imageBucketFragment.setOnBucketClickedListener(new ImageBucketFragment.OnBucketClickListener() {
            @Override
            public void onBucketClick(int position) {
                switchFragment(imageBucketFragment, imageGridFragment);
                imageGridFragment.setImageBucket(imageBucketFragment.bucketList.get(position));
            }
        });

    }


    private void switchFragment(Fragment from, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(from).show(to);
        currentFragment = to;
        if (to == newMagpieFragment) {
            transaction.disallowAddToBackStack().commit();
            menuSend.setVisible(true);
            menuFinish.setVisible(false);
        } else if (to == imageBucketFragment) {
            transaction.addToBackStack(null).commit();
            menuSend.setVisible(false);
            menuFinish.setVisible(false);
        } else if (to == imageGridFragment) {
            transaction.commit();
            menuSend.setVisible(false);
            menuFinish.setVisible(true);
        }
    }


    @Override
    public void onBackPressed() {
        if (currentFragment == newMagpieFragment) {
            super.onBackPressed();
        } else {
            switchFragment(currentFragment, newMagpieFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_magpie, menu);
        menuSend = menu.findItem(R.id.action_send);
        menuFinish = menu.findItem(R.id.action_finish);
        menuSend.setVisible(true);
        menuFinish.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (currentFragment == newMagpieFragment) {
                    finish();
                } else {
                    switchFragment(currentFragment, newMagpieFragment);
                }
                return true;

            case R.id.action_finish:
                switchFragment(currentFragment, newMagpieFragment);
                checkedMap = imageGridFragment.checkedMap;
                Collection<String> paths = checkedMap.values();
                int i = 0;
                for (String path:paths) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    newMagpieFragment.imSelected[i].setImageBitmap(bitmap);
                }
                return true;

            case R.id.action_send:
                sendMagpie();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMagpie() {
        String title = newMagpieFragment.etTitle.getText().toString();
        String basic = newMagpieFragment.etBasic.getText().toString();
        String hobby = newMagpieFragment.etHobby.getText().toString();
        String condition = newMagpieFragment.etCondition.getText().toString();
    }

}
