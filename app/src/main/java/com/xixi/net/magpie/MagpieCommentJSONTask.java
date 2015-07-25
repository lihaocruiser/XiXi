package com.xixi.net.magpie;

import com.loopj.android.http.RequestParams;
import com.xixi.net.base.JSONReceiver;
import com.xixi.net.base.JSONTask;
import com.xixi.net.base.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class MagpieCommentJSONTask extends JSONTask {

    public MagpieCommentJSONTask(int postID, int pageIndex, int pageSize, JSONReceiver receiver) {

        String url = RequestUrl.MAGPIE_COMMENT;

        RequestParams params = new RequestParams();
        params.put("postID", postID);
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);

        init(url, params, receiver);

    }

}
