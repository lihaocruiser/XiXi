package com.xixi.net.me;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xixi.net.API;
import com.xixi.net.JSONReceiver;

import org.apache.http.Header;
import org.json.JSONObject;

public class ModifyProfileTask {

	private RequestParams params;

	private JSONReceiver receiver;

    private JsonHttpResponseHandler jsonHttpResponseHandler = new JsonHttpResponseHandler() {

        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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

	public ModifyProfileTask(RequestParams params, JSONReceiver receiver) {
		this.params = params;
		this.receiver = receiver;
	}
	
	public void execute() {
		String url = API.HOST + "customer/update";
		new AsyncHttpClient().post(url, params, jsonHttpResponseHandler );
	}

}