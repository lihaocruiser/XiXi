package com.xixi.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.util.Image.BitmapUtil;

public class LocalImageShowActivity extends ActionBarActivity implements View.OnClickListener {

    ImageView imImage;

    String localImageUrl;

    //Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_show);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        localImageUrl = intent.getStringExtra("localImageUrl");

        imImage = (ImageView) findViewById(R.id.im_image);
        imImage.setOnClickListener(this);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        Bitmap bitmap = BitmapUtil.decodeScaledBitmap(localImageUrl, point.x, point.y, ImageView.ScaleType.CENTER_INSIDE);

        imImage.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
