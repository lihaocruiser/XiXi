package com.xixi.bean.user;

import org.json.JSONObject;

/**
 * Created on 2015-7-25.
 */
public class UserBean {

    private int userId;
    private int sex;
    private int age;
    private String avatar;
    private String nickname;
    private String school;
    private String label;

    public UserBean() {}

    public UserBean(JSONObject obj) {
        userId = obj.optInt("id", 0);
        sex = obj.optInt("sex", 0);
        age = obj.optInt("age", 0);
        avatar = obj.optString("headPic");
        nickname = obj.optString("nickname");
        school = obj.optString("school");
        label = obj.optString("label");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
