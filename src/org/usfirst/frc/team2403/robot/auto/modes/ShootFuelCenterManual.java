/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.Lift;
import org.usfirst.frc.team2403.robot.Turret;
import org.usfirst.frc.team2403.robot.auto.actions.ShootShooterManual;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class ShootFuelCenterManual extends AutoMode {
	
	Turret turret;
	Lift lift;
	Intake intakeFront;
	Intake intakeBack;
	
	double sideMultiplier;
	
	public ShootFuelCenterManual(boolean isRed, Turret turret, Lift lift, Intake intakeFront, Intake intakeBack){
		this.turret = turret;
		this.lift = lift;
		this.intakeFront = intakeFront;
		this.intakeBack = intakeBack;
		sideMultiplier = (isRed) ? -1 : 1;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new ShootShooterManual(2300, 82 * sideMultiplier, 10, turret, lift, intakeFront, intakeBack));
	}

}
