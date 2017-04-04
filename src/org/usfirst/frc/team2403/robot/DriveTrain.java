package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.*;

import com.kauailabs.navx.frc.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.*;

import com.ctre.*;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TrajectoryPoint;


public class DriveTrain {

	private CANTalon talonLeft;
	private CANTalon talonLeftSlave;
	private CANTalon talonRight;
	private CANTalon talonRightSlave;
	
	private AHRS navX;
	
	private double gyroAngle;
	
	Notifier motionProfileUpdater;
	
	/**
	*
	*@param leftID - CAN ID of left main Talon SRX
	*@param leftSID - CAN ID of left slave Talon SRX
	*@param rightID - CAN ID of right main Talon SRX
	*@param righttSID - CAN ID of right slave Talon SRX
	*/
	
	public DriveTrain(int leftID, int leftSID, int rightID, int rightSID){
		
		talonLeft = new CANTalon(leftID);
		talonLeftSlave = new CANTalon(leftSID);
		talonRight = new CANTalon(rightID);
		talonRightSlave = new CANTalon(rightSID);
		
		talonLeftSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		talonLeftSlave.set(talonLeft.getDeviceID());
		
		talonRightSlave.changeControlMode(CANTalon.TalonControlMode.Follower);
		talonRightSlave.set(talonRight.getDeviceID());
		
		talonLeft.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		talonRight.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		
		
		talonLeft.setPosition(0);
		talonRight.setPosition(0);
		
		talonRight.setInverted(true);
		talonLeft.reverseSensor(true);
		
		navX = new AHRS(SPI.Port.kMXP);
		
		/*
		class PeriodicRunnable implements java.lang.Runnable {
		    public void run() {  talonLeft.processMotionProfileBuffer(); talonRight.processMotionProfileBuffer();    }
		}
		
		motionProfileUpdater = new Notifier(new PeriodicRunnable());
		*/
		
		gyroAngle = 0;
	}
	
	/* SENSOR METHODS */
	public void resetEncoders(){
		double dist = Math.abs(getDistance());
		talonLeft.setPosition(0);
		talonRight.setPosition(0);
		while(Math.abs(getDistance()) > dist - 1 && Math.abs(getDistance()) > 1){
			talonLeft.setPosition(0);
			talonRight.setPosition(0);
		}
	}
	
	public double getDistance(){
		return (toDistance(talonRight) + toDistance(talonLeft))/2;
	}
	
	public double getLeftDistance(){
		return toDistance(talonLeft);
	}
	
	private static double toDistance(CANTalon talon){
		return talon.getPosition() * Constants.DRIVE_ENCODER_CONVERSION;
	}
	
	public void updateGyro(){
		gyroAngle = navX.getYaw();
	}
	
	public double getGyroAngle(){
		return gyroAngle;
	}
	
	public void zeroGyro(){
		navX.zeroYaw();
	}
	
	/* CHANGE TALON MODES */
	
	public void setMotionProfileMode(){
		talonLeft.changeControlMode(CANTalon.TalonControlMode.MotionProfile);
		talonRight.changeControlMode(CANTalon.TalonControlMode.MotionProfile);
	}
	
	public void setNormalDriveMode(){
		talonLeft.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
		talonRight.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
	}
	
	/* DRIVING MODES */
	/**
	 * Drives robot based on FPS controls
	 * 
	 * @param forwardAxis - Joystick axis that controls forward motion
	 * @param turnAxis - Joystick axis that controls turning
	 * 
	 * @author Nic and Brandon R
	 */

	public void FPSDrive(PlasmaAxis forwardAxis, PlasmaAxis turnAxis){
		
		double forwardVal = forwardAxis.getFilteredAxis() * Math.abs(forwardAxis.getFilteredAxis());
		double turnVal = turnAxis.getFilteredAxis() * Math.abs(turnAxis.getFilteredAxis()) * Math.abs(turnAxis.getFilteredAxis());
		
		FPSDrive(forwardVal, turnVal);
		
	}
	
	public void FPSDrive(double forwardVal, double turnVal){
		
		turnVal *= Constants.MAX_DRIVE_TURN;
	
		double absForward = Math.abs(forwardVal);
		double absTurn = Math.abs(turnVal);
		
		int forwardSign = (forwardVal == 0) ? 0 : (int)(forwardVal / Math.abs(forwardVal));
		int turnSign = (turnVal == 0) ? 0 : (int)(turnVal / Math.abs(turnVal));
		
		double speedL;
		double speedR;
		
		if(turnVal == 0){ //Straight forward
			speedL = forwardVal;
			speedR = forwardVal;
		}
		else if(forwardVal == 0){ //Pivot turn
			speedL = turnVal;
			speedR = -turnVal;
		}
		else if(forwardSign == 1 && turnSign == 1){ //Forward right
			speedL = forwardVal;
			speedR = (absForward - absTurn < 0) ? 0 : absForward - absTurn;
		}
		else if(forwardSign == 1 && turnSign == -1){ //Forward left
			speedL = (absForward - absTurn < 0) ? 0 : absForward - absTurn;
			speedR = forwardVal;
		}
		else if(forwardSign == -1 && turnSign == 1){ //Backward right
			speedL = forwardVal;
			speedR = (absForward - absTurn < 0) ? 0 : -(absForward - absTurn);
		}
		else if(forwardSign == -1 && turnSign == -1){ //Backward left
			speedL = (absForward - absTurn < 0) ? 0 : -(absForward - absTurn);
			speedR = forwardVal;
		}
		else{
			speedL = 0;
			speedR = 0;
			DriverStation.reportError("Bug @ fps drive code - no case triggered)", false);
		}
		
		speedL *= Constants.MAX_DRIVE_SPEED;
		speedR *= Constants.MAX_DRIVE_SPEED;
		
		talonLeft.set(speedL);
		talonRight.set(speedR);
		
		
	}
	
	/**
	*Tank drive for automated driving
	*
	*@param left - Speed for left motor
	*@param right - Speed for right motor
	*
	*@author Nic and Brandon R
	*/
	public void autonTankDrive(double left, double right){
		leftWheelDrive(left);
		rightWheelDrive(right);
	}
	
	public void leftWheelDrive(double speed){
		talonLeft.set(speed * Constants.MAX_DRIVE_SPEED);
	}
	
	public void rightWheelDrive(double speed){
		talonRight.set(speed * Constants.MAX_DRIVE_SPEED);
	}
	
	public void gyroStraight(double speed, double angle){
		autonTankDrive(speed - 0.01*(getGyroAngle() - angle), speed + 0.01*(getGyroAngle() - angle));
	}
	
	public void pivotToAngle(double angle){
		double angleDiff = getGyroAngle() - angle;
		double speed = (Math.abs(angleDiff) < 10) ? (Math.abs(angleDiff) / 10.0) * 0.15 + 0.15 : .3;
		if(angleDiff > 0){
			autonTankDrive(-speed, speed);
		}
		else{
			autonTankDrive(speed, -speed);
		}
	}
	
	public void stopDrive(){
		autonTankDrive(0, 0);
	}
	
	/*FEEDBACK*/
	
	/**
	 * Prints drive data to the smart dashboard. This will probably be changed to the other dashboard when the custom dashboard works
	 * 
	 *
	 * @author Nic A
	 */
	public void reportDriveData(){
		SmartDashboard.putNumber("encoder", talonRight.getPosition());
		SmartDashboard.putNumber("distanceR", toDistance(talonRight));
		SmartDashboard.putNumber("distanceL", toDistance(talonLeft));
		SmartDashboard.putNumber("gyro", getGyroAngle());
		//DriverStation.reportWarning("" + navX.getLastSensorTimestamp(), false);
		//DriverStation.reportWarning("" + getGyroAngle(), false);
	}
	
	
	/*MOTION PROFILING*/
	
	private static TrajectoryPoint[] generatePoints(Trajectory t, boolean invert) {
		TrajectoryPoint[] points = new TrajectoryPoint[t.length()];
		for (int i = 0; i < points.length; i++) {
			Segment s = t.get(i);
			TrajectoryPoint point = new TrajectoryPoint();
			point.position = s.position;
			point.velocity = s.velocity * 60;
			point.timeDurMs = (int) (s.dt * 1000.0);
			point.profileSlotSelect = 1;
			point.velocityOnly = false;
			point.zeroPos = i == 0;
			point.isLastPoint = i == t.length() - 1;

			if (invert) {
				point.position = -point.position;
				point.velocity = -point.velocity;
			}

			points[i] = point;
		}
		return points;
	}
	
	public void loadTrajectories(Trajectory left, Trajectory right){
		setMotionProfileMode();
		talonLeft.set(0);
		talonRight.set(0);
		talonLeft.clearMotionProfileTrajectories();
		talonRight.clearMotionProfileTrajectories();
		TrajectoryPoint[] trajectoryLeft = generatePoints(left, false);
		TrajectoryPoint[] trajectoryRight = generatePoints(right, false);
		for(int i = 0; i < trajectoryLeft.length; i++){
			talonLeft.pushMotionProfileTrajectory(trajectoryLeft[i]);
		}
		for(int i = 0; i < trajectoryRight.length; i++){
			talonRight.pushMotionProfileTrajectory(trajectoryRight[i]);
		}
	}
	
	public void startFollowing(){
		setMotionProfileMode();
		talonLeft.changeMotionControlFramePeriod(5);
		talonRight.changeMotionControlFramePeriod(5);
		motionProfileUpdater.startPeriodic(0.005);
		while(talonLeft.getMotionProfileTopLevelBufferCount() < 3 || talonRight.getMotionProfileTopLevelBufferCount() < 3 ){
		}
		talonLeft.set(1);
		talonRight.set(1);
	}
	
	public void stopFollowing(){
		talonLeft.changeMotionControlFramePeriod(5);
		talonRight.changeMotionControlFramePeriod(5);
		motionProfileUpdater.startPeriodic(0.005);
	}
	


}	