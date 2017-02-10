package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;

public class Climb {

	CANTalon climbL;
	CANTalon climbR;
	
	public Climb(int leftID, int rightID){
		climbL = new CANTalon(leftID);
		climbR = new CANTalon(rightID);
	}
	
	public void spin(double speed){
		climbL.set(speed);
		climbR.set(-speed);
	}
	
}
