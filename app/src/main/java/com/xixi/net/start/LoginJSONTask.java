package com.xixi.net.start;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class LoginJSONTask extends JSONTask {

    public LoginJSONTask(String email, String password, JSONReceiver receiver) {

        String url = RequestUrl.LOGIN;

        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("password", password);

        init(url, params, receiver);
    }
}
