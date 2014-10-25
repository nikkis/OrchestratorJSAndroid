package com.ojs.services;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ojs.OrchestratorJsActivity;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;

public class ContextDataService extends Service {

	
	
	// settings
	public static final int BT_INTERVAL = 10;
	
	
	
	// use for commanding service
	public static String USER_COMMAND = "USER_COMMAND";
	private String tag = "ContextDataService";

	private final BroadcastReceiver userCommandReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			Log.d(tag, "User command Intent Action");
			Log.d(tag, intent.getAction());
			

			
		}
	};



	private Thread _discoveryThread;

	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d(tag, "onStart command");
		


		Runnable r = new Runnable() {
			public void run() {	   
				//Looper.prepare();
				//stopSelf();
				doBTDiscovery();	
			}
		};   
		_discoveryThread = new Thread(r);
		_discoveryThread.start();
		return Service.START_STICKY;
	}
	


	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(tag, "onBind!");
		return null;
	}
	
	
	// initialize discoveries etc.
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(tag , "oncreate");
		
		IntentFilter userCommandFilter = new IntentFilter(ContextDataService.USER_COMMAND);
		this.registerReceiver(userCommandReceiver, userCommandFilter);
		

		
		// BT discovery
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		this.registerReceiver(mReceiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		this.registerReceiver(discoveryFinished, filter);

		
//		doBTDiscovery();
				
	}
	


	@Override
	public void onDestroy() {
		Log.d(tag , "destroying service..");
		
		// TODO: Stop all discoveries etc.
		
		// BT discovery
		cancelBTDiscovery();
		this.unregisterReceiver(mReceiver);
		this.unregisterReceiver(discoveryFinished);

		
		this.unregisterReceiver(userCommandReceiver);
		super.onDestroy();
	}


	
	
	
	/*
	 *   Bluetooth Discovery
	 */
	
	
	private HashMap<String, Short> devicesArray = new HashMap<String,Short>();
	private BluetoothAdapter mBtAdapter;

	private void doBTDiscovery() {
		if(true) {
			Log.d(tag, "discovering bt devices");
			mBtAdapter = BluetoothAdapter.getDefaultAdapter();
			cancelBTDiscovery();
			/*
			if (!mBtAdapter.isEnabled()){
				Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBT, 0xDEADBEEF);
			}*/
			mBtAdapter.startDiscovery();
		}	
	}

	private void cancelBTDiscovery() {
		if(mBtAdapter.isDiscovering()) {
			Log.d(tag, "Cancelling discovery");
			mBtAdapter.cancelDiscovery();
		}
	}
	
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)){
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				short tt = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
				devicesArray.put(device.getAddress(), tt);
			}
		}
	};


	private final BroadcastReceiver discoveryFinished = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			
			JSONArray devices = new JSONArray();
			Log.d(tag, "bt discovery finished -> send to ojs");
			for ( String key : devicesArray.keySet() ) {
				Short val = devicesArray.get(key);
				Log.d(tag, key);
				
				
				JSONArray fakeTuple = new JSONArray();
				fakeTuple.put(key);
				fakeTuple.put(val);
				devices.put(fakeTuple);
				
				/*
				try {
					JSONObject detail = new JSONObject();
					detail.put("mac", key);
					detail.put("ssid", val);
					devices.put(detail);
				} catch (JSONException e) {}
				*/
			}
			try {
				//Time now = new Time();
				//now.setToNow();
				
				JSONObject contextData = new JSONObject();
				contextData.put("bt_devices", devices);
				OrchestratorJsActivity.ojsContextData(contextData);
				
				devicesArray = new HashMap<String,Short>();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			
/*
			JSONObject jObject = new JSONObject();			
			try {
				jObject.putOpt("mean", "bluetooth");
				jObject.putOpt("datatype", "rssi");
				jObject.putOpt("device", BLUETOOTH_MAC);
				jObject.putOpt("devicetype", "android");


				JSONArray devices = new JSONArray();
				for ( String key : devicesArray.keySet() ) {
					Short val = devicesArray.get(key);
					JSONArray fakeTuple = new JSONArray();
					fakeTuple.put(key);
					fakeTuple.put(val);
					devices.put(fakeTuple);
				}
				devicesArray = new HashMap<String,Short>();
				jObject.putOpt("devices", devices);				
			} catch (JSONException e1) {
				p("Error while parsing parameters for proximity server: " + e1.toString());
			}


			String uri = "http://social.cs.tut.fi:8889/addedges";
			try {
				sendJson(uri, jObject);
			} catch (Exception e) {
				p("Error while making http request to the server: " + e.toString());
			}
*/

			// Lets wait some time before next loop..
			sleep(ContextDataService.BT_INTERVAL);

			doBTDiscovery();
		}
	};


	
	
	
	
	///// BLuetooth - END
	
	
	
	
	
	
	
	
	
	// Helpers
	private void sleep(int seconds) {
		try {
			Thread.sleep(1000*seconds);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



	
	
}
