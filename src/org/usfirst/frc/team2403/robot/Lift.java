package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;

public class Lift {

	private CANTalon liftFront;
	private CANTalon liftRear;
	
	/**
	 * Constructor for lift object
	 * 
	 * @param frontID - ID of left elevator lift Talon
	 * @param rearID - ID of right elevator lift Talon
	 * 
	 * @author Brandon R
	 */	
	public Lift(int frontID, int rearID){
		liftFront = new CANTalon(frontID);
		liftRear = new CANTalon(rearID);
		
		liftRear.changeControlMode(CANTalon.TalonControlMode.Follower);
		liftRear.reverseOutput(true);
		liftRear.set(liftFront.getDeviceID());
		
		liftFront.setVoltageRampRate(Constants.GENERAL_RAMP_RATE);
		liftRear.setVoltageRampRate(Constants.GENERAL_RAMP_RATE);
	}
	
	
	public void spin(double speed){
		liftFront.set(speed * Constants.MAX_LIFT_SPEED);
	}
	
	public void up(double speed){
		spin(-Math.abs(speed));
	}
	
	public void down(double speed){
		spin(Math.abs(speed));
	}
	
}
