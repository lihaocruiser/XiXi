package com.xixi.bean.magpie;

import com.xixi.util.SafeJSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MagpieBean implements Serializable {

    private int id;
    private String title;
    private String content;
    private int likeCount;
    private int[] likeId;

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    private int commentCount;
    private String time;

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    private String picUrl;

    private int userId;
    private String userName;
    private String userSex;
    private String userHeaderUrl;

    public MagpieBean() {}

    public MagpieBean(JSONObject obj) {

        if (obj == null) {
            return;
        }

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

    /**
     * get an ArrayList of CircleBean from JSONArray
     */
    public static ArrayList<MagpieBean> getBeanList(JSONArray array ) {
        ArrayList<MagpieBean> beanList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                MagpieBean circleBean = new MagpieBean(o);
                beanList.add(circleBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }

    /**
     * get an ArrayList of CircleBean from JSONArray and append it to an existed ArrayList
     */
    public static void appendBeanList(ArrayList<MagpieBean> beanList, JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                MagpieBean circleBean = new MagpieBean(o);
                beanList.add(circleBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getUserHeaderUrl() {
        return userHeaderUrl;
    }

    public void setUserHeaderUrl(String userHeaderUrl) {
        this.userHeaderUrl = userHeaderUrl;
    }

}
