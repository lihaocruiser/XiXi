package com.xixi.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xixi.R;
import com.xixi.bean.ApplicationContext;
import com.xixi.bean.user.UserBean;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.user.ProfileJSONTask;
import com.xixi.ui.base.BaseActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.Image.ImageDownloader;
import com.xixi.util.Image.ImageUploader;
import com.xixi.net.user.ModifyProfileJSONTask;
import com.xixi.ui.image.ImageBrowseActivity;
import com.xixi.util.dialog.ProgressDialog;
import com.xixi.widget.CircleImageView;

import org.json.JSONObject;

import java.util.List;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    // test
    String base = "http://home.ustc.edu.cn/~lihao90/android/";

    LinearLayout llAvatar;
    LinearLayout llNickname;
    LinearLayout llSex;
    LinearLayout llAge;
    LinearLayout llSchool;
    LinearLayout llLabel;

    CircleImageView imAvatar;
    TextView tvNickname;
    TextView tvSex;
    TextView tvAge;
    TextView tvSchool;
    TextView tvLabel;

    ImageDownloader imageDownloader;
    ApplicationContext ac;
    ProgressDialog progressDialog;

    int userId;
    int age;
    String avatar;
    String nickname;
    String sex;
    String school;
    String label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = getIntent().getIntExtra("userId", 0);

        progressDialog = new ProgressDialog(this);

        llAvatar = (LinearLayout) findViewById(R.id.ll_avatar);
        llNickname = (LinearLayout) findViewById(R.id.ll_nickname);
        llSex = (LinearLayout) findViewById(R.id.ll_sex);
        llAge = (LinearLayout) findViewById(R.id.ll_age);
        llSchool = (LinearLayout) findViewById(R.id.ll_school);
        llLabel = (LinearLayout) findViewById(R.id.ll_label);

        imAvatar = (CircleImageView) findViewById(R.id.im_avatar);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvLabel = (TextView) findViewById(R.id.tv_label);

        new ProfileJSONTask(userId, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                avatar = base + "1.jpg";
                nickname = "nickname";
                age = 0;
                school = "USTC";
                label = "DOTA2";
                initView();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                UserBean userBean = new UserBean(obj);
                avatar = userBean.getAvatar();
                nickname = userBean.getNickname();
                age = userBean.getAge();
                school = userBean.getSchool();
                label = userBean.getLabel();
                initView();
            }
        }).execute();

    }

    private void initView() {

        imageDownloader = new ImageDownloader();

        imageDownloader.setBitmap(avatar, imAvatar, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.MIDDLE);
        tvNickname.setText(nickname);
        tvSex.setText(sex);
        tvAge.setText(age + "");
        tvSchool.setText(school);
        tvLabel.setText(label);

        imAvatar.setOnClickListener(this);
        llNickname.setOnClickListener(this);
        llSex.setOnClickListener(this);
        llAge.setOnClickListener(this);
        llSchool.setOnClickListener(this);
        llLabel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.im_avatar:
                Intent intent = new Intent(ProfileActivity.this, ImageBrowseActivity.class);
                intent.putExtra("maxImageCount", 1);
                startActivityForResult(intent, 0);
                break;

            case R.id.ll_nickname:
                new MaterialDialog.Builder(this).title(R.string.txt_nickname).input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        if (charSequence.equals("")) {
                            Toast.makeText(ProfileActivity.this, "can't be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            modifyProfile();
                        }
                    }
                });
                break;

            case R.id.ll_age:
                new MaterialDialog.Builder(this).title(R.string.txt_nickname).inputType(InputType.TYPE_CLASS_NUMBER).input(null, null, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                    if (charSequence.equals("")) {
                        Toast.makeText(ProfileActivity.this, "can't be empty", Toast.LENGTH_SHORT).show();
                    } else {
                        modifyProfile();
                    }
                }
            });
                break;

            case R.id.ll_label:
                new MaterialDialog.Builder(this).title(R.string.txt_nickname).input(null, null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {
                        if (charSequence.equals("")) {
                            Toast.makeText(ProfileActivity.this, "can't be empty", Toast.LENGTH_SHORT).show();
                        } else {
                            modifyProfile();
                        }
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
        if (data == null || requestCode != 0) {
            return;
        }
        String[] urls = data.getStringArrayExtra("localImageUrls");
        String url;
        if (urls.length != 0 && urls[0] != null) {
            url = urls[0];
            ImageUploader imageUploader = ImageUploader.getInstance();
            imageUploader.setOnUploadFinishListener(new ImageUploader.OnUploadFinishListener() {
                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, "uploading image fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<String> receivedUrls) {
                    avatar = receivedUrls.get(0);
                    modifyProfile();
                }
            });
            progressDialog.show("uploading image");
            imageUploader.execute(url);
        }
    }


    /**
     * upload profile
     */
    private void modifyProfile() {

        progressDialog.show("modifying profile");

        new ModifyProfileJSONTask(userId, age, avatar, nickname, label, new JSONReceiver() {

            @Override
            public void onFailure(JSONObject obj) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "modify profile fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                ApplicationContext ac = ApplicationContext.getInstance();
                ac.setHeadPic(avatar);
                ac.setNickName(nickname);
                ac.setAge(age);
                ac.setLabel(label);
                initView();
                progressDialog.dismiss();
            }
        }).execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        MenuItem menuPrivateMessage = menu.findItem(R.id.action_private_message);
        if (userId != ApplicationContext.getInstance().getUserId()) {
            menuPrivateMessage.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_private_message:
                Intent intent = new Intent(ProfileActivity.this, NotificationActivity.class);
//                intent.putExtra("receiverId", userId);
                intent.putExtra("receiverId", 1);
                intent.putExtra("receiverNickname", nickname);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
