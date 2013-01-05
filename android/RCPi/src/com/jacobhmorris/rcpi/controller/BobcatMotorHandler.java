package com.jacobhmorris.rcpi.controller;

public class BobcatMotorHandler extends MotorHandler {
	public BobcatMotorHandler(int screenWidth, int screenHeight) {
		super(screenWidth, screenHeight);
	}
	@Override
	public void calcMotorSpeeds(int x1, int y1, int x2, int y2) {
		//x axises are ignore in this implelemtation
		int motorSpeedFirst = (y1==0) ? 0 : findRelativeControlValue(screenHeight, yMidpointFirst, y1);
		int motorSpeedSecond = (y2==0) ? 0 :  findRelativeControlValue(screenHeight, yMidpointSecond, y2);
		setMotorSpeeds(motorSpeedFirst, motorSpeedSecond);
	}

}
