/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Constants;
import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class GearLiftApproach implements Action {

	DriveTrain drive;
	
	NetworkTable table;
	
	private double angle;
	private double distance;
	
	private double speed;
	
	public GearLiftApproach(double speed, DriveTrain drive, NetworkTable table){
		this.drive = drive;
		this.table = table;
		this.speed = speed;
		angle = drive.getGyroAngle() + 90; //Ensure angle !~= navX angle
		distance = 1000;
	}
	
	@Override
	public boolean isFinished() {
		return (float)(distance - drive.getDistance()) < 12;
	}

	@Override
	public void start() {
		angle = table.getNumber(Constants.GEAR_OUTPUT_ANGLE_NAME, 0);
		distance = table.getNumber(Constants.GEAR_OUTPUT_DISTANCE_NAME, 10);
		drive.resetEncoders();
	}

	@Override
	public void update() {
		angle = table.getNumber(Constants.GEAR_OUTPUT_ANGLE_NAME, 0);
		drive.gyroStraight(speed, angle);
	}

	@Override
	public void end() {
		drive.stopDrive();
	}

}
