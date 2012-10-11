package com.jacobmorris.rccontroller;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
public class DrawMidpoint extends View {
	Paint paint = new Paint();
	float pointX;
	float pointY;
	float lineLength;
    public DrawMidpoint(Context context, int pointX, int pointY, int lineLength) {
        super(context);
        this.pointX = pointX;
        this.pointY = pointY;
        this.lineLength = lineLength;
        
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDraw(Canvas canvas) {
    	//draw x plane
    	canvas.drawLine(pointX-(lineLength/2), pointY, pointX+(lineLength/2), pointY, paint);
        
    	//draw y plane
    	canvas.drawLine(pointX, pointY-(lineLength/2), pointX, pointY+(lineLength/2), paint);
    }

}
