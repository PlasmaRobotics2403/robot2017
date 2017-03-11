/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.modes;

import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.Lift;
import org.usfirst.frc.team2403.robot.Turret;
import org.usfirst.frc.team2403.robot.auto.actions.ShootShooter;
import org.usfirst.frc.team2403.robot.auto.util.AutoMode;
import org.usfirst.frc.team2403.robot.auto.util.AutoModeEndedException;

/**
 *
 */
public class ShootFuelCenter extends AutoMode {
	
	Turret turret;
	Lift lift;
	Intake intakeFront;
	Intake intakeBack;
	
	double sideMultiplier;
	
	public ShootFuelCenter(boolean isRed, Turret turret, Lift lift, Intake intakeFront, Intake intakeBack){
		this.turret = turret;
		this.lift = lift;
		this.intakeFront = intakeFront;
		this.intakeBack = intakeBack;
		sideMultiplier = (isRed) ? -1 : 1;
	}
	
	@Override
	protected void routine() throws AutoModeEndedException {
		runAction(new ShootShooter(2500, 92, 10, turret, lift, intakeFront, intakeBack));
	}

}
