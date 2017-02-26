package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.Encoder;

public class Turret {
	
	private CANTalon shooterLeft;
	private CANTalon shooterRight;
	private CANTalon lazySusan;
	//private Encoder enc;
	
	/**
	 * Constructor for turret object
	 * 
	 * @param leftID - ID of left (flywheel) shooter Talon
	 * @param rightID - ID of right (flywheel) shooter Talon
	 * @param lazyID - ID of lazy susan Talon
	 * 
	 * @author Brandon R
	 */	
	public Turret(int leftID, int rightID, int lazyID){
		
		shooterLeft = new CANTalon(leftID);
		shooterRight = new CANTalon(rightID);
		lazySusan = new CANTalon(lazyID);
		
		lazySusan.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		lazySusan.setPulseWidthPosition(lazySusan.getPulseWidthPosition() - Constants.TURRET_ABS_ENC_OFFSET);
	}
	
	public void pivot(double speed){
		lazySusan.set(-speed * Constants.MAX_SPIN_SPEED);
		SmartDashboard.putNumber("turret angle", lazySusan.getPosition());
	}
	
	public void shoot(double speed){
		shooterLeft.set(-speed);
		shooterRight.set(speed);
	}
	
	
	
}
