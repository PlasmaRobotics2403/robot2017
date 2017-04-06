/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.GearManipulator;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.Lift;
import org.usfirst.frc.team2403.robot.Turret;
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
	Turret turret;
	Lift lift;
	Intake intakeFront;
	Intake intakeBack;
	double sideMultiplier;
	
	public FeederGear(boolean isRed, DriveTrain drive, GearManipulator gearManip, Turret turret, Lift lift, Intake intakeFront, Intake intakeBack, NetworkTable table){
		this.drive = drive;
		this.gearManip = gearManip;
		this.turret = turret;
		this.table = table;
		this.lift = lift;
		this.intakeFront = intakeFront;
		this.intakeBack = intakeBack;
		sideMultiplier = (isRed) ? 1 : -1;
	}
	
	/* (non-Javadoc)
	 * @see org.usfirst.frc.team2403.robot.auto.util.AutoMode#routine()
	 */
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new MoveGearManipulator(gearManip, false, true));
		runAction(new DriveStraight(.35, 65, drive));
		runAction(new Wait(.5));
		runAction(new TurnAngle(.35, 60.0 * sideMultiplier, drive));
		runAction(new Wait(.5));
		runAction(new DriveStraight(.35, 40, drive));
		runAction(new Wait(.5));
		runAction(new GearLiftAlign(drive, table));
		runAction(new Wait(.5));
		runAction(new GearLiftApproach(.2, drive, table));
		runAction(new MoveGearManipulator(gearManip, true, false));
		runAction(new DriveStraight(-.4, 10, drive));
		runAction(new MoveGearManipulator(gearManip, false, false));
	}

}
