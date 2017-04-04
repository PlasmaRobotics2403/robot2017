package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;

public class Climb {

	private CANTalon climbLeft;
	private CANTalon climbRight;
	
	/**
	 * 
	 * @param leftID - ID of the left climb talon
	 * @param rightID - ID of the right climb talon
	 */
	public Climb(int leftID, int rightID){
		climbLeft = new CANTalon(leftID);
		climbRight = new CANTalon(rightID);
		
		climbRight.changeControlMode(CANTalon.TalonControlMode.Follower);
		climbRight.reverseOutput(true);
		climbRight.set(climbLeft.getDeviceID());
	}
	
	/**
	 * Raises the motor up. There is no way to go down as it would break the ratcheting system
	 * 
	 * @param speed - Motor speed for the climb
	 *
	 * @author Nic A
	 */
	public void up(double speed){
		climbLeft.set(-Math.abs(speed));
	}
	
	
}
