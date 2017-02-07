package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;

public class Lift {

	private CANTalon liftLeft;
	private CANTalon liftRight;
	
	/**
	 * Constructor for lift object
	 * 
	 * @param leftID - ID of left elevator lift Talon
	 * @param rightID - ID of right elevator lift Talon
	 * 
	 * @author Brandon R
	 */	
	public Lift(int leftID, int rightID){
		
		liftLeft = new CANTalon(leftID);
		liftRight = new CANTalon(rightID);
		
	}
	
}
