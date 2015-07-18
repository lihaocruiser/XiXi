package com.xixi.bean.Circle;

import org.json.JSONObject;

/**
 * Created by lihao on 7/18/15.
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


}
