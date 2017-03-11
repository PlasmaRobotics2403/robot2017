/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.Lift;
import org.usfirst.frc.team2403.robot.Turret;
import org.usfirst.frc.team2403.robot.auto.actions.DriveStraight;
import org.usfirst.frc.team2403.robot.auto.actions.ShootShooter;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class FuelBaseline extends AutoMode {

	Turret turret;
	Lift lift;
	Intake intakeFront;
	Intake intakeBack;
	DriveTrain drive;
	
	double sideMultiplier;
	
	public FuelBaseline(boolean isRed, Turret turret, Lift lift, Intake intakeFront, Intake intakeBack, DriveTrain drive){
		this.turret = turret;
		this.lift = lift;
		this.intakeFront = intakeFront;
		this.intakeBack = intakeBack;
		this.drive = drive;
		sideMultiplier = (isRed) ? -1 : 1;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new DriveStraight(.4, 90, drive));
		runAction(new ShootShooter(2400, 37 * sideMultiplier, 10, turret, lift, intakeFront, intakeBack));
	}

}
