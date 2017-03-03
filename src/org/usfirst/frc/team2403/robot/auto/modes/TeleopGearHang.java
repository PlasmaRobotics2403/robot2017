/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.*;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class TeleopGearHang extends AutoMode {
	
	DriveTrain drive;
	GearManipulator gearManip;
	NetworkTable table;
	
	public TeleopGearHang(DriveTrain drive, GearManipulator gearManip, NetworkTable table){
		this.drive = drive;
		this.gearManip = gearManip;
		this.table = table;
	}
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new GearLiftAlign(drive, table));
		runAction(new GearLiftApproach(.2, drive, table));
		runAction(new OpenGearManipulator(gearManip));
		runAction(new DriveStraight(.5, -24, drive));
		runAction(new CloseGearManipulator(gearManip));
	}

}
