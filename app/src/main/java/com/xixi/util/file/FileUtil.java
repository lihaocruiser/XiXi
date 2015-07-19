package com.xixi.util.file;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by LiHao on 7/19/15.
 */
public class FileUtil {

    private static String appPath;
    private static String circlePath;
    private static File circleFile;
    private static String url;

    public static void init(Context context) {
        String sdPath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            sdPath = context.getExternalCacheDir().getPath();
        } else {
            sdPath = context.getCacheDir().getPath();
        }

        appPath = sdPath + File.separator + "XiXi";
        circlePath = appPath + File.separator + "Circle";

        circleFile = makeDirectory(circlePath);
    }

    private static File makeDirectory(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }


    public static String getCirclePath() {
        return circlePath;
    }

    public static File getCircleFile() {
        return circleFile;
    }

    public static String getFileName(String url) {
        int index = url.lastIndexOf(File.separator);
        return url.substring(index + 1);
    }

}
