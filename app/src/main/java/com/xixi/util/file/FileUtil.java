package com.xixi.util.file;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.SimpleTimeZone;

/**
 * Created on 7/19/15.
 */
public class FileUtil {

    private static String appPath;
    private static String imageFolder;
    private static File circleFile;

    public static void init(Context context) {
        String sdPath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            sdPath = context.getExternalCacheDir().getPath();
        } else {
            sdPath = context.getCacheDir().getPath();
        }

        appPath = sdPath + File.separator + "XiXi";
        imageFolder = appPath + File.separator + "Image";

        circleFile = makeDirectory(imageFolder);
    }

    private static File makeDirectory(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    public static String getImageFolder() {
        return imageFolder;
    }

    public static File getCircleFile() {
        return circleFile;
    }

    public static String getFileName(String url) {
        int index = url.lastIndexOf(File.separator);
        return url.substring(index + 1);
    }

    public static String getFilePath(String url) {
        return imageFolder + File.separator + getFileName(url);
    }

}
