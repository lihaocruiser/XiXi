package com.xixi.bean.magpie;

import com.xixi.bean.circle.ReplyBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created on 2015-7-26.
 */
public class MagpieCommentBean {

    private int commentId;
    private int floor;
    private String senderId;
    private String senderSex;
    private String senderNickname;
    private String senderAvatar;
    private String content;
    private ArrayList<ReplyBean> replyList;

    public MagpieCommentBean() {}

    public MagpieCommentBean(JSONObject obj) {
        if (obj == null) {
            return;
        }
        commentId = obj.optInt("id");
        floor = obj.optInt("floor");
        senderId = obj.optString("senderId");
        senderNickname = obj.optString("senderNickname");
        senderAvatar = obj.optString("");
        content = obj.optString("");
        JSONArray array = obj.optJSONArray("");
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderSex() {
        return senderSex;
    }

    public void setSenderSex(String senderSex) {
        this.senderSex = senderSex;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<ReplyBean> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<ReplyBean> replyList) {
        this.replyList = replyList;
    }
}
