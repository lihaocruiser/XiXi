package com.xixi.net.magpie;

import com.loopj.android.http.RequestParams;
import com.xixi.net.JSONReceiver;
import com.xixi.net.JSONTask;
import com.xixi.net.RequestUrl;

/**
 * Created by lihao on 7/17/15.
 */
public class MagpieListJSONTask extends JSONTask {

    public MagpieListJSONTask(int pageIndex, int pageSize, JSONReceiver receiver) {

        String url = RequestUrl.MAGPIE_LIST;

        RequestParams params = new RequestParams();
        params.put("pageIndex", pageIndex);
        params.put("pageSize", pageSize);

        init(url, params, receiver);

    }

}
