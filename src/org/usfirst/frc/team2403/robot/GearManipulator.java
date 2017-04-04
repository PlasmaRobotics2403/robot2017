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
	 * @author Nic, Alex and Troy
	 */	
	public GearManipulator(int doorLeftID, int doorRightID, int pushLeftID, int pushRightID){
		
		doorLeft = new Servo(doorLeftID);
		doorRight = new Servo(doorRightID);
		pusherLeft = new Servo(pushLeftID);
		pusherRight = new Servo(pushRightID);		
	}
	
	public void activateNoDelay(boolean open){
		if(open){
			doorLeft.setAngle(90);
			doorRight.setAngle(0);
			pusherLeft.setAngle(90);
			pusherRight.setAngle(0);
		}
		else{
			doorLeft.setAngle(0);
			doorRight.setAngle(90);
			pusherLeft.setAngle(0);
			pusherRight.setAngle(90);
		}
	}
	
	boolean wasOpen = false;
	double timeToPush = 0;
	public void activateComp(boolean open){
		if(open){
			doorLeft.setAngle(90);
			doorRight.setAngle(0);
			
			if(wasOpen == false){
				timeToPush = Timer.getFPGATimestamp() + .1;
			}
			
			if(Timer.getFPGATimestamp() > timeToPush){
				pusherLeft.setAngle(90);
				pusherRight.setAngle(0);
			}
			else{
				pusherLeft.setAngle(0);
				pusherRight.setAngle(90);
			}
		}
		else{
			doorLeft.setAngle(0);
			doorRight.setAngle(90);
			pusherLeft.setAngle(0);
			pusherRight.setAngle(90);
		}
		wasOpen = open;
		
	}
	
	boolean clampModifier = false;
	public void activate(boolean open, boolean clamp){
		if(open){
			doorLeft.setAngle(55);
			doorRight.setAngle(125);
			
			if(wasOpen == false){
				timeToPush = Timer.getFPGATimestamp() + .1;
				if(clamp != clampModifier){
					clampModifier = !clampModifier;
				}
			}
			
			if(Timer.getFPGATimestamp() > timeToPush){
				pusherLeft.setAngle(90);
				pusherRight.setAngle(0);
			}
			else{
				pusherLeft.setAngle(0);
				pusherRight.setAngle(90);
			}
		}
		else{
			doorLeft.setAngle(5);
			doorRight.setAngle(170);
			if(clamp != clampModifier){
				pusherLeft.setAngle(45);
				pusherRight.setAngle(45);
			}
			else{
				pusherLeft.setAngle(0);
				pusherRight.setAngle(90);
			}
		}
		wasOpen = open;
		
	}
	
	
}
