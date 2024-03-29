/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.DriveTrain;
import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.Lift;
import org.usfirst.frc.team2403.robot.Turret;
import org.usfirst.frc.team2403.robot.auto.actions.*;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;


/**
 *
 */
public class ShootFuelCenter extends AutoMode {

	
	DriveTrain drive;
	Turret turret;
	Lift lift;
	Intake intakeFront;
	Intake intakeBack;
	double sideMultiplier;
	
	public ShootFuelCenter(boolean isRed, DriveTrain drive, Turret turret, Lift lift, Intake intakeFront, Intake intakeBack){
		this.drive = drive;
		this.turret = turret;
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
		runAction(new ShootShooterAuto(-85.0 * sideMultiplier, 15, turret, lift, intakeFront, intakeBack));
	}

}
