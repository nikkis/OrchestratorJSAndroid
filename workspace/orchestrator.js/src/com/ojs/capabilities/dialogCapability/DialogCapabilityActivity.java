package com.ojs.capabilities.dialogCapability;

import java.util.ArrayList;

import org.json.JSONObject;

import com.ojs.OrchestratorJsActivity;
import com.ojs.capabilities.testCapability.TestCapability;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class DialogCapabilityActivity extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		Intent args = getIntent();
		
		int validTime = args.getIntExtra("dialogValidTime", -1);
		String title = args.getStringExtra("dialogTitle");
		ArrayList<String> dialogChoices = args.getStringArrayListExtra("dialogChoices");
		
		
		TableLayout layout = new TableLayout(getApplicationContext());
		TextView titleForButtons = new TextView(getApplicationContext());
		titleForButtons.setText(title);
		titleForButtons.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28);
		layout.addView(titleForButtons);
		
		
		for (String string : dialogChoices) {
			Button bb = new Button(getApplicationContext());
			bb.setText(string);
			
			Log.d("dialog", string);
			
			bb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						String choice = (String) ((Button)v).getText();
						DialogCapability.setDialogChoice( choice );
						onBackPressed();
					}catch(Exception e){ e.printStackTrace(); }
				}
			});
			layout.addView(bb);
		}
		
		setTitle(title);
		setContentView(layout);
	}
	
}
