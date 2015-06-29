package com.xixi.ui.me;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.xixi.R;
import com.xixi.bean.ApplicationContext;
import com.xixi.net.JSONReceiver;
import com.xixi.net.image.ImageUploader;
import com.xixi.net.me.ModifyProfileTask;
import com.xixi.ui.imagebrowse.ImageBrowseActivity;
import com.xixi.util.dialog.ProgressDialogManager;
import com.xixi.util.dialog.TextAlertDialogManager;
import com.xixi.widget.CircleImageView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileActivity extends ActionBarActivity implements View.OnClickListener {

    LinearLayout llNickname;
    LinearLayout llAge;
    LinearLayout llSchool;
    LinearLayout llLabel;

    CircleImageView imHeader;
    TextView tvNickname;
    TextView tvAge;
    TextView tvLabel;

    TextAlertDialogManager textAlertDialogManager;
    ProgressDialogManager progressDialogManager;

    EditText etTextAlertDialog;

    int id;
    int age;
    String headerUrl;
    String nickname;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        id = 0;

        textAlertDialogManager = new TextAlertDialogManager(ProfileActivity.this);
        progressDialogManager = new ProgressDialogManager(ProfileActivity.this);

        llNickname = (LinearLayout) findViewById(R.id.ll_nickname);
        llAge = (LinearLayout) findViewById(R.id.ll_age);
        llSchool = (LinearLayout) findViewById(R.id.ll_school);
        llLabel = (LinearLayout) findViewById(R.id.ll_label);

        imHeader = (CircleImageView) findViewById(R.id.im_header);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvLabel = (TextView) findViewById(R.id.tv_label);

        loadFromApplicationContext();

        imHeader.setOnClickListener(this);
        llNickname.setOnClickListener(this);
        llAge.setOnClickListener(this);
        llSchool.setOnClickListener(this);
        llLabel.setOnClickListener(this);
    }

    private void loadFromApplicationContext() {
        ApplicationContext ac = ApplicationContext.getInstance(ProfileActivity.this);
        headerUrl = ac.getHeadPic();
        nickname = ac.getNickname();
        age = ac.getAge();
        label = ac.getLabel();

        if (headerUrl != null) {
            imHeader.setImageBitmap(BitmapFactory.decodeFile(headerUrl));
        }
        tvNickname.setText(nickname);
        tvAge.setText(age + "");
        tvLabel.setText(label);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.im_header:
                Intent intent = new Intent(ProfileActivity.this, ImageBrowseActivity.class);
                intent.putExtra("maxImageCount", 1);
                startActivityForResult(intent, 0);
                break;

            case R.id.ll_nickname:
                textAlertDialogManager.show(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nickname = textAlertDialogManager.getText();
                        modifyProfile();
                    }
                });
                break;

            case R.id.ll_age:
                textAlertDialogManager.show(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strAge = textAlertDialogManager.getText();
                        age = (strAge.equals("")) ? 0 : Integer.parseInt(strAge);
                        modifyProfile();
                    }
                }, InputType.TYPE_CLASS_NUMBER);
                break;

            case R.id.ll_label:
                textAlertDialogManager.show(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        label = textAlertDialogManager.getText();
                        modifyProfile();
                    }
                });
                break;
        }
    }


    /**
     * upload header image
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 0) {
            return;
        }
        String[] urls = data.getStringArrayExtra("urls");
        if (urls == null) return;
        String url;
        if (urls != null && urls.length != 0 && urls[0] != null) {
            url = urls[0];
            ImageUploader imageUploader = ImageUploader.getInstance();
            imageUploader.setOnUploadFinishListener(new ImageUploader.OnUploadFinishListener() {
                @Override
                public void onFailure() {
                    progressDialogManager.dismiss();
                    Toast.makeText(ProfileActivity.this, "uploading image fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(ArrayList<String> receivedUrls) {
                    headerUrl = receivedUrls.get(0);
                    modifyProfile();
                }
            });
            progressDialogManager.show("uploading image");
            imageUploader.execute(url);
        }
    }


    /**
     * upload profile
     */
    private void modifyProfile() {
        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("headPic", headerUrl);
        params.put("nickname", nickname);
        params.put("age", age);
        params.put("label", label);
        progressDialogManager.show("modifying profile");
        new ModifyProfileTask(params, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                progressDialogManager.dismiss();
                Toast.makeText(ProfileActivity.this, "modify profile fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                ApplicationContext ac = ApplicationContext.getInstance();
                ac.setHeadPic(headerUrl);
                ac.setNickName(nickname);
                ac.setAge(age);
                ac.setLabel(label);
                loadFromApplicationContext();
                progressDialogManager.dismiss();
            }
        }).execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
