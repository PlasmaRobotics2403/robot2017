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
public class ShootShooter implements Action {

	double rpm;
	double angle;
	double time;
	Turret turret;
	Lift lift;
	Intake frontIntake;
	Intake backIntake;
	
	double startTime;
	boolean hasStartedFiring;
	
	public ShootShooter(double rpm, double angle, double time, Turret turret, Lift lift, Intake frontIntake, Intake backIntake){
		this.rpm = rpm;
		this.angle = angle;
		this.time = time;
		this.turret = turret;
		this.lift = lift;
		this.frontIntake = frontIntake;
		this.backIntake = backIntake;
		hasStartedFiring = false;
	}
	
	@Override
	public boolean isFinished() {
		return hasStartedFiring && (Timer.getFPGATimestamp() > startTime + time);
	}

	@Override
	public void start() {
		startTime = 0;
	}

	@Override
	public void update() {
		turret.spinToAngle(angle);
		turret.shoot(rpm);
		if(!hasStartedFiring && Math.abs(turret.getShooterSpeed() - rpm) < 20 && Math.abs(turret.getCurrentAngle() - angle) < 1){
			startTime = Timer.getFPGATimestamp();
			hasStartedFiring = true;
		}
		if(hasStartedFiring){
			lift.up(1);
			frontIntake.in(1);
			backIntake.in(1);
		}
	}

	@Override
	public void end() {
		lift.up(0);
		frontIntake.in(0);
		backIntake.in(0);
		turret.shoot(0);
	}

}
