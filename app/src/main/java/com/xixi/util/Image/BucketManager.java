package com.xixi.util.Image;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BucketManager {

    Context context;
    ContentResolver cr;

    HashMap<Integer, ImageBucket> bucketMap = new HashMap<>();
    List<ImageBucket> bucketList = new ArrayList<>();

    public BucketManager(Context context) {
        this.context = context;
        cr = context.getContentResolver();
    }

    public List<ImageBucket> buildBucket() {

        String projection[] = new String[] {
                Media._ID, Media.BUCKET_ID, Media.DATA, Media.DISPLAY_NAME,
                Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME };

        Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

        if (cursor.moveToFirst()) {
            int photoIDIndex = cursor.getColumnIndexOrThrow(Media._ID);
            int photoPathIndex = cursor.getColumnIndexOrThrow(Media.DATA);
            int photoNameIndex = cursor.getColumnIndexOrThrow(Media.DISPLAY_NAME);
            int photoTitleIndex = cursor.getColumnIndexOrThrow(Media.TITLE);
            int photoSizeIndex = cursor.getColumnIndexOrThrow(Media.SIZE);
            int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cursor.getColumnIndexOrThrow(Media.BUCKET_ID);
            int totalNum = cursor.getCount();

            do {
                int bucketId = cursor.getInt(bucketIdIndex);
                String bucketName = cursor.getString(bucketDisplayNameIndex);
                if (bucketName.equals("0") || bucketName.equals("1")) continue;

                int _id = cursor.getInt(photoIDIndex);
                String name = cursor.getString(photoNameIndex);
                String path = cursor.getString(photoPathIndex);
                String title = cursor.getString(photoTitleIndex);
                String size = cursor.getString(photoSizeIndex);

//                Log.i("TAG", _id + ", bucketId: " + bucketId + ", name:" + name + " path:" + path
//                        + " title: " + title + " size: " + size + " bucketName: " + bucketName);

                ImageBucket imageBucket = bucketMap.get(bucketId);
                if (imageBucket == null) {
                    imageBucket = new ImageBucket(bucketName);
                    imageBucket.bucketId = bucketId;
                    bucketMap.put(bucketId, imageBucket);
                }
                imageBucket.count++;
                ImageItem imageItem = new ImageItem();
                imageItem.imageId = _id;
                imageItem.imagePath = path;
                imageBucket.imageList.add(imageItem);

            } while (cursor.moveToNext());
        }

        // reverse to arrange the latest element to the front of the Lists
        Collection<ImageBucket> collection = bucketMap.values();
        for (ImageBucket imageBucket: collection) {
            Collections.reverse(imageBucket.imageList);
            bucketList.add(imageBucket);
        }
        Collections.reverse(bucketList);

        // for log only
        for (int i = 0; i < bucketList.size(); i++) {
            int id = bucketList.get(i).bucketId;
            String name = bucketList.get(i).bucketName;
            int size = bucketList.get(i).imageList.size();
            Log.i("TAG", id + "bucketName:" + name + " ,bucketSize:" + size);
        }

        return bucketList;
    }

}
