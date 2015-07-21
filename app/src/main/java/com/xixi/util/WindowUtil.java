package com.xixi.util;

import android.content.Context;
import android.graphics.Point;
import android.view.WindowManager;

/**
 * Created by 浩 on 2015-7-21.
 */
public class WindowUtil {

    private static int windowWidth;
    private static int windowHeight;

    public static void init(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        windowWidth = point.x;
        windowHeight = point.y;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

}
