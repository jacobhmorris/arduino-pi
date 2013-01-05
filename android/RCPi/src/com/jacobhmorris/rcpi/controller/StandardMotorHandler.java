package com.jacobhmorris.rcpi.controller;

public class StandardMotorHandler extends MotorHandler {
	public StandardMotorHandler(int screenWidth, int screenHeight) {
		super(screenWidth, screenHeight);
	}

	@Override
	public void calcMotorSpeeds(int x1, int y1, int x2, int y2) {
		//x1 gets ignored in this implementation as the first region only controls throttle (Y axis)
		
		int throttleSpeed = (y1==0) ? 0 : findRelativeControlValue(screenHeight, yMidpointFirst, y1);
		int steeringX = (x2==0) ? 0 : findRelativeControlValue((screenWidth/2), x2-xOffsetSecond, xMidpointSecond-xOffsetSecond);
		int steeringY = (y2==0) ? 0 :  findRelativeControlValue(screenHeight, yMidpointSecond, y2);
    		
		//apply throttle to both motors before applying steering maodifications
		int motorSpeedFirst = throttleSpeed;
		int motorSpeedSecond = throttleSpeed;
		
		
		motorSpeedFirst = (throttleSpeed - steeringX) + steeringY;
		motorSpeedSecond = (throttleSpeed + steeringX) + steeringY;
		
		setMotorSpeeds(motorSpeedFirst, motorSpeedSecond);
		
	}
}
