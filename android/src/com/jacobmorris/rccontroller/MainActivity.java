package com.jacobmorris.rccontroller;

import java.io.*;
import java.net.*;
import com.jacobmorris.*;

import android.os.Bundle;
import android.os.Looper;
import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.util.FloatMath;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.*;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;



public class MainActivity extends Activity {
	
	public static final String PREF_FILE_NAME = "PrefFile";
	
	private String TARGET_IP;
	
	private Button doConnect;
	boolean connected = false;
	volatile int motorSpeedFirst; //left wheel
	volatile int motorSpeedSecond; //right wheel
	volatile int motorDirectionFirst; //0 = backwards, 1 = forwards
	volatile int motorDirectionSecond; //0 = backwards, 1 = forwards
	
	
	
	
    private int screenWidth;
    private int screenHeight;
    private int xOffsetFirst;
    private int xOffsetSecond;
    
    private int xMidpointFirst;
    private int yMidpointFirst;
    private int xMidpointSecond;
    private int yMidpointSecond;
    
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.activity_main, menu);
    	
    	return true;
    }
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ipAddress", "10.1.1.10"); // value to store
        editor.commit();
        
        
        String ipAddress = preferences.getString("ipAddress", "");
        
        System.out.println(ipAddress);
       
        motorSpeedFirst = 0;
        motorSpeedSecond = 0;
        motorDirectionFirst = 1;
        motorDirectionSecond = 1;
        
        FrameLayout touchOverlay = (FrameLayout) findViewById(R.id.touchOverlay);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        screenHeight = getWindowManager().getDefaultDisplay().getHeight();

        xOffsetFirst = 0;
        xOffsetSecond = screenWidth/2;
        
        xMidpointFirst = (screenWidth/4)+xOffsetFirst;
        yMidpointFirst = screenHeight/2;
        xMidpointSecond = (screenWidth/4)+xOffsetSecond;
        yMidpointSecond = screenHeight/2;
        
    
        System.out.println("xMidpointFirst: "+xMidpointFirst+", yMidpointFirst: "+yMidpointFirst+",xMidpointSecond: "+xMidpointSecond+",yMidpointSecond: "+yMidpointSecond);

        touchOverlay.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
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
           	    		System.out.print(x1 + " " + y1);
           	    		
           	    		
           	    		//dont handle the second pointer if it exists in the same region as the first
           	    		if(pointerRegionFirst != pointerRegionSecond && pointerRegionSecond != ControlRegion.NONE){
           	    			if(pointerRegionSecond == ControlRegion.SECOND){
           	    				//x2 = x2-xOffsetSecond;  //correct the x value to represent the relative x position in this control region
           	    				regionValueSecond = regionValueSecond(x2, y2);
               	    		}else{
               	    			x2 = x2-xOffsetSecond;  //correct the x value to represent the relative x position in this control region
               	    			regionValueSecond = regionValueSecond(x2, y2);
               	    		}
           	    			System.out.print(" - pointer 2: "+ x2+" "+y2);
           	    		}
           	    		System.out.print("\n");
           	    		
           	    		calcMotorSpeeds(regionValueFirst, regionValueSecond);
           	    		
           	    		break;
        	  		}
        	  	}

        	  return true;
            }
        });
        
        
        doConnect = (Button) findViewById(R.id.doConnect);  
        
        doConnect.setText("Connect");
         doConnect.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(!connected){
					connected = true;
					 doConnect.setText("Disconnect");
					 Thread cThread = new Thread(new ClientThread());
		 		     cThread.start();
				}else{
					doConnect.setText("Connect");
					connected= false;
				}
				
			}
         });
     
    }
	
	public enum ControlRegion {
		NONE, FIRST, SECOND
	}
	
	
	
	public int regionValueFirst(int x, int y){
		int speedX = findRelativeControlValue(screenWidth/2, xMidpointFirst, x);
		int speedY = findRelativeControlValue(screenHeight, yMidpointFirst, y);
		
		return speedY;
		
    }
    public int regionValueSecond(int x, int y){
    	int speedX = findRelativeControlValue((screenWidth/2), xMidpointSecond-xOffsetSecond, x-xOffsetSecond);
    	int speedY = findRelativeControlValue(screenHeight, yMidpointSecond, y); 
    	
    	return speedX;

    }
    
    public void calcMotorSpeeds(int regionValueFirst, int regionValueSecond) {
    	//regionFirst is the throttle, regionSecond the steering
    	
    	
    	//first find the direction of the throttle
   		if(regionValueFirst < 0){
			regionValueFirst = regionValueFirst - (regionValueFirst*2); //invert the speed if it is negative
			motorDirectionFirst = 0;
			motorDirectionSecond = 0;
		}else{
			motorDirectionFirst = 1;
			motorDirectionSecond = 1;
		}
   		
		
		int motorSpeedLeft = regionValueFirst;
		int motorSpeedRight = regionValueFirst;
		//regionValueSecond will be a negative number when turning right
		//logically, the right value should be subtracted from the left wheel's throttle, and vice versa
		System.out.println(regionValueSecond);
		if(regionValueSecond >= 0){ //turning left
			if(motorDirectionFirst == 1){
				motorSpeedRight = regionValueFirst - regionValueSecond; //slow down the right wheel
			}else{
				//going backwards, slow down the opposite wheel for correct steering behaviour
				motorSpeedLeft = regionValueFirst - regionValueSecond; //slow down the right wheel
			}

		}else{
			if(motorDirectionSecond == 1){
				motorSpeedLeft = regionValueFirst + regionValueSecond; //slow down the left wheel - using + as regionValueSecond is a negative number
			}else{
				//going backwards, slow down the opposite wheel for correct steering behaviour
				motorSpeedRight = regionValueFirst - regionValueSecond; //slow down the right wheel
			}
			
		}
		
		//avoid negative values
		if(motorSpeedLeft < 0){
			motorSpeedLeft = 0;
		}
		if(motorSpeedRight < 0){
			motorSpeedRight = 0;
		}
		motorSpeedFirst = motorSpeedLeft;
		motorSpeedSecond = motorSpeedRight;
   
    }
    
    //calculates a value between 0 and 100 in the negative or positive direction (-100 to 100), of and input point (int input) from a reference point (int referencePoint) within the defined bounds (controlBounds)
    //referencePoint may be an x or y midpoint, controlBounds the width or height of the control area, and input is the x or y coords of a touch event
    public int findRelativeControlValue(int controlBounds, int referencePoint, int inputPoint) {
    	//System.out.println("input: "+controlBounds + ", "+referencePoint+", "+inputPoint);
    	float controlValue = 0;
    	float trueBounds = controlBounds/2; //as calculations are in either the positive or negative, either calculation will only involve half of the total control bounds
    	float distance = 0;
    	
    	//find distance input is from referencePoint (in positive or negative)
    	distance = referencePoint-inputPoint;
    	
    	//find the percentage that distance is of the total trueBounds
    	controlValue = (distance/trueBounds)*255;
    	//System.out.println("values: "+trueBounds + ", "+distance+", "+controlValue);
    	return (int) FloatMath.floor(controlValue);
    }
    
    public int findRealSpeed(int range, int inputX, int inputY) {
    	System.out.println("inputs: "+range+", "+ inputX + ", " + inputY);;
    	try{
    		float percent = (range/inputY);
        	System.out.print(percent);
        	System.out.print("\n");
        	return (int) FloatMath.floor(percent);
    	}catch(Exception ex){
    		System.out.println(ex.getMessage());
    		return 0;
    	}
    	
    }
    
    //find the section of the screen a touch is occurring in
    public ControlRegion FindControlRegion(float x){
    	int xPos = (int) FloatMath.floor(x);
    	ControlRegion foundRegion;
    	if(xPos >= xOffsetSecond){
    		foundRegion = ControlRegion.SECOND;
    	}else{
    		foundRegion = ControlRegion.FIRST;
    	}
    	
    	
    	return foundRegion;
    }
    
  
	
	  public class ClientThread implements Runnable {
		 
	        public void run() {
	        	try{
	        		while(connected){

	        			RCSocket rcSocket = new RCSocket("10.1.1.10", 8888);
	        			rcSocket.sendValues(motorSpeedFirst,motorSpeedSecond,motorDirectionFirst,motorDirectionSecond);
	        			rcSocket.close();
	        			Thread.sleep(500);
	        		}
		    	}catch(IOException ioEx){
		    		
		    	/*	Context context = getApplicationContext();
		    		CharSequence text = "Could Not Connect To Server!";
		    		int duration = Toast.LENGTH_SHORT;

		    		Toast toast = Toast.makeText(context, text, duration);
		    		toast.show();*/
		    		
		    		System.out.println("ERROR: "+ioEx.getMessage());
		    		connected = false;
		    		//doConnect.setText("Connect");
		    		
		    	} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}       	
	        	
	        }
	    }
	  

}
