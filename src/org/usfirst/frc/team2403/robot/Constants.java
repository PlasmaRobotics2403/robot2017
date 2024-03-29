package org.usfirst.frc.team2403.robot;

public class Constants {

	
	/* CONTROLLER CONSTANTS */
	public static final int JOYSTICK1_PORT = 0;
	public static final int JOYSTICK2_PORT = 1;
	
	/* TALON ID CONSTANTS */
	public static final int TALON_L_ID = 1;
	public static final int TALON_L_SLAVE_ID = 2;
	public static final int TALON_R_ID = 3;
	public static final int TALON_R_SLAVE_ID = 4;
	public static final int TALON_INTAKE_FRONT_ID = 5;
	public static final int TALON_INTAKE_REAR_ID = 6;
	public static final int TALON_LIFT_FRONT_ID = 7;
	public static final int TALON_LIFT_REAR_ID = 8;
	public static final int TALON_TURRET_L_ID = 9;
	public static final int TALON_TURRET_R_ID = 10;
	public static final int TALON_TURRET_SPIN_ID = 11;
	public static final int TALON_CLIMB_L_ID = 12;
	public static final int TALON_CLIMB_R_ID = 13;
	
	/* SERVO PORT CONSTANTS */
	public static final int GEAR_DOOR_L_PORT = 3;
	public static final int GEAR_DOOR_R_PORT = 0;
	public static final int GEAR_PUSH_L_PORT = 2;
	public static final int GEAR_PUSH_R_PORT = 1;
	
	/*DRIVETRAIN CONSTANTS*/
	public static final double MAX_AUTO_DRIVE_SPEED = .9;
	public static final double MAX_DRIVE_SPEED = 1;
	public static final double MAX_DRIVE_TURN = 1;	
	public static final double DRIVE_ENCODER_CONVERSION = 20.9528184;
	public static final double DRIVE_WHEEL_WIDTH = 27;
	
	/*GEAR MANIPULATOR CONSTANTS*/
	public static final double DOOR_OPEN_ANGLE = 55.0;
	public static final double DOOR_START_ANGLE = 5.0;
	public static final double PUSH_ANGLE = 90.0;
	public static final double PUSH_START_ANGLE = 0;
	public static final double MAX_ANGLE = 180;
	
	/*LIFT CONSTANTS*/
	public static final double MAX_LIFT_SPEED = .8;
	
	/*CLIMB_CONSTANTS*/
	public static final double MAX_CLIMB_SPEED = .8;
	
	/*INTAKE CONSTANTS*/
	public static final double MAX_INTAKE_SPEED = .8;
	
	/*SHOOTER CONSTANTS*/
	public static final double MAX_SPIN_SPEED = .5;
	public static final int TURRET_ABS_ENC_OFFSET = -961;
	public static final double TURRET_ROTS_PER_DEGREE = .0160048;
	public static final double MAX_TURRET_RPM = 2650.0;
	
	/*NETWORK TABLES CONSTANTS*/
	public static final String VISION_TABLE_NAME = "vision";
	public static final String DASHBOARD_TABLE_NAME = "dashboard";
	public static final String GEAR_INPUT_ANGLE_NAME = "gearPhotoAngle";
	public static final String GEAR_OUTPUT_ANGLE_NAME = "gearNeededAngle";
	public static final String GEAR_OUTPUT_DISTANCE_NAME = "gearDistance";
	public static final String TURRET_INPUT_ANGLE_NAME = "fuelPhotoAngle";
	public static final String TURRET_OUTPUT_ANGLE_NAME = "fuelNeededAngle";
	public static final String TURRET_OUTPUT_RPM_NAME = "shooterRPM";
	
	/*GENERAL CONSTANTS*/
	public static final double TALON_POWER_MULTIPLIER = 1023.0;
	public static final double ENCODER_COUNTS_PER_ROT = 4096.0;
	public static final double ENCODER_UPDATES_PER_MIN = 600.0;
	public static final double RPM_PER_ENC_VEL = Constants.ENCODER_UPDATES_PER_MIN / Constants.ENCODER_COUNTS_PER_ROT;
	public static final double GENERAL_RAMP_RATE = 36.0;
}
