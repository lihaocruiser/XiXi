package com.xixi.net.magpie;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class SendReplyMagpieJSONTask extends JSONTask {

    public SendReplyMagpieJSONTask(int postID, String content, int parentReplierID, int replierID, JSONReceiver receiver) {

        String url = RequestUrl.SEND_REPLY_MAGPIE;

        RequestParams params = new RequestParams();
        params.put("postID", postID);
        params.put("content", content);
        params.put("parentReplierID", parentReplierID);
        params.put("replierID", replierID);

        init(url, params, receiver);

    }
}
