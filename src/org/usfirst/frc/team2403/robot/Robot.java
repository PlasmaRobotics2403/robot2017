package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.autoModes.BaselineAuto;
import org.usfirst.frc.team2403.robot.controllers.*;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	PlasmaJoystick joystick;
	PlasmaJoystick joystick2;
	DriveTrain driveTrain;
	GearManipulator gearManip;
	Intake intakeFront;
	Intake intakeRear;
	Lift lift;
	Turret turret;
	Climb climb;
	
	
	NetworkTable networkTable;
	
	BaselineAuto auto;

	
	@Override
	public void robotInit() {
		joystick = new PlasmaJoystick(Constants.JOYSTICK1_PORT);
		joystick2 = new PlasmaJoystick(Constants.JOYSTICK2_PORT);
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
		
		NetworkTable.setUpdateRate(.01);
		NetworkTable.initialize();
		networkTable = NetworkTable.getTable(Constants.NETWORK_TABLE_NAME);
		
		CameraServer.getInstance().startAutomaticCapture();//.setResolution(1080, 1080);		
		
	}
	@Override
	public void robotPeriodic(){
	}
	
	double time;
	
	@Override
	public void autonomousInit() {
		time = Timer.getFPGATimestamp();
	}

	@Override
	public void autonomousPeriodic() {
		if(Timer.getFPGATimestamp() < time + 3){
			driveTrain.FPSDrive(.5, 0);
		}
		else{
			driveTrain.FPSDrive(0, 0);
		}
	}
	
	@Override
	public void teleopInit(){
		driveTrain.navX.zeroYaw();
	}
	
	@Override
	public void teleopPeriodic() {
		
		
		if(joystick.A.isPressed() && networkTable.getNumber("gearElevatorDistance", -1) != -1){
			double angle = networkTable.getNumber("gearElevatorAngle", 0);
			if(Math.abs(angle) > 5){
				driveTrain.FPSDrive(0, -angle / 20);
			}
			else{
				driveTrain.FPSDrive(.3, -angle / 20);
			}
		}
		else{
			driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
		}
		if(joystick.LT.isPressed()){
			climb.spin(joystick.LT.getFilteredAxis());
		}
		else{
			climb.spin(-joystick.RT.getFilteredAxis());
		}
		gearManip.activate(joystick.START.isPressed());
		
		
		
		if(joystick2.Y.isPressed()){
			lift.spin(1);
		}
		else if(joystick2.A.isPressed()){
			lift.spin(-1);
		}
		else{
			lift.spin(0);
		}
		
		turret.pivot(joystick2.LeftX.getFilteredAxis() * -.5);
		
		turret.shoot(joystick2.RT.getFilteredAxis());
		
		if(joystick2.RB.isPressed()){
			intakeFront.spin(-.5);
			intakeRear.spin(-.5);
		}
		else if(joystick2.LB.isPressed()){
			intakeFront.spin(.5);
			intakeRear.spin(.5);
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
			lift.spin(1);
		}
		else if(joystick.A.isPressed()){
			lift.spin(-1);
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

