package com.xixi.bean.circle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LiHao on 2015-7-21.
 */
public class CircleCommentBean {

    private int id;

    private int userId;
    private String senderNickname;
    private String receiverNickname;
    private String comment;

    public CircleCommentBean() {}

    public CircleCommentBean(JSONObject obj) {
        if (obj == null) {
            return;
        }
        id = obj.optInt("id", 0);

        userId = obj.optInt("userId", 0);
        senderNickname = obj.optString("senderNickname");

        comment = obj.optString("comment");
    }


    public static ArrayList<CircleCommentBean> getBeanList(JSONArray array) {
        ArrayList<CircleCommentBean> beanList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                CircleCommentBean circleCommentBean = new CircleCommentBean(o);
                beanList.add(circleCommentBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }


    public int getId() {
        return id;
    }

    public void setId() {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String  getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickname) {
        this.receiverNickname = receiverNickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
