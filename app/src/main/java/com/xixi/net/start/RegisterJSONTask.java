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
        params.put("nickname", nickname);
        params.put("age", age);
        params.put("school", school);
        params.put("email", email);
        params.put("password", password);
        params.put("sex", sex);
        params.put("headPic", headPic);

        init(url, params, receiver);

    }
}
