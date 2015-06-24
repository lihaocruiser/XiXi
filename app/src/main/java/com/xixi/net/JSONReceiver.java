package com.xixi.net;

import org.json.JSONObject;

public interface JSONReceiver {

    public void onFailure(JSONObject obj);

    public void onSuccess(JSONObject obj);

}
