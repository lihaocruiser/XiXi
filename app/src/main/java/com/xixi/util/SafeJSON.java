package com.xixi.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SafeJSON {

	public SafeJSON() {
		// TODO Auto-generated constructor stub
	}
	
	public static long getLong(JSONObject object, String name, long d){
		long res = d;
		try {
			res = object.getLong(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static int getBoolean(JSONObject object, String name, int d){
		int res = d;
		try {
			if(object.getBoolean(name))
				res = 1;
			else 
				res = 0;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static boolean getBoolean(JSONObject object, String name, boolean d){
		boolean res = d;
		try {
			res = object.getBoolean(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static int getInt(JSONObject object, String name, int d){
		int res = d;
		try {
			res = object.getInt(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static String getString(JSONObject object, String name, String d){
		String res = d;
		try {
			res = object.getString(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	/// put
	public static void putBoolean(JSONObject object, String name, int d){
		try {
			if (0 == d){
				object.put(name, false);
			}else{
				object.put(name, true);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/// put
	public static void putBoolean(JSONObject object, String name, Boolean d){
		try {
			object.put(name, d);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static double getDouble(JSONObject object, String name, double d){
		double res = d;
		try {
			res = object.getDouble(name);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	public static JSONObject getJSONObject(JSONObject object, String name,
			JSONObject d) {
		JSONObject res = d;
		try {
			res = object.getJSONObject(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static JSONArray getJSONArray(JSONObject object, String name,
			JSONArray d) {
		JSONArray res = d;
		try {
			res = object.getJSONArray(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static Object getObject(JSONObject object, String name, Object d){
		Object res = d;
		try {
			res = object.get(name);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static boolean putObject(JSONObject object, String name, Object mObject){
		boolean result = false;
		try {
			object.put(name, mObject);
			result = true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean transInt(JSONObject fromObject, JSONObject toObject, String name) {
		boolean success = false;
		try {
			int fromValue = fromObject.getInt(name);
			int toValue = getInt(toObject, name, -1);
			if(fromValue != toValue){
				toObject.put(name, fromValue);
				success = true;							// 数据发生变化，进行一次成功的修改
			}
		} catch (JSONException e) {
			e.printStackTrace();						// 如果新的object不含有这个字段的信息，则抛出exception
		}
		return success;
	}
	
	public static boolean transBoolean(JSONObject fromObject, JSONObject toObject, String name) {
		boolean success = false;
		try {
			boolean fromValue = fromObject.getBoolean(name);
			boolean toValue = getBoolean(toObject, name, false);
			if(fromValue != toValue){
				toObject.put(name, fromValue);
				success = true;							// 数据发生变化，进行一次成功的修改
			}
		} catch (JSONException e) {
			e.printStackTrace();						// 如果新的object不含有这个字段的信息，则抛出exception
		}
		return success;
	}
	
	public static boolean transString(JSONObject fromObject, JSONObject toObject, String name) {
		boolean success = false;
		try {
			String fromValue = fromObject.getString(name);
			String toValue = getString(toObject, name, "");
			if(!fromValue.equals(toValue)){
				toObject.put(name, fromValue);
				success = true;							// 数据发生变化，进行一次成功的修改
			}
		} catch (JSONException e) {
			e.printStackTrace();						// 如果新的object不含有这个字段的信息，则抛出exception
		} 

		return success;
	}
	
	public static boolean transDouble(JSONObject fromObject, JSONObject toObject, String name) {
		boolean success = false;
		try {
			double fromValue = fromObject.getDouble(name);
			double toValue = getDouble(toObject, name, 0);
			if((fromValue-toValue)>0.000001 || (toValue-fromValue)>0.000001){
				toObject.put(name, fromValue);
				success = true;							// 数据发生变化，进行一次成功的修改
			}
		} catch (JSONException e) {
			e.printStackTrace();						// 如果新的object不含有这个字段的信息，则抛出exception
		} 

		return success;
	}
	
	public static boolean transJsonObject(JSONObject fromObject, JSONObject toObject, String name) {
		boolean success = false;
		try {
//			JSONObject filterObject1 = getJSONObject(fromObject, name, new JSONObject());
//			JSONObject filterObject2 = getJSONObject(toObject, name, new JSONObject());
			
			String fromValue = fromObject.getJSONObject(name).toString();	// getJSONObject(fromObject, name, new JSONObject()).toString();
			String toValue = toObject.getJSONObject(name).toString();	// getJSONObject(toObject, name, new JSONObject()).toString();
			if(!fromValue.equals(toValue)){
				JSONObject object = new JSONObject(fromValue);
				toObject.put(name, fromObject.getJSONObject(name));
				success = true;							// 数据发生变化，进行一次成功的修改
			}
		} catch (JSONException e) {
			e.printStackTrace();						// 如果新的object不含有这个字段的信息，则抛出exception
		} 

		return success;
	}
	
}
