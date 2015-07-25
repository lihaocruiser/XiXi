package com.xixi.net.magpie;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class MagpieJSONTask extends JSONTask {

    public MagpieJSONTask(int id, JSONReceiver receiver) {

        String url = RequestUrl.MAGPIE;

        RequestParams params = new RequestParams();
        params.put("id", id);

        init(url, params, receiver);
    }

}
