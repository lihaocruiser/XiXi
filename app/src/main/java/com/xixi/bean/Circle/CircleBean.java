package com.xixi.bean.Circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LiHao on 7/18/15.
 */
public class CircleBean {

    private int id;
    private String content;
    private String time;

    private int repliesCount;
    private int likeCount;

    private int publisherId;
    private String publisherNickname;
    private String publisherHeadPic;

    public CircleBean() {}

    public CircleBean(JSONObject obj) {

        if (obj == null) {
            return;
        }

        id = obj.optInt("id");
        content = obj.optString("content");
        time = obj.optString("publishTime");

        repliesCount = obj.optInt("repliesCount");
        likeCount = obj.optInt("likeCount");

        JSONObject publisher = obj.optJSONObject("publisher");
        if (publisher != null) {
            publisherId = publisher.optInt("id");
            publisherNickname = publisher.optString("nickname");
            publisherHeadPic = publisher.optString("headPic");
        }
    }

    /**
     * get an ArrayList of CircleBean from JSONArray
     */
    public static ArrayList<CircleBean> getBeanList(JSONArray array ) {
        ArrayList<CircleBean> beanList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                CircleBean circleBean = new CircleBean(o);
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
    public static void appendBeanList(ArrayList<CircleBean> beanList, JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                CircleBean circleBean = new CircleBean(o);
                beanList.add(circleBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPublisherNickname() {
        return publisherNickname;
    }

    public String getPublisherHeadPic() {
        return publisherHeadPic;
    }

    public void setPublisherHeadPic(String publisherHeadPic) {
        this.publisherHeadPic = publisherHeadPic;
    }


}
