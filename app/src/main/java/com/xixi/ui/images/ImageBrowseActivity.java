package com.xixi.ui.images;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.xixi.R;

import java.util.Collection;

public class ImageBrowseActivity extends ActionBarActivity {

    ImageBucketFragment imageBucketFragment;
    ImageGridFragment imageGridFragment;
    Fragment currentFragment;

    MenuItem menuFinish;

    int maxImageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_browse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        maxImageCount = getIntent().getIntExtra("maxImageCount", 1);

        imageBucketFragment = new ImageBucketFragment();
        imageGridFragment = new ImageGridFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, imageBucketFragment)
                .add(R.id.fragment_container, imageGridFragment)
                .show(imageBucketFragment)
                .commit();
        currentFragment = imageBucketFragment;

        imageBucketFragment.setOnBucketClickedListener(new ImageBucketFragment.OnBucketClickListener() {
            @Override
            public void onBucketClick(int position) {
                switchFragment(imageBucketFragment, imageGridFragment);
                imageGridFragment.setImageBucket(imageBucketFragment.bucketList.get(position));
                imageGridFragment.setMaxImageCount(maxImageCount);
            }
        });
    }

    private void switchFragment(Fragment from, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(from).show(to);
        currentFragment = to;
        if (to == imageBucketFragment) {
            transaction.commit();
            menuFinish.setVisible(false);
        }
        if (to == imageGridFragment) {
            imageGridFragment.isFirstEnter = true;
            transaction.commit();
            menuFinish.setVisible(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == imageGridFragment) {
            switchFragment(imageGridFragment, imageBucketFragment);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image_browse, menu);
        menuFinish = menu.findItem(R.id.action_finish);
        menuFinish.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_finish:
                Intent intent = new Intent();
                Collection<String> paths = imageGridFragment.checkedMap.values();
                if (paths.size() > 0) {
                    String[] localImageUrls = new String[paths.size()];
                    int i = 0;
                    for (String path:paths) {
                        localImageUrls[i++] = path;
                    }
                    intent.putExtra("localImageUrls", localImageUrls);
                    setResult(0, intent);
                    finish();
                } else {
                    Toast.makeText(ImageBrowseActivity.this, "please select image", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
