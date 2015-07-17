package com.xixi.net.magpie;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.xixi.net.RequestUrl;
import com.xixi.net.JSONReceiver;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class MagpieCommentTask {

	private RequestParams params;

	private JSONReceiver receiver;

	private AsyncHttpResponseHandler asyncHandler = new AsyncHttpResponseHandler() {

        @Override
        public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                              Throwable arg3) {
            Log.i(getClass().toString(), "onFailure");
            receiver.onFailure(null);
        }

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			Log.i(getClass().toString(), "onSuccess");
            if (arg2 == null) {
                receiver.onSuccess(null);
                return;
            }
			try {
                String str = new String(arg2);
                JSONObject obj = new JSONObject(str);
                receiver.onSuccess(obj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	};

	public MagpieCommentTask(RequestParams params, JSONReceiver receiver) {
		this.params = params;
		this.receiver = receiver;
	}
	
	public void execute() {
		String url = RequestUrl.HOST + "comment/commentlist";
		new AsyncHttpClient().post(url, params, asyncHandler );
	}

}