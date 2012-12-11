package com.jacobhmorris.rcpi;

import com.jacobhmorris.rcpi.controller.InputController;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	
	private InputController inputController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
		inputController= new InputController(this, screenWidth, screenHeight);
		
		FrameLayout touchOverlay = (FrameLayout) findViewById(R.id.touchOverlay);
		touchOverlay.setOnTouchListener(inputController.getOnTouchListener());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflator = getMenuInflater();
		inflator.inflate(R.menu.activity_main, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			
		case R.id.itemHandleConnection:
			inputController.Connect();
		break;
			
		}
		return true;
	}

	
}
