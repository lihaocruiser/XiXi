package com.xixi.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.xixi.R;

public class LocalImageShowActivity extends ActionBarActivity {

    ImageView imImage;

    String localImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_show);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        localImageUrl = intent.getStringExtra("localImageUrl");

        imImage = (ImageView) findViewById(R.id.im_image);
        Bitmap bitmap = BitmapFactory.decodeFile(localImageUrl);
        imImage.setImageBitmap(bitmap);

        imImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
