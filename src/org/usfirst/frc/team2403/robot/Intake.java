package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;


public class Intake {

	private CANTalon intake;
	
	/**
	 * Constructor for intake object
	 * 
	 * @param port - ID of intake Talon
	 * 
	 * @author Brandon R
	 */	
	public Intake(int port){
		
		intake = new CANTalon(port);
		
	}
	
}
