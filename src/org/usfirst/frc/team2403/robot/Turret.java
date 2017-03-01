package org.usfirst.frc.team2403.robot;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.wpilibj.Encoder;

public class Turret {
	
	private CANTalon shooterLeft;
	private CANTalon shooterRight;
	private CANTalon lazySusan;
	NetworkTable table;
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
	public Turret(int leftID, int rightID, int lazyID, NetworkTable visionTable){
		
		shooterLeft = new CANTalon(leftID);
		shooterRight = new CANTalon(rightID);
		lazySusan = new CANTalon(lazyID);
		lazySusan.changeControlMode(TalonControlMode.Position);
		lazySusan.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		lazySusan.setPulseWidthPosition(0);//lazySusan.getPulseWidthPosition() - Constants.TURRET_ABS_ENC_OFFSET);
		
		lazySusan.setProfile(0);
		
		//lazySusan.setPID(.3, 0, 0, 0, 0, 3.0, 0);
		//lazySusan.setCloseLoopRampRate(3.0);
		//lazySusan.setPID(.1, .0002, 2, 0, 0, 0, 1);
		
		table = visionTable;
	}
	
	public void pivot(double speed){
		lazySusan.set(-speed * Constants.MAX_SPIN_SPEED);
		SmartDashboard.putNumber("turret angle", lazySusan.getPosition());
	}
	
	public void shoot(double speed){
		shooterLeft.set(-speed);
		shooterRight.set(speed);
	}
	
	int lastSide = 0;
	public void testTurn(double angle){
		double rots = toNumRotations(angle);
		double err = angle - toAngle(lazySusan.getPosition());
		int side = (int)(err/Math.abs(err));
		if(Math.abs(err) < 5){
			lazySusan.setProfile(1);
		}
		else{
			lazySusan.setProfile(0);
			
		}
		if(Math.abs(err) < 0.5){
			lazySusan.clearIAccum();
			lazySusan.set(lazySusan.getPosition());
		}
		else{
			lazySusan.set(rots);
		}
		if(side * lastSide == -1){
			lazySusan.clearIAccum();
		}
		SmartDashboard.putNumber("turret angle", toAngle(lazySusan.getPosition()));
		lastSide = side;
	}
	double angle = 0;
	public void autoAim(boolean doUpdate){
		table.putNumber("photoAngle", toAngle(lazySusan.getPosition()));
		if(doUpdate){
			angle = table.getNumber("realAngleNeeded", 0);
		}
		testTurn(angle);
	}
	
	public static double toNumRotations(double angle){
		return angle * Constants.TURRET_ROTS_PER_DEGREE;
	}
	
	public static double toAngle(double rots){
		return rots / Constants.TURRET_ROTS_PER_DEGREE;
	}
	
	
	
}
