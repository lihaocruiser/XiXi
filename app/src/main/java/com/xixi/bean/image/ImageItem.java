package com.xixi.bean.image;

import java.io.Serializable;

public class ImageItem implements Serializable {
    public long imageId;
    public String thumbnailPath;
    public String imagePath;
    public boolean isSelected = false;
}
