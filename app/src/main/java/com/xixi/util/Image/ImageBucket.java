package com.xixi.util.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ImageBucket implements Serializable {

    public int count = 0;
    public int bucketId;
    public String bucketName;
    public List<ImageItem> imageList;

    public ImageBucket() {
        imageList = new ArrayList<ImageItem>();
    }

    public ImageBucket(String bucketName) {
        this.bucketName = bucketName;
        imageList = new ArrayList<>();
    }

}
