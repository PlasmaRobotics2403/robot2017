/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.*;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class GearLiftAlign extends TurnAngle implements Action {
	
	public GearLiftAlign(DriveTrain drive, NetworkTable table){
		super(.3, table.getNumber(Constants.GEAR_OUTPUT_ANGLE_NAME, 0) - drive.getGyroAngle(), drive);
	}
}
