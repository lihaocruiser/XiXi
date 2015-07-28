package com.xixi.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.file.FileUtil;

import uk.co.senab.photoview.PhotoViewAttacher;

public class LocalImageShowActivity extends AppCompatActivity {

    ImageView imImage;
    PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_show);

        Intent intent = getIntent();
        String  url = intent.getStringExtra("url");
        String filePath = FileUtil.getFilePath(url);

        imImage = (ImageView) findViewById(R.id.im_image);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        Bitmap bitmap = BitmapUtil.decodeFileScaled(filePath, point.x, point.y, ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);

        imImage.setImageBitmap(bitmap);
        photoViewAttacher = new PhotoViewAttacher(imImage);
    }

}
