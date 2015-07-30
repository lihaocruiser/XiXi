package com.xixi.ui.circle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.xixi.R;
import com.xixi.ui.image.ImageBrowseActivity;
import com.xixi.ui.image.LocalImageShowActivity;
import com.xixi.util.Image.BitmapUtil;

public class NewCircleActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText etContent;
    Button btnAddPhoto;
    ImageView[] imSelected = new ImageView[3];

    String[] localImageUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_circle);
        // init layout_toolbar

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etContent = (EditText) findViewById(R.id.et_content);

        btnAddPhoto = (Button) findViewById(R.id.btn_add_photo);

        imSelected[0] = (ImageView) findViewById(R.id.im1);
        imSelected[1] = (ImageView) findViewById(R.id.im2);
        imSelected[2] = (ImageView) findViewById(R.id.im3);

        btnAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewCircleActivity.this, ImageBrowseActivity.class);
                intent.putExtra("maxImageCount", imSelected.length);
                startActivityForResult(intent, 0);
            }
        });

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
                        Intent intent = new Intent(NewCircleActivity.this, LocalImageShowActivity.class);
                        intent.putExtra("localImageUrl", localImageUrl);
                        startActivity(intent);
                    }
                });
            } else {
                imSelected[i].setImageBitmap(null);
            }
        }
    }


    /**
     * get local image url from image browser and show image thumb
     */
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
        getMenuInflater().inflate(R.menu.menu_new_circle, menu);
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
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
