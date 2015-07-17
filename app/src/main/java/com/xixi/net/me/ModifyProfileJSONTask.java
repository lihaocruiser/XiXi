package com.xixi.net.me;

import com.loopj.android.http.RequestParams;
import com.xixi.net.RequestUrl;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;

/**
 * Created on 2015-7-16.
 */
public class ModifyProfileJSONTask extends JSONTask {

    public ModifyProfileJSONTask(int id, int age, String headPic, String nickname, String label, JSONReceiver receiver) {

        String url = RequestUrl.MODIFY_PROFILE;

        RequestParams params = new RequestParams();
        params.put("id", id);
        params.put("age", age);
        params.put("headPic", headPic);
        params.put("nickname", nickname);
        params.put("label", label);

        init(url, params, receiver);

    }

}
