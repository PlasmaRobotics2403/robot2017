package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;


public class Intake {

	private CANTalon intake;
	
	/**
	 * Constructor for intake object
	 * 
	 * @param ID - ID of intake Talon
	 * 
	 * @author Brandon R
	 */	
	public Intake(int ID){
		intake = new CANTalon(ID);
		intake.setVoltageRampRate(Constants.GENERAL_RAMP_RATE);
	}
	
	
	public void spin(double speed){
		intake.set(speed * Constants.MAX_LIFT_SPEED);
	}
	
	public void in(double speed){
		spin(-Math.abs(speed));
	}
	
	public void out(double speed){
		spin(Math.abs(speed));
	}
	
}
