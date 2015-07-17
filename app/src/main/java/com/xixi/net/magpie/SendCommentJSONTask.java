package com.xixi.net.magpie;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class SendCommentJSONTask extends JSONTask {

    public SendCommentJSONTask(int postID, String  content, int publisherID, JSONReceiver receiver) {

        String url = RequestUrl.SEND_COMMENT_MAGPIE;

        RequestParams params = new RequestParams();
        params.put("postID", postID);
        params.put("content", content);
        params.put("publisherID", publisherID);

        init(url, params, receiver);

    }
}
