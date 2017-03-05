package org.usfirst.frc.team2403.robot;

import org.usfirst.frc.team2403.robot.auto.modes.*;
import org.usfirst.frc.team2403.robot.auto.util.*;
import org.usfirst.frc.team2403.robot.controllers.*;
import edu.wpi.first.wpilibj.*;
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
	
	NetworkTable visionTable;
	NetworkTable dashboardTable;
	
	AutoModeRunner autoModeRunner;
	
	@Override
	public void robotInit() {
		NetworkTable.setUpdateRate(.01);
		NetworkTable.initialize();
		visionTable = NetworkTable.getTable(Constants.VISION_TABLE_NAME);
		dashboardTable = NetworkTable.getTable(Constants.DASHBOARD_TABLE_NAME);	
		
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
		lift = new Lift(Constants.TALON_LIFT_FRONT_ID,
						Constants.TALON_LIFT_REAR_ID);
		turret = new Turret(Constants.TALON_TURRET_L_ID,
							Constants.TALON_TURRET_R_ID,
							Constants.TALON_TURRET_SPIN_ID,
							visionTable);
		climb = new Climb(Constants.TALON_CLIMB_L_ID, 
							Constants.TALON_CLIMB_R_ID);
		
		autoModeRunner = new AutoModeRunner();	
		
		CameraServer.getInstance().startAutomaticCapture();//.setResolution(1080, 1080);
		
		SmartDashboard.putNumber("wanted RPM", 0);
		
	}
	@Override
	public void robotPeriodic(){
		networkTablesBroadcast();
		driveTrain.reportDriveData();
	}
	
	@Override
	public void disabledInit(){
		autoModeRunner.stop();
	}
	
	@Override
	public void disabledPeriodic(){
		
	}
		
	@Override
	public void autonomousInit() {
		driveTrain.zeroGyro();
		autoModeRunner.chooseAutoMode(new CountingMode());
		autoModeRunner.start();
	}

	@Override
	public void autonomousPeriodic() {
		

	}
	
	@Override
	public void teleopInit(){
		autoModeRunner.stop();
		driveTrain.zeroGyro();
		autoModeRunner.chooseAutoMode(new TeleopGearHang(driveTrain, gearManip, visionTable));
	}
	
	@Override
	public void teleopPeriodic() {
		
		if(joystick.X.isOffToOn()){
			autoModeRunner.start();
		}
		else if(!joystick.X.isPressed()){
			autoModeRunner.stop();
			driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
		
			gearManip.activate(joystick.START.isPressed());
		}
		//climb.up(joystick.RT.getFilteredAxis());
		if(joystick.RB.isPressed()){
			intakeRear.in(1);
			intakeFront.in(1);
		}
		else if(joystick.LB.isPressed()){
			intakeRear.out(1);
			intakeFront.out(1);
		}
		else{
			intakeRear.in(0);
			intakeFront.in(0);
		}
		
		if(joystick.A.isPressed()){
			lift.up(1);
		}
		else if(joystick.Y.isPressed()){
			lift.down(1);
		}
		else{
			lift.up(0);
		}
		
		//turret.pivot(joystick.LeftX.getFilteredAxis());
		/*
		if(joystick.A.isPressed()){
			turret.testTurn(45);
		}
		else{
			turret.testTurn(0);
		}
		*/
		/*
		if(joystick.Y.isOffToOn()){
			CameraServer.getInstance().removeCamera("USB Camera 0");
		}
		else if(joystick.Y.isOnToOff()){
			CameraServer.getInstance().startAutomaticCapture();
		}
		*/
		
		//turret.autoAim(joystick.A.isOnToOff());
		turret.autoAim(true);
		turret.autoShoot();
		//turret.shoot(SmartDashboard.getNumber();
	}
	
	@Override
	public void testInit(){
	}
	
	@Override
	public void testPeriodic() {
		
	}
	
	public void networkTablesBroadcast(){
		visionTable.putNumber(Constants.GEAR_INPUT_ANGLE_NAME, driveTrain.getGyroAngle());
		visionTable.putNumber(Constants.TURRET_INPUT_ANGLE_NAME, turret.getCurrentAngle());
		
		SmartDashboard.putNumber("gear angle", visionTable.getNumber(Constants.GEAR_OUTPUT_ANGLE_NAME, 0));
		SmartDashboard.putNumber("gear dist", visionTable.getNumber(Constants.GEAR_OUTPUT_DISTANCE_NAME, 0));
	}
}

