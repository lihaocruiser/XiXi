package com.xixi.bean;

import com.xixi.util.SafeJSON;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class MagpieTitleBean {

    public int id;
    public String title;
    public String url;

    public MagpieTitleBean() {

    }

    public MagpieTitleBean(int id, String title, String url) {
        this.id = id;
        this.title = title;
        this.url = url;
    }

    public MagpieTitleBean(JSONObject obj) {

        id = SafeJSON.getInt(obj, "id", -1);

        title = SafeJSON.getString(obj, "title", null);

        JSONObject publisher = SafeJSON.getJSONObject(obj, "publisher", null);
        if (publisher != null) {
            url = SafeJSON.getString(publisher, "headPic", null);
        }
    }

    public boolean isInList(List<MagpieTitleBean> list) {

        for (MagpieTitleBean item : list) {
            if (id == item.id) {
                return true;
            }
        }
        return false;
    }

}
