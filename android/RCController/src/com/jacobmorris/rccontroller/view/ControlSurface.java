package com.jacobmorris.rccontroller.view;
import android.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.LinearLayout.LayoutParams;
public class ControlSurface extends Activity {
	private LinearLayout root;
	
	@Override public void onCreate(Bundle state) {
		super.onCreate(state);
		//setContentView(R.layout.control_surface);
		
		LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.WRAP_CONTENT,
			0.0F
		);
		
		LinearLayout.LayoutParams widgetParams = new LinearLayout.LayoutParams(
			ViewGroup.LayoutParams.FILL_PARENT,
			ViewGroup.LayoutParams.FILL_PARENT,
			0.0F
		);
		
		root = new LinearLayout(this);
		root.setOrientation(LinearLayout.HORIZONTAL);
		root.setBackgroundColor(Color.LTGRAY);
		root.setLayoutParams(containerParams);
		
		
	}
}
