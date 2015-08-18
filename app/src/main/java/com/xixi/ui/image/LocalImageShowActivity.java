package com.xixi.ui.image;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xixi.R;
import com.xixi.ui.base.BaseActivityNoToolbar;
import com.xixi.util.Image.BitmapUtil;
import com.xixi.util.file.FileUtil;

import uk.co.senab.photoview.PhotoViewAttacher;

public class LocalImageShowActivity extends BaseActivityNoToolbar {

    private ImageView imImage;
    private PhotoViewAttacher photoViewAttacher;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_image_show);

        Intent intent = getIntent();
        filePath = intent.getStringExtra("localImageUrl");
        if (filePath == null) {
            filePath = FileUtil.getFilePath(intent.getStringExtra("imageUrl"));
        }

        imImage = (ImageView) findViewById(R.id.im_image);

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);

        Bitmap bitmap = BitmapUtil.decodeFileScaled(filePath, point.x, point.y,
                ImageView.ScaleType.CENTER_INSIDE, BitmapUtil.Size.FULL_SCREEN);

        imImage.setImageBitmap(bitmap);
        photoViewAttacher = new PhotoViewAttacher(imImage);
        photoViewAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float v, float v1) {
                finish();
            }
        });
    }

}
