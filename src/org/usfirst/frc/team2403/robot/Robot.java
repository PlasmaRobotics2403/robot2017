package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.controllers.*;

import edu.wpi.first.wpilibj.*;

public class Robot extends IterativeRobot {

	PlasmaJoystick joystick;
	PlasmaGuitar guitar;
	DriveTrain driveTrain;
	GearManipulator gearManip;
	Intake intakeFront;
	Intake intakeRear;
	Lift lift;
	Turret turret;
	Climb climb;
	
	Servo test;
	
	@Override
	public void robotInit() {
		joystick = new PlasmaJoystick(Constants.JOYSTICK1_PORT);
		guitar = new PlasmaGuitar(1);
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
		climb = new Climb(Constants.TALON_CLIMB_L_ID, Constants.TALON_CLIMB_R_ID);
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
		if(joystick.LT.isPressed()){
			climb.spin(joystick.LT.getFilteredAxis());
		}
		else{
			climb.spin(-joystick.RT.getFilteredAxis());
		}
		gearManip.activate(joystick.START.isPressed());
		
		
		if(guitar.YELLOW.isPressed()){
			lift.spin(.5);
		}
		else if(guitar.BLUE.isPressed()){
			lift.spin(-.5);
		}
		else{
			lift.spin(0);
		}
		
		if(guitar.GREEN.isPressed()){
			turret.pivot(.6);
		}
		else if(guitar.RED.isPressed()){
			turret.pivot(-.6);
		}
		else{
			turret.pivot(0);
		}
		
		turret.shoot((guitar.whammyBar.getFilteredAxis() + 1) * .5);
		
		if(guitar.ORANGE.isPressed()){
			intakeFront.spin(-.5);
			intakeRear.spin(-.5);
		}
		else{
			intakeFront.spin(0);
			intakeRear.spin(0);
		}
		
	}
	
	@Override
	public void testInit(){
	}
	
	@Override
	public void testPeriodic() {
		gearManip.activate(false);
		if(joystick.Y.isPressed()){
			lift.spin(.5);
		}
		else if(joystick.A.isPressed()){
			lift.spin(-.5);
		}
		else{
			lift.spin(0);
		}
		
		if(joystick.X.isPressed()){
			turret.pivot(.6);
		}
		else if(joystick.B.isPressed()){
			turret.pivot(-.6);
		}
		else{
			turret.pivot(0);
		}
		
		turret.shoot(joystick.RT.getFilteredAxis());
		
		if(joystick.LB.isPressed()){
			intakeFront.spin(-.5);
		}
		else{
			intakeFront.spin(0);
		}
		if(joystick.RB.isPressed()){
			intakeRear.spin(-.5);
		}
		else{
			intakeRear.spin(0);
		}
	}
}

