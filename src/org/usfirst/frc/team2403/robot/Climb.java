package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;

public class Climb {

	private CANTalon climbLeft;
	private CANTalon climbRight;
	
	public Climb(int leftID, int rightID){
		climbLeft = new CANTalon(leftID);
		climbRight = new CANTalon(rightID);
		
		climbRight.changeControlMode(CANTalon.TalonControlMode.Follower);
		climbRight.reverseOutput(true);
		climbRight.set(climbLeft.getDeviceID());
	}
	
	public void up(double speed){
		climbLeft.set(-Math.abs(speed));
	}
	
	
}
