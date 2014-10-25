package com.ojs.helpers;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SettingHelpers {

	private static SharedPreferences prefs;

	private static final String enabled_capabilities_preferences_name = "enabled_capabilities";

	private static String defaul_orch_host = "orchestratorjs.org";
	private static String defaul_orch_port = "9000";

	private static String defaul_prox_host = "orchestratorjs.org";
	private static String defaul_prox_port = "9001";
	
	public static String getStringValue(String key, Context serviceOrActivity) {
		
		prefs = PreferenceManager.getDefaultSharedPreferences(serviceOrActivity);
		String val = prefs.getString(key,"");
		
		
		if(val.equals("") && key.equals("orchestrator_host")) {
			return defaul_orch_host;
		} else if(val.equals("") && key.equals("orchestrator_port")) {
			return defaul_orch_port;
		} else if(val.equals("") && key.equals("proximity_host")) {
			return defaul_prox_host;
		} else if(val.equals("") && key.equals("proximity_port")) {
			return defaul_prox_port;
		} else {
			return val;
		}
	}
	
	
	
	

	
	public static void setBooleanValue(String key, boolean value, Context serviceOrActivity) {
		prefs = PreferenceManager.getDefaultSharedPreferences(serviceOrActivity);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
		return ;
	}


	public static void setStringValue(String key, String value, Context serviceOrActivity) {
		prefs = PreferenceManager.getDefaultSharedPreferences(serviceOrActivity);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
		return ;
	}
	
	
	

	public static void p(String s){
		Log.e("SettingHelpers", s.toString());
	}


	
	/////////////// Multiprocess
	static String multiProcessSettingsFile = "SocialDevicesMultiProcessFileOrchestrator2";
	public static boolean getMultiProcessBooleanValue(String key, Context serviceOrActivity) {
		prefs = serviceOrActivity.getSharedPreferences(multiProcessSettingsFile, Context.MODE_MULTI_PROCESS);
		return prefs.getBoolean(key, false);
	}
	public static void setMultiProcessBooleanValue(String key, boolean value, Context serviceOrActivity) {
		prefs = serviceOrActivity.getSharedPreferences(multiProcessSettingsFile, Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
		return ;
	}
	
	
	static final String ENABLED_CAPABILITIES = "ENABLED_CAPABILITIES_SET";
	@SuppressLint("NewApi") 
	public static void saveEnabledCapabilities(Set<String> enabledCapabilities, Context serviceOrActivity) {
		prefs = serviceOrActivity.getSharedPreferences(multiProcessSettingsFile, Context.MODE_MULTI_PROCESS);
		SharedPreferences.Editor editor = prefs.edit();
		
		Set<String> newSet =  new HashSet<String>();
		for (String string : enabledCapabilities) {
			newSet.add(string);
		}
		editor.putStringSet(ENABLED_CAPABILITIES, newSet);
		editor.commit();
		return;
	}
	
	@SuppressLint("NewApi") 
	public static Set<String> loadEnabledCapabilities(Context serviceOrActivity) {
		prefs = serviceOrActivity.getSharedPreferences(multiProcessSettingsFile, Context.MODE_MULTI_PROCESS);
		Set<String> temp = prefs.getStringSet(ENABLED_CAPABILITIES, new HashSet<String>());
		
		Set<String> newSet =  new HashSet<String>();
		for (String string : temp) {
			newSet.add(string);
		}
		
		return newSet;
	}
	
	
	
	/////////////// Multiprocess end
	
	
}
