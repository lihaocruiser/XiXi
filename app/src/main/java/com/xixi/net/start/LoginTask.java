package com.xixi.net.start;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.xixi.net.API;
import com.xixi.util.JSONReceiver;
import com.loopj.android.http.*;

public class LoginTask {
	
	private RequestParams params;
	
	private JSONReceiver jsonReceiver;
	
	private AsyncHttpResponseHandler asyncHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            Log.i(getClass().toString(), "onFailure");
            jsonReceiver.onFailure(null);
        }

        @Override
        public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
            Log.i(getClass().toString(), "onSuccess");
            if (arg2 == null) {
                jsonReceiver.onSuccess(null);
                return;
            }
            try {
                String str = new String(arg2);
                JSONObject obj = new JSONObject(str);
                jsonReceiver.onSuccess(obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

	};

	public LoginTask(RequestParams params, JSONReceiver jsonReceiver) {
		this.params = params;
		this.jsonReceiver = jsonReceiver;
	}
	
	public void execute() {
		String url = API.HOST + "customer/login";
		new AsyncHttpClient().post(url, params, asyncHandler );
	}

}