package com.jacobhmorris.rcpi.model;

import android.util.FloatMath;

public class ControlRegion {
	int Height, Width;
	int xMidpoint, yMidpoint;
	int xOffset, yOffset;
	int xValue, yValue;
	
	public ControlRegion(int Height, int Width, int xMidpoint, int yMidpoint, int xOffset, int yOffset) {
		this.Height = Height;
		this.Width = Width;
		this.xMidpoint = xMidpoint;
		this.yMidpoint = yMidpoint;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.xValue = 0;
		this.yValue = 0;
	}
	
	protected int RelativeValue(int controlBounds, int referencePoint, int inputPoint) {
		float controlValue = 0;
    	float trueBounds = controlBounds/2; //as calculations are in either the positive or negative, either calculation will only involve half of the total control bounds
    	float distance = 0;
    	
    	//find distance input is from referencePoint (in positive or negative)
    	distance = referencePoint-inputPoint;
    	
    	//find the percentage that distance is of the total trueBounds
    	controlValue = (distance/trueBounds)*255;
    	
    	return (int) FloatMath.floor(controlValue);
	}
	
	protected void SetX(int x){
		this.xValue = x;
	}
	protected void SetY(int y) {
		this.yValue = y;
	}
	protected int GetX() {
		return this.xValue;
	}
	protected int GetY() {
		return this.yValue;
	}
	
}
