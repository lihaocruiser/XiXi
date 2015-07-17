package com.xixi.net.magpie;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class SendMagpieJSONTask extends JSONTask {

    public SendMagpieJSONTask(int publisherID, String title, String content, JSONReceiver receiver) {

        String url = RequestUrl.SEND_MAGPIE;

        RequestParams params = new RequestParams();
        params.put("publisherID", publisherID);
        params.put("title", title);
        params.put("content", content);

        init(url, params, receiver);

    }

}
