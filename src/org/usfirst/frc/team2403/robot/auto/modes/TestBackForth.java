/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class TestBackForth extends AutoMode {
	
	DriveTrain drive;
	
	public TestBackForth(DriveTrain drive){
		this.drive = drive;
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2403.robot.auto.util.AutoMode#routine()
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraight(.5, 24, drive));
		runAction(new DriveStraight(.5, -24, drive));
		
	}

}
