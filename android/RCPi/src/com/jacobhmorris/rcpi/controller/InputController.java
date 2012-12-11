package com.jacobhmorris.rcpi.controller;

import android.content.Context;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.jacobhmorris.rcpi.model.Motor;
import com.jacobhmorris.rcpi.model.Motor.Direction;

public final class InputController {
	private final static String TAG = "InputController";
	private Motor motorFirst, motorSecond;
	//private int xMidpointFirst, yMidpointSecond; //UNUSED
	private int yMidpointFirst, xMidpointSecond;
	private int screenWidth, screenHeight, xOffsetFirst, xOffsetSecond;
	private OnTouchListener onTouchListener;
	private SocketHandler socketHandler;
	private Context ctx;
	static enum ControlRegion {
		NONE, FIRST, SECOND
	}
	public InputController(Context ctx, int screenWidth, int screenHeight) {
		this.ctx = ctx;
		
		//set up the screen properties
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.xOffsetFirst = 0;
		this.xOffsetSecond = screenWidth/2;
        
		//this.xMidpointFirst = (screenWidth/4)+xOffsetFirst; //UNUSED
		this.yMidpointFirst = screenHeight/2;
		this.xMidpointSecond = (screenWidth/4)+xOffsetSecond;
		//this.yMidpointSecond = screenHeight/2; //UNUSED
		
		Log.d(TAG, "SCREEN H: "+screenHeight);
		
		
		onTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return handleOnTouch(v, event);
			}
		};
		
		motorFirst = new Motor(0, Direction.FORWARDS);
		motorSecond = new Motor(0, Direction.FORWARDS);
		
		
	}
	
	public OnTouchListener getOnTouchListener() {
		return onTouchListener;
	}
	public void Connect() {
		
		
		socketHandler = new SocketHandler(ctx, motorFirst, motorSecond);
		
		socketHandler.execute("");
		
	}
	private boolean handleOnTouch(View v, MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
    	int x1 = 0;
 		int y1 = 0;
 		int x2 = 0;
 		int y2 = 0;
 		ControlRegion pointerRegionFirst = ControlRegion.NONE;
 		ControlRegion pointerRegionSecond = ControlRegion.NONE;
    	  switch(action) {
    	  	//define the region the touch event is beginning in 
    	  	case MotionEvent.ACTION_DOWN : {
    	  		pointerRegionFirst = FindControlRegion(event.getX(0));
    		    break;
    	  	}
    	  	//touch has left screen - take note of this
    	  	case MotionEvent.ACTION_UP : {
    	  		pointerRegionFirst = ControlRegion.NONE;
    	  	    break;
    	  	}
    	  	
    	  	//define the region the touch event is beginning in 
    	  	case MotionEvent.ACTION_POINTER_DOWN : {
    	  		pointerRegionSecond = FindControlRegion(event.getX(1));
    	  	    break;
    	  	}
    	  	
    	  	//touch has left screen - take note of this
    	  	case MotionEvent.ACTION_POINTER_UP : {
    	  		pointerRegionSecond = ControlRegion.NONE;
    	  		setMotorValues(0, 0);
    	  	    break;
    	  	}

    	  	   
    	  	case MotionEvent.ACTION_MOVE : {
    	  		x1 = (int) FloatMath.floor(event.getX(0));
   	    		y1 = (int) FloatMath.floor(event.getY(0));
   	    		
   	    		int regionValueFirst = 0;
   	    		int regionValueSecond = 0;
   	    		pointerRegionFirst = FindControlRegion(event.getX(0));
    	  		
   	    		if(event.getPointerCount() > 1){
   	    			            	    	
   	    			x2 = (int) FloatMath.floor(event.getX(1));
   	    			y2 = (int) FloatMath.floor(event.getY(1));
   	    			pointerRegionSecond = FindControlRegion(event.getX(1));
   	    		}
   	    		           	    		
   	    		
   	    		//hand touch co-ordinates to the appropriate function based on the control region the touch occurs in
   	    		if(pointerRegionFirst == ControlRegion.FIRST){		
   	    			regionValueFirst = regionValueFirst(x1, y1);
   	    		}else{ //first finger is in the second control region
   	    			x1 = x1-xOffsetSecond; //correct the x value to represent the relative x position in this control region
   	    			regionValueFirst = regionValueFirst(x1, y1);
   	    		}
   	    		//Log.d(TAG,x1 + " " + y1);
   	    		
   	    		
   	    		//dont handle the second pointer if it exists in the same region as the first
   	    		if(pointerRegionFirst != pointerRegionSecond && pointerRegionSecond != ControlRegion.NONE){
   	    			if(pointerRegionSecond == ControlRegion.SECOND){
   	    				//x2 = x2-xOffsetSecond;  //correct the x value to represent the relative x position in this control region
   	    				regionValueSecond = regionValueSecond(x2, y2);
       	    		}else{
       	    			x2 = x2-xOffsetSecond;  //correct the x value to represent the relative x position in this control region
       	    			regionValueSecond = regionValueSecond(x2, y2);
       	    		}
   	    			//Log.d(TAG," - pointer 2: "+ x2+" "+y2);
   	    		}
   	    		
   	    		setMotorValues(regionValueFirst, regionValueSecond);
   	    		
   	    		break;
	  		}
	  	}

	  return true;
	}
	
	//find the section of the screen a touch is occurring in
    private ControlRegion FindControlRegion(float x){
    	int xPos = (int) FloatMath.floor(x);
    	ControlRegion foundRegion;
    	if(xPos >= xOffsetSecond){
    		foundRegion = ControlRegion.SECOND;
    	}else{
    		foundRegion = ControlRegion.FIRST;
    	}

    	return foundRegion;
    }
    
    private int regionValueFirst(int x, int y){
		//int speedX = findRelativeControlValue(screenWidth/2, xMidpointFirst, x); //UNUSED
		int speedY = findRelativeControlValue(screenHeight, yMidpointFirst, y);
		
		return speedY;
		
    }
    private int regionValueSecond(int x, int y){
    	int speedX = findRelativeControlValue((screenWidth/2), xMidpointSecond-xOffsetSecond, x-xOffsetSecond);
    	//int speedY = findRelativeControlValue(screenHeight, yMidpointSecond, y); //UNUSED
    	
    	return speedX;

    }
    
    //calculates a value between 0 and 100 in the negative or positive direction (-100 to 100), of and input point (int input) from a reference point (int referencePoint) within the defined bounds (controlBounds)
    //referencePoint may be an x or y midpoint, controlBounds the width or height of the control area, and input is the x or y coords of a touch event
    private int findRelativeControlValue(int controlBounds, int referencePoint, int inputPoint) {
    	float controlValue = 0;
    	float trueBounds = controlBounds/2; //as calculations are in either the positive or negative, either calculation will only involve half of the total control bounds
    	float distance = 0;
    	
    	//find distance input is from referencePoint (in positive or negative)
    	distance = referencePoint-inputPoint;
    	
    	//find the percentage that distance is of the total trueBounds
    	controlValue = (distance/trueBounds)*255;
    	
    	return (int) FloatMath.floor(controlValue);
    }
    
    private void setMotorValues(int regionValueFirst, int regionValueSecond) {
    	//regionFirst is the throttle, regionSecond the steering
    	
    	//first find the direction of the throttle
   		if(regionValueFirst < 0){
			regionValueFirst = regionValueFirst - (regionValueFirst*2); //invert the speed if it is negative
			motorFirst.setDirection(Direction.BACKWARDS);
			motorSecond.setDirection(Direction.BACKWARDS);
		}else{
			motorFirst.setDirection(Direction.FORWARDS);
			motorSecond.setDirection(Direction.FORWARDS);
		}
   		
		
		int motorSpeedFirst = regionValueFirst;
		int motorSpeedSecond = regionValueFirst;
		//regionValueSecond will be a negative number when turning right
		//logically, the right value should be subtracted from the left wheel's throttle, and vice versa
		if(regionValueSecond >= 0){ //turning left
			if(motorFirst.getDirection() == Direction.FORWARDS){
				motorSpeedSecond = regionValueFirst - regionValueSecond; //slow down the right wheel
			}else{
				//going backwards, slow down the opposite wheel for correct steering behaviour
				motorSpeedFirst = regionValueFirst - regionValueSecond; //slow down the right wheel
			}

		}else{
			if(motorSecond.getDirection() == Direction.FORWARDS){
				motorSpeedFirst = regionValueFirst + regionValueSecond; //slow down the left wheel - using + as regionValueSecond is a negative number
			}else{
				//going backwards, slow down the opposite wheel for correct steering behaviour
				motorSpeedSecond = regionValueFirst - regionValueSecond; //slow down the right wheel
			}
		}
		
		//avoid negative values
		if(motorSpeedFirst < 0){
			motorSpeedFirst = 0;
		}else if(motorSpeedFirst > 255) {
			motorSpeedFirst = 255;
		}
		if(motorSpeedSecond < 0){
			motorSpeedSecond = 0;
		}else if(motorSpeedSecond > 255) {
			motorSpeedSecond = 255;
		}
		//Log.d(TAG, "MOTOR SPEED: "+Integer.toString(motorSpeedFirst));
		motorFirst.setSpeed(motorSpeedFirst);
		motorSecond.setSpeed(motorSpeedSecond);
    }
}
