package com.xixi.net.circle;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

/**
 * Created by LiHao on 2015-7-21.
 */
public class CircleJSONTask extends JSONTask {

    public CircleJSONTask(int circleId, JSONReceiver receiver) {

        String url = RequestUrl.CIRCLE;

        RequestParams params = new RequestParams();
        params.put("xqzID", circleId);

        init(url, params, receiver);
    }

}
