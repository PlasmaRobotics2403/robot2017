/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.GearManipulator;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class CloseGearManipulator implements Action {

	GearManipulator gearManip;
	double endTime;
	
	public CloseGearManipulator(GearManipulator gearManip){
		this.gearManip = gearManip;
	}
	
	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() > endTime;
	}

	@Override
	public void start() {
		endTime = Timer.getFPGATimestamp() + 1;
	}

	@Override
	public void update() {
		gearManip.activate(false);
	}

	@Override
	public void end() {
	}

}
