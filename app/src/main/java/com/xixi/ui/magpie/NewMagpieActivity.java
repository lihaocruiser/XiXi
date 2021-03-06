package com.xixi.ui.magpie;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.xixi.R;
import com.xixi.net.base.JSONReceiver;
import com.xixi.ui.base.BaseActivity;
import com.xixi.util.Image.ImageUploader;
import com.xixi.net.magpie.SendMagpieJSONTask;
import com.xixi.ui.image.ImageBrowseActivity;
import com.xixi.ui.image.LocalImageShowActivity;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.dialog.ProgressDialog;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class NewMagpieActivity extends BaseActivity {

    EditText etTitle;
    EditText etBasic;
    EditText etHobby;
    EditText etCondition;
    Button btnAddPhoto;
    ImageView[] imSelected = new ImageView[3];

    MenuItem menuSend;

    ProgressDialog progressDialog;

    String[] localImageUrls;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_magpie);

        progressDialog = new ProgressDialog(this);

        etTitle = (EditText) findViewById(R.id.et_title);
        etBasic = (EditText) findViewById(R.id.et_basic);
        etHobby = (EditText) findViewById(R.id.et_hobby);
        etCondition = (EditText) findViewById(R.id.et_condition);

        btnAddPhoto = (Button) findViewById(R.id.btn_add_photo);

        imSelected[0] = (ImageView) findViewById(R.id.im1);
        imSelected[1] = (ImageView) findViewById(R.id.im2);
        imSelected[2] = (ImageView) findViewById(R.id.im3);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMagpieActivity.this, ImageBrowseActivity.class);
                intent.putExtra("maxImageCount", imSelected.length);
                startActivityForResult(intent, 0);
            }
        });
    }


    private void sendMagpie() {
        final int publisherID = 0;    // TODO get publisher ID
        final String title = etTitle.getText().toString();
        final String basic = etBasic.getText().toString();
        final String hobby = etHobby.getText().toString();
        final String condition = etCondition.getText().toString();
        final String content = "基本情况\n" + basic + "\n兴趣爱好\n" + hobby + "\n心动条件\n" + condition;
        if (title.equals("") || basic.equals("") || hobby.equals("") || condition.equals("")) {
            Toast.makeText(NewMagpieActivity.this, R.string.error_empty_content, Toast.LENGTH_SHORT).show();
            return;
        }

        // upload image
        if (localImageUrls != null && localImageUrls.length != 0) {
            List<String> urls = Arrays.asList(localImageUrls);
            final ImageUploader imageUploader = ImageUploader.getInstance();
            imageUploader.setOnUploadFinishListener(new ImageUploader.OnUploadFinishListener() {
                @Override
                public void onFailure() {
                    progressDialog.dismiss();
                    Toast.makeText(NewMagpieActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(List<String> receivedUrls) {
                    executeSendMagpieTask(publisherID, title, content, receivedUrls);
                }
            });
            progressDialog.show(R.string.txt_uploading);
            imageUploader.execute(urls);
        } else {
            executeSendMagpieTask(publisherID, title, content, null);
        }
    }

    private void executeSendMagpieTask(int publisherID, String title, String content, List<String> imageUrls) {
        StringBuilder stringBuilder = new StringBuilder(content);
        if (imageUrls != null) {
            for (String url : imageUrls) {
                stringBuilder.append("@|").append(url);
            }
        }
        progressDialog.show(R.string.txt_sending);
        new SendMagpieJSONTask(publisherID, title, stringBuilder.toString(), new JSONReceiver() {
            @Override
            public void onFailure(JSONObject obj) {
                progressDialog.dismiss();
                Toast.makeText(NewMagpieActivity.this, R.string.error_network, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(JSONObject obj) {
                progressDialog.dismiss();
                Toast.makeText(NewMagpieActivity.this, R.string.txt_send_successfully, Toast.LENGTH_SHORT).show();
                NewMagpieActivity.this.finish();
            }
        }).execute();
    }


    /**
     * get local image url from image browser and show image thumb
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null || requestCode != 0) {
            return;
        }
        localImageUrls = data.getStringArrayExtra("localImageUrls");
        for (int i = 0; i < imSelected.length; i++) {
            if (i < localImageUrls.length) {
                final String localImageUrl = localImageUrls[i];
                int width = imSelected[i].getWidth();
                int height = imSelected[i].getHeight();
                new BitmapTask(imSelected[i], localImageUrl, width, height).execute();
                imSelected[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NewMagpieActivity.this, LocalImageShowActivity.class);
                        intent.putExtra("localImageUrl", localImageUrl);
                        startActivity(intent);
                    }
                });
            } else {
                imSelected[i].setImageBitmap(null);
            }
        }
    }


    private class BitmapTask extends AsyncTask<Void, Void, Bitmap> {

        ImageView imageView;
        String localImageUrl;
        int width;
        int height;

        public BitmapTask(ImageView imageView, String localImageUrl, int width, int height) {
            this.imageView = imageView;
            this.localImageUrl = localImageUrl;
            this.width = width;
            this.height = height;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return BitmapUtil.decodeFileScaled(localImageUrl, width, height, ImageView.ScaleType.CENTER_CROP, BitmapUtil.Size.MIDDLE);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_magpie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.action_send:
                sendMagpie();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
