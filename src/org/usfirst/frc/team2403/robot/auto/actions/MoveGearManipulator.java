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
public class MoveGearManipulator implements Action {

	GearManipulator gearManip;
	double endTime;
	boolean open;
	boolean clamp;
	
	public MoveGearManipulator(GearManipulator gearManip, boolean open, boolean clamp){
		this.gearManip = gearManip;
		this.open = open;
		this.clamp = clamp;
	}
	
	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() > endTime;
	}

	@Override
	public void start() {
		endTime = Timer.getFPGATimestamp() + .5;
	}

	@Override
	public void update() {
		gearManip.activate(open, clamp);
	}

	@Override
	public void end() {
	}

}
