package org.usfirst.frc.team2403.robot;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class GearManipulator {

	private Servo doorLeft;
	private Servo doorRight;
	private Servo pusherLeft;
	private Servo pusherRight;
	public 	NetworkTable table;
	/**
	 * Constructor for gear manipulator object
	 * 
	 * @param doorLeftID - ID of left door Talon
	 * @param doorRightID - ID of right door Talon
	 * @param pushLeftID - ID of left pusher Talon
	 * @param pushRightID - ID of right pusher Talon
	 * 
	 * @author Nic, Alex and Troy
	 */	
	public GearManipulator(int doorLeftID, int doorRightID, int pushLeftID, int pushRightID){
		
		doorLeft = new Servo(doorLeftID);
		doorRight = new Servo(doorRightID);
		pusherLeft = new Servo(pushLeftID);
		pusherRight = new Servo(pushRightID);
		
	}
	
	public void activate(boolean open){
		if(open){
			doorLeft.setAngle(Constants.DOOR_OPEN_ANGLE);
			doorRight.setAngle(Constants.MAX_ANGLE - Constants.DOOR_OPEN_ANGLE);
			pusherLeft.setAngle(Constants.PUSH_ANGLE);
			pusherRight.setAngle(Constants.MAX_ANGLE - Constants.PUSH_ANGLE);
		}
		else{
			doorLeft.setAngle(Constants.DOOR_START_ANGLE);
			doorRight.setAngle(Constants.MAX_ANGLE - Constants.DOOR_START_ANGLE);
			pusherLeft.setAngle(Constants.PUSH_START_ANGLE);
			pusherRight.setAngle(Constants.MAX_ANGLE - Constants.PUSH_START_ANGLE);
		}
		
		
	}
	
}
