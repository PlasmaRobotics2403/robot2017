package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.PlasmaJoystick;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

	PlasmaJoystick joystick;
	DriveTrain driveTrain;
	GearManipulator gearManip;
	Intake intakeFront;
	Intake intakeRear;
	Lift lift;
	Turret turret;

	
	@Override
	public void robotInit() {
		joystick = new PlasmaJoystick(Constants.JOYSTICK1_PORT);
		driveTrain = new DriveTrain(Constants.TALON_L_ID,
									Constants.TALON_L_SLAVE_ID,
									Constants.TALON_R_ID,
									Constants.TALON_R_SLAVE_ID);
		gearManip = new GearManipulator(Constants.GEAR_DOOR_L_PORT,
												Constants.GEAR_DOOR_R_PORT,
												Constants.GEAR_PUSH_L_PORT,
												Constants.GEAR_PUSH_R_PORT);
		intakeFront = new Intake(Constants.TALON_INTAKE_FRONT_ID);
		intakeRear = new Intake(Constants.TALON_INTAKE_REAR_ID);
		lift = new Lift(Constants.TALON_LIFT_LEFT_ID,
						Constants.TALON_LIFT_RIGHT_ID);
		turret = new Turret(Constants.TALON_TURRET_L_ID,
							Constants.TALON_TURRET_R_ID,
							Constants.TALON_TURRET_SPIN_ID);
	}

	@Override
	public void autonomousInit() {

	}

	@Override
	public void autonomousPeriodic() {

	}
	
	@Override
	public void teleopInit(){
		driveTrain.navX.zeroYaw();
	}
	
	@Override
	public void teleopPeriodic() {
		driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
	}
	
	@Override
	public void testInit(){
	}

	@Override
	public void testPeriodic() {
		joystick.rumble.setLeftRumble(joystick.LT.getFilteredAxis());
		joystick.rumble.setRightRumble(joystick.RT.getFilteredAxis());

	}
}

