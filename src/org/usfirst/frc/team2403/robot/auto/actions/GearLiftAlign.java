/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.*;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class GearLiftAlign implements Action {
	
	DriveTrain drive;
	
	NetworkTable table;
	
	private double angle;
	
	public GearLiftAlign(DriveTrain drive, NetworkTable table){
		this.drive = drive;
		this.table = table;
		angle = drive.getGyroAngle() + 90; //Ensure angle !~= navX angle
	}
	
	@Override
	public boolean isFinished() {
		return Math.abs(drive.getGyroAngle() - angle) < 2;
	}

	@Override
	public void start() {
		angle = table.getNumber(Constants.GEAR_OUTPUT_ANGLE_NAME, 0);
	}

	@Override
	public void update() {
		drive.pivotToAngle(angle);
	}

	@Override
	public void end() {
		drive.stopDrive();
	}

}
