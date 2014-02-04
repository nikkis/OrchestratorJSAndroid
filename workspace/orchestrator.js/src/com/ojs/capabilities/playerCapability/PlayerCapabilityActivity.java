package com.ojs.capabilities.playerCapability;


import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;
import android.widget.RelativeLayout;

public class PlayerCapabilityActivity extends Activity {

	
	protected static final String TAG = PlayerCapabilityActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout r = new RelativeLayout(getApplicationContext());
		
		setContentView(r);
		
		p("PlayerCapabilityActivity created!");
		
		try {
			
			Bundle b = getIntent().getExtras();
			String url = b.getString("url_to_show");
			WebView myWebView = new WebView(getApplicationContext());                   
			myWebView.loadUrl(url);
			setTitle(getClass().getSimpleName());
			setContentView(myWebView);
			
			p("the end");
		} catch (Exception e) {
			p(e.toString());
		}
		
		
	}


	
	
	private void p(String s) {
		Log.d(TAG, s);
	}

	
	
}
