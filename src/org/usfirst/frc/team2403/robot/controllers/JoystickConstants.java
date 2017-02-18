package org.usfirst.frc.team2403.robot.controllers;

public class JoystickConstants {

	/* AXIS CONSTANTS */
	
	public static final double deadBand = 0.15;
	
	/* TRIGGER CONSTANTS */
	
	public static final double triggerThreshold = 0.1;
	
	/* JOYSTICK ID CONSTANTS */
	
	public static final int JOYSTICK_A_ID = 1;
	public static final int JOYSTICK_B_ID = 2;
	public static final int JOYSTICK_X_ID = 3;
	public static final int JOYSTICK_Y_ID = 4;
	public static final int JOYSTICK_LB_ID = 5;
	public static final int JOYSTICK_RB_ID = 6;
	public static final int JOYSTICK_BACK_ID = 7;
	public static final int JOYSTICK_START_ID = 8;
	public static final int JOYSTICK_L3_ID = 9;
	public static final int JOYSTICK_R3_ID = 10;
	
	public static final int JOYSTICK_LEFTX_ID = 0;
	public static final int JOYSTICK_LEFTY_ID = 1;
	public static final int JOYSTICK_RIGHTX_ID = 4;
	public static final int JOYSTICK_RIGHTY_ID = 5;
	
	public static final int JOYSTICK_DPAD_ID = 0;
	
	public static final int JOYSTICK_LT_ID = 2;
	public static final int JOYSTICK_RT_ID = 3;
	
	/* GUITAR ID CONSTANTS */
	
	public static final int GUITAR_GREEN_ID = 1;
	public static final int GUITAR_RED_ID = 2;
	public static final int GUITAR_YELLOW_ID = 4;
	public static final int GUITAR_BLUE_ID = 3;
	public static final int GUITAR_ORANGE_ID = 5;
	public static final int GUITAR_BACK_ID = 7;
	public static final int GUITAR_START_ID = 8;
	
	public static final int GUITAR_DPAD_ID = 0;
	
	public static final int GUITAR_WHAMMY_ID = 4;
	
	private JoystickConstants(){
		//This prevents java from doing funky stuff
	}

}
