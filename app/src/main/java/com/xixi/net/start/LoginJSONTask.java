package com.xixi.net.start;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class LoginJSONTask extends JSONTask {

    public LoginJSONTask(String username, String password, JSONReceiver receiver) {

        String url = RequestUrl.LOGIN;

        RequestParams params = new RequestParams();
        params.put("username", username);
        params.put("password", password);

        init(url, params, receiver);
    }
}
