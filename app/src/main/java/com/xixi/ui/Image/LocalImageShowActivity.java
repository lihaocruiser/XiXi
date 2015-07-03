package com.xixi.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xixi.R;

public class LocalImageShowActivity extends ActionBarActivity implements View.OnClickListener {

    ImageView imImage;

    String localImageUrl;

    Bitmap bitmap;
    BitmapFactory.Options options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_show);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        localImageUrl = intent.getStringExtra("localImageUrl");

        imImage = (ImageView) findViewById(R.id.im_image);
        imImage.setOnClickListener(this);

        options = new BitmapFactory.Options();
        options.outWidth = 256;
        options.outHeight = 256;
        bitmap = BitmapFactory.decodeFile(localImageUrl, options);
        imImage.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        bitmap = null;
        imImage.setImageBitmap(null);
        finish();
    }
}
