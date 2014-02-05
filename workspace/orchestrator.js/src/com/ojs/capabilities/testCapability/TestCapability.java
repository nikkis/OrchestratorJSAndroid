package com.ojs.capabilities.testCapability;




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










