package com.xixi.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created on 2015-7-24.
 */
public class CommentBean {

    private int commentId;
    private int senderId;
    private String senderNickname;
    private String senderAvatar;
    private int receiverId;
    private String receiverNickname;
    private String comment;

    public CommentBean() {}

    public CommentBean(JSONObject obj) {
        if (obj == null) {
            return;
        }
        commentId = obj.optInt("id", 0);
        senderId = obj.optInt("userId", 0);
        senderNickname = obj.optString("senderNickname");
        senderAvatar = obj.optString("senderAvatar");
        receiverId = obj.optInt("receiverId");
        receiverNickname = obj.optString("receiverNickName");
        comment = obj.optString("comment");
    }

    public static ArrayList<CommentBean> getBeanList(JSONArray array) {
        ArrayList<CommentBean> beanList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                CommentBean commentBean = new CommentBean(o);
                beanList.add(commentBean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getSenderAvatar() {
        return senderAvatar;
    }

    public void setSenderAvatar(String senderAvatar) {
        this.senderAvatar = senderAvatar;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getReceiverNickname() {
        return receiverNickname;
    }

    public void setReceiverNickname(String receiverNickName) {
        this.receiverNickname = receiverNickName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
