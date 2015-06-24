package com.xixi.bean;

import com.xixi.util.SafeJSON;

import org.json.JSONObject;

public class MagpieBean {

    private int id;
    private String title;
    private String content;
    private int likeCount;
    private int[] likeId;
    private String time;
    private String picUrl;

    private int userId;
    private String userName;
    private String userSex;
    private String userHeaderUrl;

    public MagpieBean() {}

    public MagpieBean(JSONObject obj) {

        JSONObject user = SafeJSON.getJSONObject(obj, "publisher", null);

        id = SafeJSON.getInt(obj, "id", -1);
        title = SafeJSON.getString(obj, "title", null);
        content = SafeJSON.getString(obj, "content", null);
        likeCount = SafeJSON.getInt(obj, "likeCount", 0);
        time = SafeJSON.getString(obj, "publishTime", null);

        if (user != null) {
            userId = SafeJSON.getInt(user, "id", 0);
            userName = SafeJSON.getString(user, "nickname", null);
            userSex = SafeJSON.getString(user, "sex", null);
            userHeaderUrl = SafeJSON.getString(user, "headPic", null);
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int[] getLikeId() {
        return likeId;
    }

    public String getTime() {
        return time;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserHeaderUrl() {
        return userHeaderUrl;
    }

}
