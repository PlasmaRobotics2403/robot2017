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
	
	private boolean isMovingFast;
	private double rpm;
	
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
		lazySusan.setPulseWidthPosition(0);
		
		lazySusan.setProfile(0);
		
		shooterLeft.changeControlMode(TalonControlMode.Speed);
		shooterRight.changeControlMode(TalonControlMode.Speed);
		
		shooterRight.setF(1023.0/(Constants.MAX_TURRET_RPM / Constants.RPM_PER_ENC_VEL));
		shooterLeft.setF(1023.0/(Constants.MAX_TURRET_RPM / Constants.RPM_PER_ENC_VEL));
		shooterRight.setP(.1);
		shooterLeft.setP(.1);
		shooterRight.setD(0);
		shooterLeft.setD(0);
		shooterRight.setI(.0005);
		shooterLeft.setI(.0005);
		shooterRight.setVoltageRampRate(24);
		shooterLeft.setVoltageRampRate(24);
		
		lazySusan.setPID(.5, 0, 0, 0, 0, 0, 0);
		lazySusan.setCloseLoopRampRate(0);
		lazySusan.setPID(.1, .002, 2, 0, 0, 0, 1);
		
		table = visionTable;
		isMovingFast = false;
		rpm = 0;
	}
	
	/* TURNING */
	
	public void spin(double axis){
		if(!lazySusan.getControlMode().equals(TalonControlMode.PercentVbus)){
			lazySusan.changeControlMode(TalonControlMode.PercentVbus);
		}
		angle = table.getNumber(Constants.TURRET_OUTPUT_ANGLE_NAME, 0);
		lazySusan.set(axis);
	}
	
	int lastSide = 0;
	public boolean spinToAngle(double angle){
		if(!lazySusan.getControlMode().equals(TalonControlMode.Position)){
			lazySusan.changeControlMode(TalonControlMode.Position);
		}
		
		double rots = toNumRotations(angle);
		double err = angle - toAngle(lazySusan.getPosition());
		int side = (int)(err/Math.abs(err));
		if(Math.abs(err) < 5){
			lazySusan.setProfile(1);
			isMovingFast = false;
		}
		else{
			lazySusan.setProfile(0);
			isMovingFast = true;
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
		return Math.abs(angle - getCurrentAngle()) < 1;
	}
	
	double angle = 0;
	public boolean autoAim(){
		if(!isMovingFast){
			angle = table.getNumber(Constants.TURRET_OUTPUT_ANGLE_NAME, 0);
		}
		spinToAngle(angle);
		return Math.abs(angle - getCurrentAngle()) < 1;
	}
	
	/* SHOOTING */
	
	public void shoot(double speed){
		shooterLeft.changeControlMode(TalonControlMode.Speed);
		shooterRight.changeControlMode(TalonControlMode.Speed);
		double targetSpeed = speed * (1.0/Constants.ENCODER_UPDATES_PER_MIN) * Constants.ENCODER_COUNTS_PER_ROT;
		if(targetSpeed == 0){
			shooterLeft.clearIAccum();
			shooterRight.clearIAccum();
		}
		shooterLeft.set(targetSpeed);
		shooterRight.set(-targetSpeed);
		SmartDashboard.putNumber("shoot speed left", shooterLeft.getSpeed()*Constants.RPM_PER_ENC_VEL);
		SmartDashboard.putNumber("shoot speed right", shooterRight.getSpeed()*Constants.RPM_PER_ENC_VEL);
		SmartDashboard.putNumber("right err", shooterRight.getError()*Constants.RPM_PER_ENC_VEL);
		SmartDashboard.putNumber("left err", shooterLeft.getError()*Constants.RPM_PER_ENC_VEL);
		SmartDashboard.putNumber("left I Acc", shooterLeft.GetIaccum());
		SmartDashboard.putNumber("right I Acc", shooterRight.GetIaccum());
		SmartDashboard.putNumber("ramp", shooterLeft.getCloseLoopRampRate());
		
	}
	
	public boolean autoShoot(){
		double tempRPM = table.getNumber(Constants.TURRET_OUTPUT_RPM_NAME, 0);
		if(tempRPM != -1) rpm = tempRPM;
		shoot(rpm);
		SmartDashboard.putNumber("wanted RPM", rpm);
		return Math.abs(rpm - getShooterSpeed()) < 100;
	}
	
	/* CONVERSIONS */
	
	public static double toNumRotations(double angle){
		return angle * Constants.TURRET_ROTS_PER_DEGREE;
	}
	
	public static double toAngle(double rots){
		return rots / Constants.TURRET_ROTS_PER_DEGREE;
	}
	
	/* GETTERS */
	
	public double getCurrentAngle(){
		return toAngle(lazySusan.getPosition());
	}
	
	public double getShooterSpeed(){
		return shooterLeft.getSpeed()*Constants.RPM_PER_ENC_VEL;
	}
	
	
	
}
