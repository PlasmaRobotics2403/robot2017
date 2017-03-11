/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.*;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class CenterGearHang extends AutoMode {
	
	DriveTrain drive;
	GearManipulator gearManip;
	
	public CenterGearHang(DriveTrain drive, GearManipulator gearManip){
		this.drive = drive;
		this.gearManip = gearManip;
	}
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraight(.3, 70, drive));
		runAction(new OpenGearManipulator(gearManip));
		runAction(new DriveStraight(.3, -5, drive));
		runAction(new CloseGearManipulator(gearManip));
		runAction(new DriveStraight(.3, 8, drive));
		runAction(new DriveStraight(.4, -30, drive));
	}

}
