package com.ojs.capabilities.dialogCapability;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ojs.OrchestratorJsActivity;
import com.ojs.capabilities.testCapability.TestCapability;
import com.ojs.capabilities.testCapability.TestCapabilityActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class DialogCapability {
	
	private static Context applicationContext_;
	private static String dialogChoice;

	public void initCapability( Context applicationContext ) {
		DialogCapability.applicationContext_ = applicationContext;
		
	}

	public void showDialog( String title, JSONArray dialogChoices, Integer validTime) throws Exception {
		
		Log.d(OrchestratorJsActivity.TAG, "showing dialog!");
		
		DialogCapability.dialogChoice = "FOO";
		
		Intent i = new Intent(DialogCapability.applicationContext_, DialogCapabilityActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Log.d(OrchestratorJsActivity.TAG, "showing dialog2!");
		
		ArrayList<String>choices = new ArrayList<String>();
		for (int index = 0; index < dialogChoices.length(); index++) {
			String choice = dialogChoices.getString(index);
			choices.add(choice);
		}
		
		Log.d(OrchestratorJsActivity.TAG, "showing dialog3!");
		i.putStringArrayListExtra("dialogChoices", choices);
		i.putExtra("dialogTitle", title);
		i.putExtra("dialogValidTime", validTime);
		
		Log.d(OrchestratorJsActivity.TAG, "test foo2");
		
		DialogCapability.applicationContext_.startActivity(i);
	}

	public String getDialogChoice() {
		Log.d(OrchestratorJsActivity.TAG, "getting dialog choice which is currently: "+DialogCapability.dialogChoice);
		return DialogCapability.dialogChoice;
	}
	
	

	public static void setDialogChoice(String choice) {
		DialogCapability.dialogChoice = choice;
		Log.d(OrchestratorJsActivity.TAG, "dialog choice set to: "+DialogCapability.dialogChoice);
	}
	
	
	
}
