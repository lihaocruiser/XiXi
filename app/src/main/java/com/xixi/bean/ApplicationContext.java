package com.xixi.bean;

import org.json.JSONObject;

import com.xixi.util.SafeJSON;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ApplicationContext {

	private static Context context;
	private static SharedPreferences sp;
	private static ApplicationContext instance;

	private final static String SP_NAME = "mySharedPreferences";
	private final static String USER_TOKEN = "token";
	private final static String USER_EMAIL = "email";
	private final static String USER_PASSWORD = "password";
	private final static String USER_CHECKED = "checked";

    private final static String USER_AGE = "age";
    private final static String USER_HEAD_PIC = "headPic";
    private final static String USER_LABEL = "label";
    private final static String USER_NICK_NAME = "nickname";
    private final static String USER_SEX = "sex";
    private final static String USER_SCHOOL = "school";

	public static int age;
	public static String headPic;
	public static String label;
	public static String nickname;
	public static String sex;
	public static String school;
		
	public static void setContext(Context context) {
		ApplicationContext.context = context;
	}

    public static ApplicationContext getInstance(Context context) {
        setContext(context);
        return ApplicationContext.getInstance();
    }

	public static ApplicationContext getInstance() {
		if (instance == null) {
			instance = new ApplicationContext();
		}
		return instance;
	}

	private SharedPreferences getSP() {
		if (sp == null) {
			try {
				sp = context.getSharedPreferences(
						SP_NAME, Context.MODE_PRIVATE);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return sp;
	}

	public void saveUserInfo(JSONObject obj) {
		age	= SafeJSON.getInt(obj, "age", -1);
		headPic = SafeJSON.getString(obj, "headPic", null);
		label = SafeJSON.getString(obj, "label", null);
		nickname = SafeJSON.getString(obj, "nickname", null);
		sex = SafeJSON.getString(obj, "sex", null);
		school = SafeJSON.getString(obj, "school", null);
        setAge(age);
        setHeadPic(headPic);
        setLabel(label);
        setNickName(nickname);
        setSex(sex);
        setSchool(school);
	}

	public void clearSP() {
		Editor editor = getSP().edit().clear();
		editor.apply();
	}
	
	public String getToken() {
		return getSP().getString(USER_TOKEN, null);
	}
	
	public void setToken(String token) {
		Editor editor = getSP().edit().putString(USER_TOKEN, token);
		editor.apply();
	}
	
	public String getEmail() {
		return getSP().getString(USER_EMAIL, null);
	}
	
	public void setEmail(String email) {
		Editor editor = getSP().edit().putString(USER_EMAIL, email);
		editor.apply();
	}
	
	public String getPassword() {
		return getSP().getString(USER_PASSWORD, null);
	}
	
	public void setPassword(String password) {
		Editor editor = getSP().edit().putString(USER_PASSWORD, password);
		editor.apply();
	}

    public int getChecked() {
        return getSP().getInt(USER_CHECKED, -1);
    }

    public void setChecked(int checked) {
        Editor editor = getSP().edit().putInt(USER_CHECKED, checked);
        editor.apply();
    }

    public int getAge() {
        return getSP().getInt(USER_AGE, 0);
    }

    public void setAge(int age) {
        Editor editor = getSP().edit().putInt(USER_AGE, age);
        editor.apply();
    }

    public String getHeadPic() {
        return getSP().getString(USER_HEAD_PIC, null);
    }

    public void setHeadPic(String USER_HEAD_PIC) {
        Editor editor = getSP().edit().putString(USER_HEAD_PIC, null);
        editor.apply();
    }

    public String getLabel() {
        return getSP().getString(USER_LABEL, null);
    }

    public void setLabel(String label) {
        Editor editor = getSP().edit().putString(USER_LABEL, null);
        editor.apply();
    }

    public String getNickname() {
        return getSP().getString(USER_NICK_NAME, "");
    }

    public void setNickName(String nickname) {
        Editor editor = getSP().edit().putString(USER_NICK_NAME, null);
        editor.apply();
    }

    public String getSex() {
        return getSP().getString(USER_SEX, "");
    }

    public void setSex(String sex) {
        Editor editor = getSP().edit().putString(USER_SEX, null);
        editor.apply();
    }

    public String getSchool() {
        return getSP().getString(USER_SCHOOL, "");
    }

    public void setSchool(String school) {
        Editor editor = getSP().edit().putString(USER_SCHOOL, null);
        editor.apply();
    }

}
