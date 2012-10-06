package com.jacobmorris.rccontroller;

import java.io.*;
import java.net.*;
import com.jacobmorris.*;

import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.*;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

public class MainActivity extends Activity {
	private Button doConnect;
	boolean connected = false;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        FrameLayout main = (FrameLayout) findViewById(R.id.circleLayout1);
        FrameLayout main2 = (FrameLayout) findViewById(R.id.circleLayout2);
        
        main.addView(new Ball(this,100,100,100));
        
        main.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                System.out.println("Position 1: "+x+", "+y);
                return true;
            }
        });
        
    	main2.addView(new Ball(this,100,100,100));
        
        main2.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent e) {
                float x = e.getX();
                float y = e.getY();
                System.out.println("Position 2: "+x+", "+y);
                return true;
            }
        });
        doConnect = (Button) findViewById(R.id.doConnect);  
         
         doConnect.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(!connected){
					connected = true;
					 Thread cThread = new Thread(new ClientThread());
		 		     cThread.start();
				}else{
					connected= false;
				}
				
			}
         });
     
    }
	
	
	  public class ClientThread implements Runnable {
		 
	        public void run() {
	        while(connected){
	        	try{
	        		
		    		RCSocket rcSocket = new RCSocket("10.1.1.10", 8889);
		    		rcSocket.sendValues(122,211,1,1);
		    	}catch(IOException ioEx){
		    		System.out.println("ERROR: "+ioEx.getMessage());
		    		connected = false;
		    	}
	        }
        	
	        	
	        }
	    }
	  
	  public class Ball extends View {
		    private final float x;
		    private final float y;
		    private final int r;
		    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		    public Ball(Context context, float x, float y, int r) {
		        super(context);
		        mPaint.setColor(0xFFFF0000);
		        this.x = x;
		        this.y = y;
		        this.r = r;
		    }
		    
		    @Override
		    protected void onDraw(Canvas canvas) {
		        super.onDraw(canvas);
		        canvas.drawCircle(x, y, r, mPaint);
		    }
		}
	
	  
}
