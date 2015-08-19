package com.xixi.bean.app;

import android.app.Application;

import com.xixi.util.WindowUtil;
import com.xixi.util.file.FileUtil;

/**
 * Created on 8/19/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FileUtil.init(getApplicationContext());
        WindowUtil.init(getApplicationContext());
    }

}
