package com.xixi.ui.magpie;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xixi.R;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageUploader;
import com.xixi.net.magpie.MagpieSendTask;
import com.xixi.ui.image.ImageBucketFragment;
import com.xixi.ui.image.ImageGridFragment;
import com.xixi.util.DialogManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NewMagpieActivity extends ActionBarActivity {

    NewMagpieFragment newMagpieFragment;
    ImageBucketFragment imageBucketFragment;
    ImageGridFragment imageGridFragment;
    Fragment currentFragment;

    MenuItem menuSend;
    MenuItem menuFinish;

    DialogManager dialogManager;

    HashMap<Long, String> checkedMap = new HashMap<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_magpie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogManager = new DialogManager(this);

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


    private void sendMagpie() {
        final int publisherID = 0;    // TODO get publisher ID
        final String title = newMagpieFragment.etTitle.getText().toString();
        final String basic = newMagpieFragment.etBasic.getText().toString();
        final String hobby = newMagpieFragment.etHobby.getText().toString();
        final String condition = newMagpieFragment.etCondition.getText().toString();
        final String content = "基本情况\n" + basic + "\n兴趣爱好\n" + hobby + "\n心动条件\n" + condition;
        if (title.equals("") || basic.equals("") || hobby.equals("") || condition.equals("")) {
            Toast.makeText(NewMagpieActivity.this, "empty content!", Toast.LENGTH_SHORT).show();
            return;
        }

        // upload image
        ArrayList<String> localUrls = new ArrayList<>(checkedMap.values());
        if (localUrls.size() == 0) {
            sendMagpieContent(publisherID, title, content, null);
        } else {
            ImageUploader imageUploader = ImageUploader.getInstance();
            imageUploader.setOnUploadFinishListener(new ImageUploader.OnUploadFinishListener() {
                @Override
                public void onFailure() {
                    Toast.makeText(NewMagpieActivity.this, "upload image fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ArrayList<String> receivedUrls) {
                    sendMagpieContent(publisherID, title, content, receivedUrls);
                }
            });
            dialogManager.showProgressDialog("uploading");
            imageUploader.execute(localUrls);
        }
    }

    private void sendMagpieContent(int publisherID, String title, String content, ArrayList<String> imageUrls) {
        StringBuilder stringBuilder = new StringBuilder(content);
        if (imageUrls != null) {
            for (String url : imageUrls) {
                stringBuilder.append("@|").append(url);
            }
        }
        RequestParams params = new RequestParams();
        params.put("publisherID", publisherID);
        params.put("title", title);
        params.put("content", stringBuilder);
        dialogManager.showProgressDialog("uploading");
        new MagpieSendTask(params, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                dialogManager.dismissProgressDialog();
                Toast.makeText(NewMagpieActivity.this, "sending fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                dialogManager.dismissProgressDialog();
                Toast.makeText(NewMagpieActivity.this, "sending succeed", Toast.LENGTH_SHORT).show();
                NewMagpieActivity.this.finish();
            }
        }).execute();
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

}
