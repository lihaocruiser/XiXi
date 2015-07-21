package com.xixi.net;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * 2015-7-16 20:16:44
 * Base class POST requests that return a JSONObject
 * Usage: extend this class, call init() to initialize parameters
 * Client: call constructor and execute()
 */

public class JSONTask {

    String url;

	private RequestParams params;

	private JSONReceiver receiver;

    private JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {

        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            Log.i(getClass().toString(), "onFailure");
            receiver.onFailure(null);
        }

        public void onFailure(int statueCode, Header[] headers, Throwable throwable, JSONObject response) {
            Log.i(getClass().toString(), "onFailure");
            receiver.onFailure(null);
        }

        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            Log.i(getClass().toString(), "onSuccess");
            if (response == null) {
                receiver.onFailure(null);
                return;
            }
            int checked = response.optInt("checked", 1);
            if (checked == 0) {
                receiver.onSuccess(response);
            } else {
                receiver.onFailure(null);
            }
        }

    };

	protected void init(String url, RequestParams params, JSONReceiver receiver) {
        this.url = url;
		this.params = params;
		this.receiver = receiver;
	}

    public void execute() {
        String url = RequestUrl.HOST + this.url;
        new AsyncHttpClient().post(url, params, jsonHttpResponseHandler);
    }

}