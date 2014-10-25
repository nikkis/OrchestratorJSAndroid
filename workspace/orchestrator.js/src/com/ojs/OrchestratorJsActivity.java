package com.ojs;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import com.ojs.helpers.SettingHelpers;
import com.ojs.libraries.SocketIOClient;
import com.ojs.services.ContextDataService;
import com.ojs.settings.GeneralSettingsActivity;


import android.graphics.Color;
import android.graphics.PorterDuff;

import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.Toast;

public class OrchestratorJsActivity extends Activity {

	public static final String TAG = "ojs";

	private static final String CAPABILITY_PATH = "com/ojs/capabilities/";

	private static final String OJS_FILTER = "OJS_FILTER";
	private BroadcastReceiver _myReceiver = new MyReceiver();
	private BroadcastReceiver _heartbeatReceiver = new HeartbeatReceiver();

	private BroadcastReceiver _networkStateReceiver = new ConnectionChangeReceiver();

	public static final String OJS_EVENT_FILTER = "OJS_EVENT_FILTER";
	public static final String OJS_EVENT_PARAMS = "OJS_EVENT_PARAMS";
	private BroadcastReceiver _ojseventReceiver = new OJSEventReceiver();

	private static final String OJS_NOTIFICATION_FILTER = "OJS_NOTIFICATION_FILTER";


<<<<<<< HEAD

	protected static final Object INFO_TYPE = "ojs_info";

=======
	protected static final Object INFO_TYPE = "ojs_info";
	
>>>>>>> FETCH_HEAD
	private BroadcastReceiver _notificationReceiver = new NofifyReceiver();


	private String currentActionId = "";
	private String currentMethodcallId = "";


	private static Set<String> enabledCapabilities = null; //new String[] {"TalkingDevice", "PlayerDevice", "UrlScreen"};
	private HashMap<String, Object[]> capabilityObjects;

	public static OrchestratorJsActivity singleton = null;

	// UI stuff
	//private CheckBox useSocialAsHostCH;
	private Button connectBtn;
	private Button disconnectBtn;



	// Setup stuff
	private String deviceId = null;
	private String ipAdress = null;
	private URI u = null;

	private SocketIOClient client = null;

	private boolean contextServiceEnabled;

	private boolean showDebugNotifications;

	private int reconnectAttempts = -1;


	private Thread _ojsConnectionThread;





	private void reconnect() {

		Runnable r = new Runnable() {
			public void run() {	   

				try {
					applySettings();
					if( client == null ) {
						u = new URI(ipAdress);
						client = createSocketIOClient();
					}
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}

				
				

				ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService( Context.CONNECTIVITY_SERVICE );
				NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

				if ( activeNetInfo != null ) {
					reconnectAttempts += 1;
					if( reconnectAttempts < 5) {
						//client = createSocketIOClient();
						p("connecting..");
						client.connect();

					}
				} else {
					
					p("no active internet connection");
					
					Intent si = new Intent(getApplicationContext(), ContextDataService.class);
					stopService(si);


					try {
						Thread.sleep(1000*5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}		
					reconnect();
				}
			}
		};   
		
		
		_ojsConnectionThread = new Thread(r);
		_ojsConnectionThread.start();
	}




	public void applySettings() {


		// TODO: load settings from server first!!
		enabledCapabilities = SettingHelpers.loadEnabledCapabilities(getApplicationContext());
		p("I have capabilities: " + enabledCapabilities.toString());
		initializeCapabilities();

		String host = SettingHelpers.getStringValue("orchestrator_host", this);
		p("host: " + host);
		String port = SettingHelpers.getStringValue("orchestrator_port", this);
		p("port: " + port);
		ipAdress = "http://" + host + ":" + port;

		deviceId = SettingHelpers.getStringValue("deviceIdentity", this);

		// check that settings are valid. Device identity is a must!
		if(deviceId.equals("NoIdentity")) {
			Intent i = new Intent(getApplicationContext(),
					GeneralSettingsActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		}









		// Load Context service and other settings

		showDebugNotifications = SettingHelpers.getMultiProcessBooleanValue("showDebugNotifications", this);
		if(showDebugNotifications)
			p("Showing also debug notifications");
		else
			p("Not showing debug notifications");

		contextServiceEnabled = SettingHelpers.getMultiProcessBooleanValue("enableContextDataService", this);
		if(contextServiceEnabled) {
			p("context service enabled -> starting..");

			Intent serviceStartIntent = new Intent(getApplicationContext(), ContextDataService.class);
			//startService(serviceStartIntent);

		} else {
			p("context service enabled -> ensure stop");
			Intent intent = new Intent(getApplicationContext(), ContextDataService.class);
			stopService(intent);
		}

		String BT_MAC = BluetoothAdapter.getDefaultAdapter().getAddress();
		p(BT_MAC);



		return;
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		OrchestratorJsActivity.singleton = this;

		setContentView(R.layout.activity_orchestrator_js);


		IntentFilter filter0 = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(_networkStateReceiver, filter0);

		IntentFilter filter1 = new IntentFilter(OJS_FILTER);
		registerReceiver(_myReceiver, filter1);
		IntentFilter filter2 = new IntentFilter(SocketIOClient.HEARTBEAT_INTENT);
		registerReceiver(_heartbeatReceiver, filter2);
		IntentFilter filter3 = new IntentFilter(OJS_EVENT_FILTER);
		registerReceiver(_ojseventReceiver, filter3);
		IntentFilter filter4 = new IntentFilter(OJS_NOTIFICATION_FILTER);
		registerReceiver(_notificationReceiver, filter4);


		connectBtn = (Button) findViewById(R.id.connect);

		connectBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				/*
				try {
					applySettings();

					u = new URI(ipAdress);
					client = createSocketIOClient();
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
				client.connect();
				 */
				reconnect();
			}
		});
<<<<<<< HEAD

=======
		
>>>>>>> FETCH_HEAD


		disconnectBtn = (Button) findViewById(R.id.disconnect);
		disconnectBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// disconnect();
				try {
					client.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		

		// color for buttons :-)
		connectBtn.getBackground().setColorFilter(Color.parseColor("#34A6C9"), PorterDuff.Mode.CLEAR);
		disconnectBtn.getBackground().setColorFilter(Color.parseColor("#34A6C9"), PorterDuff.Mode.CLEAR);

		// color for buttons :-)
		connectBtn.getBackground().setColorFilter(Color.parseColor("#34A6C9"), PorterDuff.Mode.CLEAR);
		disconnectBtn.getBackground().setColorFilter(Color.parseColor("#34A6C9"), PorterDuff.Mode.CLEAR);




		Button testBtn = (Button) findViewById(R.id.testBtn);
		testBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				

				Intent serviceStartIntent = new Intent(getApplicationContext(), ContextDataService.class);
				startService(serviceStartIntent);
				
				Intent i = new Intent();
				i.setAction(ContextDataService.USER_COMMAND);
				sendBroadcast(i);
			}
		});


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.orchestrator_js, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			Intent settingsActivity = new Intent(getBaseContext(), GeneralSettingsActivity.class);
			startActivity(settingsActivity);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void initializeCapabilities() {
		try {
			ClassLoader classLoader = getClassLoader();

			capabilityObjects = new HashMap<String, Object[]>();

			for (String capabilityName : enabledCapabilities) {

				String packagePrefix = Character.toString(Character.toLowerCase(capabilityName.charAt(0)))+capabilityName.substring(1);
				p("packagePrefix: " + packagePrefix);

				Class<?> clazz = classLoader.loadClass(CAPABILITY_PATH+packagePrefix+"/"+capabilityName);
				Object o = clazz.newInstance();

				Object[] object = new Object[] {clazz, o};

				try {
					Method myMethod1 = clazz.getMethod("initCapability", new Class[] { Context.class });
					myMethod1.invoke(o, new Object[] { getApplicationContext() });
				} catch (Exception e) {
					p("Error adding Application Context: " + e.toString()+", ");
				}

				if(clazz != null && o != null && object != null)
					capabilityObjects.put(capabilityName, object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	private void p(String s) {
		Log.d(TAG, s);
	}

	private SocketIOClient createSocketIOClient() {
		return new SocketIOClient(u, new SocketIOClient.Handler() {

			@Override
			public void onConnect() {
				Log.d(TAG, "Connected!");
				initConnection();

				Intent i = new Intent(OJS_NOTIFICATION_FILTER);
				i.putExtra("notificationMessage", "Connected to orchestrator.js");
				i.putExtra("notificationType", 1);
				sendBroadcast(i);
			}

			@Override
			public void on(String event, JSONArray arguments) {
				Log.d(TAG, String.format("Got event %s: %s", event, arguments.toString()));
				String METHODCALL_TYPE = "methodcall";
				if (event.equals(METHODCALL_TYPE)) {
					Intent i = new Intent(OJS_FILTER);
					i.putExtra("arguments", arguments.toString());
					sendBroadcast(i);
				} else if( event.equals( INFO_TYPE ) ) {
					saveConfig( arguments );
				}
			}

			private void saveConfig(JSONArray arguments) {
			}

			@Override
			public void onJSON(JSONObject json) {
				try {
					Log.d(TAG, String.format("Got JSON Object: %s", json.toString()));
				} catch (Exception e) {
				}
			}

			@Override
			public void onMessage(String message) {
				Log.d(TAG, String.format("Got message: %s", message));
				Intent i = new Intent(OJS_FILTER);
				i.putExtra("message", message);
				sendBroadcast(i);
			}

			@Override
			public void onDisconnect(int code, String reason) {
				Log.d(TAG, String.format("Disconnected! Code: %d Reason: %s",
						code, reason));
				if( reason.equals("EOF") )
					reconnect();
			}



			@Override
			public void onError(Exception error) {
				Log.e(TAG, "Error!");
				error.printStackTrace();
				if(error instanceof HttpHostConnectException ) {
					p("HttpHostConnectionException -> server down?");

					Intent i = new Intent(OJS_NOTIFICATION_FILTER);
					i.putExtra("notificationMessage", "Cannot connect");
					i.putExtra("notificationType", -1);
					sendBroadcast(i);


				} else if(error instanceof SocketException ) {
					p("Socket Exception -> reconnect");

					// user closed connection
					if(!error.getMessage().equals("Socket closed"))
						reconnect();

					Intent i = new Intent(OJS_NOTIFICATION_FILTER);
					i.putExtra("notificationMessage", "Connection Broken, check wifi");
					i.putExtra("notificationType", -1);
					sendBroadcast(i);

				} else if(error.getMessage().equals("Connection down")) {

				} else {

					Intent i = new Intent(OJS_NOTIFICATION_FILTER);
					i.putExtra("notificationMessage", "Other error: "+error.getMessage());
					i.putExtra("notificationType", -1);
					sendBroadcast(i);
				}

			}

			@Override
			public void onConnectToEndpoint(String endpoint) {
				Log.d(TAG, "Connected to:" + endpoint);
				reconnectAttempts = 0;
			}
		});
	}




	private class ConnectionChangeReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive( Context context, Intent intent )
		{
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService( Context.CONNECTIVITY_SERVICE );
			NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
			NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(     ConnectivityManager.TYPE_MOBILE );
			if ( activeNetInfo != null && reconnectAttempts != -1 )
			{
				//Toast.makeText( context, "Active Network Type : " + activeNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
				reconnect();
			}
			else 
			{
				Intent si = new Intent(getApplicationContext(), ContextDataService.class);
				stopService(si);
			}


			/*
	    if( mobNetInfo != null )
	    {
	      Toast.makeText( context, "Mobile Network Type : " + mobNetInfo.getTypeName(), Toast.LENGTH_SHORT ).show();
	    }
			 */
		}
	}



	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			handleMethodcallIntent(intent);
		}

	}

	private void handleMethodcallIntent(Intent intent) {

		p("Recieved intent with action: " + intent.getAction());
		String capabilityName = "";
		String methodCallName = "";

		try {

			String argumentsJsonString = intent.getStringExtra("arguments");
			JSONArray arguments = new JSONArray(argumentsJsonString);

			JSONArray methodCall = arguments.getJSONArray(0);

			// set the current action to the given one
			String actionId = (String) methodCall.get(0);
			p("methodcall from action: " + actionId);
			currentActionId = actionId;

			String methodcallId = (String) methodCall.get(1);
			currentMethodcallId = methodcallId;
			p(currentMethodcallId);


			// get methodcall name
			capabilityName = (String) methodCall.get(2);
			p(capabilityName);

			methodCallName = (String) methodCall.get(3);
			p(methodCallName);

			// get arguments for the method call
			JSONArray methodCallArguments = methodCall.getJSONArray(4);
			p(methodCallArguments.toString());

			Object retVal = invokeMethod(capabilityName, methodCallName, methodCallArguments);

			p("method invoked!");
			sendResponse(retVal);


			// handling exceptions
		} catch (InvocationTargetException e) {
			p("h1");
			String cause = e.toString();
			e.printStackTrace();
			p("-------");
			try {
				p("h1.1");
				Throwable ee = e.getTargetException();
				ee.printStackTrace();
				String stackTrace = ee.getStackTrace().toString();
				p("stackTrace: "+stackTrace);
				//cause = ee.toString();
			} catch (Exception e2) {p("h1.2");}
			sendException(cause);
		}catch (NoSuchMethodException e) {
			p("h2");
			sendException("No such method ("+capabilityName+"::"+methodCallName+"), check parameters!");
		} catch (Exception e) {
			p("h3");
			e.printStackTrace();
			sendException(e.toString());
		}
	}

	private void sendException(String reason) {
		try {
			p("sending exception to orchestrator");
			JSONArray responseArguments = new JSONArray();
			responseArguments.put(currentActionId);
			responseArguments.put(currentMethodcallId);
			responseArguments.put(deviceId);
			responseArguments.put(reason);
			client.emit("ojs_exception", responseArguments);
		} catch(Exception ee) {
			ee.printStackTrace();
		}
	}

	@SuppressLint("NewApi") 
	private Class<?>[] addElement(Class<?>[] org, Class<?> added) {
		Class<?>[] result = Arrays.copyOf(org, org.length +1);
		result[org.length] = added;
		return result;
	}

	private Object invokeMethod(String capabilityName, String methodCallName, JSONArray methodCallArguments) throws Exception {

		Object[] classAndInstance = capabilityObjects.get(capabilityName);
		if(classAndInstance == null) {
			p("interface " + capabilityName + " was not enabled!");
			throw(new Exception("interface " + capabilityName + " was not enabled."));
		}

		Class<?> clazz = (Class<?>)classAndInstance[0];
		Object whatInstance = classAndInstance[1];
		if(clazz == null || whatInstance == null) {
			p("clazz or instance was null");
			throw(new Exception("Cannot find class"));
		}

<<<<<<< HEAD


		List<Object> parameterObjects = new ArrayList<Object>();
		Class<?>[] parameterClasses = new Class[] {}; 
		for (int i = 0; i < methodCallArguments.length(); i++) {
			Object object = methodCallArguments.get(i);
			parameterClasses = addElement(parameterClasses, object.getClass());
			parameterObjects.add(object);
			p("param class: "+object.getClass().getSimpleName());
		}			

		Method theMethod = clazz.getMethod(methodCallName, parameterClasses);

		p("invoking "+methodCallName);

		p(whatInstance.toString());
		p(whatInstance.getClass().getSimpleName());

		p(theMethod.toString());
		p(theMethod.getClass().getSimpleName());

		Object methodReturnValue = theMethod.invoke(whatInstance, parameterObjects.toArray());


		p("method invoked");
		if(methodReturnValue != null) {
			p(methodReturnValue.toString());
		}
		return methodReturnValue;
=======
			
			
			List<Object> parameterObjects = new ArrayList<Object>();
			Class<?>[] parameterClasses = new Class[] {}; 
			for (int i = 0; i < methodCallArguments.length(); i++) {
				Object object = methodCallArguments.get(i);
				parameterClasses = addElement(parameterClasses, object.getClass());
				parameterObjects.add(object);
				p("param class: "+object.getClass().getSimpleName());
			}			
			
			Method theMethod = clazz.getMethod(methodCallName, parameterClasses);
			
			p("invoking "+methodCallName);

			p(whatInstance.toString());
			p(whatInstance.getClass().getSimpleName());

			p(theMethod.toString());
			p(theMethod.getClass().getSimpleName());

			Object methodReturnValue = theMethod.invoke(whatInstance, parameterObjects.toArray());

			
			p("method invoked");
			if(methodReturnValue != null) {
				p(methodReturnValue.toString());
			}
			return methodReturnValue;
>>>>>>> FETCH_HEAD
	}

	public void sendResponse(Object methodReturnValue) {
		try {

			JSONArray responseArguments = new JSONArray();
			responseArguments.put(currentActionId);
			responseArguments.put(currentMethodcallId);

			if(methodReturnValue != null && methodReturnValue instanceof JSONObject ) {
				responseArguments.put((JSONObject) methodReturnValue);
				responseArguments.put("JSON");
			} else if(methodReturnValue != null && methodReturnValue instanceof JSONArray ) {
				responseArguments.put((JSONArray) methodReturnValue);
				responseArguments.put("JSON");				
			} else if(methodReturnValue != null && methodReturnValue instanceof Boolean) {
				responseArguments.put((Boolean)methodReturnValue);
				responseArguments.put("BOOL");
			} else if(methodReturnValue != null && methodReturnValue instanceof String) {
				responseArguments.put((String)methodReturnValue);				
				responseArguments.put("STRING");
			} else if(methodReturnValue != null && methodReturnValue instanceof Integer) {
				responseArguments.put((Integer)methodReturnValue);
				responseArguments.put("INT");
			} else if(methodReturnValue != null && methodReturnValue instanceof Float) {
				responseArguments.put((Float)methodReturnValue);
				responseArguments.put("FLOAT");
			} else if(methodReturnValue != null && methodReturnValue instanceof Double) {
				responseArguments.put((Double)methodReturnValue);
				responseArguments.put("DOUBLE");
			}

			client.emit("methodcallresponse", responseArguments);

		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(),
					"Error while sending response",
					Toast.LENGTH_LONG).show();
		}

	}

	
	public static void ojsLog(String message) {
		try {
			JSONArray sendLogMessage = new JSONArray();
			sendLogMessage.put(OrchestratorJsActivity.singleton.currentActionId);
			sendLogMessage.put(OrchestratorJsActivity.singleton.deviceId);
			sendLogMessage.put(message);
			OrchestratorJsActivity.singleton.client.emit("ojs_log", sendLogMessage);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

<<<<<<< HEAD
	public static void ojsLog(String message) {
		try {
			JSONArray sendLogMessage = new JSONArray();
			sendLogMessage.put(OrchestratorJsActivity.singleton.currentActionId);
			sendLogMessage.put(OrchestratorJsActivity.singleton.deviceId);
			sendLogMessage.put(message);
			OrchestratorJsActivity.singleton.client.emit("ojs_log", sendLogMessage);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	public static void ojsContextData(JSONObject contextData) {
		try {

=======
	
	
	public static void ojsContextData(JSONObject contextData) {
		try {
			
>>>>>>> FETCH_HEAD
			JSONArray sendContextData = new JSONArray();
			sendContextData.put(OrchestratorJsActivity.singleton.currentActionId);
			sendContextData.put(OrchestratorJsActivity.singleton.deviceId);
			sendContextData.put(contextData);
			OrchestratorJsActivity.singleton.client.emit("ojs_context_data", sendContextData);
<<<<<<< HEAD

=======
			
>>>>>>> FETCH_HEAD
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(OrchestratorJsActivity.singleton.getApplicationContext(),
					"Error while sending context data",
					Toast.LENGTH_LONG).show();
		}
	}
<<<<<<< HEAD




=======
	
	
	
	
>>>>>>> FETCH_HEAD
	private void initConnection() {
		try {
			JSONArray responseArguments = new JSONArray();
			responseArguments.put(deviceId);
			client.emit("login", responseArguments);
		} catch (Exception e) {
			p("Error while initializing the connection");
			e.printStackTrace();
		}
	}
<<<<<<< HEAD




=======
	
	
	
	
>>>>>>> FETCH_HEAD
	private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
	void blink() {
		changeUIimage(R.drawable.blink3);
		Runnable task = new Runnable() {
			public void run() {
				changeUIimage(R.drawable.blink4);
			}
		};
		worker.schedule(task, 1, TimeUnit.SECONDS);
	}


	private class NofifyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			p("notification");
			String notificationMessage = intent.getStringExtra("notificationMessage");
			int notificationType = intent.getIntExtra("notificationType",0);
			if(notificationType == -1)
				Toast.makeText(getApplicationContext(), notificationMessage, Toast.LENGTH_LONG).show();
			else if(notificationType == 1)
				Toast.makeText(getApplicationContext(), notificationMessage, Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), notificationMessage, Toast.LENGTH_LONG).show();
		}
	};


	private class HeartbeatReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			p("..heartbeat..");
			blink();
<<<<<<< HEAD

=======
			
>>>>>>> FETCH_HEAD
			try {
				// for testing the context context data
				/*
				JSONObject tt = new JSONObject();
				tt.put("wifi_ssid", "peltomaa");
				OrchestratorJsActivity.ojsContextData(tt);
<<<<<<< HEAD
				 */

				// for testing the log
				//OrchestratorJsActivity.ojsLog("do you feel my heart beating, do you understand?");

			} catch (Exception e) {
				// TODO: handle exception
			}

=======
				*/
				
				// for testing the log
				//OrchestratorJsActivity.ojsLog("do you feel my heart beating, do you understand?");
				
			} catch (Exception e) {
				// TODO: handle exception
			}
			
>>>>>>> FETCH_HEAD
		}
	};


	public static void sendHeartbeatIntent() {
		Intent i = new Intent();
		i.setAction(SocketIOClient.HEARTBEAT_INTENT);
		OrchestratorJsActivity.singleton.sendBroadcast(i);
	}

	public void changeUIimage(final int drawable) {
		try {
			new Thread(new Runnable() {
				public void run() {
					Looper.prepare();
					runOnUiThread(new Runnable() {
						public void run() {
							ImageView image = (ImageView) OrchestratorJsActivity.singleton.findViewById(R.id.hearbeatBlink);
							image.setImageResource(drawable);
						}
					});
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}


	private class OJSEventReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				p("got SDEvent");

				JSONObject o = new JSONObject(intent.getStringExtra(OrchestratorJsActivity.OJS_EVENT_PARAMS));
				JSONArray args = new JSONArray();
				args.put(currentActionId);
				args.put(deviceId);
				args.put(o);
				client.emit("ojs_event", args);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	};




	@Override
	protected void onDestroy() {

		if(_networkStateReceiver != null)
			unregisterReceiver(_networkStateReceiver);

		if(_myReceiver != null)
			unregisterReceiver(_myReceiver);

		if(_heartbeatReceiver != null)
			unregisterReceiver(_heartbeatReceiver);

		if(_ojseventReceiver != null)
			unregisterReceiver(_ojseventReceiver);

		if(_notificationReceiver != null)
			unregisterReceiver(_notificationReceiver);		

	}




	@Override
	public void onBackPressed() {
		p("back was pressed");
	}



}
