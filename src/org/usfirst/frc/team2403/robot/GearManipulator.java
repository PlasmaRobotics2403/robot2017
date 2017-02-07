package org.usfirst.frc.team2403.robot;

import edu.wpi.first.wpilibj.*;

public class GearManipulator {

	private Servo doorLeft;
	private Servo doorRight;
	private Servo pusherLeft;
	private Servo pusherRight;
	
	/**
	 * Constructor for gear manipulator object
	 * 
	 * @param doorLeftID - ID of left door Talon
	 * @param doorRightID - ID of right door Talon
	 * @param pushLeftID - ID of left pusher Talon
	 * @param pushRightID - ID of right pusher Talon
	 * 
	 * @author Brandon R
	 */	
	public GearManipulator(int doorLeftID, int doorRightID, int pushLeftID, int pushRightID){
		
		doorLeft = new Servo(doorLeftID);
		doorRight = new Servo(doorRightID);
		pusherLeft = new Servo(pushLeftID);
		pusherRight = new Servo(pushRightID);
		
	}
	
}
