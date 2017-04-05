/**
 * 
 */
package org.usfirst.frc.team2403.robot.auto.actions;

import org.usfirst.frc.team2403.robot.Intake;
import org.usfirst.frc.team2403.robot.Lift;
import org.usfirst.frc.team2403.robot.Turret;
import org.usfirst.frc.team2403.robot.auto.util.Action;

import edu.wpi.first.wpilibj.Timer;

/**
 *
 */
public class ShootShooterAuto implements Action {

	double angleHint;
	double time;
	Turret turret;
	Lift lift;
	Intake frontIntake;
	Intake backIntake;
	
	double startTime;
	boolean readyToFire;
	boolean hasHinted; 
	boolean isAimed;
	boolean isAtSpeed;
	
	public ShootShooterAuto(double angleHint, double time, Turret turret, Lift lift, Intake frontIntake, Intake backIntake){
		this.angleHint = angleHint;
		this.time = time;
		this.turret = turret;
		this.lift = lift;
		this.frontIntake = frontIntake;
		this.backIntake = backIntake;
		readyToFire = false;
		hasHinted = false;
		isAimed = false;
		isAtSpeed = false;
	}
	
	@Override
	public boolean isFinished() {
		return readyToFire && (Timer.getFPGATimestamp() > startTime + time);
	}

	@Override
	public void start() {
		startTime = 0;
	}

	@Override
	public void update() {
		if(!hasHinted){
			hasHinted = turret.spinToAngle(angleHint);
			turret.shoot(2000);
		}
		else{
			isAimed = turret.autoAim();
			isAtSpeed = turret.autoShoot();
		}
		
		turret.autoShoot();
		if(!readyToFire && isAtSpeed && isAimed){
			startTime = Timer.getFPGATimestamp();
			readyToFire = true;
		}
		if(readyToFire){
			lift.up(1);
			frontIntake.in(.5);
			backIntake.in(.5);
		}
	}

	@Override
	public void end() {
		lift.down(0);
		frontIntake.in(0);
		backIntake.in(0);
		turret.shoot(0);
	}

}
