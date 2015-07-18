package com.xixi.net.circle;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

/**
 * Created by lihao on 7/18/15.
 */
public class CircleListJSONTask extends JSONTask {

    public CircleListJSONTask(int customerID, int pageIndex, int pageSize, JSONReceiver receiver) {

        String url = RequestUrl.CIRCLE_LIST;

        RequestParams params = new RequestParams();
        params.put("customerID", customerID);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);

        init(url, params, receiver);
    }
}
