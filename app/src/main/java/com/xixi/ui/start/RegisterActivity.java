package com.xixi.ui.start;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.xixi.R;
import com.xixi.net.base.JSONReceiver;
import com.xixi.ui.base.BaseActivityNoToolbar;
import com.xixi.util.Image.ImageUploader;
import com.xixi.net.start.RegisterJSONTask;
import com.xixi.net.start.SchoolListJSONTask;
import com.xixi.ui.image.ImageBrowseActivity;
import com.xixi.util.dialog.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class RegisterActivity extends BaseActivityNoToolbar {

    ImageView imHeader;
    RadioButton rbBoy;
    RadioButton rbGirl;
    Button btnSchool;
    EditText etNickname;
    EditText etAge;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordConfirm;
    Button btnRegister;
    Button btnLogin;

    ProgressDialog progressDialog;

    private String sex;
    private String nickname;
    private String age;
    private String school;
    private String email;
    private String password;
    private String localUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        imHeader = (ImageView) findViewById(R.id.im_header);
        rbBoy = (RadioButton) findViewById(R.id.rb_boy);
        rbGirl = (RadioButton) findViewById(R.id.rb_girl);
        etNickname = (EditText) findViewById(R.id.et_nickname);
        etAge = (EditText) findViewById(R.id.et_age);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPasswordConfirm = (EditText) findViewById(R.id.et_password_confirm);
        btnSchool = (Button) findViewById(R.id.btn_school);
        btnRegister = (Button) findViewById(R.id.btn_register);
        btnLogin = (Button) findViewById(R.id.btn_login);

        imHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, ImageBrowseActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show("loading school list");
                new SchoolListJSONTask(new JSONReceiver() {
                    @Override
                    public void onFailure(JSONObject obj) {
                        String[] s = new String[]{"中国科学技术大学","b"};  // for text only
                        selectSchool(s);                                    // for test only
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "request school list fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(JSONObject obj) {
                        JSONArray array = obj.optJSONArray("list");
                        int length = array.length();
                        String[] schools = new String[length];
                        String school;
                        for (int i = 0; i < length; i++) {
                            school = array.optString(i);
                            schools[i] = school;
                        }
                        selectSchool(schools);
                        progressDialog.dismiss();
                    }
                }).execute();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
//        nickname = etNickname.getText().toString();
//        age = etAge.getText().toString();
//        school = btnSchool.getText().toString();
//        email = etEmail.getText().toString();
//        password = etPassword.getText().toString();
//        String  passwordConfirm = etPasswordConfirm.getText().toString();
//        if (rbBoy.isChecked()) {
//            sex = "male";
//        } else if (rbGirl.isChecked()) {
//            sex = "female";
//        }
//        if (nickname.equals("") || age.equals("") || school.equals("") || email.equals("") || password.equals("") || sex == null) {
//            Toast.makeText(RegisterActivity.this, "empty content!", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!password.equals(passwordConfirm)) {
//            Toast.makeText(RegisterActivity.this, "password differs", Toast.LENGTH_SHORT).show();
//            return;
//        }

        // upload image
        if (localUrl == null) {
            executeRegisterTask(nickname, age, school, email, password, sex, null);
        } else {
            ImageUploader imageUploader = ImageUploader.getInstance();
            imageUploader.setOnUploadFinishListener(new ImageUploader.OnUploadFinishListener() {
                @Override
                public void onFailure() {
                    Toast.makeText(RegisterActivity.this, "upload image fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<String> receivedUrls) {
                    String headPic = receivedUrls.get(0);
                    executeRegisterTask(nickname, age, school, email, password, sex, headPic);
                }
            });
            progressDialog.show("uploading");
            imageUploader.execute(localUrl);
        }
    }

    private void executeRegisterTask(String nickname, String age, String school, String email,
                                     String password, String sex, String headPic) {
        progressDialog.show("uploading");
        new RegisterJSONTask(nickname, age, school, email, password, sex, headPic, new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "register fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, "register succeed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        }).execute();
    }


    private void selectSchool(final String[] schools) {
        new MaterialDialog.Builder(this).title("select school").items(schools).theme(Theme.LIGHT).itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
            @Override
            public boolean onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                btnSchool.setText(charSequence);
                return false;
            }
        }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || requestCode != 0) {
            return;
        }
        String[] urls = data.getStringArrayExtra("urls");
        if (urls.length != 0 && urls[0] != null) {
            localUrl = urls[0];
            Bitmap bitmap = BitmapFactory.decodeFile(localUrl);
            imHeader.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
