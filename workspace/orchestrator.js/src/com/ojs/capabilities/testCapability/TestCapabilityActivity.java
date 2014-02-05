package com.ojs.capabilities.testCapability;

import org.json.JSONObject;

import com.ojs.OrchestratorJsActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class TestCapabilityActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent args = getIntent();
		
		Button bb = new Button(getApplicationContext());
		bb.setText("jiihaa");
		bb.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					JSONObject o = new JSONObject();
					o.put("key1", "value1");
					o.put("key2", "value2");
					Intent i = new Intent(TestCapability.testCapabilityReceiverFilter);
					i.putExtra(OrchestratorJsActivity.OJS_EVENT_PARAMS, o.toString());
					getApplicationContext().sendBroadcast(i);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		RelativeLayout rl = new RelativeLayout(getApplicationContext());
		rl.addView(bb);
		setContentView(rl);
		
	}

	@Override
	protected void onPause() {
		this.finish();
		super.onPause();
	}
	
	

}
