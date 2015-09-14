package com.xixi.bean.circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LiHao on 7/18/15.
 */
public class CircleBean implements Serializable {

    private int id;
    private String content = "";
    private String time = "";
    private String pic = "";

    private int commentCount;
    private int likeCount;
    private List<Integer> likeIds = new LinkedList<>();

    private int publisherId;
    private String publisherNickname = "";
    private String publisherHeadPic = "";

    public CircleBean() {}

    public CircleBean(JSONObject obj) {

        if (obj == null) {
            return;
        }

        id = obj.optInt("id");
        content = obj.optString("content");
        time = obj.optString("publishTime");
        pic = obj.optString("pic");

        commentCount = obj.optInt("repliesCount");
        likeCount = obj.optInt("likeCount");

        String[] likeStr = obj.optString("likeIds").split(",");
        for (String str : likeStr) {
            likeIds.add(Integer.parseInt(str));
        }

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

    public void setId(int id) {
        this.id = id;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public List<Integer> getLikeIds() {
        return likeIds;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public String getPublisherNickname() {
        return publisherNickname;
    }

    public void setPublisherNickname(String publisherNickname) {
        this.publisherNickname = publisherNickname;
    }

    public String getPublisherHeadPic() {
        return publisherHeadPic;
    }

    public void setPublisherHeadPic(String publisherHeadPic) {
        this.publisherHeadPic = publisherHeadPic;
    }


}
