package com.jacobhmorris.rcpi.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


public final class InputController {
	private final static String TAG = "InputController";
	private OnTouchListener onTouchListener;
	private SocketHandler socketHandler;
	private Context ctx;
	private MotorHandler motorHandler; 
	private int screenWidth, screenHeight;
	static enum ControlRegion {
		NONE, FIRST, SECOND
	}
	public InputController(Context ctx, int screenWidth, int screenHeight) {
		this.ctx = ctx;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		//this.motorHandler = (MotorHandler) new StandardMotorHandler(screenWidth, screenHeight);
		onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return handleOnTouch(v, event);
			}
		};
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.ctx);
		
		prefs.registerOnSharedPreferenceChangeListener( 
			new OnSharedPreferenceChangeListener() {
				
				@Override
				public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
						String key) {
					Log.d(TAG, key);
					 if (key.equals("prefControlMethod")) {
						setMotorHandler(sharedPreferences.getString("prefControlMethod", "standard"));
			        }
					
				}
			}
		);
		setMotorHandler(prefs.getString("prefControlMethod", "standard"));

	}
	
	public OnTouchListener getOnTouchListener() {
		return onTouchListener;
	}
	
	public void setMotorHandler(String handlerType) {
		if(handlerType == "standard"){
			motorHandler = (MotorHandler) new StandardMotorHandler(screenWidth, screenHeight);
			Log.d(TAG, "standard selected");
		}else if(handlerType == "bobcat"){
			motorHandler = (MotorHandler) new BobcatMotorHandler(screenWidth, screenHeight);
			Log.d(TAG, "Bobcat selected");
		}else{
			Log.d(TAG, "else standard selected");
			motorHandler = (MotorHandler) new StandardMotorHandler(screenWidth, screenHeight);
			//motorHandler = (MotorHandler) new BobcatMotorHandler(screenWidth, screenHeight);
		}
	}
	
	public void Connect() {
		socketHandler = new SocketHandler(ctx, motorHandler.getMotorFirst(), motorHandler.getMotorSecond());
		socketHandler.execute("");
	}
	private boolean handleOnTouch(View v, MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
    	int x1 = 0;
 		int y1 = 0;
 		int x2 = 0;
 		int y2 = 0;
    	  switch(action) {
    	  	//define the region the touch event is beginning in 
    	  	case MotionEvent.ACTION_DOWN : {
    	  		motorHandler.calcMotorSpeeds(x1, y1, x2, y2);
    		    break;
    	  	}
    	  	//touch has left screen - take note of this
    	  	case MotionEvent.ACTION_UP : {
    	  		motorHandler.calcMotorSpeeds(x1, y1, x2, y2);
    	  	    break;
    	  	}
    	  	
    	  	//define the region the touch event is beginning in 
    	  	case MotionEvent.ACTION_POINTER_DOWN : {
    	  		motorHandler.calcMotorSpeeds(x1, y1, x2, y2);
    	  	    break;
    	  	}
    	  	
    	  	//touch has left screen - take note of this
    	  	case MotionEvent.ACTION_POINTER_UP : {
    	  		motorHandler.calcMotorSpeeds(x1, y1, x2, y2);
    	  	    break;
    	  	}
  
    	  	case MotionEvent.ACTION_MOVE : {
    	  		x1 = (int) FloatMath.floor(event.getX(0));
   	    		y1 = (int) FloatMath.floor(event.getY(0));
   	    		if(event.getPointerCount() > 1){         	    	
   	    			x2 = (int) FloatMath.floor(event.getX(1));
   	    			y2 = (int) FloatMath.floor(event.getY(1));
   	    		}
   	    		motorHandler.calcMotorSpeeds(x1, y1, x2, y2);
   	    		break;
	  		}
	  	}
	  return true;
	}
	
	
}
