package com.xixi.net.start;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class SchoolListJSONTask extends JSONTask {

    public SchoolListJSONTask(JSONReceiver receiver) {

        String url = RequestUrl.SCHOOL_LIST;

        RequestParams params = new RequestParams();

        init(url, params, receiver);
    }
}
