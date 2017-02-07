package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;

public class Turret {
	
	private CANTalon shooterLeft;
	private CANTalon shooterRight;
	private CANTalon lazySusan;
	
	/**
	 * Constructor for turret object
	 * 
	 * @param leftID - ID of left (flywheel) shooter Talon
	 * @param rightID - ID of right (flywheel) shooter Talon
	 * @param lazyID - ID of lazy Susan Talon
	 * 
	 * @author Brandon R
	 */	
	public Turret(int leftID, int rightID, int lazyID){
		
		shooterLeft = new CANTalon(leftID);
		shooterRight = new CANTalon(rightID);
		lazySusan = new CANTalon(lazyID);
		
	}

}
