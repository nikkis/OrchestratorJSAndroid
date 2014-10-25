package com.ojs.capabilities.testCapability;




import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.ojs.OrchestratorJsActivity;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

public class TestCapability {


	private static Context applicationContext;
	public static boolean canContinue;	
	public void initCapability(Context applicationContext) {
		TestCapability.applicationContext = applicationContext;
		
		TestCapability.applicationContext.registerReceiver(testCapabilityReceiver, new IntentFilter(testCapabilityReceiverFilter));
	}

	
	
	
	
	
	/////// Speed test Begin
	private List<Long> latencies;
	private Date lastEndTime = null;

	public void dummyMethod()  throws Exception {
		if(lastEndTime != null) {
			long delta = (new Date()).getTime() - lastEndTime.getTime();
			//Log.d("LAG", Long.toString(delta)+" ms.");
			Log.d("LAG", Long.toString(delta));
			latencies.add(delta);
		}
		lastEndTime = new Date();
		return;
	}
	
	public void initMeasurement()  throws Exception {	
		lastEndTime = null;
		latencies = new ArrayList<Long>();
		Log.d("LAG", "New measurement initialized");
		return;
	}

	public void calculateAverage()  throws Exception {
		if(latencies != null && !latencies.isEmpty()) {
			long total = 0;
			for (int i = 0; i < latencies.size(); i++) {
				total += latencies.get(i);
			}
			long average = total / latencies.size();
			Log.d("LAG", "On average: "+Long.toString(average) + " ms.");
		}
		return;
	}
	/////// Speed test End
	
	
	
	


	@SuppressLint("NewApi")
	public Object throwException() throws Exception {

		if(true)
			throw(new Exception("voi rahma"));
		
		return null;
	}
	
	@SuppressLint("NewApi")
	public Object test() throws Exception {
		
		TestCapability.canContinue = false;
		
		Log.d(OrchestratorJsActivity.TAG, "test foo1");
		Intent i = new Intent(TestCapability.applicationContext, TestCapabilityActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Log.d(OrchestratorJsActivity.TAG, "test foo2");
		TestCapability.applicationContext.startActivity(i);
		
		Log.d(OrchestratorJsActivity.TAG, "waiting begins");
		
		try {
			
			Thread.sleep(500);
			
			while ( !TestCapability.canContinue ) {
				//Thread.sleep(500);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Log.d(OrchestratorJsActivity.TAG, "waiting ends");
		
		
		return false;
	}

	
	
    
    
	public static String testCapabilityReceiverFilter = "testCapabilityReceiverFilter";
	private BroadcastReceiver testCapabilityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	Log.d(OrchestratorJsActivity.TAG, "got test activity event");
        	TestCapability.canContinue = true;
        }
    };
	
}










