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
public class OpenGearManipulator implements Action {

	GearManipulator gearManip;
	double endTime;
	
	public OpenGearManipulator(GearManipulator gearManip){
		this.gearManip = gearManip;
	}
	
	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() > endTime;
	}

	@Override
	public void start() {
		gearManip.activate(false, false);
		endTime = Timer.getFPGATimestamp() + 1;
	}

	@Override
	public void update() {
		gearManip.activate(true, false);
	}

	@Override
	public void end() {
	}

}
