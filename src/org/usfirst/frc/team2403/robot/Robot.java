package org.usfirst.frc.team2403.robot;

import org.opencv.core.*;
import org.opencv.imgproc.*;
import org.usfirst.frc.team2403.robot.auto.modes.*;
import org.usfirst.frc.team2403.robot.auto.util.*;
import org.usfirst.frc.team2403.robot.controllers.*;

import edu.wpi.cscore.*;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
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
	
	AutoMode[] autoModes;
	int autoModeSelection;
	
	boolean alliance; //true = red, false = blue
			
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
		
		alliance = DriverStation.getInstance().getAlliance() == Alliance.Red;
		
		autoModeRunner = new AutoModeRunner();	
		
		//CameraServer.getInstance().startAutomaticCapture();//.setResolution(1080, 1080);
		
		new Thread(() -> {
            CameraServer.getInstance().startAutomaticCapture();
            
            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("GearOverlay", 640, 480);
            
            Mat source = new Mat();
            Mat output = new Mat();
            
            while(!Thread.interrupted()) {
                cvSink.grabFrame(source);
                output = source.clone(); //resolution is 160 by 120 apparently
                Imgproc.line(output, new Point(60,0), new Point(60,120), new Scalar(0, 0, 255), 2, 8, 0);
                Imgproc.line(output, new Point(100,0), new Point(100,120), new Scalar(0, 0, 255), 2, 8, 0);
                Imgproc.line(output, new Point(80,0), new Point(80,120), new Scalar(0, 255, 0), 1, 8, 0);
                outputStream.putFrame(output);
            }
        }).start();
		
		SmartDashboard.putNumber("wanted RPM", 0);
		
		autoModes = new AutoMode[10];
		for(int i = 0; i < autoModes.length; i++){
			autoModes[i] = new Nothing();
		}
		autoModes[1] = new CrossBaseline(driveTrain);
		autoModes[2] = new CenterGear(driveTrain, gearManip, visionTable);
		autoModes[3] = new ShootFuelCenter(true, turret, lift, intakeFront, intakeRear);
		autoModes[4] = new ShootFuelCenter(false, turret, lift, intakeFront, intakeRear);
		autoModes[5] = new FuelTest(false, turret, lift, intakeFront, intakeRear);
		autoModeSelection = 0;
		SmartDashboard.putNumber("Auto Mode", 0);
	}
	@Override
	public void robotPeriodic(){
		networkTablesBroadcast();
		driveTrain.reportDriveData();
		driveTrain.updateGyro();
	}
	
	@Override
	public void disabledInit(){
		autoModeRunner.stop();
		
	}
	
	@Override
	public void disabledPeriodic(){
		if((int)SmartDashboard.getNumber("Auto Mode", 0) != 0){
			autoModeSelection = (int)SmartDashboard.getNumber("Auto Mode", 0);
		}
		else{
			try{
				autoModeSelection = Integer.parseInt(dashboardTable.getString("auto", "0"));
			}
			catch(NumberFormatException e){
				DriverStation.reportError("Auto mode must be an int", false);
				autoModeSelection = 0;
			}
		}
		driveTrain.zeroGyro();
	}
		
	@Override
	public void autonomousInit() {
		driveTrain.zeroGyro();
		autoModeSelection = (autoModeSelection >= autoModes.length) ? 0 : autoModeSelection;
		autoModeSelection = (autoModeSelection < 0) ? 0 : autoModeSelection;
		autoModeRunner.chooseAutoMode(autoModes[autoModeSelection]);
		autoModeRunner.start();
	}

	@Override
	public void autonomousPeriodic() {
	}
	
	@Override
	public void teleopInit(){
		autoModeRunner.stop();
		driveTrain.zeroGyro();
	}
	
	@Override
	public void teleopPeriodic() {	
		driver1Controls(joystick);
		driver2Controls(joystick2);
	}
	
	@Override
	public void testInit(){
	}
	
	@Override
	public void testPeriodic() {
		gearManip.activate(false, false);
		driver2Controls(joystick);
	}
	
	public void driver1Controls(PlasmaJoystick joy){
		driveTrain.FPSDrive(joystick.LeftY, joystick.RightX);
		gearManip.activate(joystick.START.isPressed(), joystick.X.isToggledOn());
		climb.up(joystick.RT.getFilteredAxis());
	}
	
	public void driver2Controls(PlasmaJoystick joy){
		if(joy.RB.isPressed()){
			intakeRear.in(1);
			intakeFront.in(1);
		}
		else if(joy.LB.isPressed()){
			intakeRear.out(1);
			intakeFront.out(1);
		}
		else{
			intakeRear.in(0);
			intakeFront.in(0);
		}
		
		if(joy.A.isPressed()){
			lift.down(1);
		}
		else if(joy.Y.isPressed()){
			lift.up(1);
		}
		else{
			lift.down(0);
		}
		
		if(joy.L3.isPressed()){
			turret.autoAim();
		}
		else{
			turret.spin(joy.LeftX.getFilteredAxis());
		}
		
		if(joy.RT.isPressed()){
			turret.autoShoot();
		}
		else{
			turret.shoot(0);
		}
	}
	
	public void outreachMode(){
		if(joystick.RT.isPressed()){
			turret.shoot(joystick.RT.getFilteredAxis() * Constants.MAX_TURRET_RPM);
			intakeRear.in(1);
			intakeFront.in(1);
			lift.up(1);
		}
		else if(joystick.A.isPressed()){
			turret.shoot(-100);
			intakeRear.in(0);
			intakeFront.in(0);
			lift.down(1);
		}
		else{
			turret.shoot(0);
			intakeRear.in(0);
			intakeFront.in(0);
			lift.up(0);
		}
		turret.spin(joystick.LeftX.getFilteredAxis());
		gearManip.activate(false, false);
	}
	
	public void networkTablesBroadcast(){
		visionTable.putNumber(Constants.GEAR_INPUT_ANGLE_NAME, driveTrain.getGyroAngle());
		visionTable.putNumber(Constants.TURRET_INPUT_ANGLE_NAME, turret.getCurrentAngle());
		
		SmartDashboard.putNumber("gear angle", visionTable.getNumber(Constants.GEAR_OUTPUT_ANGLE_NAME, 0));
		SmartDashboard.putNumber("gear dist", visionTable.getNumber(Constants.GEAR_OUTPUT_DISTANCE_NAME, 0));
		
		SmartDashboard.putNumber("current angle", visionTable.getNumber(Constants.TURRET_INPUT_ANGLE_NAME, 0));
		SmartDashboard.putNumber("fuel angle", visionTable.getNumber(Constants.TURRET_OUTPUT_ANGLE_NAME, 0));
		SmartDashboard.putNumber("fuel rpm", visionTable.getNumber(Constants.TURRET_OUTPUT_RPM_NAME, 0));
	}
}