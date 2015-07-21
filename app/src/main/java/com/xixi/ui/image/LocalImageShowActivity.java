package com.xixi.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.util.Image.BitmapUtil;

public class LocalImageShowActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imImage;

    String localImageUrl;

    //Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_show);

        Intent intent = getIntent();
        localImageUrl = intent.getStringExtra("localImageUrl");

        imImage = (ImageView) findViewById(R.id.im_image);
        imImage.setOnClickListener(this);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        // TODO
        Bitmap bitmap = BitmapUtil.decodeFileScaled(localImageUrl, point.x, point.y, ImageView.ScaleType.CENTER_INSIDE);

        imImage.setImageBitmap(bitmap);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
