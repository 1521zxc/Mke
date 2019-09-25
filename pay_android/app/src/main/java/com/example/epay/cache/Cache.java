package com.example.epay.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by liujin on 2018/1/17.
 * 
 */
public class Cache {

	private static SharedPreferences preferences;
	private static String cacheName = "CacheBase_1.0";

	public static void putString(final Context context, final String key,final String value) {
		Log.i("putString", key);
		if (context == null)
			return;
		new Thread(new Runnable() {
			@Override
			public void run() {
				preferences = context.getSharedPreferences(cacheName,
						Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString(key, value);
				Log.i("epay", "putString " + key + ":" + value);
				editor.commit();
			}
		}).start();

	}

	public static void putBoolean(Context context, String key, boolean value) {
		if (context == null)
			return;
		preferences = context.getSharedPreferences(cacheName,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static String getString(Context context, String key) {
		if (context == null)
			return null;
		preferences = context.getSharedPreferences(cacheName,
				Context.MODE_PRIVATE);
		String result = preferences.getString(key, "");
		Log.i("Cache",key+":"+result);
		return result;
	}

	public static void putInt(Context context, String key, int value) {
		if (context == null)
			return;
		preferences = context.getSharedPreferences(cacheName,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static int getInt(Context context, String key) {
		if (context == null)
			return 0;
		preferences = context.getSharedPreferences(cacheName,
				Context.MODE_PRIVATE);
		return preferences.getInt(key, 0);
	}

	public static boolean getBoolean(Context context, String key) {
		if (context == null)
			return true;
		preferences = context.getSharedPreferences(cacheName,
				Context.MODE_PRIVATE);
		return preferences.getBoolean(key, false);
	}

	public static void clearCache(Context context) {
		if (context == null)
			return;
		preferences = context.getSharedPreferences(cacheName,
				Context.MODE_PRIVATE);
		Editor e = preferences.edit();
		e.clear();
		e.commit();
	}

}
