package com.xixi.net.user;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created on 2015-7-25.
 */
public class MessageJSONTask extends JSONTask {

    public MessageJSONTask(int userId, int pageIndex, int pageSize,JSONReceiver receiver) {

        String url = RequestUrl.PROFILE;

        RequestParams params = new RequestParams();
        params.put("customerID", userId);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);

        init(url, params, receiver);

    }

}
