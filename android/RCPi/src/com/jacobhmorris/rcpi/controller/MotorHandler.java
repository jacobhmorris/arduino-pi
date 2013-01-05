package com.jacobhmorris.rcpi.controller;

import android.util.FloatMath;

import com.jacobhmorris.rcpi.model.Motor;
import com.jacobhmorris.rcpi.model.Motor.Direction;

public abstract class MotorHandler {
	protected Motor motorFirst, motorSecond;
	protected int screenWidth, screenHeight, xOffsetFirst, xOffsetSecond;
	protected int xMidpointFirst, yMidpointFirst, xMidpointSecond, yMidpointSecond;
	
	public MotorHandler(int screenWidth, int screenHeight) {
		//set up the screen properties
		this.screenHeight = screenHeight;
		this.screenWidth = screenWidth;
		this.xOffsetFirst = 0;
		this.xOffsetSecond = screenWidth/2;
        
		this.xMidpointFirst = (screenWidth/4)+xOffsetFirst; //UNUSED
		this.yMidpointFirst = screenHeight/2;
		this.xMidpointSecond = (screenWidth/4)+xOffsetSecond;
		this.yMidpointSecond = screenHeight/2;
		
		motorFirst = new Motor(0, Direction.FORWARDS);
		motorSecond = new Motor(0, Direction.FORWARDS);
	}
	
	public abstract void calcMotorSpeeds(int x1, int y1, int x2, int y2);
	
	
	public Motor getMotorFirst() {
		return motorFirst;
	}
	public Motor getMotorSecond() {
		return motorSecond;
	}
	
	//calculates a value between 0 and 100 in the negative or positive direction (-100 to 100), of and input point (int input) from a reference point (int referencePoint) within the defined bounds (controlBounds)
    //referencePoint may be an x or y midpoint, controlBounds the width or height of the control area, and input is the x or y coords of a touch event
    protected int findRelativeControlValue(int controlBounds, int referencePoint, int inputPoint) {
    	float controlValue = 0;
    	float trueBounds = controlBounds/2; //as calculations are in either the positive or negative, either calculation will only involve half of the total control bounds
    	float distance = 0;
    	
    	//find distance input is from referencePoint (in positive or negative)
    	distance = referencePoint-inputPoint;
    	
    	//find the percentage that distance is of the total trueBounds
    	controlValue = (distance/trueBounds)*255;
    	
    	return (int) FloatMath.floor(controlValue);
    }
	
    protected void setMotorSpeeds(int motorSpeedFirst, int motorSpeedSecond) {
    	// find the direction of each wheel
   		if(motorSpeedFirst < 0){
   			motorSpeedFirst = motorSpeedFirst - (motorSpeedFirst*2); //invert the speed if it is negative
			motorFirst.setDirection(Direction.BACKWARDS);
		}else{
			motorFirst.setDirection(Direction.FORWARDS);
		}
   		if(motorSpeedSecond < 0){
   			motorSpeedSecond = motorSpeedSecond - (motorSpeedSecond*2); //invert the speed if it is negative
   			motorSecond.setDirection(Direction.BACKWARDS);
		}else{
			motorSecond.setDirection(Direction.FORWARDS);
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

		motorFirst.setSpeed(motorSpeedFirst);
		motorSecond.setSpeed(motorSpeedSecond);
    }
}
