package com.xixi.bean.user;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created on 2015-7-25.
 */
public class MessageBean {

    private int id;
    private int senderId;
    private int receiverId;
    private String senderNickname;
    private String receiverNickname;
    private String content;

    public MessageBean() {}

    public MessageBean(JSONObject obj) {
        id = obj.optInt("customerID", 0);
        senderId = obj.optInt("senderId", 0);
        receiverId = obj.optInt("senderId", 0);
        senderNickname = obj.optString("senderNickname");
        receiverNickname = obj.optString("receiverNickname");
        content = obj.optString("content");
    }

    public static ArrayList<MessageBean> getBeanList(JSONArray array) {
        ArrayList<MessageBean> beanList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                MessageBean bean = new MessageBean(o);
                beanList.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }

    public static void appendBeanList(ArrayList<MessageBean> beanList, JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                MessageBean bean = new MessageBean(o);
                beanList.add(bean);
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

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public String getSenderNickname() {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
