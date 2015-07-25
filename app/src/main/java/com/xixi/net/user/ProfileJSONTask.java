package com.xixi.net.user;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created on 2015-7-25.
 */
public class ProfileJSONTask extends JSONTask {

    public ProfileJSONTask(int userId, JSONReceiver receiver) {

        String url = RequestUrl.PROFILE;

        RequestParams params = new RequestParams();
        params.put("customerID", userId);

        init(url, params, receiver);

    }

}
