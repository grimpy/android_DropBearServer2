package com.github.grimpy.android_sshd_manager;

import com.github.grimpy.android_sshd_manager.util.L;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class LocalPreferences {

	public static final String PREF_ENABLE_AUTHENTICATION = "enable_authentication";
	public static final String PREF_START_BOOT = "Start_boot";
	public static final Boolean PREF_START_BOOT_DEFAULT = true;
	public static final String PREF_PORT = "port";
	public static final String PREF_PORT_DEFAULT = "22";

	public static Boolean getBoolean(Context context, String key, Boolean defaultValue) {
		if (context != null) {
			try {
				return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key, defaultValue);
			}
			catch (ClassCastException e) {
				L.e("ClassCastException: " + e.getMessage());
			}
		}
		else {
			L.w("Context is null");
		}
		return false;
	}

	public static Boolean putBoolean(Context context, String key, Boolean value) {
		if (context != null) {
			L.d(key + " = " + value);
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putBoolean(key, value);
			return editor.commit();
		}
		else {
			L.w("Context is null");
		}
		return false;
	}

	public static String getString(Context context, String key, String defaultValue) {
		if (context != null) {
			try {
				return PreferenceManager.getDefaultSharedPreferences(context).getString(key, defaultValue);
			}
			catch (ClassCastException e) {
				L.e("ClassCastException: " + e.getMessage());
			}
		}
		else {
			L.w("Context is null");
		}
		return "";
	}

	public static Boolean putString(Context context, String key, String value) {
		if (context != null) {
			L.d(key + " = " + value);
			SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			editor.putString(key, value);
			return editor.commit();
		}
		else {
			L.w("Context is null");
		}
		return false;
	}
}
