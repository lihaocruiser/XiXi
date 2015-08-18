package com.xixi.net.start;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class RegisterJSONTask extends JSONTask {

    public RegisterJSONTask(String nickname, String age, String school, String email, String password,
                            String sex, String headPic, JSONReceiver receiver) {

        String url = RequestUrl.REGISTER;

        RequestParams params = new RequestParams();
        params.put("username", "lihaooo");
        params.put("password", "123");
        params.put("email", "a@qq.com");
        params.put("nickname", "某浩的马甲");

        init(url, params, receiver);

    }
}
