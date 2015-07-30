package com.xixi.bean.user;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created on 2015-7-25.
 */
public class NotificationBean {

    public final static int MAGPIE = 0;
    public final static int CIRCLE = 1;
    public final static int PM = 2;

    private int id;
    private int senderId;
    private boolean isRead;
    private int type;
    private int contentId;
    private String senderNickname;
    private String content;


    public NotificationBean() {}

    public NotificationBean(JSONObject obj) {
        id = obj.optInt("customerID", 0);
        senderId = obj.optInt("senderId", 0);
        senderNickname = obj.optString("senderNickname");
        content = obj.optString("content");
    }

    public static ArrayList<NotificationBean> getBeanList(JSONArray array) {
        ArrayList<NotificationBean> beanList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                NotificationBean bean = new NotificationBean(o);
                beanList.add(bean);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return beanList;
    }

    public static void appendBeanList(ArrayList<NotificationBean> beanList, JSONArray array) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject o = (JSONObject) array.get(i);
                NotificationBean bean = new NotificationBean(o);
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

    public String getSenderNickname() {
        return senderNickname;
    }

    public void setSenderNickname(String senderNickname) {
        this.senderNickname = senderNickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }
}
