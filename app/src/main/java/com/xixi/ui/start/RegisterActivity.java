package com.xixi.ui.start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xixi.R;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageUploader;
import com.xixi.net.start.RegisterTask;
import com.xixi.ui.image.ImageBucketFragment;
import com.xixi.ui.image.ImageGridFragment;
import com.xixi.util.dialog.ProgressDialogManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class RegisterActivity extends ActionBarActivity {

    RegisterFragment registerFragment;
    ImageBucketFragment imageBucketFragment;
    ImageGridFragment imageGridFragment;
    Fragment currentFragment;

    MenuItem menuFinish;

    ProgressDialogManager dialogManager;

    HashMap<Long, String> checkedMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialogManager = new ProgressDialogManager(this);

        registerFragment = new RegisterFragment();
        imageBucketFragment = new ImageBucketFragment();
        imageGridFragment = new ImageGridFragment();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, registerFragment)
                .add(R.id.fragment_container, imageBucketFragment)
                .add(R.id.fragment_container, imageGridFragment)
                .show(registerFragment)
                .commit();
        currentFragment = registerFragment;
        registerFragment.setOnAddPhotoListener(new RegisterFragment.OnAddPhotoListener() {
            @Override
            public void onAddPhoto() {
                switchFragment(registerFragment, imageBucketFragment);
            }
        });
        registerFragment.setOnRegisterListener(new RegisterFragment.OnRegisterListener() {
            @Override
            public void onRegister() {
                register();
            }
        });

        imageBucketFragment.setOnBucketClickedListener(new ImageBucketFragment.OnBucketClickListener() {
            @Override
            public void onBucketClick(int position) {
                switchFragment(imageBucketFragment, imageGridFragment);
                imageGridFragment.setImageBucket(imageBucketFragment.bucketList.get(position));
                imageGridFragment.setMaxImageCount(1);
            }
        });
    }

    private void switchFragment(Fragment from, Fragment to) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(from).show(to);
        currentFragment = to;
        if (to == registerFragment) {
            transaction.disallowAddToBackStack().commit();
            menuFinish.setVisible(false);
        } else if (to == imageBucketFragment) {
            transaction.addToBackStack(null).commit();
            menuFinish.setVisible(false);
        } else if (to == imageGridFragment) {
            transaction.commit();
            menuFinish.setVisible(true);
        }
    }

    private String sex;
    private String nickname;
    private String age;
    private String school;
    private String email;
    private String password;
    private String passwordConfirm;

    private void register() {
        nickname = registerFragment.etNickname.getText().toString();
        age = registerFragment.etAge.getText().toString();
        school = registerFragment.btnSchool.getText().toString();
        email = registerFragment.etEmail.getText().toString();
        password = registerFragment.etPassword.getText().toString();
        passwordConfirm = registerFragment.etPasswordConfirm.getText().toString();
        if (registerFragment.rbBoy.isChecked()) {
            sex = "male";
        } else if (registerFragment.rbGirl.isChecked()) {
            sex = "female";
        }
        if (nickname.equals("") || age.equals("") || school.equals("") || email.equals("") || password.equals("") || sex == null) {
            Toast.makeText(RegisterActivity.this, "empty content!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(passwordConfirm)) {
            Toast.makeText(RegisterActivity.this, "password differs", Toast.LENGTH_SHORT).show();
            return;
        }

        // upload image
        final ArrayList<String> localUrls = new ArrayList<>(checkedMap.values());
        if (localUrls.size() == 0) {
            executeRegisterTask(nickname, age, school, email, password, sex, null);
        } else {
            ImageUploader imageUploader = ImageUploader.getInstance();
            imageUploader.setOnUploadFinishListener(new ImageUploader.OnUploadFinishListener() {
                @Override
                public void onFailure() {
                    Toast.makeText(RegisterActivity.this, "upload image fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ArrayList<String> receivedUrls) {
                    String headPic = receivedUrls.get(0);
                    executeRegisterTask(nickname, age, school, email, password, sex, headPic);
                }
            });
            dialogManager.show("uploading");
            imageUploader.execute(localUrls);
        }
    }

    private void executeRegisterTask(String nickname, String age, String school, String email,
                                     String password, String sex, String headPic) {
        RequestParams params = new RequestParams();
        params.put("nickname", nickname);
        params.put("age", age);
        params.put("school", school);
        params.put("email", email);
        params.put("password", password);
        params.put("sex", sex);
        params.put("headPic", headPic);
        dialogManager.show("uploading");
        new RegisterTask(params, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                dialogManager.dismiss();
                Toast.makeText(RegisterActivity.this, "register fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                dialogManager.dismiss();
                Toast.makeText(RegisterActivity.this, "register succeed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        }).execute();
    }


    @Override
    public void onBackPressed() {
        if (currentFragment == registerFragment) {
            super.onBackPressed();
        } else {
            switchFragment(currentFragment, registerFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_register, menu);
        menuFinish = menu.findItem(R.id.action_finish);
        menuFinish.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                if (currentFragment == registerFragment) {
                    finish();
                } else {
                    switchFragment(currentFragment, registerFragment);
                }
                return true;

            case R.id.action_finish:
                switchFragment(currentFragment, registerFragment);
                checkedMap = imageGridFragment.checkedMap;
                Collection<String> paths = checkedMap.values();
                for (String path:paths) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    registerFragment.imHeader.setImageBitmap(bitmap);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
