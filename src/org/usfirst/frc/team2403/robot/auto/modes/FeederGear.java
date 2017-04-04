/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.GearManipulator;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 *
 */
public class FeederGear extends AutoMode {

	
	DriveTrain drive;
	GearManipulator gearManip;
	NetworkTable table;
	
	public FeederGear(DriveTrain drive, GearManipulator gearManip, NetworkTable table){
		this.drive = drive;
		this.gearManip = gearManip;
		this.table = table;
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2403.robot.auto.util.AutoMode#routine()
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new MoveGearManipulator(gearManip, false, true));
		runAction(new GearLiftAlign(drive, table));
		runAction(new Wait(.5));
		runAction(new GearLiftApproach(.2, drive, table));
		runAction(new MoveGearManipulator(gearManip, true, false));
		runAction(new DriveStraight(-.4, 10, drive));
		runAction(new MoveGearManipulator(gearManip, false, false));
	}

}
