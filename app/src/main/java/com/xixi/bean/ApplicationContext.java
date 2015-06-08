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

	public static String age;
	public static String headPic;
	public static String label;
	public static String nickname;
	public static String sex;
	public static String school;
		
	public void setContext(Context context) {
		ApplicationContext.context = context;
	}

	public static ApplicationContext getInstance() {
		if (instance == null) {
			instance = new ApplicationContext();
		}
		return instance;
	}

	private SharedPreferences getSP() {
		if (ApplicationContext.sp == null) {
			try {
				ApplicationContext.sp = context.getSharedPreferences(
						SP_NAME, Context.MODE_PRIVATE);
			} catch (NullPointerException e) {
				e.printStackTrace();
			}
		}
		return ApplicationContext.sp;
	}

	public void saveUserInfo(JSONObject obj) {
		age	= SafeJSON.getString(obj, "age", null);
		headPic = SafeJSON.getString(obj, "headPic", null);
		label = SafeJSON.getString(obj, "label", null);
		nickname = SafeJSON.getString(obj, "nickname", null);
		sex = SafeJSON.getString(obj, "sex", null);
		school = SafeJSON.getString(obj, "school", null);
	}

	public void clearSP() {
		Editor editor = getSP().edit().clear();
		editor.commit();
	}
	
	public String getToken() {
		return getSP().getString(USER_TOKEN, null);
	}
	
	public void setToken(String token) {
		Editor editor = getSP().edit().putString(USER_TOKEN, token);
		editor.commit();
	}
	
	public String getEmail() {
		return getSP().getString(USER_EMAIL, null);
	}
	
	public void setEmail(String email) {
		Editor editor = getSP().edit().putString(USER_EMAIL, email);
		editor.commit();
	}
	
	public String getPassword() {
		return getSP().getString(USER_PASSWORD, null);
	}
	
	public void setPassword(String password) {
		Editor editor = getSP().edit().putString(USER_PASSWORD, password);
		editor.commit();
	}
	
	public int getChecked() {
		return getSP().getInt(USER_CHECKED, -1);
	}
	
	public void setChecked(int checked) {
		Editor editor = getSP().edit().putInt(USER_CHECKED, checked);
		editor.commit();
	}
		
}
